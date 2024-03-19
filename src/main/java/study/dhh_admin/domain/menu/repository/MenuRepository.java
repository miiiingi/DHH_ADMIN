package study.dhh_admin.domain.menu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.dhh_admin.domain.menu.entity.Menu;
import study.dhh_admin.domain.store.entity.Store;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByStore(Store store);
}
