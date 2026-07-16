package tests;

import org.junit.jupiter.api.Test;
import pages.MainPage;
import pages.ProductPage;
import pages.ReviewsPage;
import pages.SearchResultPage;

import static org.assertj.core.api.Assertions.assertThat;

/** Тест 8: просмотр отзывов первого товара из поисковой выдачи. */
public class ProductReviewsTest extends BaseTest {

    /** Проверяет появление общего рейтинга на странице отзывов. */
    @Test
    public void testProductReviewsRating() {
        MainPage mainPage = new MainPage().open().closeLocationPopupIfPresent();
        SearchResultPage results = mainPage.search(HEADPHONES_QUERY);
        ProductPage productPage = results.openFirstProduct();
        ReviewsPage reviewsPage = productPage.openAllReviews();

        assertThat(reviewsPage.isOverallRatingDisplayed())
                .as("На странице отзывов должен отображаться общий рейтинг товара")
                .isTrue();
    }
}
