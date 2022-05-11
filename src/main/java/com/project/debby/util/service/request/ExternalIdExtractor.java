package com.project.debby.util.service.request;

import javax.servlet.http.HttpServletRequest;

public class ExternalIdExtractor {

    public static String getExternalID(HttpServletRequest request){
        return (String) request.getAttribute("UserID");
    }
}
