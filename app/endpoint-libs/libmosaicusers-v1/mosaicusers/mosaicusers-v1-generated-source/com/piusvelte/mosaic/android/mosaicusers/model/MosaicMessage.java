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

package com.piusvelte.mosaic.android.mosaicusers.model;

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
  @com.google.api.client.util.Key
  private String encodedKey;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @JsonString
  private Long expiry;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.util.List<String> geocells;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private Key key;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private Integer latitudeE6;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private Integer longitudeE6;

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
  private MosaicUser user;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.util.List<Key> visitors;

  static {
    // hack to force ProGuard to consider Key used, since otherwise it would be stripped out
    // see http://code.google.com/p/google-api-java-client/issues/detail?id=528
    com.google.api.client.util.Data.nullOf(Key.class);
  }

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
  public String getEncodedKey() {
    return encodedKey;
  }

  /**

   * The value set may be {@code null}.
   */
  public MosaicMessage setEncodedKey(String encodedKey) {
    this.encodedKey = encodedKey;
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
  public java.util.List<String> getGeocells() {
    return geocells;
  }

  /**

   * The value set may be {@code null}.
   */
  public MosaicMessage setGeocells(java.util.List<String> geocells) {
    this.geocells = geocells;
    return this;
  }

  /**

   * The value returned may be {@code null}.
   */
  public Key getKey() {
    return key;
  }

  /**

   * The value set may be {@code null}.
   */
  public MosaicMessage setKey(Key key) {
    this.key = key;
    return this;
  }

  /**

   * The value returned may be {@code null}.
   */
  public Integer getLatitudeE6() {
    return latitudeE6;
  }

  /**

   * The value set may be {@code null}.
   */
  public MosaicMessage setLatitudeE6(Integer latitudeE6) {
    this.latitudeE6 = latitudeE6;
    return this;
  }

  /**

   * The value returned may be {@code null}.
   */
  public Integer getLongitudeE6() {
    return longitudeE6;
  }

  /**

   * The value set may be {@code null}.
   */
  public MosaicMessage setLongitudeE6(Integer longitudeE6) {
    this.longitudeE6 = longitudeE6;
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
  public MosaicUser getUser() {
    return user;
  }

  /**

   * The value set may be {@code null}.
   */
  public MosaicMessage setUser(MosaicUser user) {
    this.user = user;
    return this;
  }

  /**

   * The value returned may be {@code null}.
   */
  public java.util.List<Key> getVisitors() {
    return visitors;
  }

  /**

   * The value set may be {@code null}.
   */
  public MosaicMessage setVisitors(java.util.List<Key> visitors) {
    this.visitors = visitors;
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
