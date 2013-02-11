/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * Warning! This file is generated. Modify at your own risk.
 */

package com.piusvelte.mosaic.android.mosaicmessageendpoint.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.json.JsonString;

/**
 * Model definition for MosaicMessage.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the . For a detailed explanation see:
 * <a href="http://code.google.com/p/google-api-java-client/wiki/Json">http://code.google.com/p/google-api-java-client/wiki/Json</a>
 * </p>
 *
 * <p>
 * Upgrade warning: starting with version 1.12 {@code getResponseHeaders()} is removed, instead use
 * {@link com.google.api.client.http.json.JsonHttpRequest#getLastResponseHeaders()}
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class MosaicMessage extends GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private String body;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @JsonString
  private Long created;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @JsonString
  private Long expiry;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private Boolean flagged;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private String id;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private Integer latitude;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private Integer longitude;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private Integer maxlatitude;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private Integer maxlongitude;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private Integer minlatitude;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private Integer minlongitude;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private Integer radius;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private Integer reports;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private String title;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private Integer visits;

  /**

   * The value returned may be {@code null}.
   */
  public String getBody() {
    return body;
  }

  /**

   * The value set may be {@code null}.
   */
  public MosaicMessage setBody(String body) {
    this.body = body;
    return this;
  }

  /**

   * The value returned may be {@code null}.
   */
  public Long getCreated() {
    return created;
  }

  /**

   * The value set may be {@code null}.
   */
  public MosaicMessage setCreated(Long created) {
    this.created = created;
    return this;
  }

  /**

   * The value returned may be {@code null}.
   */
  public Long getExpiry() {
    return expiry;
  }

  /**

   * The value set may be {@code null}.
   */
  public MosaicMessage setExpiry(Long expiry) {
    this.expiry = expiry;
    return this;
  }

  /**

   * The value returned may be {@code null}.
   */
  public Boolean getFlagged() {
    return flagged;
  }

  /**

   * The value set may be {@code null}.
   */
  public MosaicMessage setFlagged(Boolean flagged) {
    this.flagged = flagged;
    return this;
  }

  /**

   * The value returned may be {@code null}.
   */
  public String getId() {
    return id;
  }

  /**

   * The value set may be {@code null}.
   */
  public MosaicMessage setId(String id) {
    this.id = id;
    return this;
  }

  /**

   * The value returned may be {@code null}.
   */
  public Integer getLatitude() {
    return latitude;
  }

  /**

   * The value set may be {@code null}.
   */
  public MosaicMessage setLatitude(Integer latitude) {
    this.latitude = latitude;
    return this;
  }

  /**

   * The value returned may be {@code null}.
   */
  public Integer getLongitude() {
    return longitude;
  }

  /**

   * The value set may be {@code null}.
   */
  public MosaicMessage setLongitude(Integer longitude) {
    this.longitude = longitude;
    return this;
  }

  /**

   * The value returned may be {@code null}.
   */
  public Integer getMaxlatitude() {
    return maxlatitude;
  }

  /**

   * The value set may be {@code null}.
   */
  public MosaicMessage setMaxlatitude(Integer maxlatitude) {
    this.maxlatitude = maxlatitude;
    return this;
  }

  /**

   * The value returned may be {@code null}.
   */
  public Integer getMaxlongitude() {
    return maxlongitude;
  }

  /**

   * The value set may be {@code null}.
   */
  public MosaicMessage setMaxlongitude(Integer maxlongitude) {
    this.maxlongitude = maxlongitude;
    return this;
  }

  /**

   * The value returned may be {@code null}.
   */
  public Integer getMinlatitude() {
    return minlatitude;
  }

  /**

   * The value set may be {@code null}.
   */
  public MosaicMessage setMinlatitude(Integer minlatitude) {
    this.minlatitude = minlatitude;
    return this;
  }

  /**

   * The value returned may be {@code null}.
   */
  public Integer getMinlongitude() {
    return minlongitude;
  }

  /**

   * The value set may be {@code null}.
   */
  public MosaicMessage setMinlongitude(Integer minlongitude) {
    this.minlongitude = minlongitude;
    return this;
  }

  /**

   * The value returned may be {@code null}.
   */
  public Integer getRadius() {
    return radius;
  }

  /**

   * The value set may be {@code null}.
   */
  public MosaicMessage setRadius(Integer radius) {
    this.radius = radius;
    return this;
  }

  /**

   * The value returned may be {@code null}.
   */
  public Integer getReports() {
    return reports;
  }

  /**

   * The value set may be {@code null}.
   */
  public MosaicMessage setReports(Integer reports) {
    this.reports = reports;
    return this;
  }

  /**

   * The value returned may be {@code null}.
   */
  public String getTitle() {
    return title;
  }

  /**

   * The value set may be {@code null}.
   */
  public MosaicMessage setTitle(String title) {
    this.title = title;
    return this;
  }

  /**

   * The value returned may be {@code null}.
   */
  public Integer getVisits() {
    return visits;
  }

  /**

   * The value set may be {@code null}.
   */
  public MosaicMessage setVisits(Integer visits) {
    this.visits = visits;
    return this;
  }

}
