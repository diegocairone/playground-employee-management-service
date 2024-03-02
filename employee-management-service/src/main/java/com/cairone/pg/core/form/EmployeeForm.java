package com.cairone.pg.core.form;

import com.cairone.pg.base.enums.EmployeeTag;

import java.time.LocalDate;
import java.util.Set;

public interface EmployeeForm {

    String getNames();

    LocalDate getBirthDate();

    Long getCityId();

    Long getBankAccountId();

    Set<EmployeeTag> getTags();

}
