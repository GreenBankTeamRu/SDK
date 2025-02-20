package ru.sberbank.pprb.sbbol.draft.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.sberbank.pprb.sbbol.draft.dto.response.DraftView;
import ru.sberbank.pprb.sbbol.draft.entity.Draft;

@Mapper(componentModel = "spring")
public interface DraftMapper {

    @Mapping(source = "id", target = "guid")
    DraftView toDto(Draft entity);

}
