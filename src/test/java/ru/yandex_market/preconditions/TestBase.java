package ru.yandex_market.preconditions;

import allure.CustomAllure;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import config.WebDriverConfig;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.BeforeAll;

public class TestBase {

    protected static WebDriverConfig config = ConfigFactory.create(WebDriverConfig.class);

    @BeforeAll
    static void setUp() {
        WebDriverManager.chromedriver().setup();
        Configuration.browserSize = config.getBrowserSize();
        Configuration.browser = config.getBrowserName();
        SelenideLogger.addListener("AllureSelenide", new CustomAllure().screenshots(true).savePageSource(true));
    }

}
