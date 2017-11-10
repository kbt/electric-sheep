package net.kbt.esheep;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
  private final static Logger logger;
  private static final int DEFAULT_PORT = 8080;

  static {
    System.setProperty("vertx.logger-delegate-factory-class-name",
        "io.vertx.core.logging.SLF4JLogDelegateFactory");

    logger = LoggerFactory.getLogger(Main.class);
  }

  public static void main(String... args) throws Exception {
    Vertx vertx = Vertx.vertx();

    ZonedDateTime upTime = ZonedDateTime.now();

    HttpServer server = vertx.createHttpServer();

    AtomicInteger calls = new AtomicInteger();
    server.requestHandler(request -> {

      long duration = Duration.between(upTime, ZonedDateTime.now())
          .toMinutes();

      String body =
          String.format("Up since: %s (%d minutes)\nCalls: %d\n",
              upTime.format(DateTimeFormatter.ISO_DATE_TIME),
              duration,
              calls.incrementAndGet());

      logger.info("{} request from {}", request.rawMethod(), request.remoteAddress().host());

      HttpServerResponse response = request.response();
      response
          .putHeader("Content-type", "text/plain")
          .end(body);
    });

    int port = Optional.ofNullable(System.getProperty("server.port"))
        .map(Integer::valueOf)
        .orElse(DEFAULT_PORT);

    server.listen(port, result -> {
      if (result.succeeded()) {
        logger.info("App started at port {}", result.result().actualPort());
      } else {
        logger.error("App start failed", result.cause());
      }
    });
  }
}