package com.cairone.pg.services.employees.data.domain;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "departments")
public class DepartmentEntity {

    @Id
    @Column(name = "department_id")
    @SequenceGenerator(name = "department-seq-generator", sequenceName = "department_seq", allocationSize = 10, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "department-seq-generator")
    private Long id;
    
    @Column(name = "name", nullable = false, length = 50)
    private String name;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", referencedColumnName = "employee_id", nullable = false)
    private EmployeeEntity manager;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "departments_employees",
            joinColumns = @JoinColumn(name = "department_id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id"))
    private Set<EmployeeEntity> employees;
    
    public DepartmentEntity() {
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

    public EmployeeEntity getManager() {
        return manager;
    }

    public void setManager(EmployeeEntity manager) {
        this.manager = manager;
    }

    public Set<EmployeeEntity> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<EmployeeEntity> employees) {
        this.employees = employees;
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
        DepartmentEntity other = (DepartmentEntity) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public String toString() {
        return "DepartmentEntity [id=" + id + ", name=" + name + "]";
    }
    
}
