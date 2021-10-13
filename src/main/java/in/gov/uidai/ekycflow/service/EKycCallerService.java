package in.gov.uidai.ekycflow.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.gov.uidai.ekycflow.constant.Constants;
import in.gov.uidai.ekycflow.model.ekycoffline.AadhaarOrVidNumberParam;
import in.gov.uidai.ekycflow.model.ekycoffline.OfflineEkycXMLResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Slf4j
@Service
public class EKycCallerService {

    @Value("${offline.ekyc.rest.url}")
    private String URL;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ObjectMapper objectMapper;

    public OfflineEkycXMLResponse fetcheKycXml(int otp, String txnId) throws Exception {
        AadhaarOrVidNumberParam aadhaarOrVidNumberParam = new AadhaarOrVidNumberParam();
        aadhaarOrVidNumberParam.setUid(Constants.STAGING_UID);
        aadhaarOrVidNumberParam.setShareCode(Constants.EKYC_SHARE_CODE);
        aadhaarOrVidNumberParam.setOtp(String.valueOf(otp));
        aadhaarOrVidNumberParam.setTxnNumber(txnId);
        OfflineEkycXMLResponse offlineEkycXMLResponse = getEKycXml(aadhaarOrVidNumberParam);
        System.out.println(objectMapper.writeValueAsString(offlineEkycXMLResponse));
        return offlineEkycXMLResponse;
    }

    private OfflineEkycXMLResponse getEKycXml(AadhaarOrVidNumberParam aadhaarOrVidNumberParam) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(Constants.EKYC_X_REQUEST_ID, UUID.randomUUID().toString());
        headers.add(Constants.EKYC_APP_ID, Constants.EKYC_APP_ID_VALUE);
        headers.add(Constants.EKYC_TRANSACTION_ID, UUID.randomUUID().toString());

        String reqBodyData = objectMapper.writeValueAsString(aadhaarOrVidNumberParam);
        System.out.println(reqBodyData);
        HttpEntity<String> requestEntity = new HttpEntity<>(reqBodyData, headers);

        try{
            ResponseEntity<byte[]> result = restTemplate.exchange(URL, HttpMethod.POST, requestEntity, byte[].class);
            if(result==null || result.getBody().length == 0){
                throw new Exception("Offline eKYC API returned with 0 bytes in response body");
            }
            if(result.getStatusCode().equals(HttpStatus.OK)) {
                log.debug("Unmarshalling data for eKYC call");
                OfflineEkycXMLResponse offlineEkycXMLResponse = objectMapper.readValue(result.getBody(), OfflineEkycXMLResponse.class);
                if(!(offlineEkycXMLResponse.getStatus().equals("Success"))) {
                    throw new Exception("Did not get proper response from offline eKYC server");
                }
                return offlineEkycXMLResponse;
            }
            else {
                throw new Exception("HTTP Status not OK. Did not get proper response from offline eKYC server");
            }
        }
        catch (Exception e) {
            log.error("Error occurred while accessing url");
            throw new Exception("Error",e);
        }
    }
}
