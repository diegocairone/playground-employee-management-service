package com.cairone.pg.services.employees.data.domain;

import com.cairone.pg.services.employees.data.domain.id.EmployeeStatusLogPkEntity;
import com.cairone.pg.services.employees.data.enums.EmployeeStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "employee_status_log")
public class EmployeeStatusLogEntity {

    @EmbeddedId
    private EmployeeStatusLogPkEntity id;
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false, insertable = false, updatable = false)
    private EmployeeEntity employee;
    @Column(name = "log_id", nullable = false, insertable = false, updatable = false)
    private Long logId;
    @Column(name = "log_datetime", nullable = false)
    private LocalDateTime dateTime;
    @Column(name = "previous_status", nullable = false)
    private EmployeeStatus previousStatus;
    @Column(name = "current_status", nullable = false)
    private EmployeeStatus status;

    public EmployeeStatusLogEntity() {
        this.id = new EmployeeStatusLogPkEntity();
    }

    public EmployeeStatusLogEntity(EmployeeEntity employee, Long logId) {
        this.id = new EmployeeStatusLogPkEntity(employee.getId(), logId);
    }

    public EmployeeStatusLogPkEntity getId() {
        return id;
    }

    public EmployeeEntity getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeEntity employee) {
        this.employee = employee;
        this.id.setEmployeeId(employee.getId());
    }

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
        this.id.setLogId(logId);
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public EmployeeStatus getPreviousStatus() {
        return previousStatus;
    }

    public void setPreviousStatus(EmployeeStatus previousStatus) {
        this.previousStatus = previousStatus;
    }

    public EmployeeStatus getStatus() {
        return status;
    }

    public void setStatus(EmployeeStatus status) {
        this.status = status;
    }
}
