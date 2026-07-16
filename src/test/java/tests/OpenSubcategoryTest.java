package tests;

import org.junit.jupiter.api.Test;
import pages.MainPage;
import pages.CategoryPage;

import static org.assertj.core.api.Assertions.assertThat;

public class OpenSubcategoryTest extends BaseTest {

    private static final String MAIN_CATEGORY = "Электроника";
    private static final String SUB_CATEGORY = "Аудио- и видеотехника";

    @Test
    public void testOpenSubcategory() {
        MainPage mainPage = new MainPage().open();


        CategoryPage categoryPage = mainPage.openSubcategory(MAIN_CATEGORY, SUB_CATEGORY);

        assertThat(categoryPage.getProductsCount())
                .as("На странице подкатегории должны отображаться товары")
                .isGreaterThan(0);
    }
}