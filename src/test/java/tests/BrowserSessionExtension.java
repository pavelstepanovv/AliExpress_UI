package tests;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import static com.codeborne.selenide.WebDriverRunner.hasWebDriverStarted;

/**
 * Создаёт один браузер для всего тестового запуска и закрывает его в самом конце.
 */
public final class BrowserSessionExtension implements BeforeAllCallback {

    private static final ExtensionContext.Namespace NAMESPACE =
            ExtensionContext.Namespace.create(BrowserSessionExtension.class);

    @Override
    public void beforeAll(ExtensionContext context) {
        context.getRoot().getStore(NAMESPACE).getOrComputeIfAbsent(
                BrowserSession.class,
                key -> new BrowserSession(),
                BrowserSession.class);
    }

    private static final class BrowserSession implements ExtensionContext.Store.CloseableResource {

        private BrowserSession() {
            BaseTest.configureSelenide();
            Selenide.open("about:blank");
        }

        @Override
        public void close() {
            if (hasWebDriverStarted()) {
                Selenide.closeWebDriver();
            }
        }
    }
}
