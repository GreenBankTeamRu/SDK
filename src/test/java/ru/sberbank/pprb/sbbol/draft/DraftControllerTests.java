package ru.sberbank.pprb.sbbol.draft;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import ru.dcbqa.allureee.annotations.layers.ApiTestLayer;
import ru.dcbqa.coverage.swagger.reporter.reporters.RestAssuredCoverageReporter;
import ru.sberbank.pprb.sbbol.draft.invoker.JacksonObjectMapper;
import ru.sberbank.pprb.sbbol.draft.api.DraftApi;
import ru.sberbank.pprb.sbbol.draft.model.DraftCreateRequest;
import ru.sberbank.pprb.sbbol.draft.model.DraftCreateResult;
import ru.sberbank.pprb.sbbol.draft.model.DraftUpdateRequest;
import ru.sberbank.pprb.sbbol.draft.model.DraftView;
import ru.sberbank.pprb.sbbol.draft.model.ErrorResponse;
import ru.sberbank.pprb.sbbol.draft.model.ListResponse;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext
@ActiveProfiles("test")
@Import(TestReplicationConfiguration.class)
@ContextConfiguration(initializers = {HibernatePluginCleanerInitializer.class})
@ApiTestLayer
class DraftControllerTests {

    @LocalServerPort
    private int port;

    private DraftApi client;

    @BeforeAll
    void setup() {
        client = DraftApi.draft(() -> {
            return new RequestSpecBuilder()
                    .setPort(port)
                    .setConfig(new RestAssuredConfig().objectMapperConfig(new ObjectMapperConfig().defaultObjectMapper(JacksonObjectMapper.jackson())))
                    .addFilter(new RestAssuredCoverageReporter())
                    .addFilter(new AllureRestAssured());
        });
    }

    @Test
    void createDraftTest() {
        DraftCreateRequest draftCreateRequest = randomCreateRequest();
        DraftCreateResult result = sendCreateRequest(draftCreateRequest);
        assertNotNull(result.getGuid());
    }

    @Test
    void getByIdTest() {
        DraftCreateResult createResult = sendCreateRequest(randomCreateRequest());
        UUID guid = createResult.getGuid();
        assertNotNull(guid);

        DraftView entity = getById(guid);

        assertNotNull(entity.getGuid());
        assertNotNull(entity.getDigitalId());
        assertNotNull(entity.getName());
        assertNotNull(entity.getContent());
        assertNotNull(entity.getCreateDate());
        assertNotNull(entity.getModifyDate());
        assertNotNull(entity.getCreateDate());
        assertNotNull(entity.getModifyDate());
    }

    @Test
    void deleteDraftTest() {
        DraftCreateResult createResult = sendCreateRequest(randomCreateRequest());
        UUID guid = createResult.getGuid();
        assertNotNull(guid);

        client.deleteDraft()
                .uuidPath(guid)
                .execute((response) -> {
                    response.then().statusCode(HttpStatus.OK.value());
                    return response;
                });

        client.getDraft()
                .uuidPath(guid)
                .execute((response) -> {
                    response.then().statusCode(HttpStatus.NOT_FOUND.value());
                    return response;
                });
    }

    @Test
    void updateDraftTest() {
        DraftCreateResult createResult = sendCreateRequest(randomCreateRequest());
        UUID guid = createResult.getGuid();
        assertNotNull(guid);

        String modifiedContent = "MODIFIED_CONTENT";

        DraftView original = getById(guid);
        assertNotNull(original.getContent());
        assertNotEquals(modifiedContent, original.getContent());

        DraftUpdateRequest updateRequest = new DraftUpdateRequest();
        updateRequest.setContent(modifiedContent);

        // проверяем, что в ответе на изменение новый контент
        DraftView resp = client.updateDraft()
                .uuidPath(guid)
                .body(updateRequest)
                .executeAs((response) -> {
                    response.then().statusCode(HttpStatus.OK.value());
                    return response;
                });
        assertEquals(modifiedContent, resp.getContent());

        // проверяем, что в полученном по идентификатору объекте тоже новый контент
        DraftView modified = getById(guid);
        assertEquals(modifiedContent, modified.getContent());
    }

    @Test
    void listDraftsTest() {
        for (int i = 0; i < 3; i++) {
            DraftCreateRequest request = randomCreateRequest();
            request.setName("наименование" + i);
            sendCreateRequest(request);
        }
        ListResponse listResponse = client.listDrafts()
                .nameQuery("именовани")
                .sortQuery("name,DESC")
                .pageQuery("0")
                .offsetQuery("0")
                .sizeQuery("2")
                .executeAs((response) -> {
                    response.then().statusCode(HttpStatus.OK.value());
                    return response;
                });

        assertEquals(3, listResponse.getTotalElements());
        assertEquals(false, listResponse.getLast());
        List<DraftView> content = listResponse.getContent();
        assertNotNull(content);
        assertFalse(content.isEmpty());
        assertEquals("наименование2", content.get(0).getName());
        assertEquals("наименование1", content.get(1).getName());
    }

    @Test
    void validateEmptyNameInCreateRequestTest() {
        DraftCreateRequest createRequest = new DraftCreateRequest();
        createRequest.setDigitalId(RandomStringUtils.randomAlphabetic(38));

        client.createDraft()
                .body(createRequest)
                .execute(validateErrorResponse("Параметр name должен быть заполнен"));
    }

    @Test
    void validateNameLengthInCreateRequestTest() {
        DraftCreateRequest createRequest = new DraftCreateRequest();
        createRequest.setName(RandomStringUtils.randomAlphabetic(21));
        createRequest.setDigitalId(RandomStringUtils.randomAlphabetic(38));

        client.createDraft()
                .body(createRequest)
                .execute(validateErrorResponse("Длина поля name должна быть от 1 до 20 символов"));
    }

    @Test
    void validateContentLengthInCreateRequestTest() {
        DraftCreateRequest createRequest = new DraftCreateRequest();
        createRequest.setName(RandomStringUtils.randomAlphabetic(20));
        createRequest.setDigitalId(RandomStringUtils.randomAlphabetic(38));
        createRequest.setContent(RandomStringUtils.randomAlphabetic(4001));

        client.createDraft()
                .body(createRequest)
                .execute(validateErrorResponse("Длина поля content не должна превышать 4000 символов"));
    }

    @Test
    void validateEmptyDigitalIdInCreateRequestTest() {
        DraftCreateRequest createRequest = new DraftCreateRequest();
        createRequest.setName(RandomStringUtils.randomAlphabetic(20));
        createRequest.setContent(RandomStringUtils.randomAlphabetic(4000));

        client.createDraft()
                .body(createRequest)
                .execute(validateErrorResponse("Параметр digitalId должен быть заполнен"));
    }

    @Test
    void validateContentLengthInUpdateRequestTest() {
        DraftUpdateRequest updateRequest = new DraftUpdateRequest();
        updateRequest.setContent(RandomStringUtils.randomAlphabetic(4001));

        client.updateDraft()
                .uuidPath(UUID.randomUUID())
                .body(updateRequest)
                .execute(validateErrorResponse("Длина поля content не должна превышать 4000 символов"));
    }

    private Function<Response, Response> validateErrorResponse(String errorDesc) {
        return response -> {
            ErrorResponse errorResponse = response.as(ErrorResponse.class);
            assertAll(
                    () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode()),
                    () -> assertEquals(String.valueOf(HttpStatus.BAD_REQUEST.value()), errorResponse.getErrorCode()),
                    () -> assertEquals(errorDesc, errorResponse.getErrorDesc()),
                    () -> assertEquals(HttpStatus.BAD_REQUEST.name(), errorResponse.getErrorName())
            );
            return response;
        };
    }

    @NotNull
    private DraftCreateResult sendCreateRequest(DraftCreateRequest draftCreateRequest) {
        return client.createDraft()
                .body(draftCreateRequest)
                .executeAs((response) -> {
                    response.then().statusCode(HttpStatus.CREATED.value());
                    return response;
                });
    }

    @NotNull
    private DraftCreateRequest randomCreateRequest() {
        DraftCreateRequest draftCreateRequest = new DraftCreateRequest();
        draftCreateRequest.setName(RandomStringUtils.randomAlphabetic(20));
        draftCreateRequest.setContent(RandomStringUtils.randomAlphabetic(4000));
        draftCreateRequest.setDigitalId(RandomStringUtils.randomAlphabetic(38));
        return draftCreateRequest;
    }

    @NotNull
    private DraftView getById(UUID guid) {
        return client.getDraft()
                .uuidPath(guid)
                .executeAs((response) -> {
                    response.then().statusCode(HttpStatus.OK.value());
                    return response;
                });
    }
}
