package tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriverException;
import pages.MainPage;

import static com.codeborne.selenide.WebDriverRunner.hasWebDriverStarted;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Тест 13: Переключение языка интерфейса на английский.
 * URL сайта после переключения не меняется, поэтому проверяем
 * по тексту кнопки поиска: "Найти" меняется на "Find".
 */
public class LanguageTest extends BaseTest {

    private static final String EXPECTED_BUTTON_TEXT = "Find";

    /**
     * Проверяет, что после выбора "English" интерфейс переключается на английский.
     */
    @Test
    public void testSwitchLanguageToEnglish() {
        // 1. open the main page (and close the region popup if it appears)
        MainPage mainPage = new MainPage().open().closeLocationPopupIfPresent();

        // 2. switch the language to English in the header
        mainPage.switchLanguageToEnglish();

        // 3. refresh the page so the interface fully updates
        mainPage.refresh();

        // 4. the search button text must be in English now
        assertThat(mainPage.getSearchButtonText())
                .as("После смены языка кнопка поиска должна называться '%s'", EXPECTED_BUTTON_TEXT)
                .isEqualToIgnoringCase(EXPECTED_BUTTON_TEXT);

    }

    /** Возвращает русский язык, сохраняя cookie капчи и общую браузерную сессию. */
    @AfterEach
    public void resetLanguage() {
        if (hasWebDriverStarted()) {
            try {
                new MainPage().open().closeLocationPopupIfPresent().switchLanguageToRussian();
            } catch (AssertionError | WebDriverException ignored) {
                // Не маскируем результат основного теста ошибкой очистки состояния.
            }
        }
    }
}
