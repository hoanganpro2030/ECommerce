package vn.elca.training.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
public class AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    protected Long id;

//    @Column(name = "USR_LOG_I", updatable = false)
//    private String createdBy;

    @Column(name = "CREATION_DATE", updatable = false)
    private LocalDateTime creationDate;

//    @Column(name = "USR_LOG_U")
//    private String lastModifiedBy;

    @Column(name = "UPDATE_DATE")
    private LocalDateTime updateDate;

    @Version
    @Column(name = "VERSION")
    private Integer version;

    @PrePersist
    public void beforeSave() {
//        setCreatedBy(UserUtils.getUserName());
        setCreationDate(LocalDateTime.now());
//        setLastModifiedBy(UserUtils.getUserName());
        setUpdateDate(LocalDateTime.now());
    }

    @PreUpdate
    public void beforeUpdate() {
//        setLastModifiedBy(UserUtils.getUserName());
        setUpdateDate(LocalDateTime.now());
    }

    /**
     * Extracts the class name of the closest non-synthetic super class (to be able to compare across proxy entities
     * from QueryDSL, hibernate, etc...).
     */
    protected static String getRawClassName(Class<?> baseClass) {
        Class<?> clazz = baseClass;
        while (isSyntheticClass(clazz)) {
            clazz = clazz.getSuperclass();
            if (clazz == null || clazz.equals(Object.class) || clazz.equals(AbstractEntity.class)) {
                throw new IllegalArgumentException(String.format("Can't retrieve non-synthetic class from class %s", baseClass));
            }
        }

        return clazz.getName();
    }

    protected static boolean isSyntheticClass(Class<?> clazz) {
        return clazz.isSynthetic()
                || org.hibernate.proxy.HibernateProxy.class.isAssignableFrom(clazz)
                || java.lang.reflect.Proxy.class.isAssignableFrom(clazz)
                || org.springframework.cglib.proxy.Proxy.class.isAssignableFrom(clazz);
    }

    @Transient
    private boolean transientHashCodeLeaked = false;

    @Transient
    private String rawClassName = getRawClassName(getClass());

    public boolean isPersisted() {
        return this.id != null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (obj instanceof AbstractEntity) {
            final AbstractEntity other = (AbstractEntity) obj;
            if (isPersisted() && other.isPersisted()) { // both entities are not new

                // Because entities are currently used in clientside, we cannot use HibernateProxyHelper here >
                // we cannot compare class for sure they are the same class, just compare ID.
                return getId().equals(other.getId()) && rawClassName.equals(other.rawClassName);
            }
            // if one of entity is new (transient), they are considered not equal.
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        if (!isPersisted()) { // is new or is in transient state.
            transientHashCodeLeaked = true;
            return -super.hashCode();
        }

        // because hashcode has just been asked for when the object is in transient state
        // at that time, super.hashCode(); is returned. Now for consistency, we return the
        // same value.
        if (transientHashCodeLeaked) {
            return -super.hashCode();
        }
        return getId().hashCode();
        // The above mechanism obey the rule: if 2 objects are equal, their hashcode must be same.
    }
}
