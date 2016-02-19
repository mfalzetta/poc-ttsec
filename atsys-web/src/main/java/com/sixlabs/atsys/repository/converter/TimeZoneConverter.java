package com.sixlabs.atsys.repository.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.TimeZone;

/**
 * @author averri
 */
@Converter
public class TimeZoneConverter implements AttributeConverter<TimeZone, String> {

    @Override
    public String convertToDatabaseColumn(TimeZone object) {
        return object != null ? object.getID() : null;
    }

    @Override
    public TimeZone convertToEntityAttribute(String data) {
        return data != null ? TimeZone.getTimeZone(data) : null;
    }
}
