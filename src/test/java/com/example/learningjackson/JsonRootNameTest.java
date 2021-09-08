package com.example.learningjackson;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonRootNameTest {
  @JsonRootName(value = "my_item")
  public static class MyItem {
    public String name;
    public String ownerName;

    public MyItem(String name, String ownerName) {
      this.name = name;
      this.ownerName = ownerName;
    }
  }

  @Test
  public void test() throws Exception {
    MyItem myItem = new MyItem("Book", "Bob");

    ObjectMapper mapper = new ObjectMapper();
    mapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
    String result = mapper.writeValueAsString(myItem);

    String expect = """
        {"my_item":{"name":"Book","ownerName":"Bob"}}""";
    assertEquals(expect, result);
  }
}