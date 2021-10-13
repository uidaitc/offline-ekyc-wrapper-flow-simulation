package in.gov.uidai.ekycflow.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.gov.uidai.ekycflow.constant.Constants;
import in.gov.uidai.ekycflow.model.captcha.CaptchaResponse;
import in.gov.uidai.ekycflow.model.captcha.CaptchaRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class CaptchaCallerService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ObjectMapper objectMapper;

    @Value("${captcha.rest.url}")
    private String URL;

    public CaptchaResponse fetchCaptchaResponse() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        CaptchaRequest captchaRequest = new CaptchaRequest();
        captchaRequest.setCaptchaLength(Constants.CAPTCHA_LENGTH);
        captchaRequest.setCaptchaType(Constants.CAPTCHA_TYPE);
        captchaRequest.setLangCode(Constants.CAPTCHA_LANG_CODE);
        String reqBodyData = objectMapper.writeValueAsString(captchaRequest);
        System.out.println(reqBodyData);
        HttpEntity<String> requestEntity = new HttpEntity<>(reqBodyData, headers);

        try{
            ResponseEntity<byte[]> result = restTemplate.exchange(URL, HttpMethod.POST, requestEntity, byte[].class);
            if(result==null || result.getBody().length == 0){
                throw new Exception("Captcha returned with 0 bytes in response body");
            }
            if(result.getStatusCode().equals(HttpStatus.OK)) {
                log.debug(String.format("Unmarshalling data for captcha call"));
                CaptchaResponse captchaResponse = objectMapper.readValue(result.getBody(), CaptchaResponse.class);
                System.out.println(captchaResponse.toString());
                if(!(captchaResponse.getStatusCode()==200)) {
                    throw new Exception("Did not get proper response from Captcha server");
                }
                return captchaResponse;
            }
            else {
                throw new Exception("Did not get proper response from Captcha server");
            }
        }
        catch (Exception e)
        {
            log.error("Error occurred while accessing url");
            throw new Exception("Error",e);
        }

    }
}
