package com.anish.user_credential_service.Services;

import com.anish.user_credential_service.Entity.Identifier;
import com.anish.user_credential_service.Repository.IdentifierRepository;
import com.anish.user_credential_service.Services.ValidationService.CredentialValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.UUID;

@Service
public class SaveUserCredential {

    @Autowired
    CredentialValidationService credentialValidationService;

    @Autowired
    IdentifierRepository identifierRepository;

    @Autowired
    private KafkaTemplate<String, Map<String, Object>> kafkaTemplate;

    @KafkaListener(topics = "save-user-credential-request", groupId = "my-group-id")
    public void consume(Map<String, Object> message) {
        System.out.println("Received message: " + message);
        save(message);

    }

    private void save(Map<String,Object> requestMap){

        if(credentialValidationService.validate(requestMap)){

            Identifier identifier = new Identifier();

            UUID uuid = UUID.randomUUID();
            String authId = "AU." + uuid.toString().replace("-","").substring(0,27);
            identifier.setAuthId(authId);
            identifier.setUserId(requestMap.get("userId").toString());
            identifier.setIdType("MOBILE_NUMBER");
            identifier.setIdValue(requestMap.get("mobileNumber").toString());
            identifier.setPassword(hashPassword(requestMap.get("password").toString()));
            identifierRepository.saveAndFlush(identifier);
            System.out.println("credentials saved successfully");
            requestMap.put("message", "User onboarded successfully with user id : " + requestMap.get("userId"));
            sendMessage("messageKey",requestMap);
        }else{
            System.out.println("Failed saving credentials");
        }
    }

    private String hashPassword(String authValue){

        try {
            // Get an instance of the SHA-256 MessageDigest
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Perform the hash computation on the input string's bytes
            byte[] hashBytes = digest.digest(authValue.getBytes());

            // Convert the resulting byte array into a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }



    }

    public void sendMessage(String key, Map<String, Object> message) {
        String TOPIC = "registration-response";
        kafkaTemplate.send(TOPIC, key, message);
        System.out.println("Message sent: " + message);
    }
}
