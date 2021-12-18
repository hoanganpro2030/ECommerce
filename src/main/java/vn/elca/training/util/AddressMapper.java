package vn.elca.training.util;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import vn.elca.training.model.dto.AddressDto;
import vn.elca.training.model.entity.Address;

import java.util.List;
import java.util.Set;

@Mapper(config = MapConfiguration.class, uses = {UserMapper.class})
public interface AddressMapper {
    AddressMapper INSTANCE = Mappers.getMapper(AddressMapper.class);

    @Mapping(target = "transientHashCodeLeaked", ignore = true)
    @Mapping(target = "rawClassName", ignore = true)
    @Mapping(target = "acmUser", source = "acmUserDto")
    Address toAddress(AddressDto dto);

    @Mapping(target = "acmUserDto", ignore = true)
    AddressDto toAddressDto(Address entity);

    Set<Address> toAddressSet(Set<AddressDto> dto);
    Set<AddressDto> toAddressDtoSet(Set<Address> entity);

    List<Address> toAddressList(List<AddressDto> dto);
    List<AddressDto> toAddressDtoList(List<Address> entity);
}
