package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.time.Duration;
import java.util.Set;

import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.actions;
import static com.codeborne.selenide.Selenide.executeJavaScript;
import static com.codeborne.selenide.Selenide.switchTo;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

/**
 * Базовый класс страницы с общими действиями для Page Object.
 * Автор: Шакуров 4382.
 */
public class BasePage {

    private final ElementsCollection locationConfirmButtons = $$x(
            "//div[contains(@class,'ShipToHeaderItem_GeoTooltip__tooltip')]" +
                    "//button[normalize-space()='Да, верно' or contains(normalize-space(), 'Yes')]");
    private final ElementsCollection privacyConfirmButtons = $$x(
            "//*[contains(@class,'PrivacyPolicyBanner')]" +
                    "//button[normalize-space()='Понятно' or normalize-space()='OK']");

    /**
     * Открывает страницу по переданному URL.
     */
    protected void open(String url) {
        Selenide.open(url);
    }

    /** Закрывает редкие системные слои, которые могут перекрыть элементы страницы. */
    protected void dismissBlockingOverlays() {
        dismissBlockingOverlays(Duration.ofMillis(300));
    }

    /** Динамически ждёт позднее геоподтверждение и закрывает известные блокирующие слои. */
    protected void dismissBlockingOverlays(Duration timeout) {
        dismissVisibleElement(locationConfirmButtons, timeout);
        dismissVisibleElement(privacyConfirmButtons, Duration.ofMillis(300));
    }

    private void dismissVisibleElement(ElementsCollection elements, Duration timeout) {
        SelenideElement visibleElement = elements.findBy(visible);
        if (visibleElement.is(visible, timeout)) {
            executeJavaScript("arguments[0].click();", visibleElement);
            visibleElement.should(disappear, Duration.ofSeconds(10));
        }
    }

    /**
     * Возвращает заголовок текущей страницы.
     */
    public String getTitle() {
        return Selenide.title();
    }

    /**
     * Обновляет текущую страницу.
     */
    public void refresh() {
        Selenide.refresh();
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

    /** Переключается на новую вкладку и закрывает предыдущие, если сайт открыл её при переходе. */
    protected void switchToNewWindowIfOpened(Set<String> previousWindowHandles,
                                               String expectedUrlPart) {
        new WebDriverWait(getWebDriver(), Duration.ofSeconds(10))
                .until(driver -> driver.getWindowHandles().size() > previousWindowHandles.size()
                        || driver.getCurrentUrl().contains(expectedUrlPart));

        for (String windowHandle : getWebDriver().getWindowHandles()) {
            if (!previousWindowHandles.contains(windowHandle)) {
                switchTo().window(windowHandle);
                closeWindows(previousWindowHandles, windowHandle);
                return;
            }
        }
    }

    private void closeWindows(Set<String> windowHandles, String windowToKeep) {
        for (String windowHandle : windowHandles) {
            if (getWebDriver().getWindowHandles().contains(windowHandle)) {
                switchTo().window(windowHandle);
                getWebDriver().close();
            }
        }
        switchTo().window(windowToKeep);
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
