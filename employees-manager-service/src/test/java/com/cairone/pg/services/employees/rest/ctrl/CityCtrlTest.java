package com.cairone.pg.services.employees.rest.ctrl;

import com.cairone.pg.services.employees.core.mapper.CityMapper;
import com.cairone.pg.services.employees.core.model.CityModel;
import com.cairone.pg.services.employees.core.service.CityService;
import com.cairone.pg.services.employees.data.dao.CityRepository;
import com.cairone.pg.services.employees.data.domain.CityEntity;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;
import java.util.Random;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@ActiveProfiles("test")
public class CityCtrlTest {

    private URI baseUri;

    private CityRepository cityRepository;

    @Autowired
    public CityCtrlTest(CityRepository cityRepository, @LocalServerPort int port) {
        this.cityRepository = cityRepository;
        this.baseUri = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(port)
                .path("/api/cities")
                .build()
                .toUri();

    }

    @Test
    public void givenCityId_whenFindById_thenHttpOk() throws Exception {

        CityEntity expected = cityRepository.findById(1L).get();
        CityMapper cityMapper = new CityMapper();
        CityService cityService = new CityService(cityRepository, cityMapper);

        MockMvc mvc = MockMvcBuilders.standaloneSetup(new CityCtrl(cityService)).build();

        URI uri = UriComponentsBuilder.fromUri(baseUri)
                .path("/" + expected.getId())
                .build()
                .toUri();

        mvc.perform(MockMvcRequestBuilders.get(uri))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(expected.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(expected.getName()));
    }
}
