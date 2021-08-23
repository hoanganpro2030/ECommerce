package vn.elca.training.repository.custom;

import com.querydsl.jpa.impl.JPAQuery;
import vn.elca.training.model.entity.Product;
import vn.elca.training.model.entity.Project;
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
}
