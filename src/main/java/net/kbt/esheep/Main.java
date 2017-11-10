package net.kbt.esheep;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.time.ZonedDateTime;
import java.util.Optional;

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

    Runtime.getRuntime().addShutdownHook(getShutdownThread());

    HttpServer server = vertx.createHttpServer();
    server.requestHandler(new RequestHandler(ZonedDateTime.now()));

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

  private static Thread getShutdownThread() {
    return new Thread(() -> logger.info("App shutdown triggered"));
  }
}