package com.example.learningjackson;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidTypeIdException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * To confirm a problem with {@code @JsonTypeInfo(use = Id.NAME)}
 * without {@code @JsonSubTypes}.
 */
public class PolymorphicType3Test {

  @JsonTypeInfo(use = Id.NAME)
  public static abstract class Animal {
    public String name;
  }

  public static class Dog extends Animal {
    public double barkVolume;
  }

  public static class Cat extends Animal {
    public boolean likesCream;
    public int lives;
  }

  @Test
  public void canSerializeDogWithType() throws Exception {
    Dog dog = new Dog();
    dog.name = "Hachi";
    dog.barkVolume = 20.0;

    String result = new ObjectMapper()
        .writeValueAsString(dog);

    String expected = """
        {"@type":"PolymorphicType3Test$Dog","name":"Hachi","barkVolume":20.0}""";
    assertEquals(expected, result);
  }

  @Test
  public void canDeserializeDogWithType() throws Exception {
    String json = """
        {"@type":"PolymorphicType3Test$Dog","name":"Hachi","barkVolume":20.0}""";

    var mapper = new ObjectMapper();
    Dog dog = mapper.readValue(json, Dog.class);
    assertEquals("Hachi", dog.name);
    assertEquals(20.0, dog.barkVolume, 0);
  }

  @Test
  public void cannotDeserializeDogWithoutType() throws Exception {
    String json = """
        {"name":"Hachi","barkVolume":20.0}""";

    var mapper = new ObjectMapper();
    assertThrows(InvalidTypeIdException.class, () -> {
      mapper.readValue(json, Dog.class);
    });
  }

  @Test
  public void cannotDeserializeDogAsAnimal() throws Exception {
    String json = """
        {"@type":"PolymorphicType3Test$Dog","name":"Hachi","barkVolume":20.0}""";

    var mapper = new ObjectMapper();
    var e = assertThrows(InvalidTypeIdException.class, () -> {
      mapper.readValue(json, Animal.class);
    });

    assertEquals("""
        Could not resolve type id 'PolymorphicType3Test$Dog' as a subtype of `com.example.learningjackson.PolymorphicType3Test$Animal`: known type ids = []
         at [Source: (String)"{"@type":"PolymorphicType3Test$Dog","name":"Hachi","barkVolume":20.0}"; line: 1, column: 10]\
        """, e.getMessage());
  }

}