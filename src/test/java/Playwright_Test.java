import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType.LaunchOptions;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import java.util.Arrays;
import java.util.List;

public class Playwright_Test {
    Playwright playwright;
    Browser browser;
    BrowserContext context;
    Page pageAuto;

    @BeforeTest
    public void initialized() {
        playwright = Playwright.create();
        browser = playwright.chromium()
                .launch(new LaunchOptions().setHeadless(false)
                        .setArgs(Arrays.asList("--start-maximized")));
        context = browser.newContext();
        pageAuto = context.newPage();
    }

    @Test
    public void testAhh() {
        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new LaunchOptions().setHeadless(false));
        Page page = browser.newPage();
        page.navigate("https://facebook.com");
        page.locator("//input[@id='email']").fill("rifky.fahlefi");
        page.locator("//input[@name='pass']").fill("94034545");
        page.locator("//*[@name='login']").click();
        page.waitForTimeout(3000);
        assertThat(page).hasTitle("Facebook");
    }

    @Test
    public void automationPracticeRadio() {

        String[] radioValue = { "radio1", "radio2", "radio3" };

        pageAuto.navigate("https://rahulshettyacademy.com/AutomationPractice/");

        for (String radio : radioValue) {
            pageAuto.locator("//input[@value='" + radio + "']").click();
            pageAuto.waitForTimeout(1000);
        }

    }

    @Test
    public void automationPracticeFill() {
        pageAuto.navigate("https://rahulshettyacademy.com/AutomationPractice/");
        pageAuto.fill("#autocomplete", "rahullllajahh");
        pageAuto.waitForTimeout(1000);
    }

    @Test
    public void automationPracticeDropDown() {
        pageAuto.navigate("https://rahulshettyacademy.com/AutomationPractice/");
        pageAuto.selectOption("#dropdown-class-example", "option2");
        pageAuto.waitForTimeout(1000);
    }

    @Test
    public void automationPracticeCheckbox() {
        List<String> checkboxValue = List.of("checkBoxOption3", "checkBoxOption2", "checkBoxOption1");
        pageAuto.navigate("https://rahulshettyacademy.com/AutomationPractice/");
        for (String checkBoxName : checkboxValue) {
            pageAuto.locator("#" + checkBoxName).check();
            pageAuto.waitForTimeout(1000);
        }

    }

    @Test
    public void automationPracticeButton() {
        pageAuto.navigate("https://rahulshettyacademy.com/AutomationPractice/");
        Page newPage = context.waitForPage(() -> {
            pageAuto.click("#openwindow");
        });

        newPage.locator("(//a[text() ='Courses'])[1]").click();
        newPage.waitForTimeout(5000);
        pageAuto.fill("#autocomplete", "rahullllajahh");
        pageAuto.waitForTimeout(1000);
    }

    @Test
    public void automationSwitchTab() {
        pageAuto.navigate("https://rahulshettyacademy.com/AutomationPractice/");
        Page newTab = context.waitForPage(() -> {
            pageAuto.click("#opentab");
        });
        newTab.click("//a[text() = 'Access all our Courses']");
        newTab.waitForLoadState();
    }

    @Test
    public void automationAlert() {
        pageAuto.navigate("https://rahulshettyacademy.com/AutomationPractice/");

        pageAuto.fill("#name", "Rifky Pahlevy");

       

        // pageAuto.onDialog(dialog2 -> {
        // System.out.println("Dialog Type " + dialog2.type());

        // if (dialog2.type().equals("prompt")) {
        // dialog2.accept("Hello Rifky, share this practice page and share your
        // knowledge");

        // } else if (dialog2.type().equals("confirm")) {
        // dialog2.accept();
        // } else if (dialog2.type().equals("alert")) {
        // dialog2.accept();
        // } else {
        // dialog2.dismiss();
        // }

        // });

        // pageAuto.selectOption("#dropdown-class-example", "option2");
        // pageAuto.waitForTimeout(1000);

         pageAuto.click("#confirmbtn");

        pageAuto.waitForTimeout(5000);

    }

    @AfterTest
    public void afterTest() {
        pageAuto.close();
        browser.close();
        playwright.close();
    }

}
