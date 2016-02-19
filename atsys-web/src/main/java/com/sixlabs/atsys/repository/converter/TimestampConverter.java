package com.sixlabs.atsys.repository.converter;

import com.sixlabs.atsys.domain.utils.DateUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Converte um objeto {@link LocalDateTime} em uma representação em BigDecimal, para armazenamento na
 * base de dados. O MySQL, por exemplo, não suporta representação de data/hora com a parte dos
 * milisegundos. Uma solução é armazenar a data/hora no formato decimal 'yyyyMMddHHmmss.SSS'.
 *
 * @author averri
 */
@Converter
public class TimestampConverter implements AttributeConverter<LocalDateTime, BigDecimal> {

    private static final DateTimeFormatter dtf =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmss.SSS")
                    .withZone(DateUtils.AMERICA_SAO_PAULO);

    @Override
    public BigDecimal convertToDatabaseColumn(LocalDateTime object) {
        return object != null ? new BigDecimal(dtf.format(object)) : null;
    }

    @Override
    public LocalDateTime convertToEntityAttribute(BigDecimal data) {
        return data != null ? LocalDateTime.parse(data.toString(), dtf) : null;
    }
}
