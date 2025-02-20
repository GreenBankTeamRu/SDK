package ru.sberbank.pprb.sbbol.draft.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sberbank.pprb.sbbol.draft.entity.Draft;

import java.util.UUID;

@Repository
public interface DraftRepository extends JpaRepository<Draft, UUID> {

    /**
     * Постраничный поиск по имени (like)
     *
     * @param name часть имени для поиска
     * @param pageable настройка постраничной разбивки
     * @return страница с найденными элементами
     */
    Page<Draft> findDraftByNameLike(String name, Pageable pageable);

}
