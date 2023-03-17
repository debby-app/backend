package com.project.debby.util.service.request.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class IllegalExternalIdMimicry extends Exception{

    public IllegalExternalIdMimicry(String msg){
        super(msg);
    }
}
