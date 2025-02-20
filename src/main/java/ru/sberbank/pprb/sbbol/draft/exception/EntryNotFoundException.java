package ru.sberbank.pprb.sbbol.draft.exception;

/**
 * Ошибка, указывающая на отсутствие записи в БД
 */
public class EntryNotFoundException extends RuntimeException {

    public EntryNotFoundException(String message) {
        super(message);
    }

}
