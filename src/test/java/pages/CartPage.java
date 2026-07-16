package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.executeJavaScript;

public class CartPage extends BasePage {

    private final ElementsCollection productContainers = $$x("//*[@data-testid='productContainer']");
    private final SelenideElement productTitle = $x("//*[@data-product-title]");
    private final SelenideElement productQuantity = $x("//*[@data-product-quantity]");
    private final SelenideElement productPrice = $x("//*[@data-product-unformatted-price]");
    private final SelenideElement incrementButton = $x("//*[@data-testid='productIncrementButton']");
    private final SelenideElement totalPriceBlock = $x("//*[@id='snow_cart_total_price_anchor']");
    private final SelenideElement totalItemsText = $x("//*[@id='snow_cart_total_price_anchor']//*[contains(normalize-space(), 'товар') and contains(normalize-space(), 'шт.')]");
    private final SelenideElement totalPriceText = $x("(//*[@id='snow_cart_total_price_anchor']//span[contains(normalize-space(), '₽')])[1]");

    public CartPage waitUntilOpened() {
        activatePage();
        productContainers.shouldHave(sizeGreaterThan(0), Duration.ofSeconds(30));
        productTitle.shouldBe(visible, Duration.ofSeconds(30));
        productQuantity.shouldBe(visible, Duration.ofSeconds(30));
        totalPriceBlock.shouldBe(visible, Duration.ofSeconds(30));
        return this;
    }

    public String getProductTitle() {
        return productTitle.getText();
    }

    public int getProductQuantity() {
        return Integer.parseInt(productQuantity.getAttribute("data-product-quantity"));
    }

    public CartPage increaseProductQuantityTo(int expectedQuantity) {
        while (getProductQuantity() < expectedQuantity) {
            int currentQuantity = getProductQuantity();
            incrementButton.shouldBe(visible, Duration.ofSeconds(10)).click();
            productQuantity.shouldHave(attribute("data-product-quantity", String.valueOf(currentQuantity + 1)), Duration.ofSeconds(10));
        }
        return this;
    }

    public int getProductUnitPrice() {
        return Integer.parseInt(productPrice.getAttribute("data-product-unformatted-price"));
    }

    public CartPage waitTotalRecalculatedForQuantity(int expectedQuantity) {
        totalItemsText.shouldHave(text(expectedQuantity + " шт."), Duration.ofSeconds(20));
        totalPriceText.shouldBe(visible, Duration.ofSeconds(20));
        return this;
    }

    public int getTotalPrice() {
        return Integer.parseInt(totalPriceText.getText().replaceAll("[^0-9]", ""));
    }

    public CartPage removeProduct() {
        SelenideElement deleteIcon = $x("//*[name()='path' and contains(@d, 'M6 6V11.8799')]");
        if (!deleteIcon.exists()) {
            deleteIcon = $x("//*[contains(@class, 'delete') or contains(@class, 'remove')]//*[name()='path']");
        }
        if (!deleteIcon.exists()) {
            deleteIcon = $x("//*[name()='svg']/*[contains(@d, 'M6 6')]");
        }
        if (!deleteIcon.exists()) {
            deleteIcon = $x("//span[contains(@class, 'delete')]");
        }

        if (deleteIcon.exists()) {
            SelenideElement parentButton = deleteIcon.closest("button");
            if (parentButton != null && parentButton.exists()) {
                executeJavaScript("arguments[0].click();", parentButton);
                System.out.println("Клик по кнопке удаления выполнен");
            } else {
                executeJavaScript("arguments[0].click();", deleteIcon);
                System.out.println("Клик по иконке удаления выполнен");
            }

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            SelenideElement confirmButton = $x("//button[contains(text(), 'Удалить из корзины')]");
            if (!confirmButton.exists()) {
                confirmButton = $x("//span[contains(text(), 'Удалить из корзины')]/..");
            }
            if (!confirmButton.exists()) {
                confirmButton = $x("//*[contains(@class, 'modal')]//button[contains(text(), 'Удалить')]");
            }
            if (!confirmButton.exists()) {
                confirmButton = $x("//*[@role='dialog']//button[contains(text(), 'Удалить')]");
            }
            if (!confirmButton.exists()) {
                confirmButton = $x("//div[contains(@class, 'popup')]//button[contains(text(), 'Удалить')]");
            }
            if (!confirmButton.exists()) {
                confirmButton = $x("//button[contains(@data-testid, 'confirm') or contains(@data-testid, 'delete')]");
            }

            if (confirmButton.exists() && confirmButton.isDisplayed()) {
                executeJavaScript("arguments[0].click();", confirmButton);
                System.out.println("Клик по кнопке 'Удалить из корзины' выполнен");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Кнопка подтверждения не найдена");
            }
        } else {
            System.out.println("Иконка удаления не найдена");
        }

        return this;
    }

    public boolean isCartEmpty() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return productContainers.isEmpty();
    }

    public boolean isEmptyCartMessageDisplayed() {
        SelenideElement emptyMessage = $x("//*[contains(text(), 'В корзине ничего нет')]");
        if (!emptyMessage.exists()) {
            emptyMessage = $x("//*[contains(text(), 'Корзина пуста')]");
        }
        if (!emptyMessage.exists()) {
            emptyMessage = $x("//*[contains(text(), 'Your cart is empty')]");
        }
        if (!emptyMessage.exists()) {
            emptyMessage = $x("//div[contains(@class, 'empty')]");
        }
        return emptyMessage.exists() && emptyMessage.isDisplayed();
    }
}