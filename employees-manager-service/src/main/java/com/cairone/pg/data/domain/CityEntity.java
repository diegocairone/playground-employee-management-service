package com.cairone.pg.data.domain;

import java.util.Objects;

import javax.persistence.*;

@Entity
@Table(name = "cities")
public class CityEntity {

    @Id
    @Column(name = "city_id")
    @SequenceGenerator(name = "city-seq-generator", sequenceName = "city_seq", allocationSize = 10, initialValue = 20)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "city-seq-generator")
    private Long id;
    
    @Column(nullable = false, unique = true, length = 50)
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
