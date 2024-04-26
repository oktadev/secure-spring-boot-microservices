package com.example.gateway.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import java.util.Collections;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.stream.Collectors;

@RestController
class HomeController {

    @GetMapping("/")
    @PreAuthorize("hasAuthority('SCOPE_profile')")
    public ModelAndView home(@AuthenticationPrincipal OidcUser user) {
        return new ModelAndView("home", Collections.singletonMap("claims", user.getClaims()));
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('Administrator')")
    public String admin(@AuthenticationPrincipal OidcUser user) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());

        return "Hello, Admin!<br/><br/>User: " + user.getFullName() + "!<br/><br/>Authorities: " + authorities;
    }

}