package com.anish.user_credential_service.Services.ValidationService;


import java.util.Map;

public interface PayloadValidate {


    Boolean validate(Map<String,Object> requestMap);
}
