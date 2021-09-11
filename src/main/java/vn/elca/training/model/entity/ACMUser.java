package vn.elca.training.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ACM_USER")
public class ACMUser {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = true, unique = true)
    private String username;

    private String password;
}
