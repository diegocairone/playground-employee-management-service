package com.cairone.pg.rest.ctrl;

import com.cairone.pg.core.mapper.BankMapper;
import com.cairone.pg.core.service.BankService;
import com.cairone.pg.data.dao.BankRepository;
import com.cairone.pg.data.domain.BankEntity;
import com.cairone.pg.rest.ctrl.request.BankRequest;
import com.cairone.pg.rest.valid.AppControllerAdvice;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@Transactional
@ActiveProfiles("test")
class BankCtrlTest extends AbstractCtrlTest {

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

        MockMvc mvc = standaloneSetup().build();

        mvc.perform(get(baseUri))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void whenFindById_givenBankId_thenHttpOk() throws Exception {

        BankEntity expected = bankRepository.getById(1L);

        MockMvc mvc = standaloneSetup().build();

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

        MockMvc mvc = standaloneSetup().build();

        MvcResult mvcResult = mvc.perform(post(baseUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        Long createdId = findIdInResponse(mvcResult);

        BankEntity expected = new BankEntity();
        expected.setId(createdId);
        expected.setName("TEST");

        BankEntity actual = bankRepository.getById(createdId);
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void whenCreate_givenNewBankWithAnEmptyName_thenHttpBadRequest() throws Exception {

        BankRequest request = new BankRequest();
        request.setName("");

        MockMvc mvc = standaloneSetup().setControllerAdvice(new AppControllerAdvice()).build();

        mvc.perform(post(baseUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message",
                        equalTo("At least one field in the request is invalid")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors.name[0]",
                        equalTo("must not be blank")));
    }

    @Test
    void whenCreate_givenNewBankWithAnExtraLargeName_thenHttpBadRequest() throws Exception {

        BankRequest request = new BankRequest();
        request.setName("1234567890123456789012345678901");

        MockMvc mvc = standaloneSetup().setControllerAdvice(new AppControllerAdvice()).build();

        mvc.perform(post(baseUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message",
                        equalTo("At least one field in the request is invalid")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors.name[0]",
                        equalTo("size must be between 0 and 30")));
    }

    @Test
    void whenDelete_givenBankById_thenBankEntityRemovedFromDatabase() throws Exception {

        BankEntity bankEntity = new BankEntity();
        bankEntity.setName("Test Bank");
        bankRepository.save(bankEntity);

        MockMvc mvc = standaloneSetup().build();

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
    void whenUpdate_givenBankById_thenBankNameChangedInDatabase() throws Exception {

        final BankEntity before = bankRepository.getById(1L);

        MockMvc mvc = standaloneSetup().build();

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

        final BankEntity after = bankRepository.getById(1L);

        Assertions.assertThat(after.getName()).isEqualTo(request.getName());
    }

    private StandaloneMockMvcBuilder standaloneSetup() {
        BankMapper bankMapper = new BankMapper();
        BankService bankService = new BankService(bankRepository, bankMapper);
        return MockMvcBuilders.standaloneSetup(new BankCtrl(bankService));
    }
}
