package tests;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.chrome.ChromeOptions;

import static com.codeborne.selenide.Selenide.closeWebDriver;

/**
 * Базовый класс, от которого наследуются все классы-тесты.
 * Настраивает браузер перед каждым тестом и закрывает его после.
 */
public abstract class BaseTest {

    // ПУБЛИЧНЫЕ МЕТОДЫ
    /**
     * Открывается Chrome, настраивается размер окна, задаются динамические ожидания.
     */
    @BeforeEach
    public void setUp() {
        // Настройка браузера
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");

        Configuration.browser = "chrome";
        Configuration.headless = false;

        // Динамические ожидания
        Configuration.timeout = 10000;          // максимум 10 секунд
        Configuration.pollingInterval = 500;    // проверка каждые 500 мс

        Configuration.browserSize = "1920x1080";
        Configuration.browserCapabilities = options;
    }

    /**
     * Закрывает браузер после каждого теста.
     */
    @AfterEach
    public void tearDown() {
        closeWebDriver();
    }
}
