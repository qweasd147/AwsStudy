package com.aws.s3;

import com.amazonaws.services.s3.model.S3Object;

public class FileInfo{

    private S3Object s3Object;

    private FileInfo(S3Object s3Object) {
        this.s3Object = s3Object;
    }

    public static FileInfo of(S3Object s3Object){
        return new FileInfo(s3Object);
    }

    //TODO : file info details
}
