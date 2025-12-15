package repository;

import model.SuspiciousAccountTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SuspiciousAccountTransferRepository extends JpaRepository<SuspiciousAccountTransfer, Long> {}
