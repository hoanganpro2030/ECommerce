package vn.elca.training.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.elca.training.model.dto.ProductDto;
import vn.elca.training.model.entity.Product;
import vn.elca.training.model.exception.EntityNotFoundException;
import vn.elca.training.repository.ProductRepository;
import vn.elca.training.service.ProductService;
import vn.elca.training.util.MapService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Throwable.class)
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<ProductDto> getAllProduct() {
        // TODO do not use find all to avoid n+1 select
        List<Product> productList = productRepository.findAll();
        return productList.stream().map(MapService.INSTANCE::productToProductDto).collect(Collectors.toList());
    }

    @Override
    public ProductDto getProductById(Long id) throws EntityNotFoundException {
        Product product = productRepository.findOne(id);
        if (product == null) {
            throw new EntityNotFoundException(id.toString());
        }
        return MapService.INSTANCE.productToProductDto(product);
    }

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        Product product = MapService.INSTANCE.productDtoToProduct(productDto);
        Product pSaved = productRepository.save(product);
        return MapService.INSTANCE.productToProductDto(pSaved);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        if (productDto == null) {
            return null;
        }
        Product productOld = productRepository.findOne(productDto.getId());
        if (productOld == null) {
            // TODO
            return null;
        }
        Product productNew = productRepository.save(MapService.INSTANCE.productDtoToProduct(productDto));
        return MapService.INSTANCE.productToProductDto(productNew);
    }

    @Override
    public boolean deleteProduct(Long id) throws EntityNotFoundException {
        Product product = productRepository.findOne(id);
        if (product == null) {
            throw new EntityNotFoundException(id.toString());
        }
        productRepository.delete(id);
        return true;
    }

    @Override
    public boolean deleteMultiProduct(List<Long> ids) {
        return false;
    }
}
