package com.huya.server.kits.serverkits.protocol.http;

import com.huya.server.kits.serverkits.dns.ServerKitsDnsResolver;
import com.huya.server.kits.serverkits.exception.NetworkException;
import com.huya.server.kits.serverkits.utils.CommonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Kevin[wangzhuo@yy.com] on 2017/8/28.
 */
public class HttpProtocol {

    private static Logger logger = LoggerFactory.getLogger(HttpProtocol.class);
    private static PoolingHttpClientConnectionManager poolConnManager = null;
    private static int defaultTimeOut = 3000;
    public static final int keepAliveTimeOut = 60;

    static {
        RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.create();
        registryBuilder.register("http", PlainConnectionSocketFactory.getSocketFactory()).build();

        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            TrustStrategy anyTrustStrategy = new TrustStrategy() {
                public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    return true;
                }
            };
            SSLContext sslContext = SSLContexts.custom().useTLS().loadTrustMaterial(trustStore, anyTrustStrategy).build();
            LayeredConnectionSocketFactory sslSF = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            registryBuilder.register("https", sslSF);
        } catch (KeyStoreException var5) {
            throw new RuntimeException(var5);
        } catch (KeyManagementException var6) {
            throw new RuntimeException(var6);
        } catch (NoSuchAlgorithmException var7) {
            throw new RuntimeException(var7);
        }

        poolConnManager = new PoolingHttpClientConnectionManager(registryBuilder.build(), ServerKitsDnsResolver.INSTANCE);
        poolConnManager.setMaxTotal(500);
        poolConnManager.setDefaultMaxPerRoute(200);
        SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(defaultTimeOut).setSoKeepAlive(true).build();
        poolConnManager.setDefaultSocketConfig(socketConfig);
        (new IdleConnectionMonitorThread(poolConnManager)).start();
        logger.info("init PoolingHttpClientConnectionManager.");
    }

    public HttpProtocol() {
    }

    private static CloseableHttpClient getConnection(final boolean automaticRetriesDisabled) {
        RequestConfig requestConfig = RequestConfig.custom().setStaleConnectionCheckEnabled(false).setConnectTimeout(defaultTimeOut).setConnectionRequestTimeout(2000).setSocketTimeout(defaultTimeOut).build();
        ConnectionKeepAliveStrategy kaStrategy = new DefaultConnectionKeepAliveStrategy() {
            public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
                long keepAlive = super.getKeepAliveDuration(response, context);
                if (keepAlive == -1L) {
                    keepAlive = 60000L;
                }

                return keepAlive;
            }
        };
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).setConnectionManager(poolConnManager).setKeepAliveStrategy(kaStrategy).setRetryHandler(new HttpRequestRetryHandler() {
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                if (executionCount > 3) {
                    HttpProtocol.logger.warn("Maximum tries reached for client http pool ");
                    return false;
                } else {
                    if (!automaticRetriesDisabled) {
                        if (exception instanceof NoHttpResponseException) {
                            HttpProtocol.logger.warn("No response from server on " + executionCount + " call");
                            return true;
                        }

                        if (exception instanceof SocketException && exception.getMessage().indexOf("Connection reset") >= 0) {
                            HttpProtocol.logger.warn("java.net.SocketException Connection reset " + executionCount + " call");
                            return true;
                        }

                        if (exception instanceof SocketTimeoutException) {
                            HttpProtocol.logger.warn("java.net.SocketTimeoutException: Read timed out " + executionCount + " call");
                            return true;
                        }
                    }

                    return false;
                }
            }
        }).build();
        if (poolConnManager != null && poolConnManager.getTotalStats() != null && logger.isDebugEnabled()) {
            logger.debug("now client pool " + poolConnManager.getTotalStats().toString());
        }

        return httpClient;
    }

    private static RequestConfig buildRequestConfig(Integer timeOut) {
        RequestConfig requestConfig = RequestConfig.custom().setStaleConnectionCheckEnabled(false).setConnectTimeout(timeOut.intValue()).setConnectionRequestTimeout(1000).setSocketTimeout(timeOut.intValue()).build();
        return requestConfig;
    }

    public static String doGet(String url, Integer timeOut, boolean automaticRetriesDisabled) throws ClientProtocolException, IOException, NetworkException {
        return doGet(url, (Map) null, timeOut, automaticRetriesDisabled);
    }

    public static String doGet(String url, Map<String, String> headerMap, Integer timeOut, boolean automaticRetriesDisabled) throws ClientProtocolException, IOException, NetworkException {
        CloseableHttpClient httpclient = getConnection(automaticRetriesDisabled);
        HttpGet httpGet = new HttpGet(url);
        RequestConfig requestConfig = buildRequestConfig(timeOut);
        httpGet.setConfig(requestConfig);
        Header[] headers;
        if (headerMap != null) {
            int i = 0;
            headers = new Header[headerMap.size()];

            Map.Entry e;
            for (Iterator var10 = headerMap.entrySet().iterator(); var10.hasNext(); headers[i++] = new BasicHeader((String) e.getKey(), (String) e.getValue())) {
                e = (Map.Entry) var10.next();
                if (StringUtils.isBlank((CharSequence) e.getKey())) {
                    throw new IllegalArgumentException();
                }
            }

            httpGet.setHeaders(headers);
        }

        CloseableHttpResponse response = httpclient.execute(httpGet);
        headers = null;

        String res;
        try {
            StatusLine statusLine = response.getStatusLine();
            if (logger.isDebugEnabled()) {
                logger.debug(String.format("get url:%s,返回状态码:%s", url, statusLine));
            }

            if (statusLine.getStatusCode() != 200) {
                httpGet.abort();
                throw new NetworkException(statusLine.getStatusCode());
            }

            HttpEntity entity = response.getEntity();
            res = EntityUtils.toString(entity, "utf-8");
            if (logger.isDebugEnabled()) {
                logger.debug(String.format("get url:%s,返回值:%s", url, res));
            }
        } finally {
            response.close();
        }

        return res;
    }

    public static String doPost(String url, Map<String, String> params, Integer timeOut, boolean automaticRetriesDisabled) throws ClientProtocolException, IOException, NetworkException, IllegalArgumentException {
        return doPost(url, params, (Map) null, timeOut, automaticRetriesDisabled);
    }

    public static String doPost(String url, Map<String, String> params, Map<String, String> headerMap, Integer timeOut, boolean automaticRetriesDisabled) throws ClientProtocolException, IOException, NetworkException, IllegalArgumentException {
        CloseableHttpClient httpclient = getConnection(automaticRetriesDisabled);
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = buildRequestConfig(timeOut);
        httpPost.setConfig(requestConfig);
        Header[] headers;
        if (headerMap != null) {
            int i = 0;
            headers = new Header[headerMap.size()];

            Map.Entry e;
            for (Iterator var11 = headerMap.entrySet().iterator(); var11.hasNext(); headers[i++] = new BasicHeader((String) e.getKey(), (String) e.getValue())) {
                e = (Map.Entry) var11.next();
                if (StringUtils.isBlank((CharSequence) e.getKey())) {
                    throw new IllegalArgumentException();
                }
            }

            httpPost.setHeaders(headers);
        }

        List<NameValuePair> nvps = new ArrayList();
        if (params != null) {
            Iterator var19 = params.entrySet().iterator();

            while (var19.hasNext()) {
                Map.Entry<String, String> e = (Map.Entry) var19.next();
                if (StringUtils.isBlank((CharSequence) e.getKey())) {
                    throw new IllegalArgumentException();
                }

                nvps.add(new BasicNameValuePair((String) e.getKey(), (String) e.getValue()));
            }
        }

        headers = null;
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, Charset.forName("utf-8")));
        CloseableHttpResponse response = httpclient.execute(httpPost);

        String res;
        try {
            StatusLine statusLine = response.getStatusLine();
            if (logger.isDebugEnabled()) {
                logger.debug(String.format("post url:%s,参数:%s,返回状态码:%s", url, CommonUtils.MapToString(params), statusLine));
            }

            if (statusLine.getStatusCode() != 200) {
                httpPost.abort();
                throw new NetworkException(statusLine.getStatusCode());
            }

            HttpEntity entity = response.getEntity();
            res = EntityUtils.toString(entity, "utf-8");
            if (logger.isDebugEnabled()) {
                logger.debug(String.format("post url:%s,参数:%s,返回值:%s", url, CommonUtils.MapToString(params), res));
            }
        } finally {
            response.close();
        }

        return res;
    }

    public static String urlToHost(String url) {
        if (url != null && !url.trim().equals("")) {
            String host = "";
            Pattern p = Pattern.compile("(?<=://)([\\w-]+\\.)+[\\w-]+(?<=/?)");
            Matcher matcher = p.matcher(url);
            if (matcher.find()) {
                host = matcher.group();
            }

            return host;
        } else {
            return "";
        }
    }

}
