package auth.views;

import auth.Endpoints;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequiredArgsConstructor
public class AuthorizationConsentController {

    private final RegisteredClientRepository registeredClientRepository;

    @GetMapping(Endpoints.AUTHORIZATION_CONSENT)
    public String consent(Principal principal, Model model,
                          @RequestParam(OAuth2ParameterNames.CLIENT_ID) String clientId,
                          @RequestParam(OAuth2ParameterNames.SCOPE) String scope,
                          @RequestParam(OAuth2ParameterNames.STATE) String state) {

        Set<String> requestedScopes = Stream.of(scope.split(" ")).collect(Collectors.toSet());

        Set<String> registeredClientScopes = Optional.ofNullable(registeredClientRepository.findByClientId(clientId))
                .map(RegisteredClient::getScopes)
                .orElse(Set.of());

        Set<String> scopesToApprove = requestedScopes.stream()
                .filter(registeredClientScopes::contains)
                .filter(s -> !s.equals(OidcScopes.OPENID))
                .collect(Collectors.toSet());

        model.addAttribute("clientId", clientId);
        model.addAttribute("state", state);
        model.addAttribute("scopes", scopesToApprove);
        model.addAttribute("principalName", principal.getName());
        return "consent";
    }
}