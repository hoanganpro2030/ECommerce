package vn.elca.training.util;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import vn.elca.training.model.dto.ProductDto;
import vn.elca.training.model.entity.Product;

import java.util.List;

@Mapper
public interface MapService {
    MapService INSTANCE = Mappers.getMapper( MapService.class );

    ProductDto productToProductDto(Product entity);
    List<ProductDto> listProductToListProductDto(List<Product> entity);
    Product productDtoToProduct(ProductDto dto);
    List<Product> listProductDtoToListProduct(List<ProductDto> dto);
}
