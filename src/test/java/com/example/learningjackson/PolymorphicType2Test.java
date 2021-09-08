package com.example.learningjackson;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidTypeIdException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * To confirm a solution with {@code @JsonTypeInfo(use = Id.CLASS)}.
 */
public class PolymorphicType2Test {

  @JsonTypeInfo(use = Id.CLASS)
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
  public void canSerializeDogWithClass() throws Exception {
    Dog dog = new Dog();
    dog.name = "Hachi";
    dog.barkVolume = 20.0;

    String result = new ObjectMapper()
        .writeValueAsString(dog);

    String expected = """
        {"@class":"com.example.learningjackson.PolymorphicType2Test$Dog","name":"Hachi","barkVolume":20.0}""";
    assertEquals(expected, result);
  }

  @Test
  public void canDeserializeDogWithClass() throws Exception {
    String json = """
        {"@class":"com.example.learningjackson.PolymorphicType2Test$Dog","name":"Hachi","barkVolume":20.0}""";

    var mapper = new ObjectMapper();
    Dog dog = mapper.readValue(json, Dog.class);
    assertEquals("Hachi", dog.name);
    assertEquals(20.0, dog.barkVolume, 0);
  }

  @Test
  public void cannotDeserializeDogWithoutClass() throws Exception {
    String json = """
        {"name":"Hachi","barkVolume":20.0}""";

    var mapper = new ObjectMapper();
    assertThrows(InvalidTypeIdException.class, () -> {
      mapper.readValue(json, Dog.class);
    });
  }

  @Test
  public void canDeserializeDogAsAnimalWithClass() throws Exception {
    String json = """
        {"@class":"com.example.learningjackson.PolymorphicType2Test$Dog","name":"Hachi","barkVolume":20.0}""";

    var mapper = new ObjectMapper();
    Animal animal = mapper.readValue(json, Animal.class);

    if (!(animal instanceof Dog dog)) {
      fail("Animal isn't dog");
      return;
    }

    assertEquals("Hachi", dog.name);
    assertEquals(20.0, dog.barkVolume, 0);
  }

}