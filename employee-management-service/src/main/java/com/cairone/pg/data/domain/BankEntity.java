package com.cairone.pg.data.domain;

import java.util.Objects;

import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;


@Entity
@Table(name = "banks")
public class BankEntity {

    @Id
    @Column(name = "bank_id")
    @SequenceGenerator(name = "bank-seq-generator", sequenceName = "bank_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bank-seq-generator")
    private Long id;
    
    @Column(name = "name", nullable = false, length = 30, unique = true)
    private String name;

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
