package pages;

import com.microsoft.playwright.Page;

public class AppointmentPage {
  private Page page;

  public AppointmentPage(Page page) {
    this.page = page;
  }

  public void bookAppointment() {
    page.selectOption("#combo_facility", "Tokyo CURA Healthcare Center");
    page.check("#chk_hospotal_readmission");
    page.check("#radio_program_medicaid");

    /*page.click("#txt_visit_date");
    page.locator("#txt_visit_date").fill("");
    page.fill("#txt_visit_date", "30/12/2025");*/
    page.evaluate("document.querySelector('#txt_visit_date').value = '30/12/2025'");

    page.fill("#txt_comment", "Test automation");
    page.click("#btn-book-appointment");
  }

  public String getConfirmation() {
    return page.locator("#summary h2").textContent();
  }
}