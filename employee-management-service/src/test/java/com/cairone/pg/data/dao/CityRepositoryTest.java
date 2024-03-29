package com.cairone.pg.data.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import com.cairone.pg.data.domain.CityEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
class CityRepositoryTest {

    @Autowired
    private CityRepository cityRepository;
    
    @Test
    void whenFetchingAll_thenListIsNotEmpty() {
        List<CityEntity> cityEntities = cityRepository.findAll();
        assertThat(cityEntities).isNotEmpty();
    }
    
    @Test
    void givenCityId_whenFindById_thenOk() {
        Optional<CityEntity> cityEntity = cityRepository.findById(1L);
        assertThat(cityEntity).isNotEmpty();
    }
}
