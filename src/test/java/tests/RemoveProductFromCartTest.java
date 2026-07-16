package tests;

import org.junit.jupiter.api.Test;
import pages.CartPage;
import pages.MainPage;
import pages.ProductPage;
import pages.SearchResultPage;

import static org.assertj.core.api.Assertions.assertThat;

public class RemoveProductFromCartTest extends BaseTest {

    @Test
    public void testRemoveProductFromCart() {
        MainPage mainPage = new MainPage().open();
        SearchResultPage results = mainPage.search(USB_C_CABLE_QUERY);
        ProductPage productPage = results.openFirstProduct();

        productPage.addToCart();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        CartPage cartPage = productPage.openCart();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        cartPage.removeProduct();

        assertThat(cartPage.isCartEmpty())
                .as("Корзина должна быть пустой после удаления товара")
                .isTrue();

        assertThat(cartPage.isEmptyCartMessageDisplayed())
                .as("Должно отображаться сообщение о пустой корзине")
                .isTrue();
    }
}