package com.cairone.pg.core.form;

import java.util.Set;

public interface DepartmentForm {

    public String getName();

    public Long getManagerId();

    public Set<Long> getEmployeeIDs();

}
