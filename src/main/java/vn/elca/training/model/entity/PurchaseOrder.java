package vn.elca.training.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.elca.training.model.enumeration.StatusOrder;
import vn.elca.training.model.enumeration.TypeProductCode;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "PURCHASE_ORDER")
public class PurchaseOrder {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 50)
    private String fullName;

    @Column(nullable = false, length = 10)
    private String phoneNumber;

    @Column(nullable = false)
    private String addressDetail;

    @Column(nullable = false, length = 50)
    private String province;

    @Column(nullable = false, length = 50)
    private String district;

    @Column(nullable = false, length = 50)
    private String ward;

    @Column(nullable = false)
    private String note;

    @Column(nullable = false)
    private Integer totalPrice;

    @Column(nullable = false, length = Integer.MAX_VALUE)
    private String detail;

    @Column(nullable = false)
    private LocalDate orderDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusOrder status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private ACMUser acmUser;

    @Column(nullable = false)
    @Version
    private Integer version;
}
