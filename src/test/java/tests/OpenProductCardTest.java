package tests;

import org.junit.jupiter.api.Test;
import pages.MainPage;
import pages.ProductPage;
import pages.SearchResultPage;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Тест проверяет открытие карточки товара из поисковой выдачи AliExpress.
 * Автор: Шакуров 4382.
 */
public class OpenProductCardTest extends BaseTest {

    /**
     * Проверяет, что после поиска можно открыть первый товар и увидеть основные элементы карточки.
     */
    @Test
    public void testOpenProductCard() {
        MainPage mainPage = new MainPage().open();
        SearchResultPage results = mainPage.search(HEADPHONES_QUERY);

        ProductPage productPage = results.openFirstProduct();

        assertThat(productPage.isTitleDisplayed())
                .as("В карточке товара должно отображаться название")
                .isTrue();

        assertThat(productPage.isPriceDisplayed())
                .as("В карточке товара должна отображаться цена")
                .isTrue();

        assertThat(productPage.hasAddToCartButton())
                .as("В карточке товара должна отображаться кнопка 'В корзину'")
                .isTrue();
    }
}
