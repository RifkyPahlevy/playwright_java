package com.kikiproject;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.BrowserType.LaunchOptions;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class Playwright_Impl {

    public static void main(String[] args) {
        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new LaunchOptions().setHeadless(false));
        BrowserContext context = browser.newContext();

        Page page = context.newPage();
        page.navigate("https://facebook.com");
        page.locator("//input[@id='email']").fill("rifky.fahlefi");
        page.locator("//input[@name='pass']").fill("94034545");
        page.locator("//*[@name='login']").click();
        page.waitForTimeout(3000);
        assertThat(page).hasTitle("Facebook");

    }

}
