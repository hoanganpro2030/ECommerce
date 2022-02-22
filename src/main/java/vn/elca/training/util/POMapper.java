package vn.elca.training.util;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import vn.elca.training.model.dto.PurchaseOrderDto;
import vn.elca.training.model.entity.PurchaseOrder;

import java.util.List;

@Mapper(config = MapConfiguration.class)
public interface POMapper {
    POMapper INSTANCE = Mappers.getMapper(POMapper.class);

    @Mapping(target = "acmUser", ignore = true)
    PurchaseOrder purchaseOrderDtoTopurchaseOrder(PurchaseOrderDto dto);
    PurchaseOrderDto purchaseOrderTopurchaseOrderDto(PurchaseOrder entity);

    List<PurchaseOrder> purchaseOrderDtoListTopurchaseOrderList(List<PurchaseOrderDto> dto);
    List<PurchaseOrderDto> purchaseOrderListTopurchaseOrderDtoList(List<PurchaseOrder> entity);
}
