package ru.sberbank.sbbol.sberbusinessapi.factory;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import lombok.Getter;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

public class ValidatorFactoryProvider {
    @Getter
    private static final Validator validator;

    static {
        try {
            // Создание фабрики валидаторов с отключением использования EL
            ValidatorFactory factory = Validation.byDefaultProvider()
                    .configure()
                    .messageInterpolator(new ParameterMessageInterpolator()) // Отключение использования EL
                    .buildValidatorFactory();

            validator = factory.getValidator();

            // Регистрация хука для закрытия фабрики при завершении программы
            Runtime.getRuntime().addShutdownHook(new Thread(factory::close));
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }
}