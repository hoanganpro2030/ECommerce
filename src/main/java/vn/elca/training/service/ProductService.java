package vn.elca.training.service;

import vn.elca.training.model.criteria.ProductSearchCriteria;
import vn.elca.training.model.dto.ProductDto;
import vn.elca.training.model.exception.EntityNotFoundException;
import vn.elca.training.model.response.ListResponse;

import java.util.List;

public interface ProductService {
    List<ProductDto> getAllProduct();
    ListResponse<ProductDto> getAllProductPaginate(Integer page, Integer size);
    ListResponse<ProductDto> searchProductByCriteria(ProductSearchCriteria criteria, Integer page, Integer pageSize);
    ProductDto getProductById(Long id) throws EntityNotFoundException;
    List<ProductDto> getProductsByIds(List<Long> ids);
    ProductDto createProduct(ProductDto product);
    ProductDto updateProduct(ProductDto product);
    boolean deleteProduct(Long id) throws EntityNotFoundException;
    boolean deleteMultiProduct(List<Long> ids);
}
