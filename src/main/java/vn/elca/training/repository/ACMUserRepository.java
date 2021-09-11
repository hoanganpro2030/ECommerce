package vn.elca.training.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.elca.training.model.entity.ACMUser;

public interface ACMUserRepository extends JpaRepository<ACMUser, Long> {
    ACMUser findByUsername(String username);
}
