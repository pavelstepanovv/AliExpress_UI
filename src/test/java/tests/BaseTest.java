package tests;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Базовый класс для UI-тестов AliExpress.
 * Автор: Шакуров 4382.
 */
@ExtendWith(BrowserSessionExtension.class)
public abstract class BaseTest {

    protected static final String HEADPHONES_QUERY = "наушники";
    protected static final String HEADPHONES_PARTIAL_QUERY = "науш";
    protected static final String CART_PRODUCT_QUERY = "брелок";
    protected static final String CART_PRODUCT_EXPECTED_WORD = "брелок";
    protected static final String DELIVERY_CITY = "Санкт-Петербург";
    protected static final int CART_PRODUCT_QUANTITY = 3;
    protected static final int MAX_PRICE = 200;

    /**
     * Создает настройки Chrome для стабильного запуска UI-тестов.
     */
    private static ChromeOptions createChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        addChromeArguments(options);
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);
        return options;
    }

    /**
     * Добавляет аргументы запуска Chrome.
     */
    private static void addChromeArguments(ChromeOptions options) {
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
    }

    /**
     * Применяет общие настройки Selenide.
     */
    static void configureSelenide() {
        ChromeOptions options = createChromeOptions();
        Configuration.browser = "chrome";
        Configuration.headless = false;
        Configuration.timeout = 60000;
        Configuration.browserSize = "1920x1080";
        Configuration.browserCapabilities = options;
    }
}
