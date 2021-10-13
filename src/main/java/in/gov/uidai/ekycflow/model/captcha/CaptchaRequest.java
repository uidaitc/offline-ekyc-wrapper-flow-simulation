package in.gov.uidai.ekycflow.model.captcha;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class CaptchaRequest {
    private String langCode;
    private String captchaLength;
    private String captchaType;
}
