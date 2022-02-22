package vn.elca.training.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.elca.training.model.entity.ACMUser;
import vn.elca.training.repository.custom.ACMUserRepositoryCustom;

@Repository
public interface ACMUserRepository extends JpaRepository<ACMUser, Long>, ACMUserRepositoryCustom {
    ACMUser findACMUserByUsername(String username);
    ACMUser findACMUserByEmail(String username);
}
