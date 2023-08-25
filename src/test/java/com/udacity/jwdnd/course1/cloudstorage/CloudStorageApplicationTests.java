package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.File;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.firefoxdriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new FirefoxDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doMockSignUp(String firstName, String lastName, String userName, String password) {
		// Create a dummy account for logging in later.

		// Visit the sign-up page.
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		driver.get("http://localhost:" + this.port + "/signup");
		webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));

		// Fill out credentials
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
		WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
		inputFirstName.click();
		inputFirstName.sendKeys(firstName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
		WebElement inputLastName = driver.findElement(By.id("inputLastName"));
		inputLastName.click();
		inputLastName.sendKeys(lastName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.click();
		inputUsername.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.click();
		inputPassword.sendKeys(password);

		// Attempt to sign up.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
		WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
		buttonSignUp.click();

		/*
		 * Check that the sign up was successful.
		 * // You may have to modify the element "success-msg" and the sign-up
		 * // success message below depening on the rest of your code.
		 */
		Assertions
				.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
	}

	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doLogIn(String userName, String password) {
		// Log in to our dummy account.
		driver.get("http://localhost:" + this.port + "/login");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement loginUserName = driver.findElement(By.id("inputUsername"));
		loginUserName.click();
		loginUserName.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement loginPassword = driver.findElement(By.id("inputPassword"));
		loginPassword.click();
		loginPassword.sendKeys(password);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
		WebElement loginButton = driver.findElement(By.id("login-button"));
		loginButton.click();

		webDriverWait.until(ExpectedConditions.titleContains("Home"));

	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
	 * rest of your code.
	 * This test is provided by Udacity to perform some basic sanity testing of
	 * your code to ensure that it meets certain rubric criteria.
	 * 
	 * If this test is failing, please ensure that you are handling redirecting
	 * users
	 * back to the login page after a succesful sign up.
	 * Read more about the requirement in the rubric:
	 * https://review.udacity.com/#!/rubrics/2724/view
	 */
	@Test
	public void testRedirection() {
		// Create a test account
		doMockSignUp("Redirection", "Test", "RT", "123");

		// Check if we have been redirected to the log in page.
		Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
	 * rest of your code.
	 * This test is provided by Udacity to perform some basic sanity testing of
	 * your code to ensure that it meets certain rubric criteria.
	 * 
	 * If this test is failing, please ensure that you are handling bad URLs
	 * gracefully, for example with a custom error page.
	 * 
	 * Read more about custom error pages at:
	 * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
	 */
	@Test
	public void testBadUrl() {
		// Create a test account
		doMockSignUp("URL", "Test", "UT", "123");
		doLogIn("UT", "123");

		// Try to access a random made-up URL.
		driver.get("http://localhost:" + this.port + "/some-random-page");
		Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
	 * rest of your code.
	 * This test is provided by Udacity to perform some basic sanity testing of
	 * your code to ensure that it meets certain rubric criteria.
	 * 
	 * If this test is failing, please ensure that you are handling uploading large
	 * files (>1MB),
	 * gracefully in your code.
	 * 
	 * Read more about file size limits here:
	 * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload
	 * Limits" section.
	 */
	@Test
	public void testLargeUpload() {
		// Create a test account
		doMockSignUp("Large File", "Test", "LFT", "123");
		doLogIn("LFT", "123");

		// Try to upload an arbitrary large file
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		String fileName = "upload5m.zip";

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
		WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
		fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

		WebElement uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();
		try {
			webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("Large File upload failed");
		}
		Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));

	}

	@Test
	public void testNoteCreation() {
		String noteTitle = "title";
		String noteDescription = "description";
		// Create a test account
		doMockSignUp("noteCreation", "Test", "noteCreation", "123");
		doLogIn("noteCreation", "123");

		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		WebElement noteNavBar = driver.findElement(By.id("nav-notes-tab"));
		noteNavBar.click();
		createNote(noteTitle, noteDescription);

	}

	@Test
	public void testNoteUpdate() {
		String noteNewTitle = "noteNewTitle";
		String noteNewDescription = "noteNewDescription";
		// Create a test account
		doMockSignUp("noteUpdate", "Test", "noteUpdate", "123");
		doLogIn("noteUpdate", "123");

		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		WebElement noteNavBar = driver.findElement(By.id("nav-notes-tab"));
		noteNavBar.click();
		updateNote(noteNewTitle, noteNewDescription);

	}

	@Test
	public void testNoteDeletion() {
		String noteNewTitle = "title";
		String noteNewDescription = "description";
		// Create a test account
		doMockSignUp("noteDelete", "Test", "noteDelete", "123");
		doLogIn("noteDelete", "123");

		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		WebElement noteNavBar = driver.findElement(By.id("nav-notes-tab"));
		noteNavBar.click();
		deleteNote(noteNewTitle, noteNewDescription);

	}

	@Test
	public void testCredentialCreation() {
		String url = "some/url";
		String username = "credentialCreation";
		String password = "789";
		// Create a test account
		doMockSignUp(username, "Test", username, "123");
		doLogIn(username, "123");

		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		driver.findElement(By.id("nav-credentials-tab")).click();
		createCredential(url, username, password);

	}

	@Test
	public void testCredentialUpdate() {
		String url = "some/url";
		String username = "credentialCreation";
		String password = "789";
		// Create a test account
		doMockSignUp("editCred", "Test", "editCred", "123");
		doLogIn("editCred", "123");

		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		driver.findElement(By.id("nav-credentials-tab")).click();
		updateCredential(url, username, password);

	}

	@Test
	public void testCredentialDeletion() {
		String url = "some/url";
		String username = "credentialCreation";
		String password = "789";
		// Create a test account
		doMockSignUp("credDelete", "Test", "credDelete", "123");
		doLogIn("credDelete", "123");

		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		driver.findElement(By.id("nav-credentials-tab")).click();
		deleteCredential(url, username, password);

	}

	private void createNote(String title, String description) {
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-creation-button")));
		WebElement createNoteButton = driver.findElement(By.id("note-creation-button"));
		createNoteButton.click();

		typeInNoteDetails(title, description);
		checkSuccessfulOperation(webDriverWait);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userTable")));
		WebElement userTable = driver.findElement(By.id("userTable"));

		Assertions.assertTrue(userTable.getText().contains(title));
		Assertions.assertTrue(userTable.getText().contains(description));
	}

	private void updateNote(String title, String description) {
		String oldTitle = "oldTitle";
		String oldDescription = "oldDescription";
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-creation-button")));
		WebElement createNoteButton = driver.findElement(By.id("note-creation-button"));
		createNoteButton.click();
		// create note
		typeInNoteDetails(oldTitle, oldDescription);
		checkSuccessfulOperation(webDriverWait);
		// update note
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-edit-button")));
		WebElement editNoteButton = driver.findElement(By.id("note-edit-button"));
		editNoteButton.click();
		typeInNoteDetails(title, description);
		checkSuccessfulOperation(webDriverWait);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userTable")));
		WebElement userTable = driver.findElement(By.id("userTable"));

		Assertions.assertTrue(userTable.getText().contains(title));
		Assertions.assertTrue(userTable.getText().contains(description));

	}

	private void deleteNote(String title, String description) {
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-creation-button")));
		WebElement createNoteButton = driver.findElement(By.id("note-creation-button"));
		createNoteButton.click();

		typeInNoteDetails(title, description);
		checkSuccessfulOperation(webDriverWait);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userTable")));
		WebElement userTable = driver.findElement(By.id("userTable"));

		Assertions.assertTrue(userTable.getText().contains(title));
		Assertions.assertTrue(userTable.getText().contains(description));

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("delete-note")));
		driver.findElement(By.id("delete-note")).click();
		checkSuccessfulOperation(webDriverWait);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userTable")));
		WebElement userTableElement = driver.findElement(By.id("userTable"));
		Assertions.assertFalse(userTableElement.getText().contains(title));
		Assertions.assertFalse(userTableElement.getText().contains(description));

	}

	private void typeInNoteDetails(String title, String description) {
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		WebElement noteTitle = driver.findElement(By.id("note-title"));
		WebElement noteDescription = driver.findElement(By.id("note-description"));

		noteTitle.click();
		noteTitle.sendKeys(title);

		noteDescription.click();
		noteDescription.sendKeys(description);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-submit")));
		WebElement createNote = driver.findElement(By.id("note-submit"));
		createNote.click();
	}

	private void createCredential(String url, String username, String password) {
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-creation-button")));
		driver.findElement(By.id("credential-creation-button")).click();

		typeInCredentialDetails(url, username, password, false);
		checkSuccessfulOperation(webDriverWait);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
		WebElement credentialTable = driver.findElement(By.id("credentialTable"));

		Assertions.assertTrue(credentialTable.getText().contains(url));
		Assertions.assertTrue(credentialTable.getText().contains(username));
	}

	private void updateCredential(String url, String username, String password) {
		String oldUrl = "some/old/url";
		String oldUsername = "oldUsername";
		String oldPassword = "oldPassword";
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-creation-button")));
		driver.findElement(By.id("credential-creation-button")).click();
		// create credential
		typeInCredentialDetails(oldUrl, oldUsername, oldPassword, false);
		checkSuccessfulOperation(webDriverWait);
		// update credential
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-edit-button")));
		driver.findElement(By.id("credential-edit-button")).click();
		typeInCredentialDetails(url, username, password, true);
		checkSuccessfulOperation(webDriverWait);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
		WebElement credentialTable = driver.findElement(By.id("credentialTable"));

		Assertions.assertTrue(credentialTable.getText().contains(url));
		Assertions.assertTrue(credentialTable.getText().contains(username));

	}

	private void deleteCredential(String url, String username, String password) {
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-creation-button")));
		driver.findElement(By.id("credential-creation-button")).click();

		typeInCredentialDetails(url, username, password, false);
		checkSuccessfulOperation(webDriverWait);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
		WebElement credentialTable = driver.findElement(By.id("credentialTable"));

		Assertions.assertTrue(credentialTable.getText().contains(url));
		Assertions.assertTrue(credentialTable.getText().contains(username));

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("delete-credential")));
		driver.findElement(By.id("delete-credential")).click();
		checkSuccessfulOperation(webDriverWait);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
		WebElement credentialTableElement = driver.findElement(By.id("credentialTable"));
		Assertions.assertFalse(credentialTableElement.getText().contains(url));
		Assertions.assertFalse(credentialTableElement.getText().contains(username));

	}

	private void typeInCredentialDetails(String url, String username, String password, boolean isEdit) {
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		WebElement urlInput = driver.findElement(By.id("credential-url"));
		urlInput.click();
		if (isEdit) {
			urlInput.clear();
		}
		urlInput.sendKeys(url);

		WebElement usernameInput = driver.findElement(By.id("credential-username"));
		usernameInput.click();
		if (isEdit) {
			usernameInput.clear();

		}
		usernameInput.sendKeys(username);

		WebElement passwordInput = driver.findElement(By.id("credential-password"));
		passwordInput.click();
		if (isEdit) {
			passwordInput.clear();
		}
		passwordInput.sendKeys(password);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-submit")));
		driver.findElement(By.id("credential-submit")).click();
	}

	private void checkSuccessfulOperation(WebDriverWait webDriverWait) {
		// Assert that the operation was successfully
		webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
		Assertions.assertEquals("http://localhost:" + this.port + "/result", driver.getCurrentUrl());
		driver.findElement(By.id("result-to-home")).click();
		Assertions.assertEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl());

	}

}
