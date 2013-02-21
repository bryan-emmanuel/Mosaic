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
 * Model definition for Key.
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
public final class Key extends GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private String appId;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private Boolean complete;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @JsonString
  private Long id;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private String kind;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private String name;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private String namespace;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private Key parent;

  /**

   * The value returned may be {@code null}.
   */
  public String getAppId() {
    return appId;
  }

  /**

   * The value set may be {@code null}.
   */
  public Key setAppId(String appId) {
    this.appId = appId;
    return this;
  }

  /**

   * The value returned may be {@code null}.
   */
  public Boolean getComplete() {
    return complete;
  }

  /**

   * The value set may be {@code null}.
   */
  public Key setComplete(Boolean complete) {
    this.complete = complete;
    return this;
  }

  /**

   * The value returned may be {@code null}.
   */
  public Long getId() {
    return id;
  }

  /**

   * The value set may be {@code null}.
   */
  public Key setId(Long id) {
    this.id = id;
    return this;
  }

  /**

   * The value returned may be {@code null}.
   */
  public String getKind() {
    return kind;
  }

  /**

   * The value set may be {@code null}.
   */
  public Key setKind(String kind) {
    this.kind = kind;
    return this;
  }

  /**

   * The value returned may be {@code null}.
   */
  public String getName() {
    return name;
  }

  /**

   * The value set may be {@code null}.
   */
  public Key setName(String name) {
    this.name = name;
    return this;
  }

  /**

   * The value returned may be {@code null}.
   */
  public String getNamespace() {
    return namespace;
  }

  /**

   * The value set may be {@code null}.
   */
  public Key setNamespace(String namespace) {
    this.namespace = namespace;
    return this;
  }

  /**

   * The value returned may be {@code null}.
   */
  public Key getParent() {
    return parent;
  }

  /**

   * The value set may be {@code null}.
   */
  public Key setParent(Key parent) {
    this.parent = parent;
    return this;
  }

}
