package vn.elca.training.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.elca.training.model.entity.ACMUser;

@Repository
public interface ACMUserRepository extends JpaRepository<ACMUser, Long> {
    ACMUser findACMUserByUsername(String username);
    ACMUser findACMUserByEmail(String username);
}
