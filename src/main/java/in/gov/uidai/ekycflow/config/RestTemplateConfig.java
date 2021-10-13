package in.gov.uidai.ekycflow.config;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

@Configuration
public class RestTemplateConfig {

    @Value("${proxy.enable}")
    private boolean proxyEnable;
    @Value("${proxy.host}")
    private String proxyHost;
    @Value("${proxy.port}")
    private int proxyPort;
    @Value("${proxy.scheme}")
    private String scheme;
    @Value("${proxy.username}")
    private String username;
    @Value("${proxy.password}")
    private String password;
    @Value("${sslValidationDisabled}")
    private boolean sslValidationDisabled;
    @Value("${proxy.auth}")
    private boolean isProxyAuthEnabled;

    @Bean(name = "restTemplate")
    public RestTemplate restTemplate() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

        HttpClientBuilder httpClientBuilder;
        SSLContextBuilder sslcontext = new SSLContextBuilder();

        // proxy enabled with ignore certificate validation
        if(proxyEnable && sslValidationDisabled){
            TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
            sslcontext.loadTrustMaterial(null, acceptingTrustStrategy);

            httpClientBuilder = HttpClients.custom().setSSLContext(sslcontext.build()).setSSLHostnameVerifier(
                    NoopHostnameVerifier.INSTANCE)
                    .setProxy(new HttpHost(proxyHost, proxyPort, scheme));

            // proxy enabled with certificate validation
        }else if(proxyEnable){
            httpClientBuilder = HttpClients.custom().setSSLContext(sslcontext.build())
                    .setProxy(new HttpHost(proxyHost, proxyPort, scheme));

            // no proxy with ignore certificate validation
        }else if(sslValidationDisabled){
            sslcontext.loadTrustMaterial(null, new TrustSelfSignedStrategy());
            httpClientBuilder = HttpClients.custom().setSSLContext(sslcontext.build()).setSSLHostnameVerifier(
                    NoopHostnameVerifier.INSTANCE);

            // no proxy with certificate validation
        }else {
            httpClientBuilder =  HttpClients.custom();
        }

        if(isProxyAuthEnabled){
            CredentialsProvider credsProvider = new BasicCredentialsProvider();
            credsProvider.setCredentials(
                    new AuthScope(proxyHost,proxyPort),
                    new UsernamePasswordCredentials(username, password)
            );
            httpClientBuilder.setProxy(new HttpHost(proxyHost, proxyPort, scheme)).setDefaultCredentialsProvider(credsProvider).disableCookieManagement();

        }

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClientBuilder.build());
        return new RestTemplate(requestFactory);
    }
}
