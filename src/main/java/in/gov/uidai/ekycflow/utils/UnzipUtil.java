package in.gov.uidai.ekycflow.utils;

import in.gov.uidai.ekycflow.constant.Constants;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.FileHeader;
import org.springframework.stereotype.Component;

import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Base64;
import java.util.List;

@Component
public class UnzipUtil {
    public void saveXMLOnDisk(String zipBase64, String fileName) {
        File file = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            byte[] decoder = Base64.getDecoder().decode(zipBase64);
            fos.write(decoder);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        unzipFile(fileName);
    }

    public void unzipFile(String fileName) {
        final FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter("Unzipping for XML", "zip");
        final File file = new File(Constants.DIRECTORY_PATH + "/" + fileName);
        try {
            ZipFile zipFile = new ZipFile(file);
            if (extensionFilter.accept(file)) {
                if (zipFile.isEncrypted()) {
                    zipFile.setPassword(Constants.EKYC_SHARE_CODE.toCharArray());
                }
                List fileHeaderList = zipFile.getFileHeaders();

                for (int i = 0; i < fileHeaderList.size(); i++) {
                    FileHeader fileHeader = (FileHeader) fileHeaderList.get(i);
                    zipFile.extractFile(fileHeader, Constants.DIRECTORY_PATH);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
