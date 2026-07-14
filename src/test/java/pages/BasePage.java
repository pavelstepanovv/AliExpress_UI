package pages;

import com.codeborne.selenide.Selenide;

/**
 * Базовый абстрактный класс, от которого наследуются все классы страниц.
 * Содержит общие методы: открытие URL, обновление, получение заголовка.
 */
public abstract class BasePage {
    // ПУБЛИЧНЫЕ МЕТОДЫ
    /**
     * Открывает указанный URL в браузере.
     *
     * Параметры: url адрес страницы
     */
    protected void open(String url) {
        Selenide.open(url);
    }

    /**
     * Возвращает заголовок текущей страницы.
     *
     * Возвращает: заголовок страницы
     */
    public String getTitle() {
        return Selenide.title();
    }
}
