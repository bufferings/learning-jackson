package com.example.learningjackson;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static com.example.learningjackson.JsonEnumDefaultValueTest.Types.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonEnumDefaultValueTest {
  enum Types {
    TYPE_A, TYPE_B, @JsonEnumDefaultValue TYPE_C
  }

  @Test
  public void test() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    mapper.enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE);

    assertEquals(TYPE_A, mapper.readValue("\"TYPE_A\"", Types.class));
    assertEquals(TYPE_B, mapper.readValue("\"TYPE_B\"", Types.class));
    assertEquals(TYPE_C, mapper.readValue("\"TYPE_C\"", Types.class));

    assertEquals(TYPE_C, mapper.readValue("\"TYPE_UNKNOWN\"", Types.class));
  }
}