package in.gov.uidai.ekycflow.model.captcha;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class CaptchaResponse {
    private String status;
    private String captchaBase64String;
    private String captchaTxnId;
    private String requestedDate;
    private int statusCode;
    private String message;
}
