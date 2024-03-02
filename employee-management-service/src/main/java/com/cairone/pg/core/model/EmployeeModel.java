package com.cairone.pg.core.model;

import com.cairone.pg.base.enums.EmployeeTag;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.UUID;

@JsonInclude(Include.NON_NULL)
public class EmployeeModel {

    private final Long id;
    private UUID globalId;
    private final String names;
    private final LocalDate birthDate;
    private final Long yearsOld;
    private final CityModel city;
    private final Set<EmployeeTag> tags;
    
    private EmployeeModel(Long id, UUID globalId, String names, LocalDate birthDate, CityModel city, Set<EmployeeTag> tags) {
        super();
        this.id = id;
        this.globalId = globalId;
        this.names = names;
        this.birthDate = birthDate;
        this.yearsOld = ChronoUnit.YEARS.between(birthDate, LocalDate.now());
        this.city = city;
        this.tags = tags;
    }

    public Long getId() {
        return id;
    }

    public UUID getGlobalId() {
        return globalId;
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

    public Set<EmployeeTag> getTags() {
        return tags;
    }

    @Override
    public String toString() {
        return "EmployeeModel [id=" + id + ", names=" + names + "]";
    }
    
    public static class EmployeeModelBuilder {

        private Long id;
        private UUID globalId;
        private String names;
        private LocalDate birthDate;
        private CityModel city;
        private Set<EmployeeTag> tags;
        
        private EmployeeModelBuilder() {
        }
        
        public static EmployeeModelBuilder builder() {
            return new EmployeeModelBuilder();
        }

        public EmployeeModelBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public EmployeeModelBuilder setGlobalId(UUID globalId) {
            this.globalId = globalId;
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

        public EmployeeModelBuilder setTags(Set<EmployeeTag> tags) {
            this.tags = tags;
            return this;
        }
        
        public EmployeeModel build() {
            return new EmployeeModel(id, globalId, names, birthDate, city, tags);
        }
    }
}
