package vn.elca.training.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.elca.training.model.entity.Address;
import vn.elca.training.repository.custom.AddressRepositoryCustom;

import java.util.List;
import java.util.Set;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long>, AddressRepositoryCustom {
}
