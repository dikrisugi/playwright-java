package pages;

import com.microsoft.playwright.Page;

public class LoginPage {
  private Page page;

  public LoginPage(Page page) {
    this.page = page;
  }

  public void clickMakeAppointment() {
    page.click("#btn-make-appointment");
  }

  public void login(String username, String password) {
    page.fill("#txt-username", username);
    page.fill("#txt-password", password);
    page.click("#btn-login");
  }
}