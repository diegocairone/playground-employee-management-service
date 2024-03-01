package com.cairone.pg.data.domain.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class EmployeeStatusLogPkEntity implements Serializable {

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "log_id", nullable = false)
    private Long logId;

    public EmployeeStatusLogPkEntity() {
    }

    public EmployeeStatusLogPkEntity(Long employeeId, Long logId) {
        this.employeeId = employeeId;
        this.logId = logId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EmployeeStatusLogPkEntity that = (EmployeeStatusLogPkEntity) o;

        if (!employeeId.equals(that.employeeId)) return false;
        return logId.equals(that.logId);
    }

    @Override
    public int hashCode() {
        int result = employeeId.hashCode();
        result = 31 * result + logId.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "EmployeeStatusLogPkEntity{" +
                "employeeId=" + employeeId +
                ", logId=" + logId +
                '}';
    }
}
