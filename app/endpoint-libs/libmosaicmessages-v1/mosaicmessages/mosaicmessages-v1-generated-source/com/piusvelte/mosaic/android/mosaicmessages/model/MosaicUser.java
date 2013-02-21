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
  private String email;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private String encodedKey;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private Key key;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.util.List<MosaicMessage> mosaicMessages;

  static {
    // hack to force ProGuard to consider MosaicMessage used, since otherwise it would be stripped out
    // see http://code.google.com/p/google-api-java-client/issues/detail?id=528
    com.google.api.client.util.Data.nullOf(MosaicMessage.class);
  }

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private String nickname;

  /**

   * The value returned may be {@code null}.
   */
  public String getEmail() {
    return email;
  }

  /**

   * The value set may be {@code null}.
   */
  public MosaicUser setEmail(String email) {
    this.email = email;
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
  public MosaicUser setEncodedKey(String encodedKey) {
    this.encodedKey = encodedKey;
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
  public MosaicUser setKey(Key key) {
    this.key = key;
    return this;
  }

  /**

   * The value returned may be {@code null}.
   */
  public java.util.List<MosaicMessage> getMosaicMessages() {
    return mosaicMessages;
  }

  /**

   * The value set may be {@code null}.
   */
  public MosaicUser setMosaicMessages(java.util.List<MosaicMessage> mosaicMessages) {
    this.mosaicMessages = mosaicMessages;
    return this;
  }

  /**

   * The value returned may be {@code null}.
   */
  public String getNickname() {
    return nickname;
  }

  /**

   * The value set may be {@code null}.
   */
  public MosaicUser setNickname(String nickname) {
    this.nickname = nickname;
    return this;
  }

}
