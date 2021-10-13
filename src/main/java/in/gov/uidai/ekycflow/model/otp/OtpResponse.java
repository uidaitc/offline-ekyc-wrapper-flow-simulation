package in.gov.uidai.ekycflow.model.otp;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class OtpResponse {
    private String uidNumber;
    private String mobileNumber;
    private String txnId;
    private String status; //Success/Failure
    private String message;
}
