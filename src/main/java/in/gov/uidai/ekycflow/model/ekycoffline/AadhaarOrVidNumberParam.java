package in.gov.uidai.ekycflow.model.ekycoffline;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class AadhaarOrVidNumberParam {
    private long aadhaarOrVidNumber;
    private String txnNumber;
    private String shareCode;
    private String otp;
    private String deviceId;
    private String transactionId;
    private String unifiedAppRequestTxnId;
    private String uid;
    private String vid;
}
