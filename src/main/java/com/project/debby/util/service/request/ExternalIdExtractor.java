package com.project.debby.util.service.request;

import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;

public class ExternalIdExtractor {

    private static final String SELF_LINK = "me";

    public static String getExternalIDSafely(String id, HttpServletRequest request){
        if (id.equals(SELF_LINK)) return (String) request.getAttribute("UserID");
        else {
            if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                    .anyMatch(v -> v.getAuthority().equals("ADMIN"))) return id;
            else throw new IllegalAccessError();
        }
    }

    public static String getExternalID(HttpServletRequest request){
        return (String) request.getAttribute("UserID");
    }
}
