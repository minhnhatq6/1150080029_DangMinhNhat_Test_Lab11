package tests;

import framework.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class FlakyTest extends BaseTest {
    private static int callCount = 0;

    @Test(description = "Test mô phỏng lỗi thoáng qua (2 lần fail, lần 3 pass)")
    public void testFlakyScenario() {
        callCount++;
        System.out.println("[FlakyTest] Đang chạy lần thứ: " + callCount);
        
        if (callCount <= 2) {
            Assert.fail("Mô phỏng lỗi mạng tạm thời — lần " + callCount);
        }
        Assert.assertTrue(true, "Test pass ở lần thứ " + callCount);
    }
}