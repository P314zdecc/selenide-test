package helper;

import io.qameta.allure.Step;

import java.util.List;

public class Assertions {

    @Step("Проверка, что : {message}")
    public static void assertTrue(boolean condition, List<String> message) {
        org.junit.jupiter.api.Assertions.assertTrue(condition, message.toString());
    }

    @Step("Проверка, что : {message}")
    public static void fail(String message) {
        org.junit.jupiter.api.Assertions.fail(message);
    }

}
