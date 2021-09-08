package com.example.learningjackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * To confirm a problem related to polymorphic types.
 */
public class PolymorphicType1Test {

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
  public void canSerializeDog() throws Exception {
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
  public void canSerializeCat() throws Exception {
    Cat cat = new Cat();
    cat.name = "Tama";
    cat.likesCream = true;
    cat.lives = 10;

    String result = new ObjectMapper()
        .writeValueAsString(cat);

    String expected = """
        {"name":"Tama","likesCream":true,"lives":10}""";
    assertEquals(expected, result);
  }

  @Test
  public void canDeserializeDog() throws Exception {
    String json = """
        {"name":"Hachi","barkVolume":20.0}""";

    var mapper = new ObjectMapper();
    Dog dog = mapper.readValue(json, Dog.class);
    assertEquals("Hachi", dog.name);
    assertEquals(20.0, dog.barkVolume, 0);
  }

  @Test
  public void canDeserializeCat() throws Exception {
    String json = """
        {"name":"Tama","likesCream":true,"lives":10}""";

    var mapper = new ObjectMapper();
    Cat cat = mapper.readValue(json, Cat.class);
    assertEquals("Tama", cat.name);
    assertEquals(true, cat.likesCream);
    assertEquals(10, cat.lives);
  }

  @Test
  public void cannotDeserializeDogAsAnimal() throws Exception {
    String json = """
        {"name":"Hachi","barkVolume":20.0}""";

    var mapper = new ObjectMapper();
    assertThrows(InvalidDefinitionException.class, () -> {
      mapper.readValue(json, Animal.class);
    });
  }

}