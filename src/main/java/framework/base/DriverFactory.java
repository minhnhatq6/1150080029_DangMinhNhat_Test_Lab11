package framework.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import java.net.URL;
import java.time.Duration;

public class DriverFactory {
    private static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();

   public static void initDriver(String browser) {
        String gridUrl = System.getProperty("grid.url"); // Đọc URL Grid từ lệnh mvn -Dgrid.url
        boolean isCI = System.getenv("CI") != null;
        WebDriver driver;

        // KIỂM TRA: Nếu có truyền grid.url thì chạy trên Grid, ngược lại chạy Local
        if (gridUrl != null && !gridUrl.isBlank()) {
            driver = createRemoteDriver(browser, gridUrl);
        } else {
            switch (browser.toLowerCase()) {
                case "edge": driver = createEdgeDriver(isCI); break;
                default: driver = createChromeDriver(isCI); break;
            }
        }
        
        driver.manage().window().maximize();
        tlDriver.set(driver);
    }

    /** Hàm tạo Driver chạy trên Selenium Grid */
    private static WebDriver createRemoteDriver(String browser, String gridUrl) {
        try {
            System.out.println("[GRID] Đang kết nối tới Hub tại: " + gridUrl);
            URL url = new URL(gridUrl + "/wd/hub");
            if (browser.equalsIgnoreCase("edge")) {
                EdgeOptions options = new EdgeOptions();
                options.addArguments("--no-sandbox", "--disable-dev-shm-usage");
                return new RemoteWebDriver(url, options);
            } else {
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--no-sandbox", "--disable-dev-shm-usage");
                return new RemoteWebDriver(url, options);
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi kết nối Selenium Grid: " + e.getMessage());
        }
    }

    private static WebDriver createChromeDriver(boolean headless) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        if (headless) {
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox"); // Bắt buộc trên Linux CI
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--window-size=1920,1080");
            System.out.println("[Driver] Chạy Chrome HEADLESS (CI mode)");
        }
        return new ChromeDriver(options);
    }

    private static WebDriver createEdgeDriver(boolean headless) {
        WebDriverManager.edgedriver().setup();
        EdgeOptions options = new EdgeOptions();
        if (headless) {
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox"); // Bắt buộc trên Linux CI
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--window-size=1920,1080");
            System.out.println("[Driver] Chạy Edge HEADLESS (CI mode)");
        }
        return new EdgeDriver(options);
    }

    public static WebDriver getDriver() {
        return tlDriver.get();
    }

    public static void quitDriver() {
        if (tlDriver.get() != null) {
            tlDriver.get().quit();
            tlDriver.remove();
        }
    }
}