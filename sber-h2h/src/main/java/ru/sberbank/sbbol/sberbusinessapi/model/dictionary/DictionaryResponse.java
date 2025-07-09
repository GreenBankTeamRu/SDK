package ru.sberbank.sbbol.sberbusinessapi.model.dictionary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DictionaryResponse {
    private String decode_archive;
    private String name;
}

