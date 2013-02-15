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
 * This file was generated.
 *  with google-apis-code-generator 1.2.0 (build: 2013-02-14 15:45:00 UTC)
 *  on 2013-02-15 at 02:52:26 UTC 
 */

package com.piusvelte.mosaic.android.mosaicmessageendpoint;

import com.google.api.client.googleapis.GoogleUtils;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.common.base.Preconditions;

/**
 * Service definition for Mosaicmessageendpoint (v1).
 *
 * <p>
 * This is an API
 * </p>
 *
 * <p>
 * For more information about this service, see the
 * <a href="" target="_blank">API Documentation</a>
 * </p>
 *
 * <p>
 * This service uses {@link MosaicmessageendpointRequestInitializer} to initialize global parameters via its
 * {@link Builder}.
 * </p>
 *
 * <p>
 * Upgrade warning: this class now extends {@link AbstractGoogleJsonClient}, whereas in prior
 * version 1.8 it extended {@link com.google.api.client.googleapis.services.GoogleClient}.
 * </p>
 *
 * @since 1.3
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public class Mosaicmessageendpoint extends AbstractGoogleJsonClient {

  // Note: Leave this static initializer at the top of the file.
  static {
    Preconditions.checkState(GoogleUtils.VERSION.equals("1.13.2-beta"),
        "You are currently running with version %s of google-api-client. " +
        "You need version 1.13.2-beta of google-api-client to run version " +
        "1.13.2-beta of the  library.", GoogleUtils.VERSION);
  }

  /**
   * The default encoded root URL of the service. This is determined when the library is generated
   * and normally should not be changed.
   *
   * @since 1.7
   */
  public static final String DEFAULT_ROOT_URL = "https://mosaic-messaging.appspot.com/_ah/api/";

  /**
   * The default encoded service path of the service. This is determined when the library is
   * generated and normally should not be changed.
   *
   * @since 1.7
   */
  public static final String DEFAULT_SERVICE_PATH = "mosaicmessageendpoint/v1/";

  /**
   * The default encoded base URL of the service. This is determined when the library is generated
   * and normally should not be changed.
   * @deprecated (scheduled to be removed in 1.13)
   */
  @Deprecated
  public static final String DEFAULT_BASE_URL = DEFAULT_ROOT_URL + DEFAULT_SERVICE_PATH;

  /**
   * Constructor.
   *
   * <p>
   * Use {@link Builder} if you need to specify any of the optional parameters.
   * </p>
   *
   * @param transport HTTP transport
   * @param jsonFactory JSON factory
   * @param httpRequestInitializer HTTP request initializer or {@code null} for none
   * @since 1.7
   */
  public Mosaicmessageendpoint(HttpTransport transport, JsonFactory jsonFactory,
      HttpRequestInitializer httpRequestInitializer) {
    super(transport,
        jsonFactory,
        DEFAULT_ROOT_URL,
        DEFAULT_SERVICE_PATH,
        httpRequestInitializer,
        false);
  }

  /**
   * @param transport HTTP transport
   * @param httpRequestInitializer HTTP request initializer or {@code null} for none
   * @param rootUrl root URL of the service
   * @param servicePath service path
   * @param jsonObjectParser JSON object parser
   * @param googleClientRequestInitializer Google request initializer or {@code null} for none
   * @param applicationName application name to be sent in the User-Agent header of requests or
   *        {@code null} for none
   * @param suppressPatternChecks whether discovery pattern checks should be suppressed on required
   *        parameters
   */
  Mosaicmessageendpoint(HttpTransport transport,
      HttpRequestInitializer httpRequestInitializer,
      String rootUrl,
      String servicePath,
      JsonObjectParser jsonObjectParser,
      GoogleClientRequestInitializer googleClientRequestInitializer,
      String applicationName,
      boolean suppressPatternChecks) {
    super(transport,
        httpRequestInitializer,
        rootUrl,
        servicePath,
        jsonObjectParser,
        googleClientRequestInitializer,
        applicationName,
        suppressPatternChecks);
  }

  @Override
  protected void initialize(AbstractGoogleClientRequest<?> httpClientRequest) throws java.io.IOException {
    super.initialize(httpClientRequest);
  }

  /**
   * Create a request for the method "getMosaicMessage".
   *
   * This request holds the parameters needed by the the mosaicmessageendpoint server.  After setting
   * any optional parameters, call the {@link GetMosaicMessage#execute()} method to invoke the remote
   * operation.
   *
   * @param id
   * @return the request
   */
  public GetMosaicMessage getMosaicMessage(java.lang.String id) throws java.io.IOException {
    GetMosaicMessage result = new GetMosaicMessage(id);
    initialize(result);
    return result;
  }

  public class GetMosaicMessage extends MosaicmessageendpointRequest<com.piusvelte.mosaic.android.mosaicmessageendpoint.model.MosaicMessage> {

    private static final String REST_PATH = "mosaicmessage/{id}";

    /**
     * Create a request for the method "getMosaicMessage".
     *
     * This request holds the parameters needed by the the mosaicmessageendpoint server.  After
     * setting any optional parameters, call the {@link GetMosaicMessage#execute()} method to invoke
     * the remote operation. <p> {@link GetMosaicMessage#initialize(AbstractGoogleClientRequest)} must
     * be called to initialize this instance immediately after invoking the constructor. </p>
     *
     * @param id
     * @since 1.13
     */
    protected GetMosaicMessage(java.lang.String id) {
      super(Mosaicmessageendpoint.this, "GET", REST_PATH, null, com.piusvelte.mosaic.android.mosaicmessageendpoint.model.MosaicMessage.class);
      this.id = Preconditions.checkNotNull(id, "Required parameter id must be specified.");
    }

    @Override
    public com.google.api.client.http.HttpResponse executeUsingHead() throws java.io.IOException {
      return super.executeUsingHead();
    }

    @Override
    public com.google.api.client.http.HttpRequest buildHttpRequestUsingHead() throws java.io.IOException {
      return super.buildHttpRequestUsingHead();
    }

    @Override
    public GetMosaicMessage setAlt(java.lang.String alt) {
      return (GetMosaicMessage) super.setAlt(alt);
    }

    @Override
    public GetMosaicMessage setFields(java.lang.String fields) {
      return (GetMosaicMessage) super.setFields(fields);
    }

    @Override
    public GetMosaicMessage setKey(java.lang.String key) {
      return (GetMosaicMessage) super.setKey(key);
    }

    @Override
    public GetMosaicMessage setOauthToken(java.lang.String oauthToken) {
      return (GetMosaicMessage) super.setOauthToken(oauthToken);
    }

    @Override
    public GetMosaicMessage setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (GetMosaicMessage) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public GetMosaicMessage setQuotaUser(java.lang.String quotaUser) {
      return (GetMosaicMessage) super.setQuotaUser(quotaUser);
    }

    @Override
    public GetMosaicMessage setUserIp(java.lang.String userIp) {
      return (GetMosaicMessage) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private java.lang.String id;

    /**

     */
    public java.lang.String getId() {
      return id;
    }

    public GetMosaicMessage setId(java.lang.String id) {
      this.id = id;
      return this;
    }

  }

  /**
   * Create a request for the method "insertMosaicMessage".
   *
   * This request holds the parameters needed by the the mosaicmessageendpoint server.  After setting
   * any optional parameters, call the {@link InsertMosaicMessage#execute()} method to invoke the
   * remote operation.
   *
   * @param content the {@link com.piusvelte.mosaic.android.mosaicmessageendpoint.model.MosaicMessage}
   * @return the request
   */
  public InsertMosaicMessage insertMosaicMessage(com.piusvelte.mosaic.android.mosaicmessageendpoint.model.MosaicMessage content) throws java.io.IOException {
    InsertMosaicMessage result = new InsertMosaicMessage(content);
    initialize(result);
    return result;
  }

  public class InsertMosaicMessage extends MosaicmessageendpointRequest<com.piusvelte.mosaic.android.mosaicmessageendpoint.model.MosaicMessage> {

    private static final String REST_PATH = "mosaicmessage";

    /**
     * Create a request for the method "insertMosaicMessage".
     *
     * This request holds the parameters needed by the the mosaicmessageendpoint server.  After
     * setting any optional parameters, call the {@link InsertMosaicMessage#execute()} method to
     * invoke the remote operation. <p> {@link
     * InsertMosaicMessage#initialize(AbstractGoogleClientRequest)} must be called to initialize this
     * instance immediately after invoking the constructor. </p>
     *
     * @param content the {@link com.piusvelte.mosaic.android.mosaicmessageendpoint.model.MosaicMessage}
     * @since 1.13
     */
    protected InsertMosaicMessage(com.piusvelte.mosaic.android.mosaicmessageendpoint.model.MosaicMessage content) {
      super(Mosaicmessageendpoint.this, "POST", REST_PATH, content, com.piusvelte.mosaic.android.mosaicmessageendpoint.model.MosaicMessage.class);
    }

    @Override
    public InsertMosaicMessage setAlt(java.lang.String alt) {
      return (InsertMosaicMessage) super.setAlt(alt);
    }

    @Override
    public InsertMosaicMessage setFields(java.lang.String fields) {
      return (InsertMosaicMessage) super.setFields(fields);
    }

    @Override
    public InsertMosaicMessage setKey(java.lang.String key) {
      return (InsertMosaicMessage) super.setKey(key);
    }

    @Override
    public InsertMosaicMessage setOauthToken(java.lang.String oauthToken) {
      return (InsertMosaicMessage) super.setOauthToken(oauthToken);
    }

    @Override
    public InsertMosaicMessage setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (InsertMosaicMessage) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public InsertMosaicMessage setQuotaUser(java.lang.String quotaUser) {
      return (InsertMosaicMessage) super.setQuotaUser(quotaUser);
    }

    @Override
    public InsertMosaicMessage setUserIp(java.lang.String userIp) {
      return (InsertMosaicMessage) super.setUserIp(userIp);
    }

  }

  /**
   * Create a request for the method "listMosaicMessage".
   *
   * This request holds the parameters needed by the the mosaicmessageendpoint server.  After setting
   * any optional parameters, call the {@link ListMosaicMessage#execute()} method to invoke the remote
   * operation.
   *
   * @return the request
   */
  public ListMosaicMessage listMosaicMessage() throws java.io.IOException {
    ListMosaicMessage result = new ListMosaicMessage();
    initialize(result);
    return result;
  }

  public class ListMosaicMessage extends MosaicmessageendpointRequest<com.piusvelte.mosaic.android.mosaicmessageendpoint.model.MosaicMessageCollection> {

    private static final String REST_PATH = "mosaicmessage";

    /**
     * Create a request for the method "listMosaicMessage".
     *
     * This request holds the parameters needed by the the mosaicmessageendpoint server.  After
     * setting any optional parameters, call the {@link ListMosaicMessage#execute()} method to invoke
     * the remote operation. <p> {@link ListMosaicMessage#initialize(AbstractGoogleClientRequest)}
     * must be called to initialize this instance immediately after invoking the constructor. </p>
     *
     * @since 1.13
     */
    protected ListMosaicMessage() {
      super(Mosaicmessageendpoint.this, "GET", REST_PATH, null, com.piusvelte.mosaic.android.mosaicmessageendpoint.model.MosaicMessageCollection.class);
    }

    @Override
    public com.google.api.client.http.HttpResponse executeUsingHead() throws java.io.IOException {
      return super.executeUsingHead();
    }

    @Override
    public com.google.api.client.http.HttpRequest buildHttpRequestUsingHead() throws java.io.IOException {
      return super.buildHttpRequestUsingHead();
    }

    @Override
    public ListMosaicMessage setAlt(java.lang.String alt) {
      return (ListMosaicMessage) super.setAlt(alt);
    }

    @Override
    public ListMosaicMessage setFields(java.lang.String fields) {
      return (ListMosaicMessage) super.setFields(fields);
    }

    @Override
    public ListMosaicMessage setKey(java.lang.String key) {
      return (ListMosaicMessage) super.setKey(key);
    }

    @Override
    public ListMosaicMessage setOauthToken(java.lang.String oauthToken) {
      return (ListMosaicMessage) super.setOauthToken(oauthToken);
    }

    @Override
    public ListMosaicMessage setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (ListMosaicMessage) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public ListMosaicMessage setQuotaUser(java.lang.String quotaUser) {
      return (ListMosaicMessage) super.setQuotaUser(quotaUser);
    }

    @Override
    public ListMosaicMessage setUserIp(java.lang.String userIp) {
      return (ListMosaicMessage) super.setUserIp(userIp);
    }

  }

  /**
   * Create a request for the method "removeMosaicMessage".
   *
   * This request holds the parameters needed by the the mosaicmessageendpoint server.  After setting
   * any optional parameters, call the {@link RemoveMosaicMessage#execute()} method to invoke the
   * remote operation.
   *
   * @param id
   * @return the request
   */
  public RemoveMosaicMessage removeMosaicMessage(java.lang.String id) throws java.io.IOException {
    RemoveMosaicMessage result = new RemoveMosaicMessage(id);
    initialize(result);
    return result;
  }

  public class RemoveMosaicMessage extends MosaicmessageendpointRequest<com.piusvelte.mosaic.android.mosaicmessageendpoint.model.MosaicMessage> {

    private static final String REST_PATH = "mosaicmessage/{id}";

    /**
     * Create a request for the method "removeMosaicMessage".
     *
     * This request holds the parameters needed by the the mosaicmessageendpoint server.  After
     * setting any optional parameters, call the {@link RemoveMosaicMessage#execute()} method to
     * invoke the remote operation. <p> {@link
     * RemoveMosaicMessage#initialize(AbstractGoogleClientRequest)} must be called to initialize this
     * instance immediately after invoking the constructor. </p>
     *
     * @param id
     * @since 1.13
     */
    protected RemoveMosaicMessage(java.lang.String id) {
      super(Mosaicmessageendpoint.this, "DELETE", REST_PATH, null, com.piusvelte.mosaic.android.mosaicmessageendpoint.model.MosaicMessage.class);
      this.id = Preconditions.checkNotNull(id, "Required parameter id must be specified.");
    }

    @Override
    public RemoveMosaicMessage setAlt(java.lang.String alt) {
      return (RemoveMosaicMessage) super.setAlt(alt);
    }

    @Override
    public RemoveMosaicMessage setFields(java.lang.String fields) {
      return (RemoveMosaicMessage) super.setFields(fields);
    }

    @Override
    public RemoveMosaicMessage setKey(java.lang.String key) {
      return (RemoveMosaicMessage) super.setKey(key);
    }

    @Override
    public RemoveMosaicMessage setOauthToken(java.lang.String oauthToken) {
      return (RemoveMosaicMessage) super.setOauthToken(oauthToken);
    }

    @Override
    public RemoveMosaicMessage setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (RemoveMosaicMessage) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public RemoveMosaicMessage setQuotaUser(java.lang.String quotaUser) {
      return (RemoveMosaicMessage) super.setQuotaUser(quotaUser);
    }

    @Override
    public RemoveMosaicMessage setUserIp(java.lang.String userIp) {
      return (RemoveMosaicMessage) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private java.lang.String id;

    /**

     */
    public java.lang.String getId() {
      return id;
    }

    public RemoveMosaicMessage setId(java.lang.String id) {
      this.id = id;
      return this;
    }

  }

  /**
   * Create a request for the method "updateMosaicMessage".
   *
   * This request holds the parameters needed by the the mosaicmessageendpoint server.  After setting
   * any optional parameters, call the {@link UpdateMosaicMessage#execute()} method to invoke the
   * remote operation.
   *
   * @param content the {@link com.piusvelte.mosaic.android.mosaicmessageendpoint.model.MosaicMessage}
   * @return the request
   */
  public UpdateMosaicMessage updateMosaicMessage(com.piusvelte.mosaic.android.mosaicmessageendpoint.model.MosaicMessage content) throws java.io.IOException {
    UpdateMosaicMessage result = new UpdateMosaicMessage(content);
    initialize(result);
    return result;
  }

  public class UpdateMosaicMessage extends MosaicmessageendpointRequest<com.piusvelte.mosaic.android.mosaicmessageendpoint.model.MosaicMessage> {

    private static final String REST_PATH = "mosaicmessage";

    /**
     * Create a request for the method "updateMosaicMessage".
     *
     * This request holds the parameters needed by the the mosaicmessageendpoint server.  After
     * setting any optional parameters, call the {@link UpdateMosaicMessage#execute()} method to
     * invoke the remote operation. <p> {@link
     * UpdateMosaicMessage#initialize(AbstractGoogleClientRequest)} must be called to initialize this
     * instance immediately after invoking the constructor. </p>
     *
     * @param content the {@link com.piusvelte.mosaic.android.mosaicmessageendpoint.model.MosaicMessage}
     * @since 1.13
     */
    protected UpdateMosaicMessage(com.piusvelte.mosaic.android.mosaicmessageendpoint.model.MosaicMessage content) {
      super(Mosaicmessageendpoint.this, "PUT", REST_PATH, content, com.piusvelte.mosaic.android.mosaicmessageendpoint.model.MosaicMessage.class);
    }

    @Override
    public UpdateMosaicMessage setAlt(java.lang.String alt) {
      return (UpdateMosaicMessage) super.setAlt(alt);
    }

    @Override
    public UpdateMosaicMessage setFields(java.lang.String fields) {
      return (UpdateMosaicMessage) super.setFields(fields);
    }

    @Override
    public UpdateMosaicMessage setKey(java.lang.String key) {
      return (UpdateMosaicMessage) super.setKey(key);
    }

    @Override
    public UpdateMosaicMessage setOauthToken(java.lang.String oauthToken) {
      return (UpdateMosaicMessage) super.setOauthToken(oauthToken);
    }

    @Override
    public UpdateMosaicMessage setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (UpdateMosaicMessage) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public UpdateMosaicMessage setQuotaUser(java.lang.String quotaUser) {
      return (UpdateMosaicMessage) super.setQuotaUser(quotaUser);
    }

    @Override
    public UpdateMosaicMessage setUserIp(java.lang.String userIp) {
      return (UpdateMosaicMessage) super.setUserIp(userIp);
    }

  }

  /**
   * Builder for {@link Mosaicmessageendpoint}.
   *
   * <p>
   * Implementation is not thread-safe.
   * </p>
   *
   * @since 1.3.0
   */
  public static final class Builder extends AbstractGoogleJsonClient.Builder {

    /**
     * Returns an instance of a new builder.
     *
     * @param transport HTTP transport
     * @param jsonFactory JSON factory
     * @param httpRequestInitializer HTTP request initializer or {@code null} for none
     * @since 1.7
     */
    public Builder(HttpTransport transport, JsonFactory jsonFactory,
        HttpRequestInitializer httpRequestInitializer) {
      super(
          transport,
          jsonFactory,
          DEFAULT_ROOT_URL,
          DEFAULT_SERVICE_PATH,
          httpRequestInitializer,
          false);
    }

    /** Builds a new instance of {@link Mosaicmessageendpoint}. */
    @Override
    public Mosaicmessageendpoint build() {
      return new Mosaicmessageendpoint(getTransport(),
          getHttpRequestInitializer(),
          getRootUrl(),
          getServicePath(),
          getObjectParser(),
          getGoogleClientRequestInitializer(),
          getApplicationName(),
          getSuppressPatternChecks());
    }

    @Override
    public Builder setRootUrl(String rootUrl) {
      return (Builder) super.setRootUrl(rootUrl);
    }

    @Override
    public Builder setServicePath(String servicePath) {
      return (Builder) super.setServicePath(servicePath);
    }

    @Override
    public Builder setHttpRequestInitializer(HttpRequestInitializer httpRequestInitializer) {
      return (Builder) super.setHttpRequestInitializer(httpRequestInitializer);
    }

    @Override
    public Builder setApplicationName(String applicationName) {
      return (Builder) super.setApplicationName(applicationName);
    }

    @Override
    public Builder setSuppressPatternChecks(boolean suppressPatternChecks) {
      return (Builder) super.setSuppressPatternChecks(suppressPatternChecks);
    }

    /**
     * Set the {@link MosaicmessageendpointRequestInitializer}.
     *
     * @since 1.12
     */
    public Builder setMosaicmessageendpointRequestInitializer(
        MosaicmessageendpointRequestInitializer mosaicmessageendpointRequestInitializer) {
      return (Builder) super.setGoogleClientRequestInitializer(mosaicmessageendpointRequestInitializer);
    }

    @Override
    public Builder setGoogleClientRequestInitializer(
        GoogleClientRequestInitializer googleClientRequestInitializer) {
      return (Builder) super.setGoogleClientRequestInitializer(googleClientRequestInitializer);
    }
  }
}
