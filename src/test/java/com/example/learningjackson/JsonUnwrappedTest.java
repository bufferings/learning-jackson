package com.example.learningjackson;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonUnwrappedTest {

  public static class UnwrappedUser {
    public int id;

    @JsonUnwrapped
    public Name name;

    public UnwrappedUser() {
    }

    public UnwrappedUser(int id, Name name) {
      this.id = id;
      this.name = name;
    }

    public static class Name {
      public String firstName;
      public String lastName;

      public Name() {
      }

      public Name(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
      }
    }
  }

  @Test
  public void unwrapWhenSerializing() throws Exception {
    UnwrappedUser.Name name = new UnwrappedUser.Name("John", "Doe");
    UnwrappedUser user = new UnwrappedUser(1, name);

    String result = new ObjectMapper().writeValueAsString(user);

    String expected = """
        {"id":1,"firstName":"John","lastName":"Doe"}""";
    assertEquals(expected, result);
  }

  @Test
  public void wrapWhenDeserializing() throws Exception {
    String json = """
        {"id":1,"firstName":"John","lastName":"Doe"}""";

    UnwrappedUser result = new ObjectMapper().readValue(json, UnwrappedUser.class);

    assertEquals(1, result.id);
    assertEquals("John", result.name.firstName);
    assertEquals("Doe", result.name.lastName);
  }
}