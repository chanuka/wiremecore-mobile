package com.cba.core.wirememobile.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "mcc", uniqueConstraints = {@UniqueConstraint(name = "mcc_un", columnNames = {"code"})}
)
@EntityListeners(AuditingEntityListener.class) // enable entity level auditing for create,modified attributes
@Data
@NoArgsConstructor
public class Mcc implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", length = 19)
    @CreatedDate
    private Date createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", length = 19)
    @LastModifiedDate
    private Date updatedAt;
    @Column(name = "code", nullable = false, length = 6, unique = true)
    private String code;
    @Column(name = "description", length = 100)
    private String description;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "mcc")
    private Set<Merchant> merchants = new HashSet<Merchant>(0);

    public Mcc(Integer id) {
        this.id = id;
    }
}
