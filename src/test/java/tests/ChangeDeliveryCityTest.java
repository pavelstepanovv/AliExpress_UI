package tests;

import org.junit.jupiter.api.Test;
import pages.DeliveryAddressModal;
import pages.MainPage;

import static org.assertj.core.api.Assertions.assertThat;

/** Тест 10: смена города доставки. */
public class ChangeDeliveryCityTest extends BaseTest {

    @Test
    public void testChangeDeliveryCity() {
        MainPage mainPage = new MainPage().open().closeLocationPopupIfPresent();
        DeliveryAddressModal addressModal = mainPage.openDeliveryAddress();

        addressModal.selectCity(DELIVERY_CITY).save();
        mainPage.waitUntilDeliveryCityDisplayed(DELIVERY_CITY);

        assertThat(mainPage.getDeliveryCity())
                .as("В шапке должен отображаться выбранный город доставки")
                .isEqualTo(DELIVERY_CITY);
    }
}
