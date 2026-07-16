package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.$$x;

public class ReviewsPage extends BasePage {

    private final SelenideElement ratingElement = $x("//*[contains(@class, 'rating') or contains(@data-testid, 'rating')]//span[contains(@class, 'score') or contains(@class, 'number')]");
    private final ElementsCollection reviewItems = $$x("//div[contains(@class, 'review') or contains(@data-testid, 'review')]");
    private final ElementsCollection starElements = $$x("//*[contains(@class, 'star') or contains(@data-testid, 'star')]");
    private final SelenideElement noReviewsMessage = $x("//*[contains(normalize-space(), 'Нет отзывов') or contains(normalize-space(), 'No reviews') or contains(normalize-space(), 'Пока нет отзывов')]");
    private final SelenideElement ratingContainer = $x("//div[contains(@class, 'rating') or contains(@class, 'star-rating')]");

    public boolean isRatingDisplayed() {
        try {
            return ratingContainer.isDisplayed() || ratingElement.isDisplayed() || starElements.size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public double getRating() {
        String ratingText = ratingElement.getText().replace(",", ".").replaceAll("[^0-9.]", "");
        if (ratingText.isEmpty()) {
            return 0.0;
        }
        return Double.parseDouble(ratingText);
    }

    public int getReviewsCount() {
        try {
            reviewItems.shouldHave(sizeGreaterThan(0), Duration.ofSeconds(10));
            return reviewItems.size();
        } catch (AssertionError e) {
            System.out.println("Отзывы не найдены, возможно их нет");
            return 0;
        }
    }

    public boolean hasTextReviews() {
        return !reviewItems.isEmpty() && !noReviewsMessage.isDisplayed();
    }

    public List<Integer> getReviewStars() {
        List<Integer> stars = new ArrayList<>();
        for (SelenideElement starElement : starElements) {
            String starText = starElement.getAttribute("data-rating");
            if (starText == null) {
                starText = starElement.getText();
            }
            if (starText != null && !starText.isEmpty()) {
                try {
                    stars.add(Integer.parseInt(starText.replaceAll("[^0-9]", "")));
                } catch (NumberFormatException e) {
                    // пропускаем
                }
            }
        }
        return stars;
    }
}