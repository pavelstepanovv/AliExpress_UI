package tests;

import org.junit.jupiter.api.Test;
import pages.MainPage;
import pages.DeliveryPage;

import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.sleep;
import static org.assertj.core.api.Assertions.assertThat;

public class ChangeDeliveryCityTest extends BaseTest {

    private static final String DELIVERY_CITY = "Санкт-Петербург";

    @Test
    public void testChangeDeliveryCity() {
        MainPage mainPage = new MainPage().open();

        System.out.println("Если появилась капча, пройдите её вручную. Ожидание...");
        sleep(30000);

        DeliveryPage deliveryPage = mainPage.openDeliverySettings();

        deliveryPage.setCity(DELIVERY_CITY);

        mainPage = deliveryPage.saveChanges();

        String actualCity = mainPage.getDeliveryCity();
        System.out.println("Город доставки: " + actualCity);

        assertThat(actualCity)
                .as("В шапке сайта должен отображаться выбранный город")
                .containsIgnoringCase(DELIVERY_CITY);
    }
}