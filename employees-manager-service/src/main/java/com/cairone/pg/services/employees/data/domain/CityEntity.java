package com.cairone.pg.services.employees.data.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "cities")
public class CityEntity {

    @Id
    @Column(name = "city_id")
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String name;
    
    public CityEntity() {
    }

    public CityEntity(Long id, String name) {
        super();
        this.id = id;
        this.name = name;
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
        CityEntity other = (CityEntity) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public String toString() {
        return "CityEntity [id=" + id + ", name=" + name + "]";
    }
    
}
