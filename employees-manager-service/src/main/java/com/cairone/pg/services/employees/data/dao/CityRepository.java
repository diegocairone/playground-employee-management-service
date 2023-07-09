package com.cairone.pg.services.employees.data.dao;

import com.cairone.pg.services.employees.data.domain.CityEntity;
import com.cairone.pg.services.employees.data.AppDataRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CityRepository extends AppDataRepository<CityEntity, Long> {
}
