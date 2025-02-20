package ru.sberbank.pprb.sbbol.draft.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.sberbank.pprb.sbbol.draft.dto.request.DraftCreateRequest;
import ru.sberbank.pprb.sbbol.draft.dto.response.DraftCreateResult;
import ru.sberbank.pprb.sbbol.draft.dto.request.DraftUpdateRequest;
import ru.sberbank.pprb.sbbol.draft.dto.response.DraftView;
import ru.sberbank.pprb.sbbol.draft.service.DraftService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/v1/draft")
public class DraftController {

    private static final String BY_ID = "/{uuid:.+}";
    private static final String LIST = "/list";

    private final DraftService service;

    public DraftController(DraftService service) {
        this.service = service;
    }

    @GetMapping(BY_ID)
    public DraftView getById(@PathVariable("uuid") @NotNull UUID uuid) {
        return service.getById(uuid);
    }

    @PostMapping
    public DraftCreateResult create(@RequestBody @Valid DraftCreateRequest createRequest, HttpServletResponse response) {
        UUID result = service.create(createRequest.getName(), createRequest.getContent(), createRequest.getDigitalId());
        response.setStatus(HttpStatus.CREATED.value());
        return new DraftCreateResult(result);
    }

    @PutMapping(BY_ID)
    public DraftView update(@PathVariable("uuid") @NotNull UUID uuid, @RequestBody @Valid DraftUpdateRequest updateRequest) {
        return service.update(uuid, updateRequest.getContent());
    }

    @DeleteMapping(BY_ID)
    public void delete(@PathVariable("uuid") @NotNull UUID uuid) {
        service.delete(uuid);
    }

    @GetMapping(LIST)
    public Page<DraftView> listDrafts(
            @RequestParam(value = "name", required = false) String name,
            Pageable pageable
            ) {
        return service.findDraft(name, pageable);
    }

}
