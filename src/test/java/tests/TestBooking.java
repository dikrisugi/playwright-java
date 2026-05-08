package tests;

import com.microsoft.playwright.*;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.LoginPage;
import pages.AppointmentPage;
import org.testng.annotations.Test;
import org.testng.annotations.Listeners;
import utils.ScreenshotListener;
import java.nio.file.Paths;
import org.testng.ITestResult;
import io.qameta.allure.Allure;
import java.io.FileInputStream;

@Listeners(ScreenshotListener.class)

public class TestBooking {

  Playwright playwright;
  Browser browser;
  BrowserContext context;
  public Page page;

  LoginPage login;
  AppointmentPage appointment;

  @BeforeClass
  public void setup() {
    playwright = Playwright.create();
    browser = playwright.chromium().launch(
      new BrowserType.LaunchOptions().setHeadless(false)
    );  
  }
  
  @BeforeMethod
  public void setupTest(){
	  context = browser.newContext(
			    new Browser.NewContextOptions()
			      .setRecordVideoDir(java.nio.file.Paths.get("videos/"))
			      .setRecordVideoSize(1280, 720)
			  );

			  page = context.newPage();
	    login = new LoginPage(page);
	    appointment = new AppointmentPage(page);
  }

  @Test
  public void testBooking() {
    page.navigate("https://katalon-demo-cura.herokuapp.com/");

    login.clickMakeAppointment();
    login.login("John Doe", "ThisIsNotAPassword");

    appointment.bookAppointment();

    String confirm = appointment.getConfirmation();

    System.out.println("Actual: " + confirm);

    // 🔥 ASSERTION (ini standar QA)
    //Assert.assertTrue(confirm.contains("Appointment Confirmation"));
    Assert.assertTrue(false);
  }
  
  @Test
  public void testLoginFailed() {
	  page.navigate("https://katalon-demo-cura.herokuapp.com/");

	  login.clickMakeAppointment();
	  login.login("John Doe", "WrongPassword");

	  String error = page.locator(".lead.text-danger").textContent();

	  Assert.assertTrue(error.contains("Login failed"));
	  //Assert.assertTrue(false);
  }
  
  @AfterMethod
  public void tearDownTest(ITestResult result) {

    try {
      Video video = page.video();
      
      // 🔥 BARU attach ke Allure
      if (result.getStatus() == ITestResult.FAILURE) {

    	  // 🔥 SCREENSHOT
    	  String screenshotPath = "screenshots/" + result.getName() + "_" + System.currentTimeMillis() + ".png";

    	  new java.io.File("screenshots").mkdirs();

    	  page.screenshot(new Page.ScreenshotOptions()
    	      .setPath(Paths.get(screenshotPath)));

    	  System.out.println("📸 Screenshot saved: " + screenshotPath);

    	  // 🔥 ATTACH KE ALLURE (INI FIX)
    	  try (FileInputStream fis = new FileInputStream(screenshotPath)) {
    	    Allure.addAttachment("Screenshot", "image/png", fis, ".png");
    	  }
    	}

      context.close();

      if (result.getStatus() == ITestResult.FAILURE && video != null) {

        String fileName = "test-output/videos/" + result.getName() + "_" + System.currentTimeMillis() + ".webm";

        java.io.File dir = new java.io.File("test-output/videos");
        dir.mkdirs();

        java.nio.file.Path path = java.nio.file.Paths.get(fileName);

        video.saveAs(path);

        String videoUrl = "videos/" + new java.io.File(fileName).getName();

        System.out.println("🎥 Video saved: " + fileName);

        // 🔥 INI WAJIB (FIX UTAMA)
        org.testng.Reporter.setCurrentTestResult(result);

        // 🔥 LOG KE REPORT
        org.testng.Reporter.log(
        	"<br><b>🎥 Video Evidence:</b> <a href='" + videoUrl + "' target='_blank'>Open Video</a><br>",
        	true
        );
        
        // 🔥 attach ke ALLURE (INI YANG BENAR)
        try (FileInputStream fis = new FileInputStream(fileName)) {
        	  Allure.addAttachment("Video", "video/webm", fis, ".webm");
        	}
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @AfterClass
  public void tearDown() {
    browser.close();
    playwright.close();
  }
}