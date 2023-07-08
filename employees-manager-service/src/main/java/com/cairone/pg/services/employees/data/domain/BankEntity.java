package com.cairone.pg.services.employees.data.domain;

import com.cairone.pg.services.employees.App;

import java.util.Objects;

import javax.persistence.*;

@Entity
@Table(name = "banks")
public class BankEntity {

    @Id
    @Column(name = "bank_id")
    @SequenceGenerator(name = "bank-seq-generator", sequenceName = "bank_seq", allocationSize = 10, initialValue = 20)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bank-seq-generator")
    private Long id;
    
    @Column(name = "name", nullable = false, length = 30, unique = true)
    private String name;
    
    public BankEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BankEntity other = (BankEntity) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public String toString() {
        return "BankEntity [id=" + id + ", name=" + name + "]";
    }
    
}
