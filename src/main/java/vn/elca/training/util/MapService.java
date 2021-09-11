package vn.elca.training.util;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import vn.elca.training.model.dto.CategoryDto;
import vn.elca.training.model.dto.ProductDto;
import vn.elca.training.model.dto.PurchaseOrderDto;
import vn.elca.training.model.entity.Category;
import vn.elca.training.model.entity.Product;
import vn.elca.training.model.entity.PurchaseOrder;

import java.util.List;

@Mapper
public interface MapService {
    MapService INSTANCE = Mappers.getMapper( MapService.class );

    Category categoryDtoToCategory(CategoryDto categoryDto);
    CategoryDto categoryToCategoryDto(Category entity);
    ProductDto productToProductDto(Product entity);
    List<ProductDto> listProductToListProductDto(List<Product> entities);
    Product productDtoToProduct(ProductDto dto);
    List<Product> listProductDtoToListProduct(List<ProductDto> dtoes);
    PurchaseOrder purchaseOrderDtoTopurchaseOrder(PurchaseOrderDto dto);
    PurchaseOrderDto purchaseOrderTopurchaseOrderDto(PurchaseOrder entity);
}
