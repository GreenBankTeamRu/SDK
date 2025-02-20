package ru.sberbank.pprb.sbbol.draft.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sberbank.pprb.sbbol.draft.dto.response.DraftView;
import ru.sberbank.pprb.sbbol.draft.entity.Draft;
import ru.sberbank.pprb.sbbol.draft.exception.EntryNotFoundException;
import ru.sberbank.pprb.sbbol.draft.repository.DraftRepository;
import ru.sberbank.pprb.sbbol.draft.service.mapper.DraftMapper;

import java.util.UUID;

@Service
public class DraftServiceImpl implements DraftService {

    private final DraftRepository repository;
    private final DraftMapper mapper;

    public DraftServiceImpl(DraftRepository repository, DraftMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public DraftView getById(UUID uuid) {
        return mapper.toDto(
                repository.findById(uuid)
                        .orElseThrow(() -> new EntryNotFoundException("Не найден черновик по идентификатору " + uuid))
        );
    }

    @Transactional
    @Override
    public UUID create(String name, String content, String digitalId) {
        Draft draft = new Draft();
        draft.setName(name);
        draft.setContent(content);
        draft.setDigitalId(digitalId);
        return repository.save(draft).getId();
    }

    @Transactional
    @Override
    public DraftView update(UUID uuid, String content) {
        Draft draft = repository.findById(uuid)
                .orElseThrow(() -> new EntryNotFoundException("Не найден черновик по идентификатору " + uuid));
        draft.setContent(content);
        return mapper.toDto(repository.save(draft));
    }

    @Transactional
    @Override
    public void delete(UUID uuid) {
        repository.deleteById(uuid);
    }

    @Override
    public Page<DraftView> findDraft(String name, Pageable pageable) {
        return repository.findDraftByNameLike("%" + name + "%", pageable)
                .map(mapper::toDto);
    }
}
