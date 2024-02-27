package com.cairone.pg.services.employees.core.service;

import com.cairone.pg.services.employees.core.exception.EntityIntegrityException;
import com.cairone.pg.services.employees.core.exception.EntityNotFoundException;
import com.cairone.pg.services.employees.data.dao.CityRepository;
import com.cairone.pg.services.employees.data.domain.CityEntity;
import com.cairone.pg.services.employees.core.form.CityForm;
import com.cairone.pg.services.employees.core.mapper.CityMapper;
import com.cairone.pg.services.employees.core.model.CityModel;
import com.cairone.pg.services.employees.data.domain.QCityEntity;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class CityService {

    private final CityRepository cityRepository;
    
    private final CityMapper cityMapper;

    @Transactional(readOnly = true)
    public Optional<CityModel> findById(Long id) {
        return cityRepository.findById(id).map(cityMapper::convert);
    }

    @Transactional(readOnly = true)
    public Page<CityModel> findAll(Pageable pageable) {
        return cityRepository.findAll(pageable).map(cityMapper::convert);
    }

    @Transactional
    public CityModel create(CityForm form) {

        // check business rules
        String name = form.getName().trim().toUpperCase();
        verifyDuplicatedName(name, q -> q.name.eq(name));

        CityEntity cityEntity = new CityEntity();
        cityEntity.setName(form.getName().trim().toUpperCase());
        return cityMapper.convert(cityRepository.save(cityEntity));
    }

    @Transactional
    public CityModel update(Long id, CityForm form) {

        CityEntity cityEntity = cityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Requested resource to be updated does not exist in the database",
                        "City with ID %s could not be updated", id));

        // check business rules
        String name = form.getName().trim().toUpperCase();
        verifyDuplicatedName(name, q -> q.name.eq(name).and(q.id.ne(id)));

        cityEntity.setName(form.getName().trim().toUpperCase());
        return cityMapper.convert(cityRepository.save(cityEntity));
    }

    @Transactional
    public void delete(Long id) {
        CityEntity cityEntity = cityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Requested resource to be deleted does not exist in the database",
                        "City with ID %s could not be deleted", id));
        cityRepository.delete(cityEntity);
    }

    private void verifyDuplicatedName(String name, Function<QCityEntity, BooleanExpression> predicate) {
        boolean existsByName = exists(predicate);
        if (existsByName) {
            throw new EntityIntegrityException("name", "City with name %s already exists", name);
        }
    }

    private Boolean exists(Function<QCityEntity, BooleanExpression> predicate) {
        QCityEntity qCityEntity = QCityEntity.cityEntity;
        BooleanExpression booleanExpression = predicate.apply(qCityEntity);
        return cityRepository.exists(booleanExpression);
    }
}
