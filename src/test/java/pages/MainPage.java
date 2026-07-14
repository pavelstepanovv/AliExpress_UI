package pages;

import elements.Button;
import elements.Input;

/**
 * Главная страница AliExpress, где начинаются все сценарии поиска товаров.
 */
public class MainPage extends BasePage {

    // СТАТИЧЕСКИЕ ПОЛЯ
    private static final String URL = "https://aliexpress.ru";

    // ПОЛЯ ЭКЗЕМПЛЯРА
    private final Input searchInput = Input.byName("SearchText");
    private final Button searchButton = Button.byText("Найти");

    // ПУБЛИЧНЫЕ МЕТОДЫ
    /**
     * Открывает главную страницу AliExpress.
     *
     * Возвращает: объект MainPage
     */
    public MainPage open() {
        open(URL);
        return this;
    }

    /**
     * Выполняет поиск товара по ключевому слову.
     * Вводит запрос и нажимает кнопку "Найти".
     *
     * Параметры: query поисковый запрос
     * Возвращает: объект SearchResultPage (страница результатов)
     */
    public SearchResultPage search(String query) {
        searchInput.fill(query);
        searchButton.click();
        return new SearchResultPage();
    }
}
