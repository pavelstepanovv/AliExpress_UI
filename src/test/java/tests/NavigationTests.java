package tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriverException;
import pages.CategoryPage;
import pages.DeliveryAddressModal;
import pages.MainPage;

import static com.codeborne.selenide.WebDriverRunner.hasWebDriverStarted;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Группа тестов для проверки навигации по сайту.
 * Включает: переключение категорий, смену города доставки, переключение языка.
 */
public class NavigationTests extends BaseTest {
    // 1. СТАТИЧЕСКИЕ ПОЛЯ
    private static final String DELIVERY_CITY = "Санкт-Петербург";
    private static final String EXPECTED_BUTTON_TEXT = "Find";

    // 2. ТЕСТЫ

    /**
     * Тест №7: Переключение категории товаров.
     * Автор: Кислица Сергей, группа 4388
     *
     * Шаги:
     * 1. Открыть главную страницу
     * 2. Открыть меню каталога
     * 3. Выбрать категорию «Электроника»
     * 4. Перейти в подкатегорию «Аудио- и видеотехника»
     * 5. Проверить, что список товаров не пустой
     */
    @Test
    public void testNavigateToAudioVideoCategory() {
        // 1-4. Открыть главную страницу → каталог → электроника → аудио- и видеотехника
        CategoryPage categoryPage = new MainPage()
                .open()
                .openCatalog()
                .selectElectronicsCategory()
                .openAudioVideoCategory();

        // 5. Проверить, что список товаров не пустой
        assertThat(categoryPage.getProductsCount())
                .as("В подкатегории «Аудио- и видеотехника» должен быть список товаров")
                .isGreaterThan(0);
    }

    /**
     * Тест №10: Смена города доставки.
     * Автор: Кислица Сергей, группа 4388
     *
     * Шаги:
     * 1. Открыть главную страницу (закрыть попап выбора региона, если появился)
     * 2. Открыть окно изменения адреса доставки
     * 3. Выбрать город "Санкт-Петербург"
     * 4. Сохранить изменения
     * 5. Дождаться отображения выбранного города в шапке
     * 6. Проверить, что в шапке отображается выбранный город
     */
    @Test
    public void testChangeDeliveryCity() {
        // 1. Открыть главную страницу (закрыть попап выбора региона, если появился)
        MainPage mainPage = new MainPage().open().closeLocationPopupIfPresent();

        // 2. Открыть окно изменения адреса доставки
        DeliveryAddressModal addressModal = mainPage.openDeliveryAddress();

        // 3. Выбрать город и сохранить изменения
        addressModal.selectCity(DELIVERY_CITY).save();

        // 4. Дождаться отображения выбранного города в шапке
        mainPage.waitUntilDeliveryCityDisplayed(DELIVERY_CITY);

        // 5. Проверить, что в шапке отображается выбранный город
        assertThat(mainPage.getDeliveryCity())
                .as("В шапке должен отображаться выбранный город доставки '%s'", DELIVERY_CITY)
                .isEqualTo(DELIVERY_CITY);
    }

    /**
     * Тест №13: Переключение языка интерфейса.
     * Автор: Миридаштаки Фарид, группа 4343
     *
     * Шаги:
     * 1. Открыть главную страницу (закрыть попап выбора региона, если появился)
     * 2. Переключить язык на английский
     * 3. Обновить страницу для полного применения изменений
     * 4. Проверить, что текст кнопки поиска изменился на "Find"
     */
    @Test
    public void testSwitchLanguageToEnglish() {
        // 1. Открыть главную страницу (закрыть попап выбора региона, если появился)
        MainPage mainPage = new MainPage().open().closeLocationPopupIfPresent();

        // 2. Переключить язык на английский
        mainPage.switchLanguageToEnglish();

        // 3. Обновить страницу для полного применения изменений
        mainPage.refresh();

        // 4. Проверить, что текст кнопки поиска изменился на "Find"
        assertThat(mainPage.getSearchButtonText())
                .as("После смены языка кнопка поиска должна называться '%s'", EXPECTED_BUTTON_TEXT)
                .isEqualToIgnoringCase(EXPECTED_BUTTON_TEXT);
    }

    /**
     * Возвращает язык интерфейса на русский после выполнения теста.
     * Выполняется после каждого теста, сохраняя cookie и сессию.
     */
    @AfterEach
    public void resetLanguage() {
        if (hasWebDriverStarted()) {
            try {
                new MainPage().open().closeLocationPopupIfPresent().switchLanguageToRussian();
            } catch (AssertionError | WebDriverException ignored) {
                // Не маскируем результат основного теста ошибкой очистки состояния
            }
        }
    }
}
