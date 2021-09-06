package vn.elca.training.repository.custom;

import vn.elca.training.model.criteria.ProductSearchCriteria;
import vn.elca.training.model.entity.Product;
import vn.elca.training.model.response.ListResponse;

public interface ProductRepositoryCustom {
    ListResponse<Product> findPaginationProducts(int position, int pageSize);
    ListResponse<Product> searchProductByCriteria(ProductSearchCriteria criteria, Integer page, Integer pageSize);
}
