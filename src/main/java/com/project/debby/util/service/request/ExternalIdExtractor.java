package com.project.debby.util.service.request;

import com.project.debby.util.service.request.exception.IllegalExternalIdMimicry;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;

public class ExternalIdExtractor {

    private static final String SELF_LINK = "me";

    public static String getExternalIDSafely(String id, HttpServletRequest request) throws IllegalExternalIdMimicry {
        if (id.equals(SELF_LINK)) return (String) request.getAttribute("UserID");
        else {
            if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                    .anyMatch(v -> v.getAuthority().equals("admin"))) return id;
            else throw new IllegalExternalIdMimicry(
                    "Illegal attempt of user " + request.getAttribute("UserID") + " to mimicry to " + id);
        }
    }

    public static String getExternalID(HttpServletRequest request){
        return (String) request.getAttribute("UserID");
    }
}
