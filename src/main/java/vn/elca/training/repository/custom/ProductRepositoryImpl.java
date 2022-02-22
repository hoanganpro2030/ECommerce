package vn.elca.training.repository.custom;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.lang3.StringUtils;
import vn.elca.training.model.criteria.ProductSearchCriteria;
import vn.elca.training.model.entity.Product;
import vn.elca.training.model.entity.Project;
import vn.elca.training.model.entity.Status;
import vn.elca.training.model.response.ListResponse;
import vn.elca.training.model.entity.QProduct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class ProductRepositoryImpl implements ProductRepositoryCustom{
    @PersistenceContext
    private EntityManager em;

    @Override
    public ListResponse<Product> findPaginationProducts(int position, int pageSize) {
        List<Product> data = new JPAQuery<Product>(em)
                .from(QProduct.product)
                .limit(pageSize)
                .offset((long) pageSize * position)
                .fetch();
        int total = (int) new JPAQuery<Product>(em)
                .from(QProduct.product)
                .fetchCount();

        return new ListResponse<>(position, 1+(total-1)/pageSize, pageSize, data);
    }

    private BooleanExpression buildConditionForProductCriteria(ProductSearchCriteria criteria, BooleanExpression condition) {
        if (criteria == null) {
            return condition;
        }
        if (StringUtils.isNotBlank(criteria.getName())) {
            condition = condition.and(QProduct.product.name.containsIgnoreCase(criteria.getName()));
        }
        if (criteria.getProductType() != null) {
            condition = condition.and(QProduct.product.productType.eq(criteria.getProductType()));
        }
        return condition;
    }

    @Override
    public List<Product> findProductsByIds(List<Long> ids) {
        return new JPAQuery<Product>(em)
                .from(QProduct.product)
                .where(QProduct.product.id.in(ids))
                .fetch();
    }

    @Override
    public ListResponse<Product> searchProductByCriteria(ProductSearchCriteria criteria, Integer page, Integer pageSize) {
        BooleanExpression condition = QProduct.product.name.containsIgnoreCase("");
        condition = buildConditionForProductCriteria(criteria, condition);
        int total = (int) new JPAQuery<Project>(em)
                .from(QProduct.product)
                .where(condition).fetchCount();

        List<Product> data = new JPAQuery<Product>(em)
                .from(QProduct.product)
                .where(condition)
                .limit(pageSize)
                .offset((long) pageSize *page)
                .fetch();
        return new ListResponse<>(page,1+(total-1)/pageSize,pageSize, data);
    }
}
