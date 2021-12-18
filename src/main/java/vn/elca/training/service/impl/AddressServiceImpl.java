package vn.elca.training.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.elca.training.model.dto.AddressDto;
import vn.elca.training.model.entity.ACMUser;
import vn.elca.training.model.entity.Address;
import vn.elca.training.repository.ACMUserRepository;
import vn.elca.training.repository.AddressRepository;
import vn.elca.training.service.AddressService;
import vn.elca.training.util.AddressMapper;

import java.util.List;
import java.util.Set;

@Service
@Transactional(rollbackFor = Throwable.class)
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ACMUserRepository acmUserRepository;

    @Override
    public AddressDto addAddress(AddressDto addressDto, Long uid) {
        ACMUser acmUser = acmUserRepository.findOne(uid);
        Address address = AddressMapper.INSTANCE.toAddress(addressDto);
        address.setAcmUser(acmUser);
        Address addressSaved = addressRepository.save(address);
        return AddressMapper.INSTANCE.toAddressDto(addressSaved);
    }

    @Override
    public AddressDto updateAddress(AddressDto addressDto) {
        if (addressDto == null) {
            return null;
        }
        Address addressOld = addressRepository.findOne(addressDto.getId());
        if (addressOld == null) {
            // TODO
            return null;
        }
        if (addressDto.getIsDefault() != null) {
            addressOld.setIsDefault(addressDto.getIsDefault());
        }
        if (addressDto.getDistrict() != null) {
            addressOld.setDistrict(addressDto.getDistrict());
        }
        if (addressDto.getNote() != null) {
            addressOld.setNote(addressDto.getNote());
        }
        if (addressDto.getPhoneNumber() != null) {
            addressOld.setPhoneNumber(addressDto.getPhoneNumber());
        }
        if (addressDto.getProvince() != null) {
            addressOld.setProvince(addressDto.getProvince());
        }
        if (addressDto.getStreet() != null) {
            addressOld.setStreet(addressDto.getStreet());
        }
        if (addressDto.getTitle() != null) {
            addressOld.setTitle(addressDto.getTitle());
        }
        if (addressDto.getWard() != null) {
            addressOld.setWard(addressDto.getWard());
        }
        Address addressNew = addressRepository.save(addressOld);
        return AddressMapper.INSTANCE.toAddressDto(addressNew);
    }

    @Override
    public void deleteAddress(Long addressId) {
        addressRepository.delete(addressId);
    }

    @Override
    public List<AddressDto> getAddressesFromUserId(Long userId) {
        return AddressMapper.INSTANCE.toAddressDtoList(addressRepository.findAllAddressByUid(userId));
    }

    @Override
    public AddressDto getAddress(Long addressId) {
        return null;
    }
}
