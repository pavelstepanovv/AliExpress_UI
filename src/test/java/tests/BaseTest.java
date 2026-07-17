package tests;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.chrome.ChromeOptions;

@ExtendWith(BrowserSessionExtension.class)
public abstract class BaseTest {

    // СТАТИЧЕСКИЕ ПОЛЯ (константы)
    protected static final String HEADPHONES_QUERY = "наушники";
    protected static final String HEADPHONES_PARTIAL_QUERY = "науш";
    protected static final String CART_PRODUCT_QUERY = "брелок";
    protected static final String CART_PRODUCT_EXPECTED_WORD = "брелок";
    protected static final String DELIVERY_CITY = "Санкт-Петербург";
    protected static final int CART_PRODUCT_QUANTITY = 3;
    protected static final int MAX_PRICE = 200;
    protected static final int BROWSER_WIDTH = 1920;
    protected static final int BROWSER_HEIGHT = 1080;
    protected static final int TIMEOUT_SECONDS = 60;
    protected static final int TIMEOUT_MILLIS = TIMEOUT_SECONDS * 1000;

    // КОНСТРУКТОРЫ
    public BaseTest() {
        // пустой конструктор
    }

    // ПУБЛИЧНЫЕ МЕТОДЫ

    private static ChromeOptions createChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        addChromeArguments(options);
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);
        return options;
    }

    private static void addChromeArguments(ChromeOptions options) {
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=" + BROWSER_WIDTH + "," + BROWSER_HEIGHT);
    }

    static void configureSelenide() {
        ChromeOptions options = createChromeOptions();
        Configuration.browser = "chrome";
        Configuration.headless = false;
        Configuration.timeout = TIMEOUT_MILLIS;
        Configuration.browserSize = BROWSER_WIDTH + "x" + BROWSER_HEIGHT;
        Configuration.browserCapabilities = options;
    }
}
