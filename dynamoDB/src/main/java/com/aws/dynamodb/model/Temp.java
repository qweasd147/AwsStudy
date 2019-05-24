package com.aws.dynamodb.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@DynamoDBTable(tableName = "Temp")
public class Temp {


    @DynamoDBHashKey(attributeName = "idx")
    @DynamoDBGeneratedUuid(DynamoDBAutoGenerateStrategy.CREATE)
    private String idx;

    @DynamoDBAttribute
    private String data1;

    @DynamoDBAttribute
    private String data2;

    @DynamoDBAttribute
    private Map<String, String> data3;

    @Builder
    public Temp(String data1, String data2, Map<String, String> data3) {
        this.data1 = data1;
        this.data2 = data2;
        this.data3 = data3;
    }

    public void changeData1(String changeData){
        this.data1 = changeData;
    }

    public void changeData2(String changeData){
        this.data2 = changeData;
    }


    @Override
    public String toString() {
        return "Temp{" +
                "idx='" + idx + '\'' +
                ", data1='" + data1 + '\'' +
                ", data2='" + data2 + '\'' +
                ", data3=" + data3 +
                '}';
    }
}
