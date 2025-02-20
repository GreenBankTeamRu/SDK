package ru.sberbank.pprb.sbbol.draft.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.sberbank.pprb.sbbol.draft.dto.response.DraftView;

import java.util.UUID;

/**
 * Сервис работы с черновиками
 */
public interface DraftService {

    /**
     * Получение черновика по идентификатору
     *
     * @param uuid уникальный идентификатор
     * @return черновик
     */
    DraftView getById(UUID uuid);

    /**
     * Создание черновика
     *
     * @param name наименование черновика
     * @param content содержимое черновика
     * @param digitalId идентификатор организации в Digital
     * @return идентификатор созданного черновика
     */
    UUID create(String name, String content, String digitalId);

    /**
     * Обновление черновика
     *
     * @param uuid идентификатор черновика
     * @param content содержимое черновика
     * @return измененный черновик
     */
    DraftView update(UUID uuid, String content);

    /**
     * Удаление черновика
     *
     * @param uuid идентификатор черновика
     */
    void delete(UUID uuid);

    /**
     * Поиск черновика по имени (по like)
     *
     * @param name наименование черновика
     * @param pageable пагинация
     * @return найденные черновики
     */
    Page<DraftView> findDraft(String name, Pageable pageable);
}
