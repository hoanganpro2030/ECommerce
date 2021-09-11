package vn.elca.training.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;
import vn.elca.training.model.entity.Product;
import vn.elca.training.repository.custom.ProductRepositoryCustom;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, QueryDslPredicateExecutor<Product>, ProductRepositoryCustom {

}
