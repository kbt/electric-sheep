import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
  public static void main(String... args) throws Exception {
    Vertx vertx = Vertx.vertx();

    HttpServer server = vertx.createHttpServer();

    AtomicInteger calls = new AtomicInteger();
    server.requestHandler(request -> {
      HttpServerResponse response = request.response();
      response
          .putHeader("Content-type", "text/plain")
          .end("Calls: " + calls.incrementAndGet() + "\n");

    });

    int port = Optional.ofNullable(System.getProperty("server.port"))
        .map(Integer::valueOf)
        .orElse(8080);

    server.listen(port);
  }
}