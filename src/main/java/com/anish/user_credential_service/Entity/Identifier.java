package com.anish.user_credential_service.Entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Entity
@Table(name = "IDENTIFIER")
public class Identifier {

    @Id
    @Column(name = "AUTH_ID")
    private String authId;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "ID_TYPE")
    private String idType;

    @Column(name = "ID_VALUE")
    private String idValue;

    @Column(name = "PASSWORD")
    private String password;
}
