package proxy.api.config.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;


@Configuration
public class OAuth2Config {

    @Value("${clientId}")
    private String clientId;

    @Value("${clientSecret}")
    private String clientSecret;

    @Value("${tokenUrl}")
    private String tokenUrl;

    @Value("${accessTokenUser}")
    private String accessTokenUser;

    @Value("${accessTokenPass}")
    private String accessTokenPass;

    private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2Config.class);

    protected ResourceOwnerPasswordResourceDetails getResourceOwnerPassword() {
        ResourceOwnerPasswordResourceDetails resource = new ResourceOwnerPasswordResourceDetails();
        resource.setId(accessTokenUser);
        resource.setUsername(accessTokenUser);
        resource.setPassword(accessTokenPass);
        resource.setAccessTokenUri(tokenUrl);
        resource.setClientId(clientId);
        resource.setClientSecret(clientSecret);
        resource.setGrantType("password");

        resource.setClientAuthenticationScheme(AuthenticationScheme.form);
        resource.setAuthenticationScheme(AuthenticationScheme.header);

        return resource;
    }

    @Bean("oauth2RestTemplateByUser")
    public OAuth2RestTemplate oAuth2RestTemplateByUser() {

        DefaultOAuth2ClientContext clientContext = new DefaultOAuth2ClientContext();
        OAuth2RestTemplate oauth2RestTemplate = new OAuth2RestTemplate(getResourceOwnerPassword(), clientContext);

        oauth2RestTemplate.setAccessTokenProvider(new ResourceOwnerPasswordAccessTokenProvider());

        return oauth2RestTemplate;
    }

    @Bean
    protected OAuth2ProtectedResourceDetails oauth2Resource() {

        ClientCredentialsResourceDetails clientCredentialsResourceDetails = new ClientCredentialsResourceDetails();
        clientCredentialsResourceDetails.setAccessTokenUri(tokenUrl);
        clientCredentialsResourceDetails.setClientId(clientId);
        clientCredentialsResourceDetails.setClientSecret(clientSecret);
        clientCredentialsResourceDetails.setGrantType("client_credentials"); //this depends on your specific OAuth2 server
        clientCredentialsResourceDetails.setId("5a3b6209-44a0-404c-845a-452b5c63f15e");
        clientCredentialsResourceDetails.setAuthenticationScheme(AuthenticationScheme.header); //this again depends on the OAuth2 server specifications

        return clientCredentialsResourceDetails;
    }

    @Bean("oauth2RestTemplate")
    public OAuth2RestTemplate oauth2RestTemplate() {

        AccessTokenRequest atr = new DefaultAccessTokenRequest();
        OAuth2RestTemplate oauth2RestTemplate = new OAuth2RestTemplate(oauth2Resource(), new DefaultOAuth2ClientContext(atr));
        oauth2RestTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        return oauth2RestTemplate;
    }
}