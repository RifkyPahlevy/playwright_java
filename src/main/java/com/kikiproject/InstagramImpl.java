package com.kikiproject;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class InstagramImpl {
    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(
                    new BrowserType.LaunchOptions().setHeadless(false));
            BrowserContext context = browser.newContext(
                    new Browser.NewContextOptions().setStorageStatePath(Paths.get("src\\main\\resources\\State.json")));
            Page page = context.newPage();

            // 1️⃣ Login (bisa ubah ke cookies jika sudah login sebelumnya)
            // page.navigate("https://www.instagram.com/");
            // System.out.println(">>> Silakan login ke Instagram secara manual...");
            // page.waitForTimeout(200_000);
            // System.out.println("✅ Login terdeteksi, lanjut...");
            // context.storageState(
            // new
            // BrowserContext.StorageStateOptions().setPath(Paths.get("src\\main\\resources\\State.json")));

            // 2️⃣ Buka postingan yang mau discan
            String postUrl = "https://www.instagram.com/p/DPpxIdWEqwG/"; // ubah link postingan kamu
            page.navigate(postUrl);

            // // page.mouse().wheel(0, 1000);
            // Locator scrollContainer =
            // page.locator("div.x5yr21d.xw2csxc.x1odjw0f.x1n2onr6");

            // // pastikan elemen sudah ada
            // scrollContainer.waitFor();

            // // scroll beberapa kali untuk load semua komentar
            // for (int i = 0; i < 2; i++) {
            // scrollContainer.evaluate("el => el.scrollBy(0, el.scrollHeight)");
            // page.waitForTimeout(1500);
            // }

            ElementHandle scrollContainer = page.querySelector("div.x5yr21d.xw2csxc.x1odjw0f.x1n2onr6"); // ganti
                                                                                                         // selector
                                                                                                         // sesuai
                                                                                                         // elemen
                                                                                                         // scroll
            Number lastHeight = (Number) scrollContainer.evaluate("el => el.scrollHeight");

            while (true) {
                scrollContainer.evaluate("el => el.scrollTo(0, el.scrollHeight)");
                page.waitForTimeout(5000);

                Number newHeight = (Number) scrollContainer.evaluate("el => el.scrollHeight");

                if (newHeight.doubleValue() == lastHeight.doubleValue()) {
                    System.out.println("✅ Scroll selesai (sudah di akhir)");
                    break;
                }
                lastHeight = newHeight;
            }

            // 3️⃣ Scroll semua komentar (Instagram lazy-load)
            // while (true) {
            // System.err.println("Masuk while");
            // Locator more = page.locator("//*[contains(@aria-label,'comments')]");
            // if (more.count() > 0) {
            // System.out.println("ada");
            // more.first().click();
            // page.waitForTimeout(1500);
            // } else
            // break;
            // }

            page.locator(
                    "//span[@class='_ap3a _aaco _aacw _aacx _aad7 _aade']")
                    .first().hover();

            page.waitForTimeout(5000);

            // 4️⃣ Ambil komentar & username
            List<String> usernames = page.locator(
                    "//span[@class='_ap3a _aaco _aacw _aacx _aad7 _aade']")
                    .allInnerTexts();
            List<String> comments = page.locator(
                    "//span[contains(@class, 'x1lliihq x1plvlek xryxfnj x1n2onr6 xyejjpt x15dsfln x193iq5w xeuugli x1fj9vlw x13faqbe x1vvkbs x1s928wv xhkezso x1gmr53x x1cpjm7i x1fgarty x1943h6x x1i0vuye xl565be xo1l8bm x5n08af x10wh9bi xpm28yp x8viiok x1o7cslx')]")
                    .allInnerTexts();

            // Jika struktur berbeda (karena UI update), fallback
            // if (comments.isEmpty()) {
            // comments = page.locator("ul ul > li > div > div > div > div >
            // span").allInnerTexts();
            // }

            // 5️⃣ Deteksi komentar negatif atau akun fake
            List<String> negativeWords = Arrays.asList("jelek", "bodoh", "sampah", "benci", "parah", "gila");
            List<String> results = new ArrayList<>();
            results.add("username || comment");

            for (int i = 3, j = 0; i < usernames.size() && j < comments.size(); i++, j++) {

                String username = usernames.get(i).replace(",", " ");
                String comment = comments.get(j).replace(",", " ");
                System.out.println(username + " : " + comment);

                int fakeScore = 0;
                if (username.contains("test") || username.contains("fake") || username.matches(".*\\d{4,}.*")) {
                    fakeScore++;
                }

                boolean negative = negativeWords.stream().anyMatch(comment.toLowerCase()::contains);
                String action = (fakeScore >= 2 || negative) ? "flagged" : "none";

                if (!negative && action.equals("none")) {
                    results.add(String.format("%s || %s", username, comment));
                }
            }

            // 6️⃣ Simpan ke CSV
            Path file = Paths.get(
                    "C:\\Users\\Rifky_118716\\Documents\\project_study\\playwright_study\\playwright_java\\src\\main\\resources\\dataCom.csv");
            Files.write(file, results);
            System.out.println("✅ Hasil disimpan di " + file.toAbsolutePath());

            browser.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
