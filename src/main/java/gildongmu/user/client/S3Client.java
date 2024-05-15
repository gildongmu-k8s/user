package gildongmu.user.client;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import gildongmu.user.exception.S3Exception;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.UUID;

import static gildongmu.user.exception.ErrorCode.*;


@Slf4j
@Component
@RequiredArgsConstructor
public class S3Client {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;

    public String upload(MultipartFile image) {
        String fileName = createFileName(image.getOriginalFilename());
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(image.getSize());
        metadata.setContentType(image.getContentType());

        putS3(image, fileName, metadata);

        return fileName;
    }

    private File convert(MultipartFile image, String newFileName) throws IOException {
        File convertFile = new File(newFileName);
        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(image.getBytes());
            } catch (IOException e) {
                throw new S3Exception(FILE_CONVERT_ERROR);
            }
            return convertFile;
        }
        throw new S3Exception(FILE_CONVERT_ERROR);
    }

    private String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new S3Exception(WRONG_FILE_FORMAT);
        }
    }

    private String putS3(MultipartFile uploadFile, String fileName, ObjectMetadata metadata) {
        try (InputStream inputStream = uploadFile.getInputStream()) {
            amazonS3.putObject(new PutObjectRequest(bucketName, fileName, inputStream, metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            return amazonS3.getUrl(bucketName, fileName).toString();
        } catch (IOException e) {
            throw new S3Exception(UPLOAD_FAILED);
        }
    }

    public void delete(String imageUrl) {
        try {
            String key = getKeyFromImageUrl(imageUrl);
            amazonS3.deleteObject(bucketName, key);
        } catch (Exception e) {
            log.error(" Code : {}, Message : {}", DELETE_FAILED.name(), DELETE_FAILED.getMessage());
        }
    }

    private String getKeyFromImageUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            String decodingKey = URLDecoder.decode(url.getPath(), "UTF-8");
            return decodingKey.substring(1);
        } catch (MalformedURLException | UnsupportedEncodingException e) {
            throw new S3Exception(DELETE_FAILED);
        }
    }
}
