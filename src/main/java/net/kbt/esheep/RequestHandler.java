package net.kbt.esheep;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

public class RequestHandler implements Handler<HttpServerRequest> {
  private final static Logger logger = LoggerFactory.getLogger(RequestHandler.class);
  private final ZonedDateTime upTime;
  private final static AtomicInteger calls = new AtomicInteger();

  RequestHandler(ZonedDateTime upTime) {
    this.upTime = upTime;
  }

  @Override
  public void handle(HttpServerRequest request) {
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

  }
}
