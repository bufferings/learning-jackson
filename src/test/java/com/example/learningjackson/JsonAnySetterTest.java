package com.example.learningjackson;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonAnySetterTest {
  public static class MyItem {
    public String name;
    public Map<String, String> properties = new HashMap<>();

    @JsonAnySetter
    public void add(String key, String value) {
      properties.put(key, value);
    }
  }

  @Test
  public void test() throws Exception {
    String json = """
        {"id":2,"name":"book","ownerName":"John","creator":"Mike"}""";

    MyItem result = new ObjectMapper().readValue(json, MyItem.class);

    assertEquals("book", result.name);
    assertEquals("2", result.properties.get("id"));
    assertEquals("John", result.properties.get("ownerName"));
    assertEquals("Mike", result.properties.get("creator"));
  }
}