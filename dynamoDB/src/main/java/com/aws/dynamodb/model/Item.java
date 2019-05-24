package com.aws.dynamodb.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;

import java.time.Instant;
import java.time.LocalDateTime;

@DynamoDBTable(tableName="Items")
public class Item {

    @DynamoDBRangeKey(attributeName = "ItemId")
    private String itemID;

    @DynamoDBHashKey(attributeName = "TempKey")
    private String tempKey;

}
