package in.gov.uidai.ekycflow.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.gov.uidai.ekycflow.constant.Constants;
import in.gov.uidai.ekycflow.model.otp.OtpRequest;
import in.gov.uidai.ekycflow.model.otp.OtpResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Slf4j
@Service
public class OtpCallerService {
    @Value("${otp.rest.url}")
    private String URL;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ObjectMapper objectMapper;

    public OtpResponse sendOtpOnPhone(OtpRequest otpRequest) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(Constants.OTP_X_REQUEST_ID, UUID.randomUUID().toString());
        headers.add(Constants.OTP_APPID, Constants.OTP_APPID_VALUE);
        headers.add(Constants.OTP_ACCEPT_LANGUAGE, Constants.OTP_ACCEPT_LANGUAGE_VALUE);

        String reqBodyData = objectMapper.writeValueAsString(otpRequest);
        System.out.println(reqBodyData);
        HttpEntity<String> requestEntity = new HttpEntity<>(reqBodyData, headers);

        try{
            ResponseEntity<byte[]> result = restTemplate.exchange(URL, HttpMethod.POST, requestEntity, byte[].class);
            if(result==null || result.getBody().length == 0){
                throw new Exception("OTP API returned with 0 bytes in response body");
            }
            if(result.getStatusCode().equals(HttpStatus.OK)) {
                log.debug(String.format("Unmarshalling data for OTP"));
                OtpResponse otpResponse = objectMapper.readValue(result.getBody(), OtpResponse.class);
                System.out.println(otpResponse.toString());
                if(otpResponse.getStatus().equals("Failure")) {
                    throw new Exception("Did not get proper response from OTP server");
                }
                return otpResponse;
            }
            else {
                throw new Exception("Did not get proper response from OTP server");
            }
        }
        catch (Exception e)
        {
            log.error("Error occurred while accessing url");
            throw new Exception("Error",e);
        }
    }
}
