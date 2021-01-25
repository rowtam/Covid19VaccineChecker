/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sparkapp.ShotAvailabilityChecker;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rowta
 */
public class Configuration {
    // static variable single_instance of type Singleton 
    private static Configuration single_instance = null; 
  
    // variable of type String 
    private String fileName; 
    
    private ArrayList<OklahomaCovid19Registration> OklahomaList;
    
    private String emailFromName;
    private String emailFromPassword;
    private ArrayList<String> emailToNames;
    private String webDriverLocation;
    private String smtphost, smtpport, smtpauth;
    private int period;
  
    // private constructor restricted to this class itself 
    private Configuration() 
    { 
        this.fileName = "";
        this.emailFromName = "";
        this.emailFromPassword = "";
        this.emailToNames = new ArrayList<>();
        this.OklahomaList = new ArrayList<>();
        this.webDriverLocation = "";
        this.smtphost = "";
        this.smtpport = "";
        this.smtpauth = "";
        this.period = 15;
    } 
  
    // static method to create instance of Singleton class 
    public static Configuration getInstance() 
    { 
        if (getSingle_instance() == null) 
            single_instance = new Configuration(); 
  
        return single_instance; 
    } 
    
    public void loadConfiguration(String fileName) {
        this.setFileName(fileName);
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                if(!line.trim().startsWith("#") && !line.trim().isBlank())
                {
                    line = line.trim();
                    //System.out.println("Line: "+line);
                    String[] tokens = line.split("=\\[");
                    tokens[1] = tokens[1].substring(0,tokens[1].length() - 1);
                    switch(tokens[0])
                    {
                        case "webdriver":
                            this.setWebDriverLocation(tokens[1].replaceAll("\"", ""));
                            break;
                        case "entry":
                            String[] registrationTokens = tokens[1].split(",\"");
                            if (registrationTokens.length >= 8) {
                                if (registrationTokens[0].trim().replaceAll("\"", "").equalsIgnoreCase("Oklahoma")) {
                                    String name = registrationTokens[1].trim().replaceAll("\"", "");
                                    String URL = registrationTokens[2].trim().replaceAll("\"", "");
                                    String month = registrationTokens[3].trim().replaceAll("\"", "");
                                    String day = registrationTokens[4].trim().replaceAll("\"", "");
                                    String year = registrationTokens[5].trim().replaceAll("\"", "");
                                    String address = registrationTokens[6].trim().replaceAll("\"", "");
                                    String radius = registrationTokens[7].trim().replaceAll("\"", "");
                                    int monthInt = Integer.parseInt(month);
                                    int dayInt = Integer.parseInt(day);
                                    int yearInt = Integer.parseInt(year);
                                    int radiusInt = Integer.parseInt(radius);
                                    //System.out.println("Got here:"+name+","+URL+","+monthInt+","+dayInt+","+yearInt+","+address+","+radiusInt);
                                    Range r;
                                    if (radiusInt == 5) {
                                        r = Range.r5MILES;
                                    } else if (radiusInt == 10) {
                                        r = Range.r10MILES;
                                    } else if (radiusInt == 25) {
                                        r = Range.r25MILES;
                                    } else if (radiusInt == 50) {
                                        r = Range.r50MILES;
                                    } else if (radiusInt == 100) {
                                        r = Range.r100MILES;
                                    } else {
                                        r = Range.r5MILES;
                                        System.out.println("Setting to 5.  Error Valid Values: 5, 10, 25, 50, 100");
                                    }
                                    OklahomaCovid19Registration ok = new OklahomaCovid19Registration(
                                            name,
                                            URL,
                                            monthInt,
                                            dayInt,
                                            yearInt,
                                            address,
                                            r);
                                    this.getOklahomaList().add(ok);
                                }
                            }
                            break;
                        case "emailfromname":
                            this.setEmailFromName(tokens[1].trim().replaceAll("\"", ""));
                            break;
                        case "emailtoname":
                            this.getEmailToNames().add(tokens[1].trim().replaceAll("\"", ""));
                            break;
                        case "emailfrompassword":
                            this.setEmailFromPassword(tokens[1].trim().replaceAll("\"", ""));
                            break;
                        case "smtphost":
                            this.setSmtphost(tokens[1].trim().replaceAll("\"", ""));
                            break;
                        case "smtpport":
                            this.setSmtpport(tokens[1].trim().replaceAll("\"", ""));                        
                            break;
                        case "smtpauth":
                            this.setSmtpauth(tokens[1].trim().replaceAll("\"", ""));
                            break;
                        case "period":
                            this.setPeriod(Integer.parseInt(tokens[1].trim().replaceAll("\"", "")));
                            break;
                        default:
                            break;
                            
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the single_instance
     */
    public static Configuration getSingle_instance() {
        return single_instance;
    }

    /**
     * @param aSingle_instance the single_instance to set
     */
    public static void setSingle_instance(Configuration aSingle_instance) {
        single_instance = aSingle_instance;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return the OklahomaList
     */
    public ArrayList<OklahomaCovid19Registration> getOklahomaList() {
        return OklahomaList;
    }

    /**
     * @param OklahomaList the OklahomaList to set
     */
    public void setOklahomaList(ArrayList<OklahomaCovid19Registration> OklahomaList) {
        this.OklahomaList = OklahomaList;
    }

    /**
     * @return the emailFromName
     */
    public String getEmailFromName() {
        return emailFromName;
    }

    /**
     * @param emailFromName the emailFromName to set
     */
    public void setEmailFromName(String emailFromName) {
        this.emailFromName = emailFromName;
    }

    /**
     * @return the emailFromPassword
     */
    public String getEmailFromPassword() {
        return emailFromPassword;
    }

    /**
     * @param emailFromPassword the emailFromPassword to set
     */
    public void setEmailFromPassword(String emailFromPassword) {
        this.emailFromPassword = emailFromPassword;
    }

    /**
     * @return the emailToNames
     */
    public ArrayList<String> getEmailToNames() {
        return emailToNames;
    }

    /**
     * @param emailToNames the emailToNames to set
     */
    public void setEmailToNames(ArrayList<String> emailToNames) {
        this.emailToNames = emailToNames;
    }

    /**
     * @return the webDriverLocation
     */
    public String getWebDriverLocation() {
        return webDriverLocation;
    }

    /**
     * @param webDriverLocation the webDriverLocation to set
     */
    public void setWebDriverLocation(String webDriverLocation) {
        this.webDriverLocation = webDriverLocation;
    }

    /**
     * @return the smtphost
     */
    public String getSmtphost() {
        return smtphost;
    }

    /**
     * @param smtphost the smtphost to set
     */
    public void setSmtphost(String smtphost) {
        this.smtphost = smtphost;
    }

    /**
     * @return the smtpport
     */
    public String getSmtpport() {
        return smtpport;
    }

    /**
     * @param smtpport the smtpport to set
     */
    public void setSmtpport(String smtpport) {
        this.smtpport = smtpport;
    }

    /**
     * @return the smtpauth
     */
    public String getSmtpauth() {
        return smtpauth;
    }

    /**
     * @param smtpauth the smtpauth to set
     */
    public void setSmtpauth(String smtpauth) {
        this.smtpauth = smtpauth;
    }

    /**
     * @return the period
     */
    public int getPeriod() {
        return period;
    }

    /**
     * @param period the period to set
     */
    public void setPeriod(int period) {
        this.period = period;
    }

}
