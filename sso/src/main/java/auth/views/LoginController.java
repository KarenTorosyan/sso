package auth.views;

import auth.Endpoints;
import auth.entities.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

    @GetMapping(Endpoints.LOGIN)
    String login() {
        if (userService.getCount() == 0) {
            return "redirect:/register";
        }
        return "login";
    }
}
