package study.dhh_admin.domain.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.dhh_admin.domain.owner.entity.Owner;
import study.dhh_admin.domain.store.entity.Store;

public interface StoreRepository extends JpaRepository<Store, Long> {
    Store findByOwnerId(Long id);

    void deleteByOwner(Owner owner);
}
