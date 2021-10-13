package in.gov.uidai.ekycflow.utils;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

@Component
public class SaveCaptchaUtil {
    public void saveCaptcha(String base64String) throws IOException {
        byte[] base64Val= Base64.decodeBase64(base64String);
        File imgFile = new File("captcha.png");
        BufferedImage img = ImageIO.read(new ByteArrayInputStream(base64Val));
        ImageIO.write(img, "png", imgFile);
    }
}
