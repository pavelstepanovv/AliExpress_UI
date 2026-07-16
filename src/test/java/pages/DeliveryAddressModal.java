package pages;

import elements.Button;
import elements.Input;
import elements.Item;

import java.time.Duration;

/** Модальное окно изменения адреса доставки. */
public class DeliveryAddressModal extends BasePage {

    private final Item modal = Item.byClass("AddressMap_Modal__wrapper__");
    private final Item manualAddressButton = Item.byTestId("addressMapEditButton");
    private final Input cityInput = Input.byTestId("addressMapFormCity");
    private final Button saveButton = Button.byTestId("addressMapFormSaveButton");

    public DeliveryAddressModal waitUntilOpened() {
        modal.waitUntilVisible(Duration.ofSeconds(15));
        return this;
    }

    /** Заполняет город и выбирает точную подсказку из списка. */
    public DeliveryAddressModal selectCity(String city) {
        openManualFormIfNeeded();
        cityInput.waitUntilEditable(Duration.ofSeconds(15));
        cityInput.fill(city);

        Item citySuggestion = Item.byClassAndText(
                "AddressMap_Select__title__",
                city + ", город " + city);
        citySuggestion.waitUntilVisible(Duration.ofSeconds(15));
        citySuggestion.click();
        return this;
    }

    /** Сохраняет выбранный город и ждёт закрытие окна. */
    public void save() {
        saveButton.clickWhenEnabled(Duration.ofSeconds(30));
        modal.waitUntilHidden();
    }

    private void openManualFormIfNeeded() {
        if (!cityInput.isDisplayed(Duration.ofSeconds(2))) {
            manualAddressButton.waitUntilVisible(Duration.ofSeconds(15));
            manualAddressButton.click();
        }
    }
}
