# Shot Availability Checker

This is a Java client that runs on a PC and checks for vaccine availability.  It sends notifications
via SMS/MMS/E-Mail through an SMTP gateway indicating changes in availability. 

## Checks the Oklahoma Covid Vaccine Website for Availability

# Setup
1) Requires Selenium ChromeDriver matching the version of Chrome you have installed
2) You must have the user-specific URL given to you after registration at the Vaccine website.
3) SMTP account (gmail or any other).
4) SMS/MMS Gateway or E-mail address:

# Configuration File
### Location of Chrome Browser Driver
webdriver=["C:\Users\Path-To\Documents\chromedriver_win32\chromedriver.exe"]

### Multiple Entries
Parameters: State, Name (can be anything), URL,address, radius in miles from address (5,10,25,50,100 only)

entry=["Massachusetts","First Last","https://vaccinate.oklahoma.gov/follow-up-vaccine/?id=XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXX","12","25","1970","1600 Pennsylvania Blvd, Boston, MA 99999, United States","100"]

entry=["Massachusetts","First Last","https://vaccinate.oklahoma.gov/follow-up-vaccine/?id=XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXX","12","25","1970","1600 Pennsylvania Blvd, Boston, MA 99999, United States","100"]

### E-Mail Settings (Use a gmail or any othe SMTP account)
smtphost=["smtp.gmail.com"]

smtpport=["465"]

smtpauth=["true"]

emailfromname=["XXXXXX@gmail.com"]

emailfrompassword=["xxxxxxxxxxx"]

### Multiple SMS or E-mail Notification Recipients. (SMS/MMS through e-mail gateway.)
emailtoname=["######@vzwpix.com"]

emailtoname=["######@yahoo.com"]
