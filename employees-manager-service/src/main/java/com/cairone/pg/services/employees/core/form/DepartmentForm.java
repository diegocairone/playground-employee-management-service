package com.cairone.pg.services.employees.core.form;

import java.util.Set;

public interface DepartmentForm {

    public String getName();

    public Long getManagerId();

    public Set<Long> getEmployeeIDs();

}
