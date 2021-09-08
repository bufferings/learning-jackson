package com.example.learningjackson;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonViewTest {

  public static class Views {
    public static class Public {}

    public static class Internal extends Public {}
  }

  public static class Item {

    public Item() {
    }

    public Item(int id, String itemName, String ownerName, String creator) {
      this.id = id;
      this.itemName = itemName;
      this.ownerName = ownerName;
      this.creator = creator;
    }

    @JsonView(Views.Public.class)
    public int id;

    @JsonView(Views.Public.class)
    public String itemName;

    @JsonView(Views.Internal.class)
    public String ownerName;

    public String creator;
  }

  @Test
  public void withParentView() throws Exception {
    Item item = new Item(2, "book", "John", "Mike");

    String result = new ObjectMapper()
        .writerWithView(Views.Public.class)
        .writeValueAsString(item);

    String expected = """
        {"id":2,"itemName":"book","creator":"Mike"}""";
    assertEquals(expected, result);
  }

  @Test
  public void withChildView() throws Exception {
    Item item = new Item(2, "book", "John", "Mike");

    String result = new ObjectMapper()
        .writerWithView(Views.Internal.class)
        .writeValueAsString(item);

    String expected = """
        {"id":2,"itemName":"book","ownerName":"John","creator":"Mike"}""";
    assertEquals(expected, result);
  }

}