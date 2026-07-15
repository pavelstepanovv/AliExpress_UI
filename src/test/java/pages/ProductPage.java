package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.executeJavaScript;

/**
 * Page Object страницы товара AliExpress.
 * Автор: Шакуров 4382.
 */
public class ProductPage extends BasePage {

    private final SelenideElement titleElement = $x("//h1[string-length(normalize-space()) > 0]");
    private final ElementsCollection priceElements = $$x("//*[@data-testid='HazeProductPrice']");
    private final ElementsCollection addToCartButtons = $$x("//button[@data-testid='toCartBtn' or @data-button-add-to-cart='true']");
    private final ElementsCollection understoodButtons = $$x("//button[normalize-space()='Понятно' or .//*[normalize-space()='Понятно']]");
    private final SelenideElement cartLink = $x("//a[.//span[normalize-space()='Корзина'] or contains(normalize-space(), 'Корзина')]");

    /**
     * Ждет загрузки основных элементов карточки товара.
     */
    public ProductPage waitUntilOpened() {
        titleElement.shouldBe(visible, Duration.ofSeconds(30));
        getVisiblePriceElement().shouldBe(visible, Duration.ofSeconds(30));
        getVisibleAddToCartButton().shouldBe(visible, Duration.ofSeconds(30));
        return this;
    }

    /**
     * Проверяет, отображается ли название товара.
     */
    public boolean isTitleDisplayed() {
        return titleElement.isDisplayed();
    }

    /**
     * Возвращает название товара.
     */
    public String getTitleText() {
        return titleElement.getText();
    }

    /**
     * Проверяет, отображается ли цена товара.
     */
    public boolean isPriceDisplayed() {
        return getVisiblePriceElement().isDisplayed();
    }

    /**
     * Возвращает цену товара.
     */
    public String getPrice() {
        return getVisiblePriceElement().getText();
    }

    /**
     * Проверяет наличие кнопки добавления в корзину.
     */
    public boolean hasAddToCartButton() {
        return getVisibleAddToCartButton().isDisplayed();
    }

    /**
     * Нажимает кнопку добавления товара в корзину.
     */
    public void addToCart() {
        closeVisibleHints();
        getVisibleAddToCartButton().scrollIntoView(true).click();
    }

    /**
     * Открывает корзину через иконку в шапке сайта.
     */
    public CartPage openCart() {
        cartLink.shouldBe(visible, Duration.ofSeconds(20)).click();
        return new CartPage().waitUntilOpened();
    }

    /**
     * Возвращает видимый блок цены товара.
     */
    private SelenideElement getVisiblePriceElement() {
        return priceElements.findBy(visible);
    }

    /**
     * Возвращает видимую кнопку добавления товара в корзину.
     */
    private SelenideElement getVisibleAddToCartButton() {
        return addToCartButtons.findBy(visible);
    }

    /**
     * Закрывает видимые подсказки и баннеры, которые могут перекрывать кнопку корзины.
     */
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
