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

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class TakerApp extends Application<TakerConfig> {

  @Override
  public String getName() {
    return "taker";
  }

  @Override
  public void run(TakerConfig config, Environment environment) {
    environment.healthChecks().register("dumb", new DumbHealthCheck());
    environment.jersey().register(new TakingResource(config));
  }

  public static void main(String[] args) throws Exception {
    new TakerApp().run(args);
  }

}
