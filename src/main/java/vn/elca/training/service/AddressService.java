package vn.elca.training.service;

import vn.elca.training.model.dto.AddressDto;
import vn.elca.training.model.entity.Address;

import java.util.List;
import java.util.Set;

public interface AddressService {
    AddressDto addAddress(AddressDto addressDto, Long uid);
    AddressDto updateAddress(AddressDto addressDto);
    void deleteAddress(Long addressId);
    List<AddressDto> getAddressesFromUserId(Long userId);
    AddressDto getAddress(Long addressId);
}
