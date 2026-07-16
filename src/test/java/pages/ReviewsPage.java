package pages;

import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

/** Page Object полного блока отзывов о товаре. */
public class ReviewsPage extends BasePage {

    private final SelenideElement overallRating = $x(
            "//*[contains(@class,'RedReviewsProductDescription__ratingContainer__')]");

    /** Ждёт переход на страницу отзывов и появление общего рейтинга. */
    public ReviewsPage waitUntilOpened() {
        webdriver().shouldHave(urlContaining("/reviews"), Duration.ofSeconds(30));
        dismissBlockingOverlays(Duration.ofSeconds(2));
        overallRating.shouldBe(visible, Duration.ofSeconds(30));
        return this;
    }

    /** Проверяет, что общий рейтинг товара отображается. */
    public boolean isOverallRatingDisplayed() {
        return overallRating.isDisplayed();
    }
}
