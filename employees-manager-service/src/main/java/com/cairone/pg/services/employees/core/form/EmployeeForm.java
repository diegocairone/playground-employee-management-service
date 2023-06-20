package com.cairone.pg.services.employees.core.form;

import com.cairone.pg.services.employees.data.enums.EmployeeTag;

import java.time.LocalDate;
import java.util.Set;

public interface EmployeeForm {

    String getNames();

    LocalDate getBirthDate();

    Long getCityId();

    Long getBankAccountId();

    Set<EmployeeTag> getTags();

}
