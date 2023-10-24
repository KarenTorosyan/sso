package auth.views;

import auth.Endpoints;
import auth.entities.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class UserRegisterController {

    private final UserService userService;

    @GetMapping(Endpoints.REGISTER)
    String renderRegisterPage(Model model) {
        if (userService.getCount() == 0) {
            model.addAttribute("registerAsAdmin", true);
        }
        return "register";
    }
}
