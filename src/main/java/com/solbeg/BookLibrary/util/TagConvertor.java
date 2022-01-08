package com.solbeg.BookLibrary.util;

import com.solbeg.BookLibrary.entity.Tag;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class TagConvertor implements AttributeConverter<Tag, String> {
    @Override
    public String convertToDatabaseColumn(Tag attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public Tag convertToEntityAttribute(String dbData) {
        return Stream.of(Tag.values())
                .filter(c -> c.name().equals(dbData))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
