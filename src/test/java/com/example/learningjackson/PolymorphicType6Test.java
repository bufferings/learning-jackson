package com.example.learningjackson;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * To confirm a solution with {@code @JsonTypeInfo(use = Id.NAME)}
 * and {@code @JsonTypeName}.
 */
public class PolymorphicType6Test {

  @JsonTypeInfo(use = Id.DEDUCTION)
  @JsonSubTypes({
      @Type(Dog.class),
      @Type(Cat.class)
  })
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
        {"name":"Hachi","barkVolume":20.0}""";
    assertEquals(expected, result);
  }

  @Test
  public void canDeserializeDogWithType() throws Exception {
    String json = """
        {"name":"Hachi","barkVolume":20.0}""";

    var mapper = new ObjectMapper();
    Dog dog = mapper.readValue(json, Dog.class);
    assertEquals("Hachi", dog.name);
    assertEquals(20.0, dog.barkVolume, 0);
  }

  @Test
  public void canDeserializeDogAsAnimal() throws Exception {
    String json = """
        {"name":"Hachi","barkVolume":20.0}""";

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