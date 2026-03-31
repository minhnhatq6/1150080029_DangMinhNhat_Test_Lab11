package framework.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import framework.config.ConfigReader;

import java.time.Duration;

public abstract class BaseTest {
    // Dùng ThreadLocal để hỗ trợ chạy Parallel song song nhiều cửa sổ Chrome
    private static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();

    // Các class con dùng getDriver() này thay vì tự new ChromeDriver()
    public WebDriver getDriver() {
        return tlDriver.get();
    }

    @Parameters({"browser", "env"})
    @BeforeMethod(alwaysRun = true)
    public void setUp(@Optional("chrome") String browser, @Optional("dev") String env) {
        System.setProperty("env", env);
        // TRUYỀN THAM SỐ BROWSER VÀO FACTORY
        DriverFactory.initDriver(browser); 
        
        // Dùng ConfigReader thay vì hardcode giá trị
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigReader.getInstance().getExplicitWait()));
        getDriver().get(ConfigReader.getInstance().getBaseUrl());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            System.out.println("!!! TEST FAIL: Sẽ bổ sung hàm chụp ảnh ở bài sau.");
            // TODO: Chụp ảnh màn hình ở bài ConfigReader
        }

        if (getDriver() != null) {
            getDriver().quit();
            tlDriver.remove(); // Xóa khỏi bộ nhớ tránh leak RAM
        }
    }
}