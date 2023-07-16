package com.cairone.pg.services.employees.rest.ctrl;

import com.cairone.pg.services.employees.core.mapper.BankMapper;
import com.cairone.pg.services.employees.core.service.BankService;
import com.cairone.pg.services.employees.data.dao.BankRepository;
import com.cairone.pg.services.employees.data.domain.BankEntity;
import com.cairone.pg.services.employees.rest.ctrl.request.BankRequest;
import com.cairone.pg.services.employees.rest.valid.AppControllerAdvice;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@Transactional
@ActiveProfiles("test")
class BankCtrlTest {

    private URI baseUri;
    private BankRepository bankRepository;

    @Autowired
    public BankCtrlTest(BankRepository bankRepository, @LocalServerPort int port) {
        this.bankRepository = bankRepository;
        this.baseUri = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(port)
                .path("/api/banks")
                .build()
                .toUri();
    }

    @Test
    void whenFindAll_thenAllBanksAndHttpOk() throws Exception {

        BankMapper bankMapper = new BankMapper();
        BankService bankService = new BankService(bankRepository, bankMapper);

        MockMvc mvc = MockMvcBuilders.standaloneSetup(new BankCtrl(bankService))
                .build();

        mvc.perform(get(baseUri))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void whenFindById_givenBankId_thenHttpOk() throws Exception {

        BankEntity expected = bankRepository.findById(1L).get();

        BankMapper bankMapper = new BankMapper();
        BankService bankService = new BankService(bankRepository, bankMapper);

        MockMvc mvc = MockMvcBuilders.standaloneSetup(new BankCtrl(bankService))
                .build();

        URI uri = UriComponentsBuilder.fromUri(baseUri)
                .path("/" + expected.getId())
                .build()
                .toUri();

        mvc.perform(get(uri))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(expected.getId()), Long.class))
                .andExpect(jsonPath("$.name", equalTo(expected.getName())));
    }

    @Test
    void whenCreate_givenNewBank_thenHttpCreated() throws Exception {

        BankRequest request = new BankRequest();
        request.setName("test");

        BankMapper bankMapper = new BankMapper();
        BankService bankService = new BankService(bankRepository, bankMapper);

        MockMvc mvc = MockMvcBuilders.standaloneSetup(new BankCtrl(bankService))
                .build();

        URI uri = UriComponentsBuilder.fromUri(baseUri)
                .build()
                .toUri();

        mvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("20"));

        BankEntity expected = new BankEntity();
        expected.setId(20L);
        expected.setName("TEST");

        Optional<BankEntity> optional = bankRepository.findById(20L);
        Assertions.assertThat(optional.get()).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void whenCreate_givenNewBankWithAnEmptyName_thenHttpBadRequest() throws Exception {

        BankRequest request = new BankRequest();
        request.setName("");

        BankMapper bankMapper = new BankMapper();
        BankService bankService = new BankService(bankRepository, bankMapper);

        MockMvc mvc = MockMvcBuilders.standaloneSetup(new BankCtrl(bankService), new AppControllerAdvice())
                .build();

        URI uri = UriComponentsBuilder.fromUri(baseUri)
                .build()
                .toUri();

        mvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message",
                        equalTo("At least one field in the request is invalid")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors.name[0]",
                        equalTo("must not be blank")));
    }
    @Test
    public void whenCreate_givenNewBankWithAnExtraLargeName_thenHttpBadRequest() throws Exception {

        BankRequest request = new BankRequest();
        request.setName("1234567890123456789012345678901");

        BankMapper bankMapper = new BankMapper();
        BankService bankService = new BankService(bankRepository, bankMapper);

        MockMvc mvc = MockMvcBuilders.standaloneSetup(new BankCtrl(bankService), new AppControllerAdvice())
                .build();

        URI uri = UriComponentsBuilder.fromUri(baseUri)
                .build()
                .toUri();

        mvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message",
                        equalTo("At least one field in the request is invalid")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors.name[0]",
                        equalTo("size must be between 0 and 30")));
    }
    @Test
    public void whenDelete_givenBankById_thenBankEntityRemovedFromDatabase() throws Exception {

        BankEntity bankEntity = new BankEntity();
        bankEntity.setName("Test Bank");
        bankRepository.save(bankEntity);

        BankMapper bankMapper = new BankMapper();
        BankService bankService = new BankService(bankRepository, bankMapper);

        MockMvc mvc = MockMvcBuilders.standaloneSetup(new BankCtrl(bankService))
                .build();

        URI uri = UriComponentsBuilder.fromUri(baseUri)
                .path("/" + bankEntity.getId())
                .build()
                .toUri();

        mvc.perform(delete(uri))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        boolean exists = bankRepository.existsById(bankEntity.getId());
        Assertions.assertThat(exists).isFalse();
    }

    @Test
    public void whenUpdate_givenBankById_thenBankNameChangedInDatabase() throws Exception {

        final BankEntity before = bankRepository.findById(1L).get();

        BankMapper bankMapper = new BankMapper();
        BankService bankService = new BankService(bankRepository, bankMapper);

        MockMvc mvc = MockMvcBuilders.standaloneSetup(new BankCtrl(bankService))
                .build();

        BankRequest request = new BankRequest();
        request.setName("CHANGED");

        URI uri = UriComponentsBuilder.fromUri(baseUri)
                .path("/" + before.getId())
                .build()
                .toUri();

        mvc.perform(put(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        final BankEntity after = bankRepository.findById(1L).get();

        Assertions.assertThat(after.getName()).isEqualTo(request.getName());
    }

    private String asJsonString(Object object) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
