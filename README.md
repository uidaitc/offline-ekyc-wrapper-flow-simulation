## Client Application to simulate offline eKYC wrapper API flow

### Introduction
This is a Spring boot application which can be used to download offline eKYC XML.

There are three API calls made in following order to download the XML-

1. Captcha API
2. OTP Wrapper API
3. Offline eKYC Wrapper API 

All of these APIs have JSON request and response body.

#### Minimum Requirements
- Java 8
- Maven 3.6.1

### Steps to run

##### Clone the repo and resolve dependencies. Then, assign appropriate values to constants as follows-
- application.properties - You can get these URLs from official hackathon website
    - `offline.ekyc.rest.url`
    - `captcha.rest.url`
    - `otp.rest.url`
    - If you are behind a proxy, then set `proxy.enable` to `true` and set corresponding fields. Otherwise leave it as it is 
- Constants.java
    - `EKYC_SHARE_CODE` is 4 digit code used to encrypt ZIP
    - `STAGING_UID` is the uid for which offline eKYC XML is to be downloaded
    - `DIRECTORY_PATH` - location where ZIP and XML files will be saved

##### Run the application
- The application makes call to captcha service and the captcha image is saved as `captcha.png`. Enter the captcha value in commandline.
- The application calls OTP wrapper API. You will get an OTP on phone linked with staging UID. Enter the OTP in commandline.
- The ZIP file and extracted XML will be downloaded to `DIRECTORY_PATH' location which is already provided

#### Contact
If you have any doubts around it, feel free to post those on [UIDAI hackathon forum](https://uidaiforum.cnihackathon.in/ "forum")