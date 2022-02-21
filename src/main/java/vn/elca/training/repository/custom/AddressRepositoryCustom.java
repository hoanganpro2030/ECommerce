package vn.elca.training.repository.custom;

import vn.elca.training.model.entity.Address;

import java.util.List;

public interface AddressRepositoryCustom {
    List<Address> findAllAddressByUid(Long uid);
}
