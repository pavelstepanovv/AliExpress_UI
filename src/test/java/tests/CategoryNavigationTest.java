package tests;

import org.junit.jupiter.api.Test;
import pages.CategoryPage;
import pages.MainPage;

import static org.assertj.core.api.Assertions.assertThat;

/** Тест 7: переход в товарную подкатегорию через меню каталога. */
public class CategoryNavigationTest extends BaseTest {

    /** Проверяет открытие «Аудио- и видеотехника» и непустой список товаров. */
    @Test
    public void testNavigateToAudioVideoCategory() {
        CategoryPage categoryPage = new MainPage()
                .open()
                .openCatalog()
                .selectElectronicsCategory()
                .openAudioVideoCategory();

        assertThat(categoryPage.getProductsCount())
                .as("В подкатегории «Аудио- и видеотехника» должен быть список товаров")
                .isGreaterThan(0);
    }
}
