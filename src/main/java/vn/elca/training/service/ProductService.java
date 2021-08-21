package vn.elca.training.service;

import vn.elca.training.model.dto.ProductDto;
import vn.elca.training.model.entity.Product;
import vn.elca.training.model.exception.EntityNotFoundException;
import vn.elca.training.model.exception.ProjectNotFoundException;

import java.util.List;

public interface ProductService {
    List<ProductDto> getAllProduct();
    ProductDto getProductById(Long id) throws EntityNotFoundException;
    ProductDto createProduct(ProductDto product);
    ProductDto updateProduct(ProductDto product);
    boolean deleteProduct(Long id) throws EntityNotFoundException;
    boolean deleteMultiProduct(List<Long> ids);
}
