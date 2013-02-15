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

/**
 * Model definition for MosaicUser.
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
public final class MosaicUser extends GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String email;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String id;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String nickname;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String userid;

  /**

   * The value returned may be {@code null}.
   */
  public java.lang.String getEmail() {
    return email;
  }

  /**

   * The value set may be {@code null}.
   */
  public MosaicUser setEmail(java.lang.String email) {
    this.email = email;
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
  public MosaicUser setId(java.lang.String id) {
    this.id = id;
    return this;
  }

  /**

   * The value returned may be {@code null}.
   */
  public java.lang.String getNickname() {
    return nickname;
  }

  /**

   * The value set may be {@code null}.
   */
  public MosaicUser setNickname(java.lang.String nickname) {
    this.nickname = nickname;
    return this;
  }

  /**

   * The value returned may be {@code null}.
   */
  public java.lang.String getUserid() {
    return userid;
  }

  /**

   * The value set may be {@code null}.
   */
  public MosaicUser setUserid(java.lang.String userid) {
    this.userid = userid;
    return this;
  }

}
