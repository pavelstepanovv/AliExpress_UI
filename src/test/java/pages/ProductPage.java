package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.executeJavaScript;

public class ProductPage extends BasePage {

    private final SelenideElement titleElement = $x("//h1[string-length(normalize-space()) > 0]");
    private final ElementsCollection priceElements = $$x("//*[@data-testid='HazeProductPrice']");
    private final ElementsCollection addToCartButtons = $$x("//button[@data-testid='toCartBtn' or @data-button-add-to-cart='true']");
    private final ElementsCollection understoodButtons = $$x("//button[normalize-space()='Понятно' or .//*[normalize-space()='Понятно']]");
    private final SelenideElement cartLink = $x("//a[.//span[normalize-space()='Корзина'] or contains(normalize-space(), 'Корзина')]");

    public ProductPage waitUntilOpened() {
        titleElement.shouldBe(visible, Duration.ofSeconds(30));
        getVisiblePriceElement().shouldBe(visible, Duration.ofSeconds(30));
        getVisibleAddToCartButton().shouldBe(visible, Duration.ofSeconds(30));
        return this;
    }

    public boolean isTitleDisplayed() {
        return titleElement.isDisplayed();
    }

    public String getTitleText() {
        return titleElement.getText();
    }

    public boolean isPriceDisplayed() {
        return getVisiblePriceElement().isDisplayed();
    }

    public String getPrice() {
        return getVisiblePriceElement().getText();
    }

    public boolean hasAddToCartButton() {
        return getVisibleAddToCartButton().isDisplayed();
    }

    public void addToCart() {
        closeVisibleHints();
        getVisibleAddToCartButton().scrollIntoView(true).click();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public CartPage openCart() {
        cartLink.shouldBe(visible, Duration.ofSeconds(20)).click();
        return new CartPage().waitUntilOpened();
    }

    public ReviewsPage openReviews() {
        SelenideElement reviewsTab = $x("//*[contains(@class, 'anchor') or contains(@class, 'tab') or self::a]//*[contains(text(), 'Отзыв') or contains(text(), 'review')]");

        if (!reviewsTab.exists()) {
            reviewsTab = $x("//a[contains(text(), 'Отзыв')]");
        }
        if (!reviewsTab.exists()) {
            reviewsTab = $x("//*[contains(text(), 'Отзыв')]");
        }

        if (reviewsTab.exists()) {
            reviewsTab.shouldBe(visible, Duration.ofSeconds(15)).scrollIntoView("{block: 'center'}");
            executeJavaScript("arguments[0].click();", reviewsTab);
        }

        return new ReviewsPage();
    }

    public SharePage openShareDialog() {
        SelenideElement shareButton = $x("//button[contains(text(), 'Поделиться') or contains(@data-testid, 'share')]");
        if (!shareButton.exists()) {
            shareButton = $x("//*[contains(text(), 'Поделиться')]");
        }
        if (!shareButton.exists()) {
            shareButton = $x("//*[contains(@class, 'share')]//button");
        }
        if (!shareButton.exists()) {
            shareButton = $x("//button[contains(@aria-label, 'share') or contains(@aria-label, 'Share')]");
        }
        shareButton.shouldBe(visible, Duration.ofSeconds(10)).scrollIntoView("{block: 'center'}");
        executeJavaScript("arguments[0].click();", shareButton);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new SharePage();
    }

    private SelenideElement getVisiblePriceElement() {
        return priceElements.findBy(visible);
    }

    private SelenideElement getVisibleAddToCartButton() {
        return addToCartButtons.findBy(visible);
    }

    private void closeVisibleHints() {
        for (int i = 0; i < 2; i++) {
            SelenideElement button = understoodButtons.findBy(visible);
            if (!button.exists()) {
                return;
            }
            executeJavaScript("arguments[0].click();", button);
        }
    }
}
