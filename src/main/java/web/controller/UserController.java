package web.controller;

import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import web.model.PreparedRoles;
import web.model.Role;
import web.model.User;
import web.service.RoleService;
import web.service.SecurityService;
import web.service.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("")
public class UserController {

    UserDetailsService userDetailsService;
    UserService userService;
    RoleService roleService;
    private SecurityService securityService;

    @Autowired
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    @Autowired
    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }
    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
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

//    @PostMapping("/registration")
//    public String regUser(@ModelAttribute("user") @Valid User user,
//                          BindingResult bindingResult,
//                          Model model) {
//        String errorExist;
//        if (bindingResult.hasErrors()) {
//            return "regForm";
//        }
//        try {
//            userService.addUser(user);
//            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
//            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            return String.format("redirect:/user/%s", user.getUsername());
//        } catch (DataIntegrityViolationException e) {
//            System.out.println("!!!!!!!!!!!!!!!!!!!! - controller - DataIntegrityViolationException - regUser");
//            errorExist = "this name is already exist";
//        }
//        user.setPassword("password");
//        user.setUsername("login");
//        model.addAttribute("errorExist", errorExist);
//        return "regForm";
//
////        return "redirect:/logincustom";
//    }

    @PostMapping("/registration")
    public String regUser(@ModelAttribute("user") @Valid User user,
                          BindingResult bindingResult,
                          Model model) {
        String errorExist;
        if (bindingResult.hasErrors()) {
            return "regForm";
        }
        if (userService.isExistingUser(user)) {
            errorExist = "this name is already exist";
            model.addAttribute("errorExist", errorExist);
            return "regForm";
        }
        userService.addUser(user);
        securityService.autoLogin(user);
        return String.format("redirect:/user/%s", user.getUsername());
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

    private PreparedRoles getNewPreparedRole(User user) {
        PreparedRoles preparedRoles = new PreparedRoles();
        preparedRoles.setAllRoles(roleService.getAllRoles());
        preparedRoles.setUserRoles(user);
        preparedRoles.setUsername(user.getUsername());
        preparedRoles.setPassword(user.getPassword());
        return preparedRoles;
    }

    @GetMapping(value = "edit/user/{id}")
    public String editUserForm(@PathVariable("id") long id,
                               @ModelAttribute("preparedRoles") PreparedRoles preparedRoles,
                               Model model){
        User user = userService.showById(id);
        preparedRoles.setAllRoles(roleService.getAllRoles());
        preparedRoles.setUserRoles(user);
        preparedRoles.setUsername(user.getUsername());
        preparedRoles.setPassword(user.getPassword());

        model.addAttribute("user", user);
        model.addAttribute("preparedRoles", preparedRoles);
        return "editUser";
    }



    @PatchMapping(value = "/edit/user/{id}")
    public String getEditUser(@ModelAttribute("preparedRoles") @Valid PreparedRoles preparedRoles,
                           BindingResult bindingResult,
                           @PathVariable("id") long id,
                           Model model) {
        User user = userService.showById(id);
        boolean err = false;
        if ((!user.getUsername().equals(preparedRoles.getUsername()))
                && userService.isExistingUserByName(preparedRoles.getUsername())) {
            model.addAttribute("errorExist", "this name is already exist");
            err = true;
        }
        if (err || !preparedRoles.isNotErrorsTest()) {
            model.addAttribute("errorNameEmpty", preparedRoles.getMessageIfNameEmpty());
            model.addAttribute("errorPassEmpty", preparedRoles.getMessageIfPasswordEmpty());
            model.addAttribute("user", user);
            model.addAttribute("preparedRoles", getNewPreparedRole(user));
            return "editUser";
        }
        Set<Role> newRolesSet = preparedRoles.getActualRoles()
                                             .stream()
                                             .map(roleService::getRole)
                                             .collect(Collectors.toSet());
        user.setRoles(newRolesSet);
        user.setUsername(preparedRoles.getUsername());
        user.setPassword(preparedRoles.getPassword());
        userService.updateUser(user);
        return String.format("redirect:/user/%s", user.getUsername());
    }

    @DeleteMapping("/delete/user/{id}")
    public String deleteUser(@PathVariable("id") long id, Model model) {
//        User user = userService.showById(id);
        userService.deleteUserById(id);
        return "redirect:/admin";
    }




}
