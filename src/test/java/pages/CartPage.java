package pages;

import com.codeborne.selenide.SelenideElement;
import elements.Button;
import elements.Input;

import static com.codeborne.selenide.Selenide.$x;

public class CartPage extends BasePage {

    private final SelenideElement cartIcon = $x("//div[contains(@class, 'cart-icon')]");
    private final Button removeButton = Button.byText("Удалить");
    private final Button confirmRemoveButton = Button.byText("Удалить из корзины");
    private final Input quantityInput = Input.byName("quantity");

    public void openCart() {
        cartIcon.click();
    }

    public int getCartCount() {
        SelenideElement count = $x("//span[contains(@class, 'cart-count')]");
        if (count.isDisplayed()) {
            return Integer.parseInt(count.getText());
        }
        return 0;
    }

    public void setQuantity(int quantity) {
        quantityInput.fill(String.valueOf(quantity));
    }

    public int getQuantity() {
        String value = quantityInput.getText();
        return Integer.parseInt(value);
    }

    public void removeProduct() {
        removeButton.click();
    }

    public void confirmRemove() {
        confirmRemoveButton.click();
    }

    public boolean isCartEmpty() {
        SelenideElement emptyMessage = $x("//div[contains(text(), 'в корзине нет товаров')]");
        return emptyMessage.isDisplayed();
    }

    public boolean hasProduct(String productName) {
        SelenideElement product = $x("//div[contains(text(), '" + productName + "')]");
        return product.isDisplayed();
    }

    public String getTotalPrice() {
        SelenideElement total = $x("//div[contains(@class, 'total-price')]");
        return total.getText();
    }
}