package com.cairone.pg.services.employees.core.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class EmployeeModel {

    private final Long id;
    private final String names;
    private final LocalDate birthDate;
    private final Long yearsOld;
    private final CityModel city;
    
    private EmployeeModel(Long id, String names, LocalDate birthDate, CityModel city) {
        super();
        this.id = id;
        this.names = names;
        this.birthDate = birthDate;
        this.yearsOld = ChronoUnit.YEARS.between(birthDate, LocalDate.now());
        this.city = city;
    }

    public Long getId() {
        return id;
    }

    public String getNames() {
        return names;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public Long getYearsOld() {
        return yearsOld;
    }

    public CityModel getCity() {
        return city;
    }

    @Override
    public String toString() {
        return "EmployeeModel [id=" + id + ", names=" + names + "]";
    }
    
    public static class EmployeeModelBuilder {

        private Long id;
        private String names;
        private LocalDate birthDate;
        private CityModel city;
        
        private EmployeeModelBuilder() {
        }
        
        public static EmployeeModelBuilder builder() {
            return new EmployeeModelBuilder();
        }

        public EmployeeModelBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public EmployeeModelBuilder setNames(String names) {
            this.names = names;
            return this;
        }

        public EmployeeModelBuilder setBirthDate(LocalDate birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        public EmployeeModelBuilder setCity(CityModel city) {
            this.city = city;
            return this;
        }
        
        public EmployeeModel build() {
            return new EmployeeModel(id, names, birthDate, city);
        }
    }
}
