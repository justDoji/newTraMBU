package be.doji.productivity.trambu.zulma.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

public final class JsonFormatter {

  private JsonFormatter() {
    throw new UnsupportedOperationException("Utility classes should not be instantiated");
  }

  public static String prettyPrintJSON(String response) {
    return new GsonBuilder().setPrettyPrinting().create()
        .toJson(new JsonParser().parse(response));
  }

  public static ObjectMapper objectMapper() {
    return new ObjectMapper()
        .registerModule(new ParameterNamesModule())
        .registerModule(new Jdk8Module())
        .registerModule(new JavaTimeModule());
  }

}
