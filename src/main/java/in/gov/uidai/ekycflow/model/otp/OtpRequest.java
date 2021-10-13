package in.gov.uidai.ekycflow.model.otp;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class OtpRequest {
    private String uidNumber;
    private String captchaTxnId;
    private String captchaValue;
    private String transactionId;
}
