package vn.elca.training.repository.custom;

import vn.elca.training.model.criteria.ProductSearchCriteria;
import vn.elca.training.model.entity.Product;
import vn.elca.training.model.response.ListResponse;

import java.util.List;

public interface ProductRepositoryCustom {
    ListResponse<Product> findPaginationProducts(int position, int pageSize);
    List<Product> findProductsByIds(List<Long> ids);
    ListResponse<Product> searchProductByCriteria(ProductSearchCriteria criteria, Integer page, Integer pageSize);
}
