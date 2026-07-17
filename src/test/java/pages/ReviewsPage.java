package pages;

import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

/** Page Object полного блока отзывов о товаре. */
public class ReviewsPage extends BasePage {

    // СТАТИЧЕСКИЕ ПОЛЯ (константы)
    private static final int PAGE_LOAD_TIMEOUT_SEC = 30;
    private static final int SHORT_TIMEOUT_SEC = 2;

    private final SelenideElement overallRating = $x(
            "//*[contains(@class,'RedReviewsProductDescription__ratingContainer__')]");

    // КОНСТРУКТОРЫ
    public ReviewsPage() {
        // пустой конструктор
    }

    // ПУБЛИЧНЫЕ МЕТОДЫ

    public ReviewsPage waitUntilOpened() {
        webdriver().shouldHave(urlContaining("/reviews"), Duration.ofSeconds(PAGE_LOAD_TIMEOUT_SEC));
        dismissBlockingOverlays(Duration.ofSeconds(SHORT_TIMEOUT_SEC));
        overallRating.shouldBe(visible, Duration.ofSeconds(PAGE_LOAD_TIMEOUT_SEC));
        return this;
    }

    public boolean isOverallRatingDisplayed() {
        return overallRating.isDisplayed();
    }
}
