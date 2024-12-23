package com.nttdata.credit.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.nttdata.credit.model.enums.TypeCredit;

import java.io.IOException;

public class CreditTypeDeserializer extends JsonDeserializer<TypeCredit> {
    @Override
    public TypeCredit deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getText().toUpperCase();
        return TypeCredit.valueOf(value);
    }
}