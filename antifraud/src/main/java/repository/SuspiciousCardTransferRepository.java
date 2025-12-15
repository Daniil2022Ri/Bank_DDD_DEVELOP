package repository;

import model.SuspiciousCardTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SuspiciousCardTransferRepository extends JpaRepository<SuspiciousCardTransfer, Long>{}
