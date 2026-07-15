package pages;

import elements.Item;

/**
 * Страница магазина продавца.
 * Открывается после клика по названию магазина на странице товара.
 */
public class SellerPage extends BasePage {

    // ПОЛЯ ЭКЗЕМПЛЯРА
    private final Item shopTitle = Item.byClass("ShopInfo_ShopInfo__title");

    // ПУБЛИЧНЫЕ МЕТОДЫ
    /**
     * Возвращает название магазина из заголовка страницы.
     *
     * Возвращает: название магазина
     */
    public String getShopTitle() {
        return shopTitle.getText();
    }

    /**
     * Проверяет, отображается ли название магазина на странице.
     *
     * Возвращает: true, если название видно
     */
    public boolean isShopTitleDisplayed() {
        return shopTitle.isDisplayed();
    }
}
