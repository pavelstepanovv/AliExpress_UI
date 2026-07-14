package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$$x;

/**
 * Страница результатов поиска.
 * Содержит список карточек товаров и методы для работы с ними.
 */
public class SearchResultPage extends BasePage {

    // СТАТИЧЕСКИЕ ПОЛЯ (локаторы)
    private static final String PRODUCT_CARDS_XPATH = "//a[contains(@class, 'universalSnippetLink')]";
    private static final String PRODUCT_TITLE_XPATH = ".//div[@data-type='PartWrap-Text'][@title]";
    private static final String PRODUCT_TITLE_FALLBACK_XPATH = ".//div[@data-type='PartWrap-Text'][contains(@style, 'line-clamp: 2')]";
    private static final String PRODUCT_PRICE_XPATH = ".//div[@data-type='PartWrap-Text'][contains(@style, 'font-size: 21px')]";

    // ПОЛЯ ЭКЗЕМПЛЯРА
    private final ElementsCollection productCards = $$x(PRODUCT_CARDS_XPATH);

    // ПУБЛИЧНЫЕ МЕТОДЫ
    /**
     * Проверяет, есть ли на странице хотя бы один товар.
     *
     * Возвращает: true, если товары есть
     */
    public boolean hasProducts() {
        return !productCards.isEmpty();
    }

    /**
     * Возвращает количество товаров на странице.
     *
     * Возвращает: количество карточек товаров
     */
    public int getProductsCount() {
        return productCards.size();
    }

    /**
     * Проверяет, есть ли хотя бы один товар с заданным словом в названии.
     *
     * Параметры: word слово, которое должно быть в названии
     * Возвращает: true, если есть хотя бы один такой товар
     */
    public boolean hasProductContainingWord(String word) {
        if (productCards.isEmpty()) {
            return false;
        }

        for (SelenideElement card : productCards) {
            String title = card.$x(PRODUCT_TITLE_XPATH).getAttribute("title");

            if (title != null && title.toLowerCase().contains(word.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Возвращает название первого товара на странице.
     *
     * Возвращает: название товара или "Нет товаров"
     */
    public String getFirstProductTitle() {
        if (productCards.isEmpty()) {
            return "Нет товаров";
        }

        SelenideElement firstCard = productCards.first();

        String title = firstCard.$x(PRODUCT_TITLE_XPATH).getAttribute("title");

        if (title == null || title.isEmpty()) {
            title = firstCard.$x(PRODUCT_TITLE_FALLBACK_XPATH).getText();
        }

        return title;
    }

    /**
     * Возвращает цену первого товара на странице.
     *
     * Возвращает: цену товара или "Нет цены"
     */
    public String getFirstProductPrice() {
        if (productCards.isEmpty()) {
            return "Нет цены";
        }

        return productCards.first().$x(PRODUCT_PRICE_XPATH).getText();
    }
}
