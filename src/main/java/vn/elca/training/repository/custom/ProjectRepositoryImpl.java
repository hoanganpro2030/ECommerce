package vn.elca.training.repository.custom;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import vn.elca.training.model.entity.Project;
import vn.elca.training.model.entity.QProject;
import vn.elca.training.model.entity.Status;
import vn.elca.training.model.response.ListResponse;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class ProjectRepositoryImpl implements ProjectRepositoryCustom {
    @PersistenceContext
    private EntityManager em;

    @Override
    public ListResponse<Project> findAllPagination(int position, int pageSize) {
        List<Project> data = new JPAQuery<Project>(em)
                                        .from(QProject.project)
                                        .limit(pageSize)
                                        .offset(pageSize*position)
                                        .fetch();
        int total = (int) new JPAQuery<Project>(em)
                .from(QProject.project)
                .fetchCount();

        return new ListResponse<>(position, 1+(total-1)/5,5, data);
    }

    @Override
    public Project findOneProject(Long id) {
        return new JPAQuery<Project>(em)
                .from(QProject.project)
                .where(QProject.project.id.eq(id))
                .leftJoin(QProject.project.group)
                .fetchJoin()
                .leftJoin(QProject.project.employees)
                .fetchJoin()
                .fetchOne();
    }

    @Override
    public ListResponse<Project> searchProject(String searchText, Status status, int page) {
        BooleanExpression conditions = QProject.project.name.containsIgnoreCase(searchText)
                .or(QProject.project.customer.containsIgnoreCase(searchText))
                .or(QProject.project.projectNumber.like(searchText));

        if (status != null) {
            conditions = conditions.and(QProject.project.status.eq(status));
        }
        int total =(int) new JPAQuery<Project>(em)
                .from(QProject.project)
                .where(conditions).fetchCount();



        List<Project> data =  new JPAQuery<Project>(em)
                .from(QProject.project)
                .where(conditions)
                .orderBy(QProject.project.projectNumber.asc())
                .limit(5)
                .offset(5*page)
                .fetch();
        return new ListResponse<>(page,1+(total-1)/5,5, data);
    }
}
