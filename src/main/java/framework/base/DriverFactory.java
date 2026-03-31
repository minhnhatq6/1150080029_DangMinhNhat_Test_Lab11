package framework.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

public class DriverFactory {
    private static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();

    public static void initDriver(String browser) {
        boolean isCI = System.getenv("CI") != null; // Nhận diện xem có đang chạy trên GitHub Actions không
        WebDriver driver;

        switch (browser.toLowerCase()) {
            case "edge":
                driver = createEdgeDriver(isCI);
                break;
            case "chrome":
            default:
                driver = createChromeDriver(isCI);
                break;
        }
        
        driver.manage().window().maximize();
        tlDriver.set(driver);
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