package pages;


import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.StaleElementReferenceException;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.*;
import static helper.Assertions.assertTrue;
import static helper.Assertions.fail;

public class YandexResultPage extends BasePage {

    /**
     * @return
     * @autor Сергей Лебедев
     * Локатор для кнопки 'Вперед'
     */
    private SelenideElement getButtonNext() {
        return $x("//*[@data-auto='pagination-next']");
    }

    private SelenideElement getLoaderSpinner() {
        return $("[data-auto=\"spinner\"]");
    }

    /**
     * @return
     * @autor Сергей Лебедев
     * Возврвщает список найденных товаров
     */
    public ElementsCollection getList1() {
        return $$x("//*[@data-auto='searchOrganic']//*[@data-auto='snippet-title']");
    }

    /**
     * @autor Сергей Лебедев
     * Список для хранения наименования товаров на каждой странице, которые не подходят по установленному фильтру
     * @return
     */
    private List<String> errors = new ArrayList<>();

    public List<String> getErrors() {
        return errors;
    }


    /**
     * Проверяет поисковую выдачу на наличие указанных ключевых слов в описании товаров
     * Выполняет проверку по всем страницам результатов поиска в течение заданного времени
     *
     * @param searchRes список ключевых слов для проверки в описаниях товаров
     * @param time      время выполнения проверки в секундах
     * @return текущую страницу результатов YandexResultPage
     * @throws RuntimeException если не удалось кликнуть по кнопке "Следующая страница"
     * @throws AssertionError   если время выполнения истекло или найдены товары не соответствующие фильтру
     * @author Сергей Лебедев
     */
    @Step("Проверка поисковой выдачи на присутствие в описании товара {searchRes}")
    public YandexResultPage checkedProductList(List<String> searchRes, Integer time) {
        Instant endTime = Instant.now().plusSeconds(time);
        Duration timeout = Duration.ofSeconds(10);
        Duration productTimeout = Duration.ofSeconds(6);
        while (Instant.now().isBefore(endTime)) {
            ElementsCollection productList = getList1();
            SelenideElement nextButton = getButtonNext();
            if (nextButton.is(hidden) || productList.isEmpty()) {
                return this;
            }
            if (!clickNextButton(nextButton, productList, timeout)) {
                throw new RuntimeException("Не удалось кликнуть по кнопке Следующая страница");
            }
            waitForPageLoad();
            checkProductsOnPage(searchRes, endTime, time, productTimeout);
        }
        fail("Время выполнения теста истекло и превышает: " + (time / 60) + " минут.");
        return this;
    }

    /**
     * Выполняет клик по кнопке перехода на следующую страницу с обработкой исключений
     * Выполняет несколько попыток клика с обновлением ссылки на элемент для избежания StaleElementReferenceException
     *
     * @param nextButton  кнопка перехода на следующую страницу
     * @param productList список товаров на текущей странице
     * @param timeout     таймаут ожидания появления элементов
     * @return true если клик выполнен успешно, false если все попытки не увенчались успехом
     * @author Сергей Лебедев
     */
    private boolean clickNextButton(SelenideElement nextButton, ElementsCollection productList, Duration timeout) {
        Selenide.executeJavaScript("arguments[0].scrollIntoView(true);", nextButton);
        nextButton.should(appear, timeout);
        for (int attempt = 0; attempt < 5; attempt++) {
            try {
                SelenideElement freshButton = getButtonNext();
                Selenide.executeJavaScript("arguments[0].scrollIntoView(true);", freshButton);
                freshButton.should(appear, timeout);
                Selenide.executeJavaScript("arguments[0].click();", freshButton);
                return true;
            } catch (StaleElementReferenceException | ElementClickInterceptedException e) {
                productList.last().should(appear, timeout);
                // Небольшая пауза перед повторной попыткой, можно убрать - при условии привязки к элементу
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        return false;
    }

    /**
     * Ожидает полной загрузки страницы после перехода
     * Выполняет небольшую прокрутку для инициализации динамического контента
     * Дожидается скрытия спиннера загрузки и прокручивает к последнему элементу списка
     *
     * @author Сергей Лебедев
     */
    private void waitForPageLoad() {
        Selenide.executeJavaScript("window.scrollBy(0, 20); window.scrollBy(0, -20);");
        getLoaderSpinner().should(hidden, Duration.ofSeconds(10));
        getList1().last().scrollTo();
    }

    /**
     * Проверяет товары на текущей странице на соответствие критериям поиска
     * Для каждого товара проверяет наличие хотя бы одного из искомых ключевых слов
     * Добавляет в список ошибок товары, не соответствующие фильтру
     *
     * @param searchRes      список ключевых слов для проверки
     * @param endTime        время окончания выполнения проверки
     * @param time           общее время выполнения в секундах
     * @param productTimeout таймаут ожидания появления товара
     * @throws AssertionError если время выполнения истекло во время проверки
     * @author Сергей Лебедев
     */
    private void checkProductsOnPage(List<String> searchRes, Instant endTime, Integer time, Duration productTimeout) {
        ElementsCollection currentProductList = getList1();
        for (SelenideElement product : currentProductList) {
            if (Instant.now().isAfter(endTime)) {
                fail("Время выполнения теста истекло и превышает: " + (time / 60) + " минут.");
            }
            product.should(exist, productTimeout);
            String text = product.getText();
            if (!containsAnySearchTerm(text, searchRes)) {
                errors.add("  |   Товар: " + text + " не соответствует по фильтру : " + searchRes + "   |   ");
            }
        }
    }

    /**
     * Проверяет содержит ли текст хотя бы одно из искомых ключевых слов
     *
     * @param text      текст для проверки
     * @param searchRes список ключевых слов
     * @return true если текст содержит хотя бы одно ключевое слово, false в противном случае
     * @author Сергей Лебедев
     */
    private boolean containsAnySearchTerm(String text, List<String> searchRes) {
        return searchRes.stream().anyMatch(text::contains);
    }

    /**
     * Проверяет наличие товаров в сохранённом списке, которые не подходят по параметрам фильтрации
     * Выбрасывает исключение если в списке ошибок есть записи о несоответствующих товарах
     *
     * @return текущую страницу результатов YandexResultPage
     * @throws AssertionError если найдены товары не соответствующие критериям фильтрации
     * @author Сергей Лебедев
     */
    @Step("Проверка наличия товаров в сохранённом списке, которые не подходят по параметрам фильтрации")
    public YandexResultPage checkedError() {
        assertTrue(getErrors().isEmpty(), getErrors());
        return this;
    }
}