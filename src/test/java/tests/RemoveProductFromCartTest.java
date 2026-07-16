package tests;

import org.junit.jupiter.api.Test;
import pages.CartPage;
import pages.MainPage;
import pages.ProductPage;
import pages.SearchResultPage;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Тест 6: удаление товара из корзины.
 */
public class RemoveProductFromCartTest extends BaseTest {

    private static final String SEARCH_QUERY = "цепочка";

    /** Проверяет, что после удаления единственного товара корзина становится пустой. */
    @Test
    public void testRemoveProductFromCart() {
        MainPage mainPage = new MainPage().open().closeLocationPopupIfPresent();
        SearchResultPage results = mainPage.search(SEARCH_QUERY);
        ProductPage productPage = results.openFirstProduct();

        productPage.addToCart();
        CartPage cartPage = productPage.openCart();
        cartPage.removeAllProducts();

        assertThat(cartPage.isCartEmpty())
                .as("После удаления товара должно отображаться сообщение о пустой корзине")
                .isTrue();
    }
}
