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
 *  on 2013-02-24 at 03:16:30 UTC 
 */

package com.piusvelte.mosaic.android.mosaicmessages;

import com.google.api.client.googleapis.GoogleUtils;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.JsonString;
import com.google.common.base.Preconditions;

/**
 * Service definition for Mosaicmessages (v1).
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
 * This service uses {@link MosaicmessagesRequestInitializer} to initialize global parameters via its
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
public class Mosaicmessages extends AbstractGoogleJsonClient {

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
  public static final String DEFAULT_SERVICE_PATH = "mosaicmessages/v1/message/";

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
  public Mosaicmessages(HttpTransport transport, JsonFactory jsonFactory,
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
  Mosaicmessages(HttpTransport transport,
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
   * An accessor for creating requests from the Message collection.
   *
   * <p>The typical use is:</p>
   * <pre>
   *   {@code Mosaicmessages mosaicmessages = new Mosaicmessages(...);}
   *   {@code Mosaicmessages.Message.List request = mosaicmessages.message().list(parameters ...)}
   * </pre>
   *
   * @return the resource collection
   */
  public Message message() {
    return new Message();
  }

  /**
   * The "message" collection of methods.
   */
  public class Message {

    /**
     * Create a request for the method "message.get".
     *
     * This request holds the parameters needed by the the mosaicmessages server.  After setting any
     * optional parameters, call the {@link Get#execute()} method to invoke the remote operation.
     *
     * @param id
     * @return the request
     */
    public Get get(Long id) throws java.io.IOException {
      Get result = new Get(id);
      initialize(result);
      return result;
    }

    public class Get extends MosaicmessagesRequest<com.piusvelte.mosaic.android.mosaicmessages.model.MosaicMessage> {

      private static final String REST_PATH = "get/{id}";

      /**
       * Create a request for the method "message.get".
       *
       * This request holds the parameters needed by the the mosaicmessages server.  After setting any
       * optional parameters, call the {@link Get#execute()} method to invoke the remote operation. <p>
       * {@link Get#initialize(AbstractGoogleClientRequest)} must be called to initialize this instance
       * immediately after invoking the constructor. </p>
       *
       * @param id
       * @since 1.13
       */
      protected Get(Long id) {
        super(Mosaicmessages.this, "GET", REST_PATH, null, com.piusvelte.mosaic.android.mosaicmessages.model.MosaicMessage.class);
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
      public Get setAlt(String alt) {
        return (Get) super.setAlt(alt);
      }

      @Override
      public Get setFields(String fields) {
        return (Get) super.setFields(fields);
      }

      @Override
      public Get setKey(String key) {
        return (Get) super.setKey(key);
      }

      @Override
      public Get setOauthToken(String oauthToken) {
        return (Get) super.setOauthToken(oauthToken);
      }

      @Override
      public Get setPrettyPrint(Boolean prettyPrint) {
        return (Get) super.setPrettyPrint(prettyPrint);
      }

      @Override
      public Get setQuotaUser(String quotaUser) {
        return (Get) super.setQuotaUser(quotaUser);
      }

      @Override
      public Get setUserIp(String userIp) {
        return (Get) super.setUserIp(userIp);
      }

      @com.google.api.client.util.Key
      private Long id;

      /**

       */
      public Long getId() {
        return id;
      }

      public Get setId(Long id) {
        this.id = id;
        return this;
      }

    }
    /**
     * Create a request for the method "message.insert".
     *
     * This request holds the parameters needed by the the mosaicmessages server.  After setting any
     * optional parameters, call the {@link Insert#execute()} method to invoke the remote operation.
     *
     * @param content the {@link com.piusvelte.mosaic.android.mosaicmessages.model.MosaicMessage}
     * @return the request
     */
    public Insert insert(com.piusvelte.mosaic.android.mosaicmessages.model.MosaicMessage content) throws java.io.IOException {
      Insert result = new Insert(content);
      initialize(result);
      return result;
    }

    public class Insert extends MosaicmessagesRequest<com.piusvelte.mosaic.android.mosaicmessages.model.MosaicMessage> {

      private static final String REST_PATH = "insert";

      /**
       * Create a request for the method "message.insert".
       *
       * This request holds the parameters needed by the the mosaicmessages server.  After setting any
       * optional parameters, call the {@link Insert#execute()} method to invoke the remote operation.
       * <p> {@link Insert#initialize(AbstractGoogleClientRequest)} must be called to initialize this
       * instance immediately after invoking the constructor. </p>
       *
       * @param content the {@link com.piusvelte.mosaic.android.mosaicmessages.model.MosaicMessage}
       * @since 1.13
       */
      protected Insert(com.piusvelte.mosaic.android.mosaicmessages.model.MosaicMessage content) {
        super(Mosaicmessages.this, "POST", REST_PATH, content, com.piusvelte.mosaic.android.mosaicmessages.model.MosaicMessage.class);
      }

      @Override
      public Insert setAlt(String alt) {
        return (Insert) super.setAlt(alt);
      }

      @Override
      public Insert setFields(String fields) {
        return (Insert) super.setFields(fields);
      }

      @Override
      public Insert setKey(String key) {
        return (Insert) super.setKey(key);
      }

      @Override
      public Insert setOauthToken(String oauthToken) {
        return (Insert) super.setOauthToken(oauthToken);
      }

      @Override
      public Insert setPrettyPrint(Boolean prettyPrint) {
        return (Insert) super.setPrettyPrint(prettyPrint);
      }

      @Override
      public Insert setQuotaUser(String quotaUser) {
        return (Insert) super.setQuotaUser(quotaUser);
      }

      @Override
      public Insert setUserIp(String userIp) {
        return (Insert) super.setUserIp(userIp);
      }

    }
    /**
     * Create a request for the method "message.list".
     *
     * This request holds the parameters needed by the the mosaicmessages server.  After setting any
     * optional parameters, call the {@link List#execute()} method to invoke the remote operation.
     *
     * @param latE6
     * @param lonE6
     * @return the request
     */
    public List list(Integer latE6, Integer lonE6) throws java.io.IOException {
      List result = new List(latE6, lonE6);
      initialize(result);
      return result;
    }

    public class List extends MosaicmessagesRequest<com.piusvelte.mosaic.android.mosaicmessages.model.MosaicMessageCollection> {

      private static final String REST_PATH = "list";

      /**
       * Create a request for the method "message.list".
       *
       * This request holds the parameters needed by the the mosaicmessages server.  After setting any
       * optional parameters, call the {@link List#execute()} method to invoke the remote operation. <p>
       * {@link List#initialize(AbstractGoogleClientRequest)} must be called to initialize this instance
       * immediately after invoking the constructor. </p>
       *
       * @param latE6
       * @param lonE6
       * @since 1.13
       */
      protected List(Integer latE6, Integer lonE6) {
        super(Mosaicmessages.this, "GET", REST_PATH, null, com.piusvelte.mosaic.android.mosaicmessages.model.MosaicMessageCollection.class);
        this.latE6 = Preconditions.checkNotNull(latE6, "Required parameter latE6 must be specified.");
        this.lonE6 = Preconditions.checkNotNull(lonE6, "Required parameter lonE6 must be specified.");
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
      public List setAlt(String alt) {
        return (List) super.setAlt(alt);
      }

      @Override
      public List setFields(String fields) {
        return (List) super.setFields(fields);
      }

      @Override
      public List setKey(String key) {
        return (List) super.setKey(key);
      }

      @Override
      public List setOauthToken(String oauthToken) {
        return (List) super.setOauthToken(oauthToken);
      }

      @Override
      public List setPrettyPrint(Boolean prettyPrint) {
        return (List) super.setPrettyPrint(prettyPrint);
      }

      @Override
      public List setQuotaUser(String quotaUser) {
        return (List) super.setQuotaUser(quotaUser);
      }

      @Override
      public List setUserIp(String userIp) {
        return (List) super.setUserIp(userIp);
      }

      @com.google.api.client.util.Key
      private Integer latE6;

      /**

       */
      public Integer getLatE6() {
        return latE6;
      }

      public List setLatE6(Integer latE6) {
        this.latE6 = latE6;
        return this;
      }

      @com.google.api.client.util.Key
      private Integer lonE6;

      /**

       */
      public Integer getLonE6() {
        return lonE6;
      }

      public List setLonE6(Integer lonE6) {
        this.lonE6 = lonE6;
        return this;
      }

    }
    /**
     * Create a request for the method "message.patch".
     *
     * This request holds the parameters needed by the the mosaicmessages server.  After setting any
     * optional parameters, call the {@link Patch#execute()} method to invoke the remote operation.
     *
     * @param id
     * @param content the {@link com.piusvelte.mosaic.android.mosaicmessages.model.MosaicMessage}
     * @return the request
     */
    public Patch patch(Long id, com.piusvelte.mosaic.android.mosaicmessages.model.MosaicMessage content) throws java.io.IOException {
      Patch result = new Patch(id, content);
      initialize(result);
      return result;
    }

    public class Patch extends MosaicmessagesRequest<com.piusvelte.mosaic.android.mosaicmessages.model.MosaicMessage> {

      private static final String REST_PATH = "update";

      /**
       * Create a request for the method "message.patch".
       *
       * This request holds the parameters needed by the the mosaicmessages server.  After setting any
       * optional parameters, call the {@link Patch#execute()} method to invoke the remote operation.
       * <p> {@link Patch#initialize(AbstractGoogleClientRequest)} must be called to initialize this
       * instance immediately after invoking the constructor. </p>
       *
       * @param id
       * @param content the {@link com.piusvelte.mosaic.android.mosaicmessages.model.MosaicMessage}
       * @since 1.13
       */
      protected Patch(Long id, com.piusvelte.mosaic.android.mosaicmessages.model.MosaicMessage content) {
        super(Mosaicmessages.this, "PATCH", REST_PATH, content, com.piusvelte.mosaic.android.mosaicmessages.model.MosaicMessage.class);
        this.id = Preconditions.checkNotNull(id, "Required parameter id must be specified.");
      }

      @Override
      public Patch setAlt(String alt) {
        return (Patch) super.setAlt(alt);
      }

      @Override
      public Patch setFields(String fields) {
        return (Patch) super.setFields(fields);
      }

      @Override
      public Patch setKey(String key) {
        return (Patch) super.setKey(key);
      }

      @Override
      public Patch setOauthToken(String oauthToken) {
        return (Patch) super.setOauthToken(oauthToken);
      }

      @Override
      public Patch setPrettyPrint(Boolean prettyPrint) {
        return (Patch) super.setPrettyPrint(prettyPrint);
      }

      @Override
      public Patch setQuotaUser(String quotaUser) {
        return (Patch) super.setQuotaUser(quotaUser);
      }

      @Override
      public Patch setUserIp(String userIp) {
        return (Patch) super.setUserIp(userIp);
      }

      @com.google.api.client.util.Key
      private Long id;

      /**

       */
      public Long getId() {
        return id;
      }

      public Patch setId(Long id) {
        this.id = id;
        return this;
      }

    }
    /**
     * Create a request for the method "message.remove".
     *
     * This request holds the parameters needed by the the mosaicmessages server.  After setting any
     * optional parameters, call the {@link Remove#execute()} method to invoke the remote operation.
     *
     * @param id
     * @return the request
     */
    public Remove remove(Long id) throws java.io.IOException {
      Remove result = new Remove(id);
      initialize(result);
      return result;
    }

    public class Remove extends MosaicmessagesRequest<com.piusvelte.mosaic.android.mosaicmessages.model.MosaicMessage> {

      private static final String REST_PATH = "remove/{id}";

      /**
       * Create a request for the method "message.remove".
       *
       * This request holds the parameters needed by the the mosaicmessages server.  After setting any
       * optional parameters, call the {@link Remove#execute()} method to invoke the remote operation.
       * <p> {@link Remove#initialize(AbstractGoogleClientRequest)} must be called to initialize this
       * instance immediately after invoking the constructor. </p>
       *
       * @param id
       * @since 1.13
       */
      protected Remove(Long id) {
        super(Mosaicmessages.this, "GET", REST_PATH, null, com.piusvelte.mosaic.android.mosaicmessages.model.MosaicMessage.class);
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
      public Remove setAlt(String alt) {
        return (Remove) super.setAlt(alt);
      }

      @Override
      public Remove setFields(String fields) {
        return (Remove) super.setFields(fields);
      }

      @Override
      public Remove setKey(String key) {
        return (Remove) super.setKey(key);
      }

      @Override
      public Remove setOauthToken(String oauthToken) {
        return (Remove) super.setOauthToken(oauthToken);
      }

      @Override
      public Remove setPrettyPrint(Boolean prettyPrint) {
        return (Remove) super.setPrettyPrint(prettyPrint);
      }

      @Override
      public Remove setQuotaUser(String quotaUser) {
        return (Remove) super.setQuotaUser(quotaUser);
      }

      @Override
      public Remove setUserIp(String userIp) {
        return (Remove) super.setUserIp(userIp);
      }

      @com.google.api.client.util.Key
      private Long id;

      /**

       */
      public Long getId() {
        return id;
      }

      public Remove setId(Long id) {
        this.id = id;
        return this;
      }

    }
    /**
     * Create a request for the method "message.report".
     *
     * This request holds the parameters needed by the the mosaicmessages server.  After setting any
     * optional parameters, call the {@link Report#execute()} method to invoke the remote operation.
     *
     * @param id
     * @return the request
     */
    public Report report(Long id) throws java.io.IOException {
      Report result = new Report(id);
      initialize(result);
      return result;
    }

    public class Report extends MosaicmessagesRequest<Void> {

      private static final String REST_PATH = "report/{id}";

      /**
       * Create a request for the method "message.report".
       *
       * This request holds the parameters needed by the the mosaicmessages server.  After setting any
       * optional parameters, call the {@link Report#execute()} method to invoke the remote operation.
       * <p> {@link Report#initialize(AbstractGoogleClientRequest)} must be called to initialize this
       * instance immediately after invoking the constructor. </p>
       *
       * @param id
       * @since 1.13
       */
      protected Report(Long id) {
        super(Mosaicmessages.this, "GET", REST_PATH, null, Void.class);
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
      public Report setAlt(String alt) {
        return (Report) super.setAlt(alt);
      }

      @Override
      public Report setFields(String fields) {
        return (Report) super.setFields(fields);
      }

      @Override
      public Report setKey(String key) {
        return (Report) super.setKey(key);
      }

      @Override
      public Report setOauthToken(String oauthToken) {
        return (Report) super.setOauthToken(oauthToken);
      }

      @Override
      public Report setPrettyPrint(Boolean prettyPrint) {
        return (Report) super.setPrettyPrint(prettyPrint);
      }

      @Override
      public Report setQuotaUser(String quotaUser) {
        return (Report) super.setQuotaUser(quotaUser);
      }

      @Override
      public Report setUserIp(String userIp) {
        return (Report) super.setUserIp(userIp);
      }

      @com.google.api.client.util.Key
      private Long id;

      /**

       */
      public Long getId() {
        return id;
      }

      public Report setId(Long id) {
        this.id = id;
        return this;
      }

    }
    /**
     * Create a request for the method "message.update".
     *
     * This request holds the parameters needed by the the mosaicmessages server.  After setting any
     * optional parameters, call the {@link Update#execute()} method to invoke the remote operation.
     *
     * @param content the {@link com.piusvelte.mosaic.android.mosaicmessages.model.MosaicMessage}
     * @return the request
     */
    public Update update(com.piusvelte.mosaic.android.mosaicmessages.model.MosaicMessage content) throws java.io.IOException {
      Update result = new Update(content);
      initialize(result);
      return result;
    }

    public class Update extends MosaicmessagesRequest<com.piusvelte.mosaic.android.mosaicmessages.model.MosaicMessage> {

      private static final String REST_PATH = "update";

      /**
       * Create a request for the method "message.update".
       *
       * This request holds the parameters needed by the the mosaicmessages server.  After setting any
       * optional parameters, call the {@link Update#execute()} method to invoke the remote operation.
       * <p> {@link Update#initialize(AbstractGoogleClientRequest)} must be called to initialize this
       * instance immediately after invoking the constructor. </p>
       *
       * @param content the {@link com.piusvelte.mosaic.android.mosaicmessages.model.MosaicMessage}
       * @since 1.13
       */
      protected Update(com.piusvelte.mosaic.android.mosaicmessages.model.MosaicMessage content) {
        super(Mosaicmessages.this, "POST", REST_PATH, content, com.piusvelte.mosaic.android.mosaicmessages.model.MosaicMessage.class);
      }

      @Override
      public Update setAlt(String alt) {
        return (Update) super.setAlt(alt);
      }

      @Override
      public Update setFields(String fields) {
        return (Update) super.setFields(fields);
      }

      @Override
      public Update setKey(String key) {
        return (Update) super.setKey(key);
      }

      @Override
      public Update setOauthToken(String oauthToken) {
        return (Update) super.setOauthToken(oauthToken);
      }

      @Override
      public Update setPrettyPrint(Boolean prettyPrint) {
        return (Update) super.setPrettyPrint(prettyPrint);
      }

      @Override
      public Update setQuotaUser(String quotaUser) {
        return (Update) super.setQuotaUser(quotaUser);
      }

      @Override
      public Update setUserIp(String userIp) {
        return (Update) super.setUserIp(userIp);
      }

    }
    /**
     * Create a request for the method "message.view".
     *
     * This request holds the parameters needed by the the mosaicmessages server.  After setting any
     * optional parameters, call the {@link View#execute()} method to invoke the remote operation.
     *
     * @param id
     * @return the request
     */
    public View view(Long id) throws java.io.IOException {
      View result = new View(id);
      initialize(result);
      return result;
    }

    public class View extends MosaicmessagesRequest<Void> {

      private static final String REST_PATH = "view/{id}";

      /**
       * Create a request for the method "message.view".
       *
       * This request holds the parameters needed by the the mosaicmessages server.  After setting any
       * optional parameters, call the {@link View#execute()} method to invoke the remote operation. <p>
       * {@link View#initialize(AbstractGoogleClientRequest)} must be called to initialize this instance
       * immediately after invoking the constructor. </p>
       *
       * @param id
       * @since 1.13
       */
      protected View(Long id) {
        super(Mosaicmessages.this, "GET", REST_PATH, null, Void.class);
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
      public View setAlt(String alt) {
        return (View) super.setAlt(alt);
      }

      @Override
      public View setFields(String fields) {
        return (View) super.setFields(fields);
      }

      @Override
      public View setKey(String key) {
        return (View) super.setKey(key);
      }

      @Override
      public View setOauthToken(String oauthToken) {
        return (View) super.setOauthToken(oauthToken);
      }

      @Override
      public View setPrettyPrint(Boolean prettyPrint) {
        return (View) super.setPrettyPrint(prettyPrint);
      }

      @Override
      public View setQuotaUser(String quotaUser) {
        return (View) super.setQuotaUser(quotaUser);
      }

      @Override
      public View setUserIp(String userIp) {
        return (View) super.setUserIp(userIp);
      }

      @com.google.api.client.util.Key
      private Long id;

      /**

       */
      public Long getId() {
        return id;
      }

      public View setId(Long id) {
        this.id = id;
        return this;
      }

    }

  }

  /**
   * Builder for {@link Mosaicmessages}.
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

    /** Builds a new instance of {@link Mosaicmessages}. */
    @Override
    public Mosaicmessages build() {
      return new Mosaicmessages(getTransport(),
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
     * Set the {@link MosaicmessagesRequestInitializer}.
     *
     * @since 1.12
     */
    public Builder setMosaicmessagesRequestInitializer(
        MosaicmessagesRequestInitializer mosaicmessagesRequestInitializer) {
      return (Builder) super.setGoogleClientRequestInitializer(mosaicmessagesRequestInitializer);
    }

    @Override
    public Builder setGoogleClientRequestInitializer(
        GoogleClientRequestInitializer googleClientRequestInitializer) {
      return (Builder) super.setGoogleClientRequestInitializer(googleClientRequestInitializer);
    }
  }
}
