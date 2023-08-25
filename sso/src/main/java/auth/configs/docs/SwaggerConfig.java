package auth.configs.docs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.*;
import org.springframework.boot.autoconfigure.security.oauth2.server.servlet.OAuth2AuthorizationServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    OpenAPI openAPI(OAuth2AuthorizationServerProperties properties) {
        return new OpenAPI()
                .info(new Info()
                        .title("SSO Authorization Server")
                        .version("1.0.0"))
                .components(new Components()
                        .addSecuritySchemes("oAuth2", oAuth2SecurityScheme(properties))
                        .addSecuritySchemes("bearer", bearerSecurityScheme()))
                .addSecurityItem(new SecurityRequirement()
                        .addList("oAuth2").addList("bearer"));
    }

    private SecurityScheme oAuth2SecurityScheme(OAuth2AuthorizationServerProperties properties) {
        OAuth2AuthorizationServerProperties.Registration publicClient =
                properties.getClient().get("public").getRegistration();
        Scopes scopes = new Scopes();
        publicClient.getScopes().forEach(scope -> scopes.put(scope, null));
        return new SecurityScheme()
                .type(SecurityScheme.Type.OAUTH2)
                .flows(new OAuthFlows()
                        .authorizationCode(new OAuthFlow()
                                .authorizationUrl(properties.getEndpoint().getAuthorizationUri())
                                .tokenUrl(properties.getEndpoint().getTokenUri())
                                .scopes(scopes)))
                .description("Authorization with 'public' client");
    }

    private SecurityScheme bearerSecurityScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .description("Authorization using access token as Bearer");
    }
}
