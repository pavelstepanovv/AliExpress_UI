package tests;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import pages.MainPage;

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

    /**
     * Удаляет cookie после теста.
     * Браузер не закрывается между тестами, а выбранный язык хранится в cookie:
     * без очистки остальные тесты не найдут элементы с русским текстом.
     */
    @AfterEach
    public void resetLanguageCookie() {
        Selenide.clearBrowserCookies();
    }
}
