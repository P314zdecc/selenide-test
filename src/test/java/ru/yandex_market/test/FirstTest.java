package ru.yandex_market.test;

import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import pages.*;
import ru.yandex_market.preconditions.TestBase;

import java.util.List;

import static com.codeborne.selenide.Selenide.open;


public class FirstTest extends TestBase {


    @Owner("Лебедев Сергей")
    @Feature("Проверка результатов поиска")
    @DisplayName("Проверка соответствия товара, по установленным фильтрам производителя")
    @ParameterizedTest(name = "{displayName}: {arguments}")
    @MethodSource("helper.DataHelper#parameterTest")
    public void checkingForComplianceOfTheDesiredProductWithTheSetFilters(
            List<String> manufacturer,
            List<String> searchProduct,
            String section,
            String category,
            String filter) throws InterruptedException {
        open(config.getBaseUrl(), YandexMainPage.class)
                .checkButton()
                .selectedCategoryAndSection(section, category, YandexFilterPage.class)
                .checkTitle(category)
                .checkedFilter(filter, manufacturer)
                .checkedProductList(searchProduct, config.getTime())
                .checkedError();
    }
}
