package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.model.Role;
import web.model.User;
import web.service.UserService;

import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("")
public class UserController {

    UserDetailsService userDetailsService;
    UserService userService;

    @Autowired
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/")
    public String getHomePage(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Set<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());

        String username;
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        model.addAttribute("username", username);
        model.addAttribute("roles", roles);
        return "index";



        /*        // SOME METHODS TO AUTH FILTER
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        assert(authentication.isAuthenticated);

        Set<String> roles = SuccessUserHandler.getRoles();

        @RequestMapping("/foo")
        public String foo(@AuthenticationPrincipal User user) {
             do stuff with user
        }
        */
    }

    @GetMapping(value = "/logincustom")
    public String getLoginPage(@RequestParam(value = "error", required = false) String error,
                               @RequestParam(value = "logout", required = false) String logout,
                               Model model) {
        model.addAttribute("error", error != null);
        model.addAttribute("logout", logout != null);
        return "logincust";
    }

    @GetMapping("/registration")
    public String getRegisterForm(@ModelAttribute("user") User user) {
        return "regForm";
    }

    @PostMapping("/registration")
    public String regUser(@ModelAttribute("user") User user,
                            Model model) {
        userService.addUser(user);
//        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
//        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
//        authentication.setAuthenticated(true);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        return String.format("redirect:/user/%s", user.getUsername());
        return "redirect:/logincustom";
    }

    // !!! REGISTRATION ERROR

    @GetMapping(value = "/user")
    public String getUserPage() {
        return "user";
    }

    @GetMapping(value = "/user/{name}")
    public String getPersonal(@PathVariable("name") String name, Model model) {
        User user = (User) userDetailsService.loadUserByUsername(name);
        model.addAttribute("user", user);
        return "user";
    }

    @GetMapping(value = "/admin")
    public String getAdminPage(Model model) {
        List<User> listUsers = userService.allUsers();
        model.addAttribute("listUsers", listUsers);
        return "admin";
    }

    @GetMapping(value = "/some")
    public String getSomePage() {
        return "some";
    }

}
