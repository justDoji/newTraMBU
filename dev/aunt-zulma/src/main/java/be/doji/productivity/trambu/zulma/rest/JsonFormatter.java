package be.doji.productivity.trambu.zulma.rest;

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
}
