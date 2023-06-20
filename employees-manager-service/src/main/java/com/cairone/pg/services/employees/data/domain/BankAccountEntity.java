package com.cairone.pg.services.employees.data.domain;

import com.cairone.pg.services.employees.data.enums.BankAccountType;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "bank_accounts")
public class BankAccountEntity {

    @Id
    @Column(name = "account_id")
    private Long id;
    
    @Column(name = "account_number", nullable = false, unique = true, length = 10)
    private String accountNumber;

    @Column(name = "account_type", nullable = false)
    private BankAccountType accountType;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id", referencedColumnName = "bank_id", nullable = false)
    private BankEntity bank;

    @OneToOne(optional = true, mappedBy = "bankAccount")
    private EmployeeEntity employee;
    
    public BankAccountEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BankAccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(BankAccountType accountType) {
        this.accountType = accountType;
    }

    public BankEntity getBank() {
        return bank;
    }

    public void setBank(BankEntity bank) {
        this.bank = bank;
    }

    public EmployeeEntity getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeEntity employee) {
        this.employee = employee;
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
        BankAccountEntity other = (BankAccountEntity) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public String toString() {
        return "BankAccountEntity [id=" + id + ", accountNumber=" + accountNumber + "]";
    }
    
}
