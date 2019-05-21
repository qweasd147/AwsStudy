package com.aws.api;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.aws.s3.S3FileUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/s3")
@AllArgsConstructor
public class AwsS3Controller {

    private S3FileUtils s3FileUtils;

    @GetMapping
    public ResponseEntity searchListAll(){

        List<S3ObjectSummary> list = s3FileUtils.searchDir("");
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{fileIdx}")
    public ResponseEntity searchFile(@PathVariable Long fileIdx){

        //TODO : converter
        String filePath = "";
        S3Object s3Object = s3FileUtils.getS3Object(filePath).get();
        return ResponseEntity.ok(s3Object);
    }

    @PostMapping
    public ResponseEntity updateFile(@RequestParam(value="uploadFile") MultipartFile uploadFile){

        String filePath = null;
        try {
            filePath = s3FileUtils.upload(uploadFile, "");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(filePath, HttpStatus.CREATED);
    }

    @DeleteMapping("/{fileIdx}")
    public ResponseEntity deleteFile(@PathVariable Long fileIdx){

        //TODO : converter
        String filePath = "";
        s3FileUtils.delete(filePath);

        return ResponseEntity.ok(null);
    }
}
