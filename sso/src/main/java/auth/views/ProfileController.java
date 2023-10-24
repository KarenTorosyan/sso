package auth.views;

import auth.Endpoints;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {

    @GetMapping(Endpoints.PROFILE)
    String renderProfilePage() {
        return "profile";
    }
}
