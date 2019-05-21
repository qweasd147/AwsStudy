package com.aws.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class S3FileUtils {

    private final AmazonS3Client amazonS3Client;
    private final String bucket;
    private final String basePath;

    public S3FileUtils(AmazonS3Client amazonS3Client
            , @Value("${cloud.aws.s3.bucket}")String bucket
            , @Value("${cloud.aws.s3.basePath}")String basePath) {

        this.amazonS3Client = amazonS3Client;
        this.bucket = bucket;
        this.basePath = basePath;
    }

    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File로 전환이 실패했습니다."));

        return upload(uploadFile, dirName);
    }

    public String upload(File uploadFile, String dirName) {
        String fileName = this.basePath + dirName + uploadFile.getName();
        //String fileName = "temp/" + uploadFile.getName();
        String uploadImageUrl = null;
        try{
            uploadImageUrl = putS3(uploadFile, fileName);
        }catch (RuntimeException e){
            log.error("파일 올리기 실패", e);
            throw e;
        }finally {
            removeNewFile(uploadFile);
        }

        return uploadImageUrl;
    }

    public Optional<S3Object> getS3Object(String filePath){
        String searchFilePullPath = this.basePath + filePath;
        S3Object s3Object = amazonS3Client.getObject(new GetObjectRequest(this.bucket, searchFilePullPath));
        return Optional.of(s3Object);
    }

    public List<S3ObjectSummary> searchDir(String dirPath){
        String searchDirPullPath = this.basePath + dirPath;

        ObjectListing objects = amazonS3Client.listObjects(this.bucket, searchDirPullPath);

        /*
        ListObjectsV2Request v2Req = new ListObjectsV2Request()
                .withBucketName(this.bucket)
                .withPrefix(searchDirPullPath)
                .withDelimiter("/");

        ListObjectsV2Result v2Result = amazonS3Client.listObjectsV2(v2Req);

        List<S3ObjectSummary> summaries = v2Result.getObjectSummaries();
        List<String> prefixes = v2Result.getCommonPrefixes();
        */

        return objects.getObjectSummaries();
    }

    public void delete(String filePath){

        String deleteFilePullPath = this.basePath + filePath;

        amazonS3Client.deleteObject(new DeleteObjectRequest(this.bucket, deleteFilePullPath));
    }

    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(
                new PutObjectRequest(bucket, fileName, uploadFile)
                        .withCannedAcl(CannedAccessControlList.PublicRead)
        );

        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private void removeNewFile(File targetFile) {
        String filePath = targetFile.getAbsolutePath();
        if (targetFile.delete()) {
            log.debug(String.format("%s 파일이 삭제되었습니다. ", filePath));
        } else {
            log.debug(String.format("%s 파일이 삭제되지 못했습니다 ", filePath));
        }
    }

    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(file.getOriginalFilename());
        if(convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }

        return Optional.empty();
    }

    private boolean isDirectory(String filePathFromS3){
        // '/'로 끝나면 디렉토리라고 판단
        return filePathFromS3.endsWith("/");
    }
}
