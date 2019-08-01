// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   VtigerConnector.java

package utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.scheme.*;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

// Referenced classes of package utils:
//            InsecureSSLSocketFactory, PropertiesReader

public class CrmConnector
{
    
    private static final Logger logger = Logger.getLogger(CrmConnector.class);
    private final String requestPrefix;
    private final String requestUri;
    private final String requestHost;
    private final String requestScheme;
    private final String requestPath;

    public CrmConnector()
    {
        requestPrefix = PropertiesReader.getProperty("CrmURLPrefix");
        requestUri = (new StringBuilder()).append(PropertiesReader.getProperty("CrmURL")).append(requestPrefix).toString();
        requestPath = PropertiesReader.getProperty("CrmUrlPath");
        requestHost = PropertiesReader.getProperty("CrmUrlHost");
        requestScheme = PropertiesReader.getProperty("CrmUrlScheme");
    }

    public CrmConnector(String requestUri)
    {
        this.requestUri = requestUri;
        this.requestPrefix = PropertiesReader.getProperty("CrmURLPrefix");
        this.requestPath = PropertiesReader.getProperty("CrmUrlPath");
        this.requestHost = PropertiesReader.getProperty("CrmUrlHost");
        this.requestScheme = PropertiesReader.getProperty("CrmUrlScheme");        
    }

    public void sendCommand(List eventParams)
    {

        try
        {
            URIBuilder uriBuilder = new URIBuilder()
                .setScheme(this.requestScheme)
                .setHost(this.requestHost)
                .setPath(this.requestPath);
                
            Iterator<BasicNameValuePair> it = eventParams.iterator();
            while(it.hasNext()) {
                BasicNameValuePair param = it.next();
                uriBuilder.setParameter(param.getName(), param.getValue());
            }
            URI uri = uriBuilder.build();
            
            SSLContext sslcontext = SSLContexts.custom().useSSL().build();
            sslcontext.init(null, new X509TrustManager[]{new HttpsTrustManager()}, new SecureRandom());
            SSLConnectionSocketFactory factory = new SSLConnectionSocketFactory(sslcontext,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
            // client = HttpClients.custom().setSSLSocketFactory(factory).build();
            
            HttpGet request = new HttpGet(uri);
            HttpClient client = getNewHttpClient();            
                        HttpResponse response = client.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();

            // Getting the response body.
            String responseBody = EntityUtils.toString(response.getEntity());
            
            logger.info("Respuesta del CRM code: " + statusCode + ", body: " + responseBody);
        }
        catch(UnsupportedEncodingException ex )
        {
            logger.fatal("Error on send GET request to crm", ex);
        }
        catch(IOException ex)
        {
            logger.fatal("Error on send GET request to crm", ex);
        } catch (URISyntaxException ex) {
            java.util.logging.Logger.getLogger(CrmConnector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            java.util.logging.Logger.getLogger(CrmConnector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyManagementException ex) {
            java.util.logging.Logger.getLogger(CrmConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private HttpClient getNewHttpClient()
    {
        try
        {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            InsecureSSLSocketFactory sf = new InsecureSSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            org.apache.http.params.HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, "UTF-8");
            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));
            org.apache.http.conn.ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
            return new DefaultHttpClient(ccm, params);
        }
        catch(Exception e)
        {
            return new DefaultHttpClient();
        }
    }



}
