package tests;

import org.junit.jupiter.api.Test;
import pages.MainPage;
import pages.SearchResultPage;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Группа тестов для проверки функционала поиска.
 * Включает: поиск товара, поисковые подсказки, сортировку, очистку поиска.
 */
public class SearchTests extends BaseTest {
    // 1. СТАТИЧЕСКИЕ ПОЛЯ
    private static final String HEADPHONES_QUERY = "наушники";
    private static final String HEADPHONES_PARTIAL_QUERY = "науш";
    private static final String FIRST_QUERY = "наушники";
    private static final String SECOND_QUERY = "смартфон";
    private static final String SEARCH_QUERY_SORT = "рюкзак";
    private static final String EXPECTED_SORT_TYPE = "price_asc";
    private static final String EXPECTED_SORT_TEXT = "Сначала дешёвые";

    // 2. ТЕСТЫ

    /**
     * Тест №1: Поиск товара по ключевому слову.
     * Автор: Степанов Павел, группа 4383
     *
     * Шаги:
     * 1. Открыть главную страницу
     * 2. Выполнить поиск "наушники"
     * 3. Проверить, что товары найдены
     * 4. Проверить, что есть товар со словом "наушники" в названии
     * 5. Вывести информацию о найденных товарах
     */
    @Test
    public void testSearchProduct() {
        // 1. Открыть главную страницу
        MainPage mainPage = new MainPage().open();

        // 2. Выполнить поиск
        SearchResultPage results = mainPage.search(HEADPHONES_QUERY);

        // 3. Проверить, что товары найдены
        assertThat(results.getProductsCount())
                .as("Должны быть найдены товары по запросу '%s'", HEADPHONES_QUERY)
                .isGreaterThan(0);

        // 4. Проверить, что есть товар со словом "наушники" в названии
        assertThat(results.getProductTitles())
                .as("Хотя бы один товар должен содержать слово '%s' в названии", HEADPHONES_QUERY)
                .anyMatch(title -> title.toLowerCase().contains(HEADPHONES_QUERY.toLowerCase()));

        // 5. Вывод информации для отладки
        System.out.println("Найдено товаров: " + results.getProductsCount());
        System.out.println("Первый товар: " + results.getFirstProductTitle() + " | Цена: " + results.getFirstProductPrice());
    }

    /**
     * Тест №3: Проверка поисковых подсказок.
     * Автор: Шакуров Альберт, группа 4382
     *
     * Шаги:
     * 1. Открыть главную страницу
     * 2. Ввести в поле поиска "науш"
     * 3. Дождаться появления подсказок
     * 4. Проверить, что список подсказок не пустой
     * 5. Проверить, что все подсказки содержат "науш"
     */
    @Test
    public void testSearchSuggestions() {
        // 1. Открыть главную страницу
        MainPage mainPage = new MainPage().open();

        // 2. Ввести "науш" и дождаться появления подсказок
        mainPage.fillSearchAndWaitSuggestions(HEADPHONES_PARTIAL_QUERY);

        // 3. Проверить, что список подсказок не пустой
        assertThat(mainPage.getSuggestionsCount())
                .as("Список поисковых подсказок не должен быть пустым")
                .isGreaterThan(0);

        // 4. Проверить, что все подсказки содержат "науш"
        assertThat(mainPage.getSuggestionValues())
                .as("Все подсказки должны содержать введённый текст '%s'", HEADPHONES_PARTIAL_QUERY)
                .allMatch(suggestion -> suggestion.toLowerCase().contains(HEADPHONES_PARTIAL_QUERY.toLowerCase()));
    }

    /**
     * Тест №11: Сортировка результатов поиска по цене.
     * Автор: Миридаштаки Фарид, группа 4343
     *
     * Шаги:
     * 1. Открыть главную страницу (закрыть попап выбора региона, если появился)
     * 2. Выполнить поиск "рюкзак"
     * 3. Применить сортировку "Сначала дешёвые"
     * 4. Проверить, что на странице есть хотя бы два товара
     * 5. Проверить, что применён режим сортировки "price_asc"
     * 6. Проверить, что в списке сортировки отображается "Сначала дешёвые"
     */
    @Test
    public void testSortByPriceAscending() {
        // 1. Открыть главную страницу (закрыть попап выбора региона, если появился)
        MainPage mainPage = new MainPage().open().closeLocationPopupIfPresent();

        // 2. Выполнить поиск
        SearchResultPage results = mainPage.search(SEARCH_QUERY_SORT);

        // 3. Применить сортировку "Сначала дешёвые"
        results.sortByPriceAscending();

        // 4. Проверить, что есть хотя бы два товара с ценой
        List<Integer> prices = results.getProductPrices();
        assertThat(prices)
                .as("После сортировки на странице должно быть хотя бы два товара с ценой")
                .hasSizeGreaterThanOrEqualTo(2);

        // 5. Проверить, что применён режим сортировки "price_asc"
        assertThat(results.getSelectedSortType())
                .as("Должен быть применён режим '%s'. Цены в выдаче: %s",
                        EXPECTED_SORT_TYPE, prices)
                .isEqualTo(EXPECTED_SORT_TYPE);

        // 6. Проверить, что отображается "Сначала дешёвые"
        assertThat(results.getSelectedSortText())
                .as("В списке сортировки должен отображаться пункт '%s'. Цены в выдаче: %s",
                        EXPECTED_SORT_TEXT, prices)
                .isEqualTo(EXPECTED_SORT_TEXT);
    }

    /**
     * Тест №12: Очистка строки поиска.
     * Автор: Миридаштаки Фарид, группа 4343
     *
     * Шаги:
     * 1. Открыть главную страницу (закрыть попап выбора региона, если появился)
     * 2. Ввести первый запрос "наушники"
     * 3. Нажать кнопку очистки поля (крестик)
     * 4. Ввести второй запрос "смартфон"
     * 5. Проверить, что в поле отображается только новый запрос
     * 6. Проверить, что старый запрос отсутствует
     */
    @Test
    public void testClearSearchField() {
        // 1. Открыть главную страницу (закрыть попап выбора региона, если появился)
        MainPage mainPage = new MainPage().open().closeLocationPopupIfPresent();

        // 2. Ввести первый запрос
        mainPage.fillSearch(FIRST_QUERY);

        // 3. Нажать кнопку очистки поля (крестик)
        mainPage.clickClearSearch();

        // 4. Ввести второй запрос
        mainPage.fillSearch(SECOND_QUERY);

        // 5. Получить значение поля
        String fieldValue = mainPage.getSearchFieldValue();

        // 6. Проверить, что в поле отображается только новый запрос
        assertThat(fieldValue)
                .as("В поле поиска должно быть '%s'", SECOND_QUERY)
                .isEqualTo(SECOND_QUERY);

        // 7. Проверить, что старый запрос отсутствует
        assertThat(fieldValue)
                .as("Старого запроса '%s' в поле быть не должно", FIRST_QUERY)
                .doesNotContain(FIRST_QUERY);
    }
}package tests;

import org.junit.jupiter.api.Test;
import pages.MainPage;
import pages.SearchResultPage;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Группа тестов для проверки функционала поиска.
 * Включает: поиск товара, поисковые подсказки, сортировку, очистку поиска.
 */
public class SearchTests extends BaseTest {
    // 1. СТАТИЧЕСКИЕ ПОЛЯ
    private static final String HEADPHONES_QUERY = "наушники";
    private static final String HEADPHONES_PARTIAL_QUERY = "науш";
    private static final String FIRST_QUERY = "наушники";
    private static final String SECOND_QUERY = "смартфон";
    private static final String SEARCH_QUERY_SORT = "рюкзак";
    private static final String EXPECTED_SORT_TYPE = "price_asc";
    private static final String EXPECTED_SORT_TEXT = "Сначала дешёвые";

    // 2. ТЕСТЫ

    /**
     * Тест №1: Поиск товара по ключевому слову.
     * Автор: Степанов Павел, группа 4383
     *
     * Шаги:
     * 1. Открыть главную страницу
     * 2. Выполнить поиск "наушники"
     * 3. Проверить, что товары найдены
     * 4. Проверить, что есть товар со словом "наушники" в названии
     * 5. Вывести информацию о найденных товарах
     */
    @Test
    public void testSearchProduct() {
        // 1. Открыть главную страницу
        MainPage mainPage = new MainPage().open();

        // 2. Выполнить поиск
        SearchResultPage results = mainPage.search(HEADPHONES_QUERY);

        // 3. Проверить, что товары найдены
        assertThat(results.getProductsCount())
                .as("Должны быть найдены товары по запросу '%s'", HEADPHONES_QUERY)
                .isGreaterThan(0);

        // 4. Проверить, что есть товар со словом "наушники" в названии
        assertThat(results.getProductTitles())
                .as("Хотя бы один товар должен содержать слово '%s' в названии", HEADPHONES_QUERY)
                .anyMatch(title -> title.toLowerCase().contains(HEADPHONES_QUERY.toLowerCase()));

        // 5. Вывод информации для отладки
        System.out.println("Найдено товаров: " + results.getProductsCount());
        System.out.println("Первый товар: " + results.getFirstProductTitle() + " | Цена: " + results.getFirstProductPrice());
    }

    /**
     * Тест №3: Проверка поисковых подсказок.
     * Автор: Шакуров Альберт, группа 4382
     *
     * Шаги:
     * 1. Открыть главную страницу
     * 2. Ввести в поле поиска "науш"
     * 3. Дождаться появления подсказок
     * 4. Проверить, что список подсказок не пустой
     * 5. Проверить, что все подсказки содержат "науш"
     */
    @Test
    public void testSearchSuggestions() {
        // 1. Открыть главную страницу
        MainPage mainPage = new MainPage().open();

        // 2. Ввести "науш" и дождаться появления подсказок
        mainPage.fillSearchAndWaitSuggestions(HEADPHONES_PARTIAL_QUERY);

        // 3. Проверить, что список подсказок не пустой
        assertThat(mainPage.getSuggestionsCount())
                .as("Список поисковых подсказок не должен быть пустым")
                .isGreaterThan(0);

        // 4. Проверить, что все подсказки содержат "науш"
        assertThat(mainPage.getSuggestionValues())
                .as("Все подсказки должны содержать введённый текст '%s'", HEADPHONES_PARTIAL_QUERY)
                .allMatch(suggestion -> suggestion.toLowerCase().contains(HEADPHONES_PARTIAL_QUERY.toLowerCase()));
    }

    /**
     * Тест №11: Сортировка результатов поиска по цене.
     * Автор: Миридаштаки Фарид, группа 4343
     *
     * Шаги:
     * 1. Открыть главную страницу (закрыть попап выбора региона, если появился)
     * 2. Выполнить поиск "рюкзак"
     * 3. Применить сортировку "Сначала дешёвые"
     * 4. Проверить, что на странице есть хотя бы два товара
     * 5. Проверить, что применён режим сортировки "price_asc"
     * 6. Проверить, что в списке сортировки отображается "Сначала дешёвые"
     */
    @Test
    public void testSortByPriceAscending() {
        // 1. Открыть главную страницу (закрыть попап выбора региона, если появился)
        MainPage mainPage = new MainPage().open().closeLocationPopupIfPresent();

        // 2. Выполнить поиск
        SearchResultPage results = mainPage.search(SEARCH_QUERY_SORT);

        // 3. Применить сортировку "Сначала дешёвые"
        results.sortByPriceAscending();

        // 4. Проверить, что есть хотя бы два товара с ценой
        List<Integer> prices = results.getProductPrices();
        assertThat(prices)
                .as("После сортировки на странице должно быть хотя бы два товара с ценой")
                .hasSizeGreaterThanOrEqualTo(2);

        // 5. Проверить, что применён режим сортировки "price_asc"
        assertThat(results.getSelectedSortType())
                .as("Должен быть применён режим '%s'. Цены в выдаче: %s",
                        EXPECTED_SORT_TYPE, prices)
                .isEqualTo(EXPECTED_SORT_TYPE);

        // 6. Проверить, что отображается "Сначала дешёвые"
        assertThat(results.getSelectedSortText())
                .as("В списке сортировки должен отображаться пункт '%s'. Цены в выдаче: %s",
                        EXPECTED_SORT_TEXT, prices)
                .isEqualTo(EXPECTED_SORT_TEXT);
    }

    /**
     * Тест №12: Очистка строки поиска.
     * Автор: Миридаштаки Фарид, группа 4343
     *
     * Шаги:
     * 1. Открыть главную страницу (закрыть попап выбора региона, если появился)
     * 2. Ввести первый запрос "наушники"
     * 3. Нажать кнопку очистки поля (крестик)
     * 4. Ввести второй запрос "смартфон"
     * 5. Проверить, что в поле отображается только новый запрос
     * 6. Проверить, что старый запрос отсутствует
     */
    @Test
    public void testClearSearchField() {
        // 1. Открыть главную страницу (закрыть попап выбора региона, если появился)
        MainPage mainPage = new MainPage().open().closeLocationPopupIfPresent();

        // 2. Ввести первый запрос
        mainPage.fillSearch(FIRST_QUERY);

        // 3. Нажать кнопку очистки поля (крестик)
        mainPage.clickClearSearch();

        // 4. Ввести второй запрос
        mainPage.fillSearch(SECOND_QUERY);

        // 5. Получить значение поля
        String fieldValue = mainPage.getSearchFieldValue();

        // 6. Проверить, что в поле отображается только новый запрос
        assertThat(fieldValue)
                .as("В поле поиска должно быть '%s'", SECOND_QUERY)
                .isEqualTo(SECOND_QUERY);

        // 7. Проверить, что старый запрос отсутствует
        assertThat(fieldValue)
                .as("Старого запроса '%s' в поле быть не должно", FIRST_QUERY)
                .doesNotContain(FIRST_QUERY);
    }
}
