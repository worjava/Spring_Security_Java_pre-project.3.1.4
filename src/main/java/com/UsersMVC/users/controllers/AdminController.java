package com.UsersMVC.users.controllers;


import com.UsersMVC.users.models.User;

import com.UsersMVC.users.services.RoleService;
import com.UsersMVC.users.services.UserServiceImpl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserServiceImpl userServiceImp;
    public final RoleService roleService;

    public AdminController(UserServiceImpl userService, RoleService roleService) {
        this.userServiceImp = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String index(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("user", userServiceImp.loadUserByUsername(auth.getName()));
        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("users", userServiceImp.index());// user добавляем атрибут в html
        model.addAttribute("updatedUser", new User());
        return "users/index";
    }


    @GetMapping("/new")
    public String newPerson(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.findAll());
        return "users/new";
    }

    @PostMapping()
    public String creat(@ModelAttribute("user") @Valid User person,
                        BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "redirect:/admin";
        if (userServiceImp.existsUserByEmail(person.getEmail())) {
            bindingResult.rejectValue("email", "user.email.exists",
                    "Пользователь с таким email уже существует");

            return "redirect:/admin";
        }

        userServiceImp.save(person);
        return "redirect:/admin";
    }


    @DeleteMapping("{id}")
    public String delete(@PathVariable("id") int id) {
        userServiceImp.delete(id);
        return "redirect:/admin";
    }


}

