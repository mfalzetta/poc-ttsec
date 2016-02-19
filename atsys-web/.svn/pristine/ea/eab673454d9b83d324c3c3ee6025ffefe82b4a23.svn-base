package com.sixlabs.atsys.repository.converter;

import com.sixlabs.atsys.domain.utils.DateUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author averri
 */
@Converter(autoApply = true)
public class LocalDateTimeJpaConverter implements AttributeConverter<LocalDateTime, Date> {

    @Override
    public Date convertToDatabaseColumn(LocalDateTime object) {
        return object != null ? Date.from(object.atZone(DateUtils.AMERICA_SAO_PAULO).toInstant()) : null;
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Date data) {
        return data != null ? LocalDateTime.ofInstant(data.toInstant(), DateUtils.AMERICA_SAO_PAULO)  : null;
    }
}
