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
    String register(Model model) {
        long usersCount = userService.getCount();
        if (usersCount == 0) {
            model.addAttribute("registerAsAdminText", "register_as_admin");
        }
        return "register";
    }
}
