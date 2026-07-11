package tests;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.chrome.ChromeOptions;

import static com.codeborne.selenide.Selenide.closeWebDriver;

public class BaseTest {

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);

        Configuration.browser = "chrome";
        Configuration.headless = false;
        Configuration.timeout = 60000;
        Configuration.browserSize = "1920x1080";
        Configuration.browserCapabilities = options;
    }

    @AfterAll
    public static void tearDown() {
        // Блок написан Шакуров 4382: при общем запуске держим одну сессию Chrome, чтобы не провоцировать антибот-защиту.
        closeWebDriver();
    }
}
