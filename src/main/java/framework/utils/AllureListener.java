package framework.utils;

import framework.base.DriverFactory;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class AllureListener implements ITestListener {

    @Attachment(value = "Ảnh chụp lỗi màn hình", type = "image/png")
    public byte[] saveScreenshotPNG(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("--- TEST FAIL: Đang tự động đính kèm ảnh vào Allure ---");
        if (DriverFactory.getDriver() != null) {
            saveScreenshotPNG(DriverFactory.getDriver());
        }
    }
}