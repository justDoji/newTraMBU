package be.doji.productivity.trambu;

import static io.restassured.config.HttpClientConfig.httpClientConfig;
import static io.restassured.config.RestAssuredConfig.newConfig;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;

import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import org.junit.Test;

public class TrambuSmokeTest {

  private static final HttpClientConfig HTTP_CLIENT_CONFIG;
  public static final int EXPOSED_PORT = 8888;
  private static final int APPLICATION_PORT = EXPOSED_PORT;

  static {

    HTTP_CLIENT_CONFIG = httpClientConfig()
        .setParam("http.connection.timeout", APPLICATION_PORT)
        .setParam("http.socket.timeout", APPLICATION_PORT)
        .setParam("http.connection-manager.timeout", APPLICATION_PORT);
  }

  @Test
  public void givenAStartedApplication_whenAskingForTheHealthPage_thenReturnHttp200() {
    String requestUrl = ofNullable(System.getProperty("REQUEST_URL")).orElse(
        "http://192.168.99.100:" + EXPOSED_PORT + "/index.xhtml");
    await().atMost(60, SECONDS).pollInterval(5, SECONDS).ignoreExceptions()
        .untilAsserted(() -> {
          RestAssured.given()
              .config(newConfig().httpClient(HTTP_CLIENT_CONFIG))
              .log().all()
              .when().get(requestUrl)
              .then()
              .statusCode(200)
              .log().all();
        });
  }
}
