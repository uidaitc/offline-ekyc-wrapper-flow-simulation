package in.gov.uidai.ekycflow.model.ekycoffline;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class OfflineEkycXMLResponse implements Serializable {
    private static final long serialVersionUID = 6449564989990671308L;

    private String eKycXML;
    private String fileName;
    private String status;
    private LocalDate requestDate;
    private String uidNumber;

    public String geteKycXML() {
        return eKycXML;
    }

    public void seteKycXML(String eKycXML) {
        this.eKycXML = eKycXML;
    }
}
