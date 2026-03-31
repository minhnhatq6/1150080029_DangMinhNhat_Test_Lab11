package tests;

import framework.base.BaseTest;
import framework.pages.LoginPage;
import framework.utils.ExcelReader;
import org.testng.Assert;
import org.testng.ITest;
import org.testng.ITestContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class này thực hiện kiểm thử Data-Driven cho chức năng đăng nhập,
 * đọc dữ liệu từ file Excel và đáp ứng các yêu cầu nâng cao của Lab 9 - Bài 3.
 */
// Kế thừa ITest để ghi đè tên Test hiển thị trong Báo cáo HTML
public class LoginDDTTest extends BaseTest implements ITest {

    // Biến dùng để lưu Description từ Excel làm tên Test
    private ThreadLocal<String> testName = new ThreadLocal<>();

    @Override
    public String getTestName() {
        return testName.get();
    }

    // Chạy trước mỗi test, lấy cột Description (cột số 3) gán làm tên test
    @BeforeMethod(alwaysRun = true)
    public void setTestNameFromExcel(Method method, Object[] testData) {
        // Ghi chú: hàm setup() của BaseTest đã được gọi từ file testng.xml,
        // chúng ta chỉ cần xử lý phần tên test ở đây.
        if (testData != null && testData.length > 3 && testData[3] != null) {
            testName.set(testData[3].toString());
        } else {
            testName.set(method.getName());
        }
    }

    /**
     * DataProvider cực xịn: Tự động đọc ITestContext để biết đang chạy group nào
     * - Nếu chạy "smoke": Chỉ đọc sheet SmokeCases
     * - Nếu chạy "regression": Đọc gộp cả 3 sheet
     */
    @DataProvider(name = "excelLoginData")
    public Object[][] getLoginDataFromExcel(ITestContext context) {
        String path = "src/test/resources/testdata/login_data.xlsx";
        List<String> includedGroups = Arrays.asList(context.getIncludedGroups());

        List<Object[]> combinedData = new ArrayList<>();

        // Nếu chạy nhóm smoke, hoặc không chỉ định nhóm nào (chạy mặc định)
        if (includedGroups.contains("smoke") || includedGroups.isEmpty()) {
            combinedData.addAll(Arrays.asList(ExcelReader.getData(path, "SmokeCases")));
        } 
        
        // Nếu chạy nhóm regression, nạp cả 3 sheet
        if (includedGroups.contains("regression")) {
            // Thêm các sheet còn lại để tránh trùng lặp nếu regression cũng bao gồm smoke
            combinedData.addAll(Arrays.asList(ExcelReader.getData(path, "NegativeCases")));
            combinedData.addAll(Arrays.asList(ExcelReader.getData(path, "BoundaryCases")));
        }

        return combinedData.toArray(new Object[0][]);
    }

    /**
     * Test method chính, nhận dữ liệu từ DataProvider và thực thi kiểm thử.
     */
    @Test(dataProvider = "excelLoginData", description = "Kiểm thử đăng nhập Data-Driven từ Excel")
    public void testLoginFromExcel(String username, String password, String expected, String description) {
        LoginPage loginPage = new LoginPage(getDriver());
        getDriver().get("https://www.saucedemo.com"); // Đảm bảo luôn ở đúng trang trước mỗi lần chạy

        // Nếu 'expected' là một đường dẫn URL (trường hợp Smoke thành công)
        if (expected.startsWith("/")) {
            loginPage.login(username, password);
            Assert.assertTrue(getDriver().getCurrentUrl().contains(expected),
                    "Lỗi: Không chuyển hướng đúng URL mong đợi cho: " + description);
        } 
        // Nếu 'expected' là một thông báo lỗi (trường hợp Negative/Boundary)
        else {
            loginPage.loginExpectingFailure(username, password);
            Assert.assertTrue(loginPage.getErrorMessage().contains(expected),
                    "Lỗi: Thông báo lỗi không khớp cho: " + description);
        }
    }
}