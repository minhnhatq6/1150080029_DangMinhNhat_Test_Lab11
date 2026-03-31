package tests;

import framework.base.BaseTest;
import framework.pages.LoginPage;
import framework.utils.JsonReader;
import framework.utils.UserData;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.io.IOException;
import java.util.List;

public class UserLoginTest extends BaseTest {

    @DataProvider(name = "jsonUsers")
    public Object[][] getUsers() throws IOException {
        List<UserData> users = JsonReader.readUsers("src/test/resources/testdata/users.json");
        Object[][] data = new Object[users.size()][1];
        for (int i = 0; i < users.size(); i++) {
            data[i][0] = users.get(i);
        }
        return data;
    }

    @Test(dataProvider = "jsonUsers", description = "Test đăng nhập dùng dữ liệu JSON")
    public void testLoginWithJson(UserData user) {
        LoginPage loginPage = new LoginPage(getDriver());
        
        if (user.expectSuccess) {
            loginPage.login(user.username, user.password);
            Assert.assertTrue(getDriver().getCurrentUrl().contains("inventory"), "Lỗi: " + user.description);
        } else {
            loginPage.loginExpectingFailure(user.username, user.password);
            Assert.assertTrue(loginPage.isErrorDisplayed(), "Lỗi: " + user.description);
        }
    }
}