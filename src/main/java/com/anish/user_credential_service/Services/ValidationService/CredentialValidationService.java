package com.anish.user_credential_service.Services.ValidationService;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CredentialValidationService implements PayloadValidate {

    @Override
    public Boolean validate(Map<String, Object> requestMap) {

        return requestMap.get("password") != null &&
                requestMap.get("mobileNumber") != null &&
                requestMap.get("userId") != null;
    }
}
