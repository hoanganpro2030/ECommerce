package vn.elca.training.util;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import vn.elca.training.model.dto.ProductDto;
import vn.elca.training.model.entity.Product;

@Mapper
public interface MapService {
    MapService INSTANCE = Mappers.getMapper( MapService.class );

    ProductDto productToProductDto(Product entity);
    Product productDtoToProduct(ProductDto entity);
}
