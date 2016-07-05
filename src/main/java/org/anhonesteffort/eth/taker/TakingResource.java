/*
 * Copyright (C) 2015 An Honest Effort LLC, coping.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.anhonesteffort.eth.taker;

import org.glassfish.jersey.server.ManagedAsync;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Path("/")
public class TakingResource {

  private static final Logger log = LoggerFactory.getLogger(TakingResource.class);
  private final TakerConfig config;

  public TakingResource(TakerConfig config) {
    this.config = config;
  }

  @GET
  public Response serveHtml(@Context HttpServletRequest request) {
    log.debug(request.getRemoteAddr() + " requested html");
    return Response.ok(
        new File("src/main/resources/take.html"),
        MediaType.TEXT_HTML
    ).header("Connection", "close").build();
  }

  @GET
  @Path("/bundle.js")
  public Response serveJs(@Context HttpServletRequest request) {
    log.debug(request.getRemoteAddr() + " requested js");
    return Response.ok(
        new File("src/main/resources/bundle.js"),
        MediaType.TEXT_PLAIN
    ).header("Connection", "close").build();
  }

  @GET
  @Path("/goodbye")
  @ManagedAsync
  public void goodbye(@Context   HttpServletRequest request,
                      @Suspended AsyncResponse      response)
  {
    log.info(request.getRemoteAddr() + " time to say goodbye");
    CompletableFuture<Integer> future = new CompletableFuture<>();

    response.setTimeout(config.getGoodbyeTimeoutMs(), TimeUnit.MILLISECONDS);
    response.setTimeoutHandler(asyncResponse -> {
      log.error(request.getRemoteAddr() + " goodbye response timed out");
      future.cancel(true);
    });

    future.whenComplete((ok, ex) -> {
      if (ok != null) {
        log.info(request.getRemoteAddr() + " iptables hook succeeded");
        response.resume(Response.ok("goodbye", MediaType.TEXT_PLAIN).header("Connection", "close").build());
      } else {
        log.error(request.getRemoteAddr() + " iptables hook failed", ex);
        response.resume(Response.status(500).header("Connection", "close").build());
      }
    });

    new IptablesHook(future, request.getRemoteAddr()).run();
  }

  private class IptablesHook implements Runnable {

    private final CompletableFuture<Integer> future;
    private final String command;

    public IptablesHook(CompletableFuture<Integer> future, String clientAddress) {
      this.future = future;
      command     = config.getIptablesPath() + " " + config.getServerPort() + " " + clientAddress;
    }

    @Override
    public void run() {
      try {

        Process process = Runtime.getRuntime().exec(command);

        if (process.waitFor() == 0) {
          future.complete(1337);
        } else {
          BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
          String         line   = reader.readLine();

          while (line != null) {
            log.error("iptables error -> " + line);
            line = reader.readLine();
          }

          future.completeExceptionally(new IllegalStateException("iptables"));
        }

      } catch (Throwable e) {
        future.completeExceptionally(e);
      }
    }
  }

}
