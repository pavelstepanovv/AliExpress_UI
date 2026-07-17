package pages;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.$$x;

/**
 * Страница настроек доставки.
 * Содержит поле для ввода города и кнопку сохранения.
 */
public class DeliveryPage extends BasePage {

    // СТАТИЧЕСКИЕ ПОЛЯ (константы)
    private static final int HEADER_TIMEOUT_SEC = 15;
    private static final int CONTAINER_TIMEOUT_SEC = 15;
    private static final int INPUT_TIMEOUT_SEC = 5;
    private static final int SAVE_BUTTON_TIMEOUT_SEC = 10;

    // СТАТИЧЕСКИЕ ПОЛЯ (локаторы)
    private static final String HEADER_XPATH =
            "//*[not(self::script or self::style)]" +
                    "[contains(text(), 'Адрес доставки') or contains(text(), 'Выберите на карте')]";
    private static final String ADDRESS_CONTAINER_XPATH =
            "//span[contains(@class, 'AddressMap_Textarea')]";
    private static final String INPUT_XPATH =
            "//span[contains(@class, 'AddressMap_Textarea')]//textarea | " +
                    "//span[contains(@class, 'AddressMap_Textarea')]//input";
    private static final String CLEAR_CROSS_XPATH =
            "//span[contains(@class, 'AddressMap_Textarea')]/..//*[local-name()='svg'] | " +
                    "//span[contains(@class, 'AddressMap_Textarea')]//*[contains(@class, 'close') or contains(@class, 'clear')]";
    private static final String SUGGESTION_XPATH_TEMPLATE =
            "//*[not(self::input or self::textarea or self::script or self::style)]" +
                    "[contains(text(), '%s')]";
    private static final String SAVE_BUTTON_XPATH =
            "//button[@data-testid='addressMapSaveButton']";

    // ПОЛЯ ЭКЗЕМПЛЯРА
    private final SelenideElement headerElement = $x(HEADER_XPATH);
    private final SelenideElement addressContainer = $x(ADDRESS_CONTAINER_XPATH);
    private final SelenideElement inputElement = $x(INPUT_XPATH);
    private final SelenideElement clearCross = $x(CLEAR_CROSS_XPATH);
    private final SelenideElement saveButton = $x(SAVE_BUTTON_XPATH);

    // КОНСТРУКТОРЫ
    public DeliveryPage() {
        // пустой конструктор
    }

    // ПУБЛИЧНЫЕ МЕТОДЫ

    public DeliveryPage setCity(String city) {
        waitForDeliveryHeader();
        clickAddressContainer();
        clearInput();
        enterCity(city);
        selectFirstSuggestion(city);
        return this;
    }

    public MainPage saveChanges() {
        clickSaveButton();
        return new MainPage();
    }

    // ПРИВАТНЫЕ МЕТОДЫ

    private void waitForDeliveryHeader() {
        headerElement.shouldBe(visible, Duration.ofSeconds(HEADER_TIMEOUT_SEC));
    }

    private void clickAddressContainer() {
        addressContainer.shouldBe(visible, Duration.ofSeconds(CONTAINER_TIMEOUT_SEC));
        addressContainer.click();
    }

    private void clearInput() {
        inputElement.shouldBe(visible, Duration.ofSeconds(INPUT_TIMEOUT_SEC));
        inputElement.click();

        if (clearCross.exists() && clearCross.isDisplayed()) {
            clearCross.click();
        } else {
            inputElement.sendKeys(Keys.chord(Keys.COMMAND, "a"), Keys.BACK_SPACE);
            inputElement.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
            inputElement.clear();
        }
    }

    private void enterCity(String city) {
        inputElement.setValue(city);
    }

    private void selectFirstSuggestion(String city) {
        inputElement.sendKeys(Keys.ARROW_DOWN);
        inputElement.sendKeys(Keys.ENTER);

        SelenideElement suggestion = $$x(
                String.format(SUGGESTION_XPATH_TEMPLATE, city)
        ).filterBy(visible).last();

        if (suggestion.exists()) {
            suggestion.click();
        }
    }

    private void clickSaveButton() {
        saveButton.shouldBe(visible, Duration.ofSeconds(SAVE_BUTTON_TIMEOUT_SEC));
        saveButton.click();
    }
}
