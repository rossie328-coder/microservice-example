package org.example.controller.mqtt;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

public class TLSUtil {
    public SSLSocketFactory trustAllSocketFactory() throws Exception {
        SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
        sslContext.init(null, new javax.net.ssl.TrustManager[]{
                new javax.net.ssl.X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() { return null; }
                    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
                    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
                }
        }, new java.security.SecureRandom());
        return sslContext.getSocketFactory();
    }

}
