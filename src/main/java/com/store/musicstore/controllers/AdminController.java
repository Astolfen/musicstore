package com.store.musicstore.controllers;

import com.store.musicstore.models.User;
import com.store.musicstore.models.enums.Role;
import com.store.musicstore.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {
    private final UserService userService;

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("users", userService.list());
        return "admin";
    }

    @PostMapping("/admin/user/ban/{id}")
    public String userBan(@PathVariable("id") Long id) {
        userService.banUser(id);
        return "redirect:/admin";
    }

    @GetMapping("/admin/user/edit/{user}")
    public String userEdit(@PathVariable("user") User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "user-edit";
    }


//    @PostMapping("/admin/user/edit")
//    public String userEdit(@RequestParam("userId") User user, @RequestParam Map<String,String> form) {
//        userService.changeUserRoles(user,form);
//        return "redirect:/admin";
//    }

    @PostMapping("/admin/user/edit")
    public String userEdit(@RequestParam("userId") User user, @RequestParam("selectedRole") String form) {
        userService.changeUserRoles(user, form);
        return "redirect:/admin";
    }
}
