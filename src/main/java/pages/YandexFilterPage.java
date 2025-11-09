package pages;

import com.codeborne.selenide.ElementsCollection;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.util.List;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class YandexFilterPage extends BasePage {

    /**
     * @autor Сергей Лебедев
     * Метод проверяет содержание наименования раздела в заголовке страницы
     * @param title -> 'Смартфоны'
     * @return
     */
    @Step("Проверка, что заголовок раздела содержит слово {title}")
    public YandexFilterPage checkTitle(String title) {
        $x("//h1").shouldHave(text(title), visible);
            return this;
    }

    /**
     * @autor Сергей Лебедев
     * Метод выбирает подходящего по параметру производителя и нажимает checkbox
     * После чего проверяет, что в коллекции элементов поиска содержиться производитель,
     * а также, что он отображается
     * @param filterSection -> секция фильтра
     * @return первая страница поисковой выдачи
     */
    @Step("Выбор фильтра {filterSection} и значений {param}")
    public YandexResultPage checkedFilter(String filterSection, List<String> param) {
        ElementsCollection man_R = $$x("//span[text()='"+filterSection+"']/ancestor::div[@data-zone-name='filter']//div[@data-zone-name='filterValue']//span[text()]");
        for (WebElement f : man_R) {
            for (String s : param) {
                if (f.getText().equalsIgnoreCase(s)) {
                    f.click();
                    $("[data-auto=\"preloader\"]").should(visible, Duration.ofSeconds(8));
                    $("[data-auto=\"spinner\"]").should(hidden, Duration.ofSeconds(8));
                }
            }
        }
        return page(YandexResultPage.class);
    }
}
