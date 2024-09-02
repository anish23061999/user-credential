package com.anish.user_credential_service.Repository;

import com.anish.user_credential_service.Entity.Identifier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdentifierRepository extends JpaRepository<Identifier,String> {


}
