package tests;

import org.junit.jupiter.api.Test;
import pages.MainPage;
import pages.ProductPage;
import pages.SearchResultPage;
import pages.ReviewsPage;

import static org.assertj.core.api.Assertions.assertThat;

public class ViewProductReviewsTest extends BaseTest {

    @Test
    public void testViewProductReviews() {
        MainPage mainPage = new MainPage().open();
        SearchResultPage results = mainPage.search(HEADPHONES_QUERY);
        ProductPage productPage = results.openFirstProduct();

        ReviewsPage reviewsPage = productPage.openReviews();

        assertThat(reviewsPage.isRatingDisplayed())
                .as("Должен отображаться общий рейтинг товара")
                .isTrue();

        assertThat(reviewsPage.getReviewsCount())
                .as("Должен быть хотя бы один отзыв")
                .isGreaterThan(0);

        if (reviewsPage.hasTextReviews()) {
            assertThat(reviewsPage.getReviewStars())
                    .as("У текстовых отзывов должны быть звёзды рейтинга")
                    .allMatch(stars -> stars >= 1 && stars <= 5);
        }
    }
}