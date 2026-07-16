package pages;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;
import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.sleep;

public class DeliveryPage extends BasePage {

    public DeliveryPage setCity(String city) {
        $x("//*[not(self::script or self::style)][contains(text(), 'Адрес доставки') or contains(text(), 'Выберите на карте')]")
                .shouldBe(visible, Duration.ofSeconds(15));

        SelenideElement addressContainer = $x("//span[contains(@class, 'AddressMap_Textarea')]");
        addressContainer.shouldBe(visible, Duration.ofSeconds(15)).click();
        sleep(1000);

        SelenideElement actualInput = $x("//span[contains(@class, 'AddressMap_Textarea')]//textarea | //span[contains(@class, 'AddressMap_Textarea')]//input");
        actualInput.shouldBe(visible, Duration.ofSeconds(5)).click();

        SelenideElement clearCross = $x("//span[contains(@class, 'AddressMap_Textarea')]/..//*[local-name()='svg'] | //span[contains(@class, 'AddressMap_Textarea')]//*[contains(@class, 'close') or contains(@class, 'clear')]");
        if (clearCross.exists() && clearCross.isDisplayed()) {
            clearCross.click();
        } else {
            actualInput.sendKeys(Keys.chord(Keys.COMMAND, "a"), Keys.BACK_SPACE);
            actualInput.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
            actualInput.clear();
        }
        sleep(500);

        actualInput.setValue(city);
        sleep(2000);

        actualInput.sendKeys(Keys.ARROW_DOWN);
        sleep(500);
        actualInput.sendKeys(Keys.ENTER);
        sleep(1000);

        SelenideElement suggestion = $$x("//*[not(self::input or self::textarea or self::script or self::style)][contains(text(), '" + city + "')]")
                .filterBy(visible)
                .last();

        if (suggestion.exists()) {
            suggestion.click();
            sleep(1000);
        }

        return this;
    }

    public MainPage saveChanges() {
        // Железобетонный локатор кнопки от разработчиков
        SelenideElement saveButton = $x("//button[@data-testid='addressMapSaveButton']");
        saveButton.shouldBe(visible, Duration.ofSeconds(10)).click();

        sleep(3000); // Даем время шапке обновиться
        return new MainPage();
    }
}