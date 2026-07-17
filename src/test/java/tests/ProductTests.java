package tests;

import org.junit.jupiter.api.Test;
import pages.MainPage;
import pages.ProductPage;
import pages.ReviewsPage;
import pages.SearchResultPage;
import pages.SellerPage;
import pages.SharePopup;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Группа тестов для проверки функционала карточки товара.
 * Включает: открытие карточки, отзывы, поделиться, продавец, количество.
 */
public class ProductTests extends BaseTest {
    // 1. СТАТИЧЕСКИЕ ПОЛЯ
    private static final String SEARCH_QUERY = "наушники";

    // 2. ТЕСТЫ

    /**
     * Тест №4: Открытие карточки товара.
     * Автор: Шакуров Альберт, группа 4382
     *
     * Шаги:
     * 1. Открыть главную страницу
     * 2. Выполнить поиск "наушники"
     * 3. Открыть карточку первого товара
     * 4. Проверить, что отображается название товара
     * 5. Проверить, что отображается цена товара
     * 6. Проверить, что отображается кнопка "В корзину"
     */
    @Test
    public void testOpenProductCard() {
        // 1. Открыть главную страницу
        MainPage mainPage = new MainPage().open();

        // 2. Выполнить поиск
        SearchResultPage results = mainPage.search(SEARCH_QUERY);

        // 3. Открыть карточку первого товара
        ProductPage productPage = results.openFirstProduct();

        // 4. Проверить, что отображается название товара
        assertThat(productPage.isTitleDisplayed())
                .as("В карточке товара должно отображаться название")
                .isTrue();

        // 5. Проверить, что отображается цена товара
        assertThat(productPage.isPriceDisplayed())
                .as("В карточке товара должна отображаться цена")
                .isTrue();

        // 6. Проверить, что отображается кнопка "В корзину"
        assertThat(productPage.hasAddToCartButton())
                .as("В карточке товара должна отображаться кнопка 'В корзину'")
                .isTrue();
    }

    /**
     * Тест №8: Просмотр отзывов.
     * Автор: Кислица Сергей, группа 4388
     *
     * Шаги:
     * 1. Открыть главную страницу (закрыть попап выбора региона, если появился)
     * 2. Выполнить поиск "наушники"
     * 3. Открыть карточку первого товара
     * 4. Открыть полный список отзывов
     * 5. Проверить, что отображается общий рейтинг товара
     */
    @Test
    public void testProductReviewsRating() {
        // 1. Открыть главную страницу (закрыть попап выбора региона, если появился)
        MainPage mainPage = new MainPage().open().closeLocationPopupIfPresent();

        // 2. Выполнить поиск
        SearchResultPage results = mainPage.search(SEARCH_QUERY);

        // 3. Открыть карточку первого товара
        ProductPage productPage = results.openFirstProduct();

        // 4. Открыть полный список отзывов
        ReviewsPage reviewsPage = productPage.openAllReviews();

        // 5. Проверить, что отображается общий рейтинг товара
        assertThat(reviewsPage.isOverallRatingDisplayed())
                .as("На странице отзывов должен отображаться общий рейтинг товара")
                .isTrue();
    }

    /**
     * Тест №9: Открытие окна "Поделиться" в карточке товара.
     * Автор: Кислица Сергей, группа 4388
     *
     * Шаги:
     * 1. Открыть главную страницу (закрыть попап выбора региона, если появился)
     * 2. Выполнить поиск "наушники"
     * 3. Открыть карточку первого товара
     * 4. Открыть окно "Поделиться"
     * 5. Проверить, что окно отображается
     * 6. Проверить, что отображается кнопка "Скопировать ссылку"
     * 7. Проверить, что отображаются иконки социальных сетей
     */
    @Test
    public void testOpenSharePopup() {
        // 1. Открыть главную страницу (закрыть попап выбора региона, если появился)
        MainPage mainPage = new MainPage().open().closeLocationPopupIfPresent();

        // 2. Выполнить поиск
        SearchResultPage results = mainPage.search(SEARCH_QUERY);

        // 3. Открыть карточку первого товара
        ProductPage productPage = results.openFirstProduct();

        // 4. Открыть окно "Поделиться"
        SharePopup sharePopup = productPage.openSharePopup();

        // 5. Проверить, что окно отображается
        assertThat(sharePopup.isDisplayed())
                .as("Окно 'Поделиться' должно отображаться")
                .isTrue();

        // 6. Проверить, что отображается кнопка "Скопировать ссылку"
        assertThat(sharePopup.isCopyLinkButtonDisplayed())
                .as("В окне должна отображаться кнопка 'Скопировать ссылку'")
                .isTrue();

        // 7. Проверить, что отображаются иконки социальных сетей
        assertThat(sharePopup.getVisibleSocialIconsCount())
                .as("В окне должны отображаться иконки социальных сетей")
                .isGreaterThan(0);
    }

    /**
     * Тест №14: Проверка ссылки на продавца.
     * Автор: Миридаштаки Фарид, группа 4343
     *
     * Шаги:
     * 1. Открыть главную страницу (закрыть попап выбора региона, если появился)
     * 2. Выполнить поиск "наушники"
     * 3. Открыть карточку первого товара
     * 4. Нажать на название магазина на странице товара
     * 5. Проверить, что страница магазина открылась
     * 6. Проверить, что название магазина отображается
     * 7. Проверить, что название магазина не пустое
     */
    @Test
    public void testSellerLinkOpensShopPage() {
        // 1. Открыть главную страницу (закрыть попап выбора региона, если появился)
        MainPage mainPage = new MainPage().open().closeLocationPopupIfPresent();

        // 2. Выполнить поиск
        SearchResultPage results = mainPage.search(SEARCH_QUERY);

        // 3. Открыть карточку первого товара
        ProductPage productPage = results.openFirstProduct();

        // 4. Нажать на название магазина
        SellerPage sellerPage = productPage.openSellerPage();

        // 5. Проверить, что название магазина отображается
        assertThat(sellerPage.isShopTitleDisplayed())
                .as("Название магазина должно быть видно на странице продавца")
                .isTrue();

        // 6. Проверить, что название магазина не пустое
        String shopTitle = sellerPage.getShopTitle();
        assertThat(shopTitle)
                .as("Название магазина не должно быть пустым")
                .isNotBlank();
    }

    /**
     * Тест №15: Проверка выбора количества товара в карточке.
     * Автор: Миридаштаки Фарид, группа 4343
     *
     * Шаги:
     * 1. Открыть главную страницу (закрыть попап выбора региона, если появился)
     * 2. Выполнить поиск "наушники"
     * 3. Открыть карточку первого товара
     * 4. Добавить товар в корзину (кнопка превращается в счётчик)
     * 5. Проверить, что счётчик показывает "1"
     * 6. Нажать "+" для увеличения количества
     * 7. Проверить, что счётчик показывает "2"
     */
    @Test
    public void testIncreaseQuantityToTwo() {
        // 1. Открыть главную страницу (закрыть попап выбора региона, если появился)
        MainPage mainPage = new MainPage().open().closeLocationPopupIfPresent();

        // 2. Выполнить поиск
        SearchResultPage results = mainPage.search(SEARCH_QUERY);

        // 3. Открыть карточку первого товара
        ProductPage productPage = results.openFirstProduct();

        // 4. Добавить товар в корзину (кнопка превращается в счётчик)
        productPage.addToCart();

        // 5. Проверить, что счётчик показывает "1"
        assertThat(productPage.getCurrentQuantity())
                .as("После добавления в корзину счётчик должен показывать '1'")
                .isEqualTo("1");

        // 6. Нажать "+" для увеличения количества
        productPage.incrementQuantity();

        // 7. Проверить, что счётчик показывает "2"
        assertThat(productPage.getCurrentQuantity())
                .as("После нажатия '+' счётчик должен показывать '2'")
                .isEqualTo("2");
    }
}
