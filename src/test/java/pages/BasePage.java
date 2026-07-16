package pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.actions;
import static com.codeborne.selenide.Selenide.executeJavaScript;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

/**
 * Базовый класс страницы с общими действиями для Page Object.
 * Автор: Шакуров 4382.
 */
public class BasePage {

    /**
     * Открывает страницу по переданному URL.
     */
    protected void open(String url) {
        Selenide.open(url);
    }

    /**
     * Возвращает заголовок текущей страницы.
     */
    public String getTitle() {
        return Selenide.title();
    }

    /**
     * Активирует текущую страницу после переходов, где AliExpress оставляет окно без фокуса.
     */
    protected void activatePage() {
        clickBrowserWindow();
        executeJavaScript("window.focus();");
        SelenideElement body = $("body").shouldBe(visible, Duration.ofSeconds(10));
        actions().moveToElement(body).click().perform();
        executeJavaScript("if (document.body) document.body.focus();");
    }

    /**
     * Выполняет системный клик по окну браузера, чтобы Chrome получил фокус ОС.
     */
    private void clickBrowserWindow() {
        try {
            WebDriver driver = getWebDriver();
            driver.switchTo().window(driver.getWindowHandle());

            Point position = driver.manage().window().getPosition();
            Dimension size = driver.manage().window().getSize();
            int x = position.getX() + size.getWidth() / 2;
            int y = position.getY() + size.getHeight() / 2;

            Robot robot = new Robot();
            robot.mouseMove(x, y);
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        } catch (AWTException | WebDriverException ignored) {
            // Если системный клик недоступен, ниже остается DOM-активация страницы.
        }
    }
}
