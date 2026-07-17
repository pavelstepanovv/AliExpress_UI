package tests;

import org.junit.jupiter.api.Test;
import pages.CartPage;
import pages.MainPage;
import pages.ProductPage;
import pages.SearchResultPage;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Группа тестов для проверки функционала корзины.
 * Включает: добавление товара, изменение количества, удаление товара.
 */
public class CartTests extends BaseTest {
    // 1. СТАТИЧЕСКИЕ ПОЛЯ
    private static final String CART_PRODUCT_QUERY = "брелок";
    private static final String CART_PRODUCT_EXPECTED_WORD = "брелок";
    private static final int CART_PRODUCT_QUANTITY = 3;
    private static final String REMOVE_SEARCH_QUERY = "цепочка";

    // 2. ТЕСТЫ

    /**
     * Тест №5: Добавление товара в корзину и изменение его количества.
     * Автор: Шакуров Альберт, группа 4382
     *
     * Шаги:
     * 1. Открыть главную страницу
     * 2. Выполнить поиск "брелок"
     * 3. Открыть карточку первого товара
     * 4. Добавить товар в корзину
     * 5. Открыть корзину
     * 6. Увеличить количество товара до 3
     * 7. Проверить, что в корзине отображается добавленный товар
     * 8. Проверить, что количество товара равно 3
     * 9. Проверить, что итоговая стоимость пересчитана
     */
    @Test
    public void testAddProductToCartAndChangeQuantity() {
        // 1. Открыть главную страницу
        MainPage mainPage = new MainPage().open();

        // 2. Выполнить поиск
        SearchResultPage results = mainPage.search(CART_PRODUCT_QUERY);

        // 3. Открыть карточку первого товара
        ProductPage productPage = results.openFirstProduct();

        // 4. Добавить товар в корзину
        productPage.addToCart();

        // 5. Открыть корзину
        CartPage cartPage = productPage.openCart();

        // 6. Увеличить количество до 3
        int unitPrice = cartPage.getProductUnitPrice();
        cartPage.increaseProductQuantityTo(CART_PRODUCT_QUANTITY)
                .waitTotalRecalculatedForQuantity(CART_PRODUCT_QUANTITY);

        // 7. Проверить, что отображается добавленный товар
        assertThat(cartPage.getProductTitle())
                .as("В корзине должен отображаться добавленный товар")
                .containsIgnoringCase(CART_PRODUCT_EXPECTED_WORD);

        // 8. Проверить, что количество равно 3
        assertThat(cartPage.getProductQuantity())
                .as("Количество товара в корзине должно быть равно %d", CART_PRODUCT_QUANTITY)
                .isEqualTo(CART_PRODUCT_QUANTITY);

        // 9. Проверить, что итоговая стоимость пересчитана
        assertThat(cartPage.getTotalPrice())
                .as("Итоговая стоимость должна быть пересчитана с учетом количества")
                .isEqualTo(unitPrice * CART_PRODUCT_QUANTITY);
    }

    /**
     * Тест №6: Удаление товара из корзины.
     * Автор: Кислица Сергей, группа 4388
     *
     * Шаги:
     * 1. Открыть главную страницу (закрыть попап выбора региона, если появился)
     * 2. Выполнить поиск "цепочка"
     * 3. Открыть карточку первого товара
     * 4. Добавить товар в корзину
     * 5. Открыть корзину
     * 6. Удалить все товары из корзины
     * 7. Проверить, что корзина стала пустой
     */
    @Test
    public void testRemoveProductFromCart() {
        // 1. Открыть главную страницу (закрыть попап выбора региона, если появился)
        MainPage mainPage = new MainPage().open().closeLocationPopupIfPresent();

        // 2. Выполнить поиск
        SearchResultPage results = mainPage.search(REMOVE_SEARCH_QUERY);

        // 3. Открыть карточку первого товара
        ProductPage productPage = results.openFirstProduct();

        // 4. Добавить товар в корзину
        productPage.addToCart();

        // 5. Открыть корзину
        CartPage cartPage = productPage.openCart();

        // 6. Удалить все товары
        cartPage.removeAllProducts();

        // 7. Проверить, что корзина стала пустой
        assertThat(cartPage.isCartEmpty())
                .as("После удаления товара должно отображаться сообщение о пустой корзине")
                .isTrue();
    }
}
