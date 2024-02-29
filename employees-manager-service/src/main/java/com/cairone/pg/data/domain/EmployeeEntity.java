package com.cairone.pg.data.domain;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

import javax.persistence.*;

import com.cairone.pg.base.enums.EmployeeStatus;
import com.cairone.pg.base.enums.EmployeeTag;

@Entity
@Table(name = "employees")
public class EmployeeEntity {
    
    @Id
    @Column(name = "employee_id")
    @SequenceGenerator(name = "employee-seq-generator", sequenceName = "employee_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employee-seq-generator")
    private Long id;

    @Column(name = "names", nullable = false, length = 50)
    private String names;
    
    @Column(name = "birthdate", nullable = false)
    private LocalDate birthDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", referencedColumnName = "city_id", nullable = false)
    private CityEntity city;

    @Column(name = "status", nullable = false)
    private EmployeeStatus status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_account_id", referencedColumnName = "account_id", nullable = true, unique = true)
    private BankAccountEntity bankAccount;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "employees")
    private Set<DepartmentEntity> departments;

    // Note: Using ElementCollection instead of ManyToMany to avoid creating a new JPA entity for tags
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "employees_tags", joinColumns = @JoinColumn(name = "employee_id"))
    @Column(name = "tag_id")
    private Set<EmployeeTag> tags;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public CityEntity getCity() {
        return city;
    }

    public void setCity(CityEntity city) {
        this.city = city;
    }

    public EmployeeStatus getStatus() {
        return status;
    }

    public void setStatus(EmployeeStatus status) {
        this.status = status;
    }

    public BankAccountEntity getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccountEntity bankAccount) {
        this.bankAccount = bankAccount;
    }

    public Set<DepartmentEntity> getDepartments() {
        return departments;
    }

    public void setDepartments(Set<DepartmentEntity> departments) {
        this.departments = departments;
    }

    public Set<EmployeeTag> getTags() {
        return tags;
    }

    public void setTags(Set<EmployeeTag> tags) {
        this.tags = tags;
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
        EmployeeEntity other = (EmployeeEntity) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public String toString() {
        return "EmployeeEntity [id=" + id + ", names=" + names + "]";
    }
}
