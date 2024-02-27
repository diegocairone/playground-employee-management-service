package com.cairone.pg.rest.ctrl;

import com.cairone.pg.rest.ctrl.request.BankAccountRequest;
import com.cairone.pg.core.mapper.BankAccountMapper;
import com.cairone.pg.core.mapper.BankMapper;
import com.cairone.pg.core.service.BankAccountService;
import com.cairone.pg.data.dao.BankAccountRepository;
import com.cairone.pg.data.dao.BankRepository;
import com.cairone.pg.data.dao.EmployeeRepository;
import com.cairone.pg.data.domain.BankAccountEntity;
import com.cairone.pg.data.domain.BankEntity;
import com.cairone.pg.data.enums.BankAccountType;
import com.cairone.pg.rest.valid.AppControllerAdvice;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@Transactional
@ActiveProfiles("test")
class BankAccountCtrlTest extends AbstractCtrlTest {

    private final URI baseUri;
    private final EmployeeRepository employeeRepository;
    private final BankRepository bankRepository;
    private final BankAccountRepository bankAccountRepository;

    @Autowired
    public BankAccountCtrlTest(EmployeeRepository employeeRepository, BankRepository bankRepository, BankAccountRepository bankAccountRepository, @LocalServerPort int port) {
        this.employeeRepository = employeeRepository;
        this.bankRepository = bankRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.baseUri = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(port)
                .path("/api/bank-accounts")
                .build()
                .toUri();
    }

    @Test
    void whenFindAll_thenAllBankAccountsAndHttpOk() throws Exception {

        PageableHandlerMethodArgumentResolver pageableResolver = new PageableHandlerMethodArgumentResolver();
        pageableResolver.setFallbackPageable(PageRequest.of(0, 10));

        MockMvc mvc = standaloneSetup()
                .setCustomArgumentResolvers(pageableResolver)
                .build();

        URI uri = UriComponentsBuilder.fromUri(baseUri)
                .queryParam("page", 0)
                .queryParam("size",  5)
                .queryParam("sort",  "accountNumber")
                .queryParam("accountNumber", "SS")
                .queryParam("accountIs", "STARTS_WITH")
                .build()
                .toUri();

        mvc.perform(MockMvcRequestBuilders.get(uri))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content",
                        Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].accountNumber",
                        Matchers.equalTo("SS00000001")));
    }

    @Test
    void whenFindById_givenBankAccountId_thenHttpOk() throws Exception {

        BankAccountEntity expected = bankAccountRepository.getById(1L);
        MockMvc mvc = standaloneSetup().build();

        URI uri = UriComponentsBuilder.fromUri(baseUri)
                .path("/" + expected.getId())
                .queryParam("includeBank", "true")
                .build()
                .toUri();

        mvc.perform(MockMvcRequestBuilders.get(uri))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", equalTo(expected.getId()), Long.class))
                .andExpect(jsonPath("$.accountNumber", equalTo(expected.getAccountNumber())))
                .andExpect(jsonPath("$.accountType", equalTo(expected.getAccountType().name())))
                .andExpect(jsonPath("$.bank.id", equalTo(expected.getBank().getId()), Long.class));
    }

    @Test
    void whenCreate_givenNewBankAccount_thenHttpCreated() throws Exception {

        BankAccountRequest request = new BankAccountRequest();
        request.setBankId(1l);
        request.setAccountType(BankAccountType.SAVINGS);
        request.setAccountNumber("ss00000010");

        MockMvc mvc = standaloneSetup().build();

        MvcResult mvcResult = mvc.perform(post(baseUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        Long createdId = findIdInResponse(mvcResult);
        BankAccountEntity bankAccountEntity = bankAccountRepository.getById(createdId);

        Assertions.assertThat(bankAccountEntity.getBank().getId())
                .isEqualTo(1l);
        Assertions.assertThat(bankAccountEntity.getAccountType())
                .isEqualTo(BankAccountType.SAVINGS);
        Assertions.assertThat(bankAccountEntity.getAccountNumber())
                .isEqualTo("SS00000010");
    }

    @Test
    void whenCreate_givenAccountNumberInvalid_thenHttpBadRequest() throws Exception {

        BankAccountRequest request = new BankAccountRequest();
        request.setBankId(1l);
        request.setAccountType(BankAccountType.SAVINGS);
        request.setAccountNumber("CC00000001");

        MockMvc mvc = standaloneSetup()
                .setControllerAdvice(new AppControllerAdvice())
                .build();

        mvc.perform(post(baseUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.message",
                        Matchers.is("At least one field in the request is invalid")))
                .andExpect(jsonPath("$.fieldErrors.accountNumber[0]",
                        Matchers.is("Account number for CHECKING account is invalid")));
    }

    @Test
    void whenCreate_givenAnExistingAccountNumber_thenHttpBadRequest() throws Exception {

        BankAccountRequest request = new BankAccountRequest();
        request.setBankId(1l);
        request.setAccountType(BankAccountType.SAVINGS);
        request.setAccountNumber("SS00000001");

        MockMvc mvc = standaloneSetup()
                .setControllerAdvice(new AppControllerAdvice())
                .build();

        mvc.perform(post(baseUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.message",
                        Matchers.is("Data integrity violation")))
                .andExpect(jsonPath("$.reason",
                        Matchers.is("Exception thrown when an attempt to insert or update data results in violation of a business rule")));
    }
    @Test
    void whenDelete_givenBankAccountById_thenBankAccountEntityRemovedFromDatabase() throws Exception {

        BankEntity bankEntity = bankRepository.getById(1l);

        BankAccountEntity bankAccountEntity = new BankAccountEntity();
        bankAccountEntity.setBank(bankEntity);
        bankAccountEntity.setAccountNumber("SS99999999");
        bankAccountEntity.setAccountType(BankAccountType.SAVINGS);

        BankAccountEntity expected = bankAccountRepository.save(bankAccountEntity);

        MockMvc mvc = standaloneSetup().build();

        URI uri = UriComponentsBuilder.fromUri(baseUri)
                .path("/" + expected.getId())
                .build()
                .toUri();

        mvc.perform(delete(uri))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        boolean exists = bankAccountRepository.existsById(expected.getId());
        Assertions.assertThat(exists).isFalse();
    }

    @Test
    void whenUpdate_givenBankById_thenBankNameChangedInDatabase() throws Exception {

        final BankAccountEntity before = bankAccountRepository.getById(1L);

        MockMvc mvc = standaloneSetup().build();

        BankAccountRequest request = new BankAccountRequest();
        request.setBankId(1l);
        request.setAccountType(BankAccountType.SAVINGS);
        request.setAccountNumber("ss99999999");

        URI uri = UriComponentsBuilder.fromUri(baseUri)
                .path("/" + before.getId())
                .build()
                .toUri();

        mvc.perform(put(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        final BankAccountEntity after = bankAccountRepository.getById(1L);

        Assertions.assertThat(after.getAccountNumber()).isEqualTo(request.getAccountNumber().toUpperCase());
    }

    private StandaloneMockMvcBuilder standaloneSetup() {

        BankMapper bankMapper = new BankMapper();
        BankAccountMapper bankAccountMapper = new BankAccountMapper(bankMapper);
        BankAccountService bankAccountService = new BankAccountService(employeeRepository, bankRepository, bankAccountRepository, bankAccountMapper);

        return MockMvcBuilders.standaloneSetup(new BankAccountCtrl(bankAccountService));
    }
}
