package org.sparkapp.covid19checker;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.Select;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author rowta
 */


public class OklahomaCovid19Registration {

    private String URL;
    private String name;
    private int birthMonth;
    private int birthDay;
    private int birthYear;
    private Range radius;
    private String homeAddress;
    private WebDriver driver;
    private ArrayList<String> lastAvailableCenters;
    
   

    public OklahomaCovid19Registration(String name, String URL, int birthMonth, int birthDay, int birthYear, String homeAddress, Range radius ) {
        this.name = name;
        this.URL = URL;
        this.birthMonth = birthMonth;
        this.birthDay = birthDay;
        this.birthYear = birthYear;
        this.radius = radius;
        this.homeAddress = homeAddress;
        this.lastAvailableCenters = new ArrayList<String>();
    }

    public boolean process() {
        try {
            // Setting system properties of FirefoxDriver
            this.driver = new ChromeDriver();
            driver.manage().window().maximize();
            driver.manage().deleteAllCookies();
            driver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            driver.get(this.URL);
            WebElement monthDropDown = driver.findElement(By.id("vras_followupmonth"));
            WebElement dayDropDown = driver.findElement(By.id("vras_followupday"));
            WebElement yearEntry = driver.findElement(By.id("vras_followupyear"));
            sendRepeatKeys(monthDropDown, Keys.DOWN, this.birthMonth);
            sendRepeatKeys(dayDropDown, Keys.DOWN, this.birthDay);
            yearEntry.sendKeys(Integer.toString(this.birthYear));
            WebElement submit = driver.findElement(By.id("NextButton"));//name locator for google search
            submit.click();
            
            WebDriverWait _wait = new WebDriverWait(driver, 5);
            WebElement addressBlank = driver.findElement(By.id("entity-list-map-location"));
            addressBlank.clear();
            addressBlank.sendKeys(this.homeAddress);
            WebElement radiusDistance = driver.findElement(By.id("entity-list-map-distance"));
            Select radiusSelect = new Select(radiusDistance);
            radiusSelect.selectByVisibleText(radius.distance);
            // sendRepeatKeys(radiusDistance, Keys.DOWN, radius.distance);
            WebElement searchButton = driver.findElement(By.id("entity-list-map-search"));
            searchButton.click();
            
            int size = driver.findElements(By.tagName("table")).size();
            System.out.println("Size: " + size);
            
            Thread.sleep(5000);
            WebElement baseTable = driver.findElement(By.tagName("table"));
            
            List<WebElement> tableRows = baseTable.findElements(By.tagName("tr"));
            
            int i = 0;
            
            boolean found = false;
            ArrayList<String> AvailableList = new ArrayList<>();
            for (WebElement we : tableRows) {
                
                String s = we.getText();
                
                if (!s.contains("No available booking")) {
                    found = true;
                    if (!AvailableList.contains(s)) {
                        System.out.println(this.name + ": Found Appointment:" + s);
                        AvailableList.add(s);
                    }
                }

                i++;
            }
            
            driver.quit();
            System.out.println("Searched " + i + " entries.");
            if (found) {
                // find newly available centers on list
                for (String s2 : AvailableList) {
                    if(!this.lastAvailableCenters.contains(s2))
                    {
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
                        LocalDateTime now = LocalDateTime.now();
                        String dateString = dtf.format(now);
                        String emailFromName = Configuration.getInstance().getEmailFromName();
                        String emailFromPassword = Configuration.getInstance().getEmailFromPassword();
                        for (String email : Configuration.getInstance().getEmailToNames()) {
                            Mailer.send(emailFromName, emailFromPassword, email, dateString + ": FOUND COVID-19 APPOINTMENTS FOR " + this.name, "[" + s2 + "]");
                        }
                    }      
                }
                // find centers that have dropped off the list
                for(String s2 : this.lastAvailableCenters)
                {
                    if(!AvailableList.contains(s2))
                    {
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
                        LocalDateTime now = LocalDateTime.now();
                        String dateString = dtf.format(now);
                        String emailFromName = Configuration.getInstance().getEmailFromName();
                        String emailFromPassword = Configuration.getInstance().getEmailFromPassword();
                        for (String email : Configuration.getInstance().getEmailToNames()) 
                        {
                            Mailer.send(emailFromName, emailFromPassword, email, dateString + ": CENTERS NO LONGER AVAILABLE " + this.name, "[" + s2 + "]");
                        }                        
                    }
                }
                // Set the current list equal to the last available list
                this.lastAvailableCenters = AvailableList;
            } else {
                // Notify of all available centers gone
                for(String s2 : this.lastAvailableCenters)
                {                    
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
                        LocalDateTime now = LocalDateTime.now();
                        String dateString = dtf.format(now);
                        String emailFromName = Configuration.getInstance().getEmailFromName();
                        String emailFromPassword = Configuration.getInstance().getEmailFromPassword();
                        for (String email : Configuration.getInstance().getEmailToNames()) 
                        {
                            Mailer.send(emailFromName, emailFromPassword, email, dateString + ": CENTERS NO LONGER AVAILABLE " + this.name, "[" + s2 + "]");
                        }                                          
                }
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                String dateString = dtf.format(now);
                String emailFromName = Configuration.getInstance().getEmailFromName();
                String emailFromPassword = Configuration.getInstance().getEmailFromPassword();
                // only send notification if the list wasn't empty last time but it is empty now.
                if(!this.lastAvailableCenters.isEmpty())
                {
                    for (String email : Configuration.getInstance().getEmailToNames()) {
                        Mailer.send(emailFromName, emailFromPassword, email, dateString + ": NO COVID-19 APPOINTMENTS FOR " + this.name, "Radius Checked: " + radius.distance + ". " + tableRows.size() + " Locations Scanned.");
                    }
                }
                this.lastAvailableCenters = AvailableList;                
            }

            
            return found;
        } catch (InterruptedException ex) {
            Logger.getLogger(OklahomaCovid19Registration.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
     public void sendRepeatKeys(WebElement we, Keys k, int numTimes) {
        for (int i = 0; i < numTimes; i++) {
            we.sendKeys(k);
        }
    }

}