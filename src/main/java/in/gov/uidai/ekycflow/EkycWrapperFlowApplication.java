package in.gov.uidai.ekycflow;

import in.gov.uidai.ekycflow.constant.Constants;
import in.gov.uidai.ekycflow.model.captcha.CaptchaResponse;
import in.gov.uidai.ekycflow.model.ekycoffline.OfflineEkycXMLResponse;
import in.gov.uidai.ekycflow.model.otp.OtpRequest;
import in.gov.uidai.ekycflow.model.otp.OtpResponse;
import in.gov.uidai.ekycflow.service.CaptchaCallerService;
import in.gov.uidai.ekycflow.service.EKycCallerService;
import in.gov.uidai.ekycflow.service.OtpCallerService;
import in.gov.uidai.ekycflow.utils.SaveCaptchaUtil;
import in.gov.uidai.ekycflow.utils.UnzipUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class EkycWrapperFlowApplication implements CommandLineRunner {

    @Autowired
    CaptchaCallerService captchaCallerService;

    @Autowired
    OtpCallerService otpCallerService;

    @Autowired
    EKycCallerService eKycCallerService;

    @Autowired
    UnzipUtil unzipUtil;

    @Autowired
    SaveCaptchaUtil saveCaptchaUtil;

    public static void main(String[] args) {
        SpringApplication.run(EkycWrapperFlowApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        CaptchaResponse captchaResponse = captchaCallerService.fetchCaptchaResponse();
        OtpRequest otpRequest = new OtpRequest();
        otpRequest.setUidNumber(Constants.STAGING_UID);
        otpRequest.setCaptchaTxnId(captchaResponse.getCaptchaTxnId());
        System.out.println("######  Captcha base64 #######");
        System.out.println(captchaResponse.getCaptchaBase64String());

        saveCaptchaUtil.saveCaptcha(captchaResponse.getCaptchaBase64String());

        Scanner scanner = new Scanner(System.in);
        String captchaText = scanner.nextLine();
        otpRequest.setCaptchaValue(captchaText);
        otpRequest.setTransactionId(Constants.TRANSACTION_ID);
        OtpResponse otpResponse = otpCallerService.sendOtpOnPhone(otpRequest);
        System.out.println("Enter OTP");
        int otp = scanner.nextInt();
        OfflineEkycXMLResponse offlineEkycXMLResponse = eKycCallerService.fetcheKycXml(otp, otpResponse.getTxnId());
        unzipUtil.saveXMLOnDisk(offlineEkycXMLResponse.geteKycXML(), offlineEkycXMLResponse.getFileName());
    }
}
