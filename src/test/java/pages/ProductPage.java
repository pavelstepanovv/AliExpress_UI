package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import elements.Button;
import elements.Link;

import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.$$x;

public class ProductPage extends BasePage {

    private final SelenideElement priceElement = $x("//div[contains(@class, 'price')]");
    private final Button addToCartButton = Button.byText("В корзину");
    private final Button shareButton = Button.byText("Поделиться");
    private final Link sellerLink = Link.byText("Продавец");

    public boolean isPriceDisplayed() {
        return priceElement.isDisplayed();
    }

    public String getPrice() {
        return priceElement.getText();
    }

    public void addToCart() {
        addToCartButton.click();
    }

    public void clickShare() {
        shareButton.click();
    }

    public void clickSeller() {
        sellerLink.click();
    }

    public boolean hasAddToCartButton() {
        return addToCartButton.isDisplayed();
    }

    public void selectQuantity(int quantity) {
        SelenideElement quantityInput = $x("//input[contains(@class, 'quantity')]");
        quantityInput.clear();
        quantityInput.sendKeys(String.valueOf(quantity));
    }

    public void clickReviewsTab() {
        SelenideElement reviewsTab = $x("//div[contains(text(), 'Отзывы')]");
        reviewsTab.click();
    }

    public boolean isReviewsBlockDisplayed() {
        SelenideElement reviewsBlock = $x("//div[contains(@class, 'reviews')]");
        return reviewsBlock.isDisplayed();
    }

    public boolean isRatingDisplayed() {
        SelenideElement rating = $x("//div[contains(@class, 'rating')]");
        return rating.isDisplayed();
    }

    public boolean hasTextReviews() {
        ElementsCollection reviews = $$x("//div[contains(@class, 'review-text')]");
        return !reviews.isEmpty();
    }

    public boolean allReviewsHaveStars() {
        ElementsCollection reviews = $$x("//div[contains(@class, 'review-item')]");
        for (SelenideElement review : reviews) {
            SelenideElement stars = review.$x(".//div[contains(@class, 'stars')]");
            if (!stars.isDisplayed()) {
                return false;
            }
        }
        return true;
    }

    public boolean isShareModalDisplayed() {
        SelenideElement modal = $x("//div[contains(@class, 'share-modal')]");
        return modal.isDisplayed();
    }

    public boolean hasCopyLinkButton() {
        SelenideElement copyButton = $x("//button[contains(text(), 'Скопировать ссылку')]");
        return copyButton.isDisplayed();
    }

    public boolean hasSocialIcons() {
        ElementsCollection icons = $$x("//div[contains(@class, 'social-icon')]");
        return !icons.isEmpty();
    }

    public String getSellerName() {
        return sellerLink.getText();
    }
}