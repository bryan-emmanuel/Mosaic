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

package com.piusvelte.mosaic.android.mosaicmessages.model;

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
  private java.lang.String body;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @JsonString
  private java.lang.Long created;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @JsonString
  private java.lang.Long expiry;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String id;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Integer latitude;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Integer longitude;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Integer maxLatitude;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Integer maxLongitude;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Integer minLatitude;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Integer minLongitude;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Integer minlongitude;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private MosaicUser mosaicUser;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String mosaicUserEmail;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Integer radius;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Integer reports;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String title;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.util.List<java.lang.String> visitors;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Integer visits;

  /**

   * The value returned may be {@code null}.
   */
  public java.lang.String getBody() {
    return body;
  }

  /**

   * The value set may be {@code null}.
   */
  public MosaicMessage setBody(java.lang.String body) {
    this.body = body;
    return this;
  }

  /**

   * The value returned may be {@code null}.
   */
  public java.lang.Long getCreated() {
    return created;
  }

  /**

   * The value set may be {@code null}.
   */
  public MosaicMessage setCreated(java.lang.Long created) {
    this.created = created;
    return this;
  }

  /**

   * The value returned may be {@code null}.
   */
  public java.lang.Long getExpiry() {
    return expiry;
  }

  /**

   * The value set may be {@code null}.
   */
  public MosaicMessage setExpiry(java.lang.Long expiry) {
    this.expiry = expiry;
    return this;
  }

  /**

   * The value returned may be {@code null}.
   */
  public java.lang.String getId() {
    return id;
  }

  /**

   * The value set may be {@code null}.
   */
  public MosaicMessage setId(java.lang.String id) {
    this.id = id;
    return this;
  }

  /**

   * The value returned may be {@code null}.
   */
  public java.lang.Integer getLatitude() {
    return latitude;
  }

  /**

   * The value set may be {@code null}.
   */
  public MosaicMessage setLatitude(java.lang.Integer latitude) {
    this.latitude = latitude;
    return this;
  }

  /**

   * The value returned may be {@code null}.
   */
  public java.lang.Integer getLongitude() {
    return longitude;
  }

  /**

   * The value set may be {@code null}.
   */
  public MosaicMessage setLongitude(java.lang.Integer longitude) {
    this.longitude = longitude;
    return this;
  }

  /**

   * The value returned may be {@code null}.
   */
  public java.lang.Integer getMaxLatitude() {
    return maxLatitude;
  }

  /**

   * The value set may be {@code null}.
   */
  public MosaicMessage setMaxLatitude(java.lang.Integer maxLatitude) {
    this.maxLatitude = maxLatitude;
    return this;
  }

  /**

   * The value returned may be {@code null}.
   */
  public java.lang.Integer getMaxLongitude() {
    return maxLongitude;
  }

  /**

   * The value set may be {@code null}.
   */
  public MosaicMessage setMaxLongitude(java.lang.Integer maxLongitude) {
    this.maxLongitude = maxLongitude;
    return this;
  }

  /**

   * The value returned may be {@code null}.
   */
  public java.lang.Integer getMinLatitude() {
    return minLatitude;
  }

  /**

   * The value set may be {@code null}.
   */
  public MosaicMessage setMinLatitude(java.lang.Integer minLatitude) {
    this.minLatitude = minLatitude;
    return this;
  }

  /**

   * The value returned may be {@code null}.
   */
  public java.lang.Integer getMinLongitude() {
    return minLongitude;
  }

  /**

   * The value set may be {@code null}.
   */
  public MosaicMessage setMinLongitude(java.lang.Integer minLongitude) {
    this.minLongitude = minLongitude;
    return this;
  }

  /**

   * The value returned may be {@code null}.
   */
  public java.lang.Integer getMinlongitude() {
    return minlongitude;
  }

  /**

   * The value set may be {@code null}.
   */
  public MosaicMessage setMinlongitude(java.lang.Integer minlongitude) {
    this.minlongitude = minlongitude;
    return this;
  }

  /**

   * The value returned may be {@code null}.
   */
  public MosaicUser getMosaicUser() {
    return mosaicUser;
  }

  /**

   * The value set may be {@code null}.
   */
  public MosaicMessage setMosaicUser(MosaicUser mosaicUser) {
    this.mosaicUser = mosaicUser;
    return this;
  }

  /**

   * The value returned may be {@code null}.
   */
  public java.lang.String getMosaicUserEmail() {
    return mosaicUserEmail;
  }

  /**

   * The value set may be {@code null}.
   */
  public MosaicMessage setMosaicUserEmail(java.lang.String mosaicUserEmail) {
    this.mosaicUserEmail = mosaicUserEmail;
    return this;
  }

  /**

   * The value returned may be {@code null}.
   */
  public java.lang.Integer getRadius() {
    return radius;
  }

  /**

   * The value set may be {@code null}.
   */
  public MosaicMessage setRadius(java.lang.Integer radius) {
    this.radius = radius;
    return this;
  }

  /**

   * The value returned may be {@code null}.
   */
  public java.lang.Integer getReports() {
    return reports;
  }

  /**

   * The value set may be {@code null}.
   */
  public MosaicMessage setReports(java.lang.Integer reports) {
    this.reports = reports;
    return this;
  }

  /**

   * The value returned may be {@code null}.
   */
  public java.lang.String getTitle() {
    return title;
  }

  /**

   * The value set may be {@code null}.
   */
  public MosaicMessage setTitle(java.lang.String title) {
    this.title = title;
    return this;
  }

  /**

   * The value returned may be {@code null}.
   */
  public java.util.List<java.lang.String> getVisitors() {
    return visitors;
  }

  /**

   * The value set may be {@code null}.
   */
  public MosaicMessage setVisitors(java.util.List<java.lang.String> visitors) {
    this.visitors = visitors;
    return this;
  }

  /**

   * The value returned may be {@code null}.
   */
  public java.lang.Integer getVisits() {
    return visits;
  }

  /**

   * The value set may be {@code null}.
   */
  public MosaicMessage setVisits(java.lang.Integer visits) {
    this.visits = visits;
    return this;
  }

}
