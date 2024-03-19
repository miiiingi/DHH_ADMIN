package study.dhh_admin.domain.owner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.dhh_admin.domain.owner.entity.Owner;

import java.util.Optional;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
    Optional<Owner> findByEmail(String username);
}

