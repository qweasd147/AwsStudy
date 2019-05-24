package com.aws.dynamodb.converter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.TimeZone;

public class LocalDateTimeTypeConverter implements DynamoDBTypeConverter<Long, LocalDateTime> {
    @Override
    public Long convert(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
    }

    @Override
    public LocalDateTime unconvert(Long dateTime) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(dateTime)
                , TimeZone.getDefault().toZoneId());
    }
}
