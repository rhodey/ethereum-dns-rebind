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

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;

public class TakerConfig extends Configuration {

  @Min(100) private Long    goodbyeTimeoutMs;
  @NotEmpty private String  iptablesPath;
  @Min(1)   private Integer serverPort;

  @JsonProperty
  public Long getGoodbyeTimeoutMs() {
    return goodbyeTimeoutMs;
  }

  @JsonProperty
  public String getIptablesPath() {
    return iptablesPath;
  }

  @JsonProperty
  public Integer getServerPort() {
    return serverPort;
  }

}
