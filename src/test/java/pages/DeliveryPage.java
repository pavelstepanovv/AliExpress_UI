package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.executeJavaScript;

public class DeliveryPage extends BasePage {

    public DeliveryPage setCity(String city) {
        ElementsCollection allInputs = $$x("//input");
        SelenideElement cityInput = null;

        for (SelenideElement input : allInputs) {
            if (input.isDisplayed()) {
                String placeholder = input.getAttribute("placeholder");
                if (placeholder != null && (placeholder.toLowerCase().contains("адрес") ||
                        placeholder.toLowerCase().contains("индекс") ||
                        placeholder.toLowerCase().contains("город"))) {
                    cityInput = input;
                    break;
                }
            }
        }

        if (cityInput == null) {
            cityInput = $x("//input[contains(@class, 'address') or contains(@class, 'city')]");
        }

        cityInput.shouldBe(visible, Duration.ofSeconds(15));
        executeJavaScript("arguments[0].scrollIntoView(true);", cityInput);
        executeJavaScript("arguments[0].click();", cityInput);
        executeJavaScript("arguments[0].value = '';", cityInput);
        executeJavaScript("arguments[0].value = arguments[1];", cityInput, city);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return this;
    }

    public MainPage saveChanges() {
        SelenideElement saveButton = $x("//button[contains(text(), 'Сохранить')]");
        saveButton.shouldBe(visible, Duration.ofSeconds(15));
        executeJavaScript("arguments[0].click();", saveButton);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return new MainPage();
    }
}