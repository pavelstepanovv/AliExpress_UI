package tests;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.chrome.ChromeOptions;

public abstract class BaseTest {

    protected static final String HEADPHONES_QUERY = "наушники";
    protected static final String HEADPHONES_PARTIAL_QUERY = "науш";
    protected static final String USB_C_CABLE_QUERY = "кабель 1 м";
    protected static final String CART_PRODUCT_QUERY = "кабель";
    protected static final String CART_PRODUCT_EXPECTED_WORD = "кабель";
    protected static final int CART_PRODUCT_QUANTITY = 3;
    protected static final int MAX_PRICE = 200;

    @BeforeEach
    public void setUp() {
        configureSelenide(createChromeOptions());
    }

    private ChromeOptions createChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        addChromeArguments(options);
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);
        return options;
    }

    private void addChromeArguments(ChromeOptions options) {
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
    }

    private void configureSelenide(ChromeOptions options) {
        Configuration.browser = "chrome";
        Configuration.headless = false;
        Configuration.timeout = 60000;
        Configuration.browserSize = "1920x1080";
        Configuration.browserCapabilities = options;
    }
}