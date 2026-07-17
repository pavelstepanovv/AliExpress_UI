package pages;

import elements.Button;
import elements.Input;
import elements.Item;

import java.time.Duration;

/** Модальное окно изменения адреса доставки. */
public class DeliveryAddressModal extends BasePage {

    // СТАТИЧЕСКИЕ ПОЛЯ (константы)
    private static final int MODAL_TIMEOUT_SEC = 15;
    private static final int SAVE_TIMEOUT_SEC = 30;
    private static final int CHECK_VISIBILITY_TIMEOUT_SEC = 2;

    // ПОЛЯ ЭКЗЕМПЛЯРА
    private final Item modal = Item.byClass("AddressMap_Modal__wrapper__");
    private final Item manualAddressButton = Item.byTestId("addressMapEditButton");
    private final Input cityInput = Input.byTestId("addressMapFormCity");
    private final Button saveButton = Button.byTestId("addressMapFormSaveButton");

    // КОНСТРУКТОРЫ
    public DeliveryAddressModal() {
        // пустой конструктор
    }

    // ПУБЛИЧНЫЕ МЕТОДЫ

    public DeliveryAddressModal waitUntilOpened() {
        modal.waitUntilVisible(Duration.ofSeconds(MODAL_TIMEOUT_SEC));
        return this;
    }

    public DeliveryAddressModal selectCity(String city) {
        openManualFormIfNeeded();
        cityInput.waitUntilEditable(Duration.ofSeconds(MODAL_TIMEOUT_SEC));
        cityInput.fill(city);

        Item citySuggestion = Item.byClassAndText(
                "AddressMap_Select__title__",
                city + ", город " + city);
        citySuggestion.waitUntilVisible(Duration.ofSeconds(MODAL_TIMEOUT_SEC));
        citySuggestion.click();
        return this;
    }

    public void save() {
        saveButton.clickWhenEnabled(Duration.ofSeconds(SAVE_TIMEOUT_SEC));
        modal.waitUntilHidden();
    }

    private void openManualFormIfNeeded() {
        if (!cityInput.isDisplayed(Duration.ofSeconds(CHECK_VISIBILITY_TIMEOUT_SEC))) {
            manualAddressButton.waitUntilVisible(Duration.ofSeconds(MODAL_TIMEOUT_SEC));
            manualAddressButton.click();
        }
    }
}
