/*
 * Copyright (c) 2011 Kevin Sawicki <kevinsawicki@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */
package mobi.zty.sdk.util;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.net.HttpURLConnection.HTTP_NOT_MODIFIED;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.Proxy.Type.HTTP;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.zip.GZIPInputStream;

/**
 * A fluid interface for making HTTP requests using an underlying
 * {@link HttpURLConnection} (or sub-class).
 * <p>
 * Each instance supports making a single request and cannot be reused for
 * further requests.
 */
public class HttpRequestt {

  /**
   * 'UTF-8' charset name
   */
  public static final String CHARSET_UTF8 = "UTF-8";

  /**
   * 'application/x-www-form-urlencoded' content type header value
   */
  public static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";

  /**
   * 'application/json' content type header value
   */
  public static final String CONTENT_TYPE_JSON = "application/json";

  /**
   * 'gzip' encoding header value
   */
  public static final String ENCODING_GZIP = "gzip";

  /**
   * 'Accept' header name
   */
  public static final String HEADER_ACCEPT = "Accept";

  /**
   * 'Accept-Charset' header name
   */
  public static final String HEADER_ACCEPT_CHARSET = "Accept-Charset";

  /**
   * 'Accept-Encoding' header name
   */
  public static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";

  /**
   * 'Authorization' header name
   */
  public static final String HEADER_AUTHORIZATION = "Authorization";

  /**
   * 'Cache-Control' header name
   */
  public static final String HEADER_CACHE_CONTROL = "Cache-Control";

  /**
   * 'Content-Encoding' header name
   */
  public static final String HEADER_CONTENT_ENCODING = "Content-Encoding";

  /**
   * 'Content-Length' header name
   */
  public static final String HEADER_CONTENT_LENGTH = "Content-Length";

  /**
   * 'Content-Type' header name
   */
  public static final String HEADER_CONTENT_TYPE = "Content-Type";

  /**
   * 'Date' header name
   */
  public static final String HEADER_DATE = "Date";

  /**
   * 'ETag' header name
   */
  public static final String HEADER_ETAG = "ETag";

  /**
   * 'Expires' header name
   */
  public static final String HEADER_EXPIRES = "Expires";

  /**
   * 'If-None-Match' header name
   */
  public static final String HEADER_IF_NONE_MATCH = "If-None-Match";

  /**
   * 'Last-Modified' header name
   */
  public static final String HEADER_LAST_MODIFIED = "Last-Modified";

  /**
   * 'Location' header name
   */
  public static final String HEADER_LOCATION = "Location";

  /**
   * 'Proxy-Authorization' header name
   */
  public static final String HEADER_PROXY_AUTHORIZATION = "Proxy-Authorization";

  /**
   * 'Referer' header name
   */
  public static final String HEADER_REFERER = "Referer";

  /**
   * 'Server' header name
   */
  public static final String HEADER_SERVER = "Server";

  /**
   * 'User-Agent' header name
   */
  public static final String HEADER_USER_AGENT = "User-Agent";

  /**
   * 'DELETE' request method
   */
  public static final String METHOD_DELETE = "DELETE";

  /**
   * 'GET' request method
   */
  public static final String METHOD_GET = "GET";

  /**
   * 'HEAD' request method
   */
  public static final String METHOD_HEAD = "HEAD";

  /**
   * 'OPTIONS' options method
   */
  public static final String METHOD_OPTIONS = "OPTIONS";

  /**
   * 'POST' request method
   */
  public static final String METHOD_POST = "POST";

  /**
   * 'PUT' request method
   */
  public static final String METHOD_PUT = "PUT";

  /**
   * 'TRACE' request method
   */
  public static final String METHOD_TRACE = "TRACE";

  /**
   * 'charset' header value parameter
   */
  public static final String PARAM_CHARSET = "charset";
  private static final String BOUNDARY = "00content0boundary00";
  private static final String CRLF = "\r\n";
  private static String getValidCharset(final String charset) {
    if (charset != null && charset.length() > 0)
      return charset;
    else
      return CHARSET_UTF8;
  }

  private static StringBuilder addPathSeparator(final String baseUrl,
      final StringBuilder result) {
    // Add trailing slash if the base URL doesn't have any path segments.
    //
    // The following test is checking for the last slash not being part of
    // the protocol to host separator: '://'.
    if (baseUrl.indexOf(':') + 2 == baseUrl.lastIndexOf('/'))
      result.append('/');
    return result;
  }

  private static StringBuilder addParamPrefix(final String baseUrl,
      final StringBuilder result) {
    // Add '?' if missing and add '&' if params already exist in base url
    final int queryStart = baseUrl.indexOf('?');
    final int lastChar = result.length() - 1;
    if (queryStart == -1)
      result.append('?');
    else if (queryStart < lastChar && baseUrl.charAt(lastChar) != '&')
      result.append('&');
    return result;
  }

  /**
   * Creates {@link HttpURLConnection HTTP connections} for
   * {@link URL urls}.
   */
  public interface ConnectionFactory {
    /**
     * Open an {@link HttpURLConnection} for the specified {@link URL}.
     *
     * @throws IOException
     */
    HttpURLConnection create(URL url) throws IOException;

    /**
     * Open an {@link HttpURLConnection} for the specified {@link URL}
     * and {@link Proxy}.
     *
     * @throws IOException
     */
    HttpURLConnection create(URL url, Proxy proxy) throws IOException;

    /**
     * A {@link ConnectionFactory} which uses the built-in
     * {@link URL#openConnection()}
     */
    ConnectionFactory DEFAULT = new ConnectionFactory() {
      public HttpURLConnection create(URL url) throws IOException {
        return (HttpURLConnection) url.openConnection();
      }

      public HttpURLConnection create(URL url, Proxy proxy) throws IOException {
        return (HttpURLConnection) url.openConnection(proxy);
      }
    };
  }

  private static ConnectionFactory CONNECTION_FACTORY = ConnectionFactory.DEFAULT;

  /**
   * Specify the {@link ConnectionFactory} used to create new requests.
   */
  public static void setConnectionFactory(final ConnectionFactory connectionFactory) {
    if (connectionFactory == null)
      CONNECTION_FACTORY = ConnectionFactory.DEFAULT;
    else
      CONNECTION_FACTORY = connectionFactory;
  }

  /**
   * Callback interface for reporting upload progress for a request.
   */
  public interface UploadProgress {
    /**
     * Callback invoked as data is uploaded by the request.
     *
     * @param uploaded The number of bytes already uploaded
     * @param total The total number of bytes that will be uploaded or -1 if
     *              the length is unknown.
     */
    void onUpload(long uploaded, long total);

    UploadProgress DEFAULT = new UploadProgress() {
      public void onUpload(long uploaded, long total) {
      }
    };
  }

  /**
   * HTTP request exception whose cause is always an {@link IOException}
   */
  public static class HttpRequestException extends RuntimeException {

    private static final long serialVersionUID = -1170466989781746231L;

    /**
     * Create a new HttpRequestException with the given cause
     *
     * @param cause
     */
    public HttpRequestException(final IOException cause) {
      super(cause);
    }

    /**
     * Get {@link IOException} that triggered this request exception
     *
     * @return {@link IOException} cause
     */
    @Override
    public IOException getCause() {
      return (IOException) super.getCause();
    }
  }

  /**
   * Operation that handles executing a callback once complete and handling
   * nested exceptions
   *
   * @param <V>
   */
  protected static abstract class Operation<V> implements Callable<V> {

    /**
     * Run operation
     *
     * @return result
     * @throws HttpRequestException
     * @throws IOException
     */
    protected abstract V run() throws HttpRequestException, IOException;

    /**
     * Operation complete callback
     *
     * @throws IOException
     */
    protected abstract void done() throws IOException;

    public V call() throws HttpRequestException {
      boolean thrown = false;
      try {
        return run();
      } catch (HttpRequestException e) {
        thrown = true;
        throw e;
      } catch (IOException e) {
        thrown = true;
        throw new HttpRequestException(e);
      } finally {
        try {
          done();
        } catch (IOException e) {
          if (!thrown)
            throw new HttpRequestException(e);
        }
      }
    }
  }

  /**
   * Class that ensures a {@link Closeable} gets closed with proper exception
   * handling.
   *
   * @param <V>
   */
  protected static abstract class CloseOperation<V> extends Operation<V> {

    private final Closeable closeable;

    private final boolean ignoreCloseExceptions;

    /**
     * Create closer for operation
     *
     * @param closeable
     * @param ignoreCloseExceptions
     */
    protected CloseOperation(final Closeable closeable,
        final boolean ignoreCloseExceptions) {
      this.closeable = closeable;
      this.ignoreCloseExceptions = ignoreCloseExceptions;
    }

    @Override
    protected void done() throws IOException {
      if (closeable instanceof Flushable)
        ((Flushable) closeable).flush();
      if (ignoreCloseExceptions)
        try {
          closeable.close();
        } catch (IOException e) {
          // Ignored
        }
      else
        closeable.close();
    }
  }

  /**
   * Class that and ensures a {@link Flushable} gets flushed with proper
   * exception handling.
   *
   * @param <V>
   */
  protected static abstract class FlushOperation<V> extends Operation<V> {

    private final Flushable flushable;

    /**
     * Create flush operation
     *
     * @param flushable
     */
    protected FlushOperation(final Flushable flushable) {
      this.flushable = flushable;
    }

    @Override
    protected void done() throws IOException {
      flushable.flush();
    }
  }

  /**
   * Request output stream
   */
  public static class RequestOutputStream extends BufferedOutputStream {

    private final CharsetEncoder encoder;

    /**
     * Create request output stream
     *
     * @param stream
     * @param charset
     * @param bufferSize
     */
    public RequestOutputStream(final OutputStream stream, final String charset,
        final int bufferSize) {
      super(stream, bufferSize);
      encoder = Charset.forName(getValidCharset(charset)).newEncoder();
    }

    /**
     * Write string to stream
     *
     * @param value
     * @return this stream
     * @throws IOException
     */
    public RequestOutputStream write(final String value) throws IOException {
      final ByteBuffer bytes = encoder.encode(CharBuffer.wrap(value));

      super.write(bytes.array(), 0, bytes.limit());

      return this;
    }
  }

  /**
   * Encode the given URL as an ASCII {@link String}
   * <p>
   * This method ensures the path and query segments of the URL are properly
   * encoded such as ' ' characters being encoded to '%20' or any UTF-8
   * characters that are non-ASCII. No encoding of URLs is done by default by
   * the {@link HttpRequestt} constructors and so if URL encoding is needed this
   * method should be called before calling the {@link HttpRequestt} constructor.
   *
   * @param url
   * @return encoded URL
   * @throws HttpRequestException
   */
  public static String encode(final CharSequence url)
      throws HttpRequestException {
    URL parsed;
    try {
      parsed = new URL(url.toString());
    } catch (IOException e) {
      throw new HttpRequestException(e);
    }

    String host = parsed.getHost();
    int port = parsed.getPort();
    if (port != -1)
      host = host + ':' + Integer.toString(port);

    try {
      String encoded = new URI(parsed.getProtocol(), host, parsed.getPath(),
          parsed.getQuery(), null).toASCIIString();
      int paramsStart = encoded.indexOf('?');
      if (paramsStart > 0 && paramsStart + 1 < encoded.length())
        encoded = encoded.substring(0, paramsStart + 1)
                  + encoded.substring(paramsStart + 1).replace("+", "%2B");
      return encoded;
    } catch (URISyntaxException e) {
      IOException io = new IOException("Parsing URI failed");
      io.initCause(e);
      throw new HttpRequestException(io);
    }
  }

  /**
   * Append given map as query parameters to the base URL
   * <p>
   * Each map entry's key will be a parameter name and the value's
   * {@link Object#toString()} will be the parameter value.
   *
   * @param url
   * @param params
   * @return URL with appended query params
   */
  public static String append(final CharSequence url, final Map<?, ?> params) {
    final String baseUrl = url.toString();
    if (params == null || params.isEmpty())
      return baseUrl;

    final StringBuilder result = new StringBuilder(baseUrl);

    addPathSeparator(baseUrl, result);
    addParamPrefix(baseUrl, result);

    Entry<?, ?> entry;
    Object value;
    Iterator<?> iterator = params.entrySet().iterator();
    entry = (Entry<?, ?>) iterator.next();
    result.append(entry.getKey().toString());
    result.append('=');
    value = entry.getValue();
    if (value != null)
      result.append(value);

    while (iterator.hasNext()) {
      result.append('&');
      entry = (Entry<?, ?>) iterator.next();
      result.append(entry.getKey().toString());
      result.append('=');
      value = entry.getValue();
      if (value != null)
        result.append(value);
    }

    return result.toString();
  }

  /**
   * Append given name/value pairs as query parameters to the base URL
   * <p>
   * The params argument is interpreted as a sequence of name/value pairs so the
   * given number of params must be divisible by 2.
   *
   * @param url
   * @param params
   *          name/value pairs
   * @return URL with appended query params
   */
  public static String append(final CharSequence url, final Object... params) {
    final String baseUrl = url.toString();
    if (params == null || params.length == 0)
      return baseUrl;

    if (params.length % 2 != 0)
      throw new IllegalArgumentException(
          "Must specify an even number of parameter names/values");

    final StringBuilder result = new StringBuilder(baseUrl);

    addPathSeparator(baseUrl, result);
    addParamPrefix(baseUrl, result);

    Object value;
    result.append(params[0]);
    result.append('=');
    value = params[1];
    if (value != null)
      result.append(value);

    for (int i = 2; i < params.length; i += 2) {
      result.append('&');
      result.append(params[i]);
      result.append('=');
      value = params[i + 1];
      if (value != null)
        result.append(value);
    }

    return result.toString();
  }

  /**
   * Start a 'GET' request to the given URL
   *
   * @param url
   * @return request
   * @throws HttpRequestException
   */
  public static HttpRequestt get(final CharSequence url)
      throws HttpRequestException {
    return new HttpRequestt(url, METHOD_GET);
  }

  /**
   * Start a 'GET' request to the given URL
   *
   * @param url
   * @return request
   * @throws HttpRequestException
   */
  public static HttpRequestt get(final URL url) throws HttpRequestException {
    return new HttpRequestt(url, METHOD_GET);
  }

  /**
   * Start a 'GET' request to the given URL along with the query params
   *
   * @param baseUrl
   * @param params
   *          The query parameters to include as part of the baseUrl
   * @param encode
   *          true to encode the full URL
   *
   * @see #append(CharSequence, Map)
   * @see #encode(CharSequence)
   *
   * @return request
   */
  public static HttpRequestt get(final CharSequence baseUrl,
      final Map<?, ?> params, final boolean encode) {
    String url = append(baseUrl, params);
    Util_G.debugE("httpURL",url);
    return get(encode ? encode(url) : url);
  }

  /**
   * Start a 'GET' request to the given URL along with the query params
   *
   * @param baseUrl
   * @param encode
   *          true to encode the full URL
   * @param params
   *          the name/value query parameter pairs to include as part of the
   *          baseUrl
   *
   * @see #append(CharSequence, String...)
   * @see #encode(CharSequence)
   *
   * @return request
   */
  public static HttpRequestt get(final CharSequence baseUrl,
      final boolean encode, final Object... params) {
    String url = append(baseUrl, params);
    return get(encode ? encode(url) : url);
  }

  /**
   * Start a 'POST' request to the given URL
   *
   * @param url
   * @return request
   * @throws HttpRequestException
   */
  public static HttpRequestt post(final CharSequence url)
      throws HttpRequestException {
    return new HttpRequestt(url, METHOD_POST);
  }

  /**
   * Start a 'POST' request to the given URL
   *
   * @param url
   * @return request
   * @throws HttpRequestException
   */
  public static HttpRequestt post(final URL url) throws HttpRequestException {
    return new HttpRequestt(url, METHOD_POST);
  }

  /**
   * Start a 'POST' request to the given URL along with the query params
   *
   * @param baseUrl
   * @param params
   *          the query parameters to include as part of the baseUrl
   * @param encode
   *          true to encode the full URL
   *
   * @see #append(CharSequence, Map)
   * @see #encode(CharSequence)
   *
   * @return request
   */
  public static HttpRequestt post(final CharSequence baseUrl,
      final Map<?, ?> params, final boolean encode) {
    String url = append(baseUrl, params);
    return post(encode ? encode(url) : url);
  }

  /**
   * Start a 'POST' request to the given URL along with the query params
   *
   * @param baseUrl
   * @param encode
   *          true to encode the full URL
   * @param params
   *          the name/value query parameter pairs to include as part of the
   *          baseUrl
   *
   * @see #append(CharSequence, String...)
   * @see #encode(CharSequence)
   *
   * @return request
   */
  public static HttpRequestt post(final CharSequence baseUrl,
      final boolean encode, final Object... params) {
    String url = append(baseUrl, params);
    return post(encode ? encode(url) : url);
  }
 
  private HttpURLConnection connection = null;

  private final URL url;

  private final String requestMethod;

  private RequestOutputStream output;

  private boolean multipart;

  private boolean ignoreCloseExceptions = true;

  private boolean uncompress = false;

  private int bufferSize = 8192;

  private long totalSize = -1;

  private long totalWritten = 0;

  private String httpProxyHost;

  private int httpProxyPort;

  private UploadProgress progress = UploadProgress.DEFAULT;

  /**
   * Create HTTP connection wrapper
   *
   * @param url Remote resource URL.
   * @param method HTTP request method (e.g., "GET", "POST").
   * @throws HttpRequestException
   */
  public HttpRequestt(final CharSequence url, final String method)
      throws HttpRequestException {
    try {
      this.url = new URL(url.toString());
    } catch (MalformedURLException e) {
      throw new HttpRequestException(e);
    }
    this.requestMethod = method;
  }

  /**
   * Create HTTP connection wrapper
   *
   * @param url Remote resource URL.
   * @param method HTTP request method (e.g., "GET", "POST").
   * @throws HttpRequestException
   */
  public HttpRequestt(final URL url, final String method)
      throws HttpRequestException {
    this.url = url;
    this.requestMethod = method;
  }

  private Proxy createProxy() {
    return new Proxy(HTTP, new InetSocketAddress(httpProxyHost, httpProxyPort));
  }

  private HttpURLConnection createConnection() {
    try {
      final HttpURLConnection connection;
      if (httpProxyHost != null)
        connection = CONNECTION_FACTORY.create(url, createProxy());
      else
        connection = CONNECTION_FACTORY.create(url);
      connection.setRequestMethod(requestMethod);
      return connection;
    } catch (IOException e) {
      throw new HttpRequestException(e);
    }
  }

  @Override
  public String toString() {
    return method() + ' ' + url();
  }
  /**
   * Get underlying connection
   *
   * @return connection
   */
  public HttpURLConnection getConnection() {
    if (connection == null)
      connection = createConnection();
    return connection;
  }

  /**
   * Set whether or not to ignore exceptions that occur from calling
   * {@link Closeable#close()}
   * <p>
   * The default value of this setting is <code>true</code>
   *
   * @param ignore
   * @return this request
   */
  public HttpRequestt ignoreCloseExceptions(final boolean ignore) {
    ignoreCloseExceptions = ignore;
    return this;
  }

  /**
   * Get whether or not exceptions thrown by {@link Closeable#close()} are
   * ignored
   *
   * @return true if ignoring, false if throwing
   */
  public boolean ignoreCloseExceptions() {
    return ignoreCloseExceptions;
  }

  /**
   * Get the status code of the response
   *
   * @return the response code
   * @throws HttpRequestException
   */
  public int code() throws HttpRequestException {
    try {
      closeOutput();
      return getConnection().getResponseCode();
    } catch (IOException e) {
      throw new HttpRequestException(e);
    }
  }

  /**
   * Set the value of the given {@link AtomicInteger} to the status code of the
   * response
   *
   * @param output
   * @return this request
   * @throws HttpRequestException
   */
  public HttpRequestt code(final AtomicInteger output)
      throws HttpRequestException {
    output.set(code());
    return this;
  }

  /**
   * Is the response code a 200 OK?
   *
   * @return true if 200, false otherwise
   * @throws HttpRequestException
   */
  public boolean ok() throws HttpRequestException {
    return HTTP_OK == code();
  }

  /**
   * Is the response code a 201 Created?
   *
   * @return true if 201, false otherwise
   * @throws HttpRequestException
   */
  public boolean created() throws HttpRequestException {
    return HTTP_CREATED == code();
  }

  /**
   * Is the response code a 500 Internal Server Error?
   *
   * @return true if 500, false otherwise
   * @throws HttpRequestException
   */
  public boolean serverError() throws HttpRequestException {
    return HTTP_INTERNAL_ERROR == code();
  }

  /**
   * Is the response code a 400 Bad Request?
   *
   * @return true if 400, false otherwise
   * @throws HttpRequestException
   */
  public boolean badRequest() throws HttpRequestException {
    return HTTP_BAD_REQUEST == code();
  }

  /**
   * Is the response code a 404 Not Found?
   *
   * @return true if 404, false otherwise
   * @throws HttpRequestException
   */
  public boolean notFound() throws HttpRequestException {
    return HTTP_NOT_FOUND == code();
  }

  /**
   * Is the response code a 304 Not Modified?
   *
   * @return true if 304, false otherwise
   * @throws HttpRequestException
   */
  public boolean notModified() throws HttpRequestException {
    return HTTP_NOT_MODIFIED == code();
  }

  /**
   * Get status message of the response
   *
   * @return message
   * @throws HttpRequestException
   */
  public String message() throws HttpRequestException {
    try {
      closeOutput();
      return getConnection().getResponseMessage();
    } catch (IOException e) {
      throw new HttpRequestException(e);
    }
  }

  /**
   * Disconnect the connection
   *
   * @return this request
   */
  public HttpRequestt disconnect() {
    getConnection().disconnect();
    return this;
  }

  /**
   * Set chunked streaming mode to the given size
   *
   * @param size
   * @return this request
   */
  public HttpRequestt chunk(final int size) {
    getConnection().setChunkedStreamingMode(size);
    return this;
  }

  /**
   * Set the size used when buffering and copying between streams
   * <p>
   * This size is also used for send and receive buffers created for both char
   * and byte arrays
   * <p>
   * The default buffer size is 8,192 bytes
   *
   * @param size
   * @return this request
   */
  public HttpRequestt bufferSize(final int size) {
    if (size < 1)
      throw new IllegalArgumentException("Size must be greater than zero");
    bufferSize = size;
    return this;
  }

  /**
   * Get the configured buffer size
   * <p>
   * The default buffer size is 8,192 bytes
   *
   * @return buffer size
   */
  public int bufferSize() {
    return bufferSize;
  }

  /**
   * Set whether or not the response body should be automatically uncompressed
   * when read from.
   * <p>
   * This will only affect requests that have the 'Content-Encoding' response
   * header set to 'gzip'.
   * <p>
   * This causes all receive methods to use a {@link GZIPInputStream} when
   * applicable so that higher level streams and readers can read the data
   * uncompressed.
   * <p>
   * Setting this option does not cause any request headers to be set
   * automatically so {@link #acceptGzipEncoding()} should be used in
   * conjunction with this setting to tell the server to gzip the response.
   *
   * @param uncompress
   * @return this request
   */
  public HttpRequestt uncompress(final boolean uncompress) {
    this.uncompress = uncompress;
    return this;
  }

  /**
   * Create byte array output stream
   *
   * @return stream
   */
  protected ByteArrayOutputStream byteStream() {
    final int size = contentLength();
    if (size > 0)
      return new ByteArrayOutputStream(size);
    else
      return new ByteArrayOutputStream();
  }

  /**
   * Get response as {@link String} in given character set
   * <p>
   * This will fall back to using the UTF-8 character set if the given charset
   * is null
   *
   * @param charset
   * @return string
   * @throws HttpRequestException
   */
  public String body(final String charset) throws HttpRequestException {
    final ByteArrayOutputStream output = byteStream();
    try {
      copy(buffer(), output);
      return output.toString(getValidCharset(charset));
    } catch (IOException e) {
      throw new HttpRequestException(e);
    }
  }

  /**
   * Get response as {@link String} using character set returned from
   * {@link #charset()}
   *
   * @return string
   * @throws HttpRequestException
   */
  public String body() throws HttpRequestException {
    return body(charset());
  }

  /**
   * Get the response body as a {@link String} and set it as the value of the
   * given reference.
   *
   * @param output
   * @return this request
   * @throws HttpRequestException
   */
  public HttpRequestt body(final AtomicReference<String> output) throws HttpRequestException {
    output.set(body());
    return this;
  }

  /**
   * Get the response body as a {@link String} and set it as the value of the
   * given reference.
   *
   * @param output
   * @param charset
   * @return this request
   * @throws HttpRequestException
   */
  public HttpRequestt body(final AtomicReference<String> output, final String charset) throws HttpRequestException {
    output.set(body(charset));
    return this;
  }


  /**
   * Is the response body empty?
   *
   * @return true if the Content-Length response header is 0, false otherwise
   * @throws HttpRequestException
   */
  public boolean isBodyEmpty() throws HttpRequestException {
    return contentLength() == 0;
  }

  /**
   * Get response as byte array
   *
   * @return byte array
   * @throws HttpRequestException
   */
  public byte[] bytes() throws HttpRequestException {
    final ByteArrayOutputStream output = byteStream();
    try {
      copy(buffer(), output);
    } catch (IOException e) {
      throw new HttpRequestException(e);
    }
    return output.toByteArray();
  }

  /**
   * Get response in a buffered stream
   *
   * @see #bufferSize(int)
   * @return stream
   * @throws HttpRequestException
   */
  public BufferedInputStream buffer() throws HttpRequestException {
    return new BufferedInputStream(stream(), bufferSize);
  }

  /**
   * Get stream to response body
   *
   * @return stream
   * @throws HttpRequestException
   */
  public InputStream stream() throws HttpRequestException {
    InputStream stream;
    if (code() < HTTP_BAD_REQUEST)
      try {
        stream = getConnection().getInputStream();
      } catch (IOException e) {
        throw new HttpRequestException(e);
      }
    else {
      stream = getConnection().getErrorStream();
      if (stream == null)
        try {
          stream = getConnection().getInputStream();
        } catch (IOException e) {
          if (contentLength() > 0)
            throw new HttpRequestException(e);
          else
            stream = new ByteArrayInputStream(new byte[0]);
        }
    }

    if (!uncompress || !ENCODING_GZIP.equals(contentEncoding()))
      return stream;
    else
      try {
        return new GZIPInputStream(stream);
      } catch (IOException e) {
        throw new HttpRequestException(e);
      }
  }

  /**
   * Get reader to response body using given character set.
   * <p>
   * This will fall back to using the UTF-8 character set if the given charset
   * is null
   *
   * @param charset
   * @return reader
   * @throws HttpRequestException
   */
  public InputStreamReader reader(final String charset)
      throws HttpRequestException {
    try {
      return new InputStreamReader(stream(), getValidCharset(charset));
    } catch (UnsupportedEncodingException e) {
      throw new HttpRequestException(e);
    }
  }


  /**
   * Get buffered reader to response body using the given character set r and
   * the configured buffer size
   *
   *
   * @see #bufferSize(int)
   * @param charset
   * @return reader
   * @throws HttpRequestException
   */
  public BufferedReader bufferedReader(final String charset)
      throws HttpRequestException {
    return new BufferedReader(reader(charset), bufferSize);
  }

  /**
   * Get buffered reader to response body using the character set returned from
   * {@link #charset()} and the configured buffer size
   *
   * @see #bufferSize(int)
   * @return reader
   * @throws HttpRequestException
   */
  public BufferedReader bufferedReader() throws HttpRequestException {
    return bufferedReader(charset());
  }


  /**
   * Set read timeout on connection to given value
   *
   * @param timeout
   * @return this request
   */
  public HttpRequestt readTimeout(final int timeout) {
    getConnection().setReadTimeout(timeout);
    return this;
  }

  /**
   * Set connect timeout on connection to given value
   *
   * @param timeout
   * @return this request
   */
  public HttpRequestt connectTimeout(final int timeout) {
    getConnection().setConnectTimeout(timeout);
    return this;
  }

  /**
   * Set header name to given value
   *
   * @param name
   * @param value
   * @return this request
   */
  public HttpRequestt header(final String name, final String value) {
    getConnection().setRequestProperty(name, value);
    return this;
  }

  /**
   * Get a response header
   *
   * @param name
   * @return response header
   * @throws HttpRequestException
   */
  public String header(final String name) throws HttpRequestException {
    closeOutputQuietly();
    return getConnection().getHeaderField(name);
  }

  /**
   * Get all the response headers
   *
   * @return map of response header names to their value(s)
   * @throws HttpRequestException
   */
  public Map<String, List<String>> headers() throws HttpRequestException {
    closeOutputQuietly();
    return getConnection().getHeaderFields();
  }

  

  /**
   * Get an integer header from the response falling back to returning -1 if the
   * header is missing or parsing fails
   *
   * @param name
   * @return header value as an integer, -1 when missing or parsing fails
   * @throws HttpRequestException
   */
  public int intHeader(final String name) throws HttpRequestException {
    return intHeader(name, -1);
  }

  /**
   * Get an integer header value from the response falling back to the given
   * default value if the header is missing or if parsing fails
   *
   * @param name
   * @param defaultValue
   * @return header value as an integer, default value when missing or parsing
   *         fails
   * @throws HttpRequestException
   */
  public int intHeader(final String name, final int defaultValue)
      throws HttpRequestException {
    closeOutputQuietly();
    return getConnection().getHeaderFieldInt(name, defaultValue);
  }


  /**
   * Get parameter with given name from header value in response
   *
   * @param headerName
   * @param paramName
   * @return parameter value or null if missing
   */
  public String parameter(final String headerName, final String paramName) {
    return getParam(header(headerName), paramName);
  }

  /**
   * Get all parameters from header value in response
   * <p>
   * This will be all key=value pairs after the first ';' that are separated by
   * a ';'
   *
   * @param headerName
   * @return non-null but possibly empty map of parameter headers
   */
  public Map<String, String> parameters(final String headerName) {
    return getParams(header(headerName));
  }

  /**
   * Get parameter values from header value
   *
   * @param header
   * @return parameter value or null if none
   */
  protected Map<String, String> getParams(final String header) {
    if (header == null || header.length() == 0)
      return Collections.emptyMap();

    final int headerLength = header.length();
    int start = header.indexOf(';') + 1;
    if (start == 0 || start == headerLength)
      return Collections.emptyMap();

    int end = header.indexOf(';', start);
    if (end == -1)
      end = headerLength;

    Map<String, String> params = new LinkedHashMap<String, String>();
    while (start < end) {
      int nameEnd = header.indexOf('=', start);
      if (nameEnd != -1 && nameEnd < end) {
        String name = header.substring(start, nameEnd).trim();
        if (name.length() > 0) {
          String value = header.substring(nameEnd + 1, end).trim();
          int length = value.length();
          if (length != 0)
            if (length > 2 && '"' == value.charAt(0)
                && '"' == value.charAt(length - 1))
              params.put(name, value.substring(1, length - 1));
            else
              params.put(name, value);
        }
      }

      start = end + 1;
      end = header.indexOf(';', start);
      if (end == -1)
        end = headerLength;
    }

    return params;
  }

  /**
   * Get parameter value from header value
   *
   * @param value
   * @param paramName
   * @return parameter value or null if none
   */
  protected String getParam(final String value, final String paramName) {
    if (value == null || value.length() == 0)
      return null;

    final int length = value.length();
    int start = value.indexOf(';') + 1;
    if (start == 0 || start == length)
      return null;

    int end = value.indexOf(';', start);
    if (end == -1)
      end = length;

    while (start < end) {
      int nameEnd = value.indexOf('=', start);
      if (nameEnd != -1 && nameEnd < end
          && paramName.equals(value.substring(start, nameEnd).trim())) {
        String paramValue = value.substring(nameEnd + 1, end).trim();
        int valueLength = paramValue.length();
        if (valueLength != 0)
          if (valueLength > 2 && '"' == paramValue.charAt(0)
              && '"' == paramValue.charAt(valueLength - 1))
            return paramValue.substring(1, valueLength - 1);
          else
            return paramValue;
      }

      start = end + 1;
      end = value.indexOf(';', start);
      if (end == -1)
        end = length;
    }

    return null;
  }

  /**
   * Get 'charset' parameter from 'Content-Type' response header
   *
   * @return charset or null if none
   */
  public String charset() {
    return parameter(HEADER_CONTENT_TYPE, PARAM_CHARSET);
  }

  /**
   * Set the 'User-Agent' header to given value
   *
   * @param userAgent
   * @return this request
   */
  public HttpRequestt userAgent(final String userAgent) {
    return header(HEADER_USER_AGENT, userAgent);
  }

  /**
   * Set the 'Referer' header to given value
   *
   * @param referer
   * @return this request
   */
  public HttpRequestt referer(final String referer) {
    return header(HEADER_REFERER, referer);
  }

  /**
   * Set value of {@link HttpURLConnection#setUseCaches(boolean)}
   *
   * @param useCaches
   * @return this request
   */
  public HttpRequestt useCaches(final boolean useCaches) {
    getConnection().setUseCaches(useCaches);
    return this;
  }

  /**
   * Set the 'Accept-Encoding' header to given value
   *
   * @param acceptEncoding
   * @return this request
   */
  public HttpRequestt acceptEncoding(final String acceptEncoding) {
    return header(HEADER_ACCEPT_ENCODING, acceptEncoding);
  }

  /**
   * Set the 'Accept-Encoding' header to 'gzip'
   *
   * @see #uncompress(boolean)
   * @return this request
   */
  public HttpRequestt acceptGzipEncoding() {
    return acceptEncoding(ENCODING_GZIP);
  }

  /**
   * Set the 'Accept-Charset' header to given value
   *
   * @param acceptCharset
   * @return this request
   */
  public HttpRequestt acceptCharset(final String acceptCharset) {
    return header(HEADER_ACCEPT_CHARSET, acceptCharset);
  }

  /**
   * Get the 'Content-Encoding' header from the response
   *
   * @return this request
   */
  public String contentEncoding() {
    return header(HEADER_CONTENT_ENCODING);
  }

  /**
   * Get the 'Content-Length' header from the response
   *
   * @return response header value
   */
  public int contentLength() {
    return intHeader(HEADER_CONTENT_LENGTH);
  }

  /**
   * Set the 'Content-Length' request header to the given value
   *
   * @param contentLength
   * @return this request
   */
  public HttpRequestt contentLength(final String contentLength) {
    return contentLength(Integer.parseInt(contentLength));
  }

  /**
   * Set the 'Content-Length' request header to the given value
   *
   * @param contentLength
   * @return this request
   */
  public HttpRequestt contentLength(final int contentLength) {
    getConnection().setFixedLengthStreamingMode(contentLength);
    return this;
  }


  /**
   * Copy from input stream to output stream
   *
   * @param input
   * @param output
   * @return this request
   * @throws IOException
   */
  protected HttpRequestt copy(final InputStream input, final OutputStream output)
      throws IOException {
    return new CloseOperation<HttpRequestt>(input, ignoreCloseExceptions) {

      @Override
      public HttpRequestt run() throws IOException {
        final byte[] buffer = new byte[bufferSize];
        int read;
        while ((read = input.read(buffer)) != -1) {
          output.write(buffer, 0, read);
          totalWritten += read;
          progress.onUpload(totalWritten, totalSize);
        }
        return HttpRequestt.this;
      }
    }.call();
  }

  
  /**
   * Set the UploadProgress callback for this request
   *
   * @param callback
   * @return this request
   */
  public HttpRequestt progress(final UploadProgress callback) {
    if (callback == null)
      progress = UploadProgress.DEFAULT;
    else
      progress = callback;
    return this;
  }

  /**
   * Close output stream
   *
   * @return this request
   * @throws HttpRequestException
   * @throws IOException
   */
  protected HttpRequestt closeOutput() throws IOException {
    progress(null);
    if (output == null)
      return this;
    if (multipart)
      output.write(CRLF + "--" + BOUNDARY + "--" + CRLF);
    if (ignoreCloseExceptions)
      try {
        output.close();
      } catch (IOException ignored) {
        // Ignored
      }
    else
      output.close();
    output = null;
    return this;
  }

  /**
   * Call {@link #closeOutput()} and re-throw a caught {@link IOException}s as
   * an {@link HttpRequestException}
   *
   * @return this request
   * @throws HttpRequestException
   */
  protected HttpRequestt closeOutputQuietly() throws HttpRequestException {
    try {
      return closeOutput();
    } catch (IOException e) {
      throw new HttpRequestException(e);
    }
  }
  /**
   * Get the {@link URL} of this request's connection
   *
   * @return request URL
   */
  public URL url() {
    return getConnection().getURL();
  }

  /**
   * Get the HTTP method of this request
   *
   * @return method
   */
  public String method() {
    return getConnection().getRequestMethod();
  }
}
