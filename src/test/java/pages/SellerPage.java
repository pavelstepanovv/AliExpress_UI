package pages;

import elements.Item;

/**
 * Страница магазина продавца.
 * Открывается после клика по названию магазина на странице товара.
 */
public class SellerPage extends BasePage {

    // ПОЛЯ ЭКЗЕМПЛЯРА
    private final Item shopTitle = Item.byClass("ShopInfo_ShopInfo__title");

    // КОНСТРУКТОРЫ
    public SellerPage() {
        // пустой конструктор
    }

    // ПУБЛИЧНЫЕ МЕТОДЫ

    public String getShopTitle() {
        return shopTitle.getText();
    }

    public boolean isShopTitleDisplayed() {
        return shopTitle.isDisplayed();
    }
}
