package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.page;

public class YandexMainPage extends BasePage {

    /**
     * @autor Сергей Лебедев
     * Нажатие на кнопку 'Каталог'
     * @return
     */
    @Step("Клик по кнопке 'Каталог'")
    public YandexMainPage checkButton() {
        $x("//button/span[text()='Каталог']").should(visible, Duration.ofSeconds(8)).click();
        return this;
    }

    /**
     * @autor Сергей Лебедев
     * Метод сначала наводит на нужную секцию в открывшемся каталоге, а затем выбирает необходимую категорию
     * исходя из результата выбора, есть возможность передать в аргументы необходимый класс страницы Page Object
     * @param section -> (Электроника)
     * @param category -> (Смартфоны)
     * @param typeNextPage
     * @return
     * @param <T>
     */
    @Step("Перейти в раздел {section} -> {category}")
    public <T extends BasePage> T selectedCategoryAndSection(String section, String category, Class<T> typeNextPage) {
        $x("//*[@data-auto='catalog-content']//span[text()='" + section + "']")
                .shouldHave(visible, Duration.ofSeconds(8)).hover();
        SelenideElement element = $x("//a[contains(text(), '" + category + "')]");
        element.should(visible, Duration.ofSeconds(8)).click();
        return typeNextPage.cast(page(typeNextPage));
    }
}
