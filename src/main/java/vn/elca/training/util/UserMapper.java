package vn.elca.training.util;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import vn.elca.training.model.dto.ACMUserDto;
import vn.elca.training.model.entity.ACMUser;

import java.util.List;

@Mapper(config = MapConfiguration.class, uses = {AddressMapper.class})
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "isActive", source = "active")
    @Mapping(target = "isNotLocked", source = "notLocked")
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "authorities", ignore = true)
//    @Mapping(target = "addressDto", source = "address")
    ACMUserDto ACMUserToACMUserDto(ACMUser entity);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "active", source = "isActive")
    @Mapping(target = "notLocked", source = "isNotLocked")
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "verificationCode", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "purchaseOrders", ignore = true)
    ACMUser ACMUserDtoToACMUser(ACMUserDto dto);

    List<ACMUserDto> listACMUserToListACMUserDto(List<ACMUser> entity);

    List<ACMUser> listACMUserDtoToListACMUser(List<ACMUserDto> dto);
}
