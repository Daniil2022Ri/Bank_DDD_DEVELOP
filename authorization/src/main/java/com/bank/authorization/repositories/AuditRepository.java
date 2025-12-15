package com.bank.authorization.repositories;

import com.bank.authorization.entities.Audit;
import lombok.Generated;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Generated
public interface AuditRepository extends JpaRepository<Audit, Long> {
}
