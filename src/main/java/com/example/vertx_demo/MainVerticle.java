package com.example.vertx_demo;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.MultiMap;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    // 创建路由
    Router router = Router.router(vertx);
    // 嵌入处理逻辑
    router.route().handler(context -> {
      // 获取请求相关信息
      String address = context.request().connection().remoteAddress().toString();
      MultiMap queryParams = context.queryParams();
      String name = queryParams.contains("name") ? queryParams.get("name") : "unknown";
      // 响应内容
      context.json(
        new JsonObject()
          .put("name", name)
          .put("address", address)
          .put("message", "Hello " + name + " connected from " + address)
      );
    });

    // 将绑定logic的Router注册到HttpServer上
    int port = 8888;
    vertx.createHttpServer()
      .requestHandler(router)
      .listen(port, http -> {
        if (http.succeeded()) {
          startPromise.complete();
          System.out.println("HTTP server started on port: " + port);
        } else {
          startPromise.fail(http.cause());
        }
      });
  }
}
