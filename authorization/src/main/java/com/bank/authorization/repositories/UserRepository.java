package com.bank.authorization.repositories;

import com.bank.authorization.entities.User;
import lombok.Generated;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Generated
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByProfileId(long profileId);

    boolean existsByProfileId(long profileId);

    boolean existsByProfileIdAndIdNot(long profileId, long id);
}
