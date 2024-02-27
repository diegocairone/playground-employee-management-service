package com.cairone.pg.rest.ctrl;

import com.cairone.pg.core.mapper.BankAccountMapper;
import com.cairone.pg.core.mapper.BankMapper;
import com.cairone.pg.core.mapper.CityMapper;
import com.cairone.pg.core.mapper.EmployeeMapper;
import com.cairone.pg.core.pageable.EmployeePageableConverter;
import com.cairone.pg.core.service.BankAccountService;
import com.cairone.pg.core.service.EmployeeService;
import com.cairone.pg.data.dao.*;
import com.cairone.pg.data.domain.BankAccountEntity;
import com.cairone.pg.data.domain.CityEntity;
import com.cairone.pg.data.domain.EmployeeEntity;
import com.cairone.pg.base.enums.EmployeeStatus;
import com.cairone.pg.base.enums.EmployeeTag;
import com.cairone.pg.rest.ctrl.request.EmployeeRequest;
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
import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@Transactional
@ActiveProfiles("test")
class EmployeeCtrlTest extends AbstractCtrlTest {

    private URI baseUri;
    private EmployeeRepository employeeRepository;
    private EmployeeStatusLogRepository employeeStatusLogRepository;
    private BankRepository bankRepository;
    private BankAccountRepository bankAccountRepository;
    private CityRepository cityRepository;
    private DepartmentRepository departmentRepository;
    private EmployeePageableConverter employeePageableConverter;

    @Autowired
    public EmployeeCtrlTest(
            EmployeeRepository employeeRepository,
            EmployeeStatusLogRepository employeeStatusLogRepository,
            BankRepository bankRepository,
            BankAccountRepository bankAccountRepository,
            CityRepository cityRepository,
            DepartmentRepository departmentRepository,
            EmployeePageableConverter employeePageableConverter,
            @LocalServerPort int port) {

        this.employeeRepository = employeeRepository;
        this.employeeStatusLogRepository = employeeStatusLogRepository;
        this.bankRepository = bankRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.cityRepository = cityRepository;
        this.departmentRepository = departmentRepository;
        this.employeePageableConverter = employeePageableConverter;

        this.baseUri = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(port)
                .path("/api/employees")
                .build()
                .toUri();
    }

    @Test
    void whenFindAll_thenAllEmployeesAndHttpOk() throws Exception {

        PageableHandlerMethodArgumentResolver pageableResolver = new PageableHandlerMethodArgumentResolver();
        pageableResolver.setFallbackPageable(PageRequest.of(0, 10));

        MockMvc mvc = standaloneSetup().setCustomArgumentResolvers(pageableResolver).build();

        mvc.perform(get(baseUri)
                        .queryParam("page", "0")
                        .queryParam("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(8)));
    }

    @Test
    void whenFindById_givenEmployeeId_thenHttpOk() throws Exception {

        EmployeeEntity expected = employeeRepository.getById(1L);

        MockMvc mvc = standaloneSetup().build();

        URI uri = UriComponentsBuilder.fromUri(baseUri)
                .path("/" + expected.getId())
                .build()
                .toUri();

        mvc.perform(get(uri))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(expected.getId()), Long.class))
                .andExpect(jsonPath("$.names", equalTo(expected.getNames())));
    }

    @Test
    void whenCreate_givenNewEmployee_thenHttpCreated() throws Exception {

        EmployeeRequest request = new EmployeeRequest();
        request.setNames(" test ");
        request.setBirthDate(LocalDate.of(2000, 1, 1));
        request.setCityId(1L);
        request.setBankAccountId(4L);
        request.setTags(Stream.of(EmployeeTag.FULLSTACK_DEV, EmployeeTag.JAVA_DEV).collect(Collectors.toSet()));

        MockMvc mvc = standaloneSetup().build();

        MvcResult mvcResult = mvc.perform(post(baseUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        final CityEntity cityExpected = cityRepository.getById(1L);
        final BankAccountEntity bankAccountEntityExpected = bankAccountRepository.getById(4L);

        Long createdId = findIdInResponse(mvcResult);

        EmployeeEntity expected = new EmployeeEntity();
        expected.setId(createdId);
        expected.setNames("TEST");
        expected.setBirthDate(LocalDate.of(2000, 1, 1));
        expected.setCity(cityExpected);
        expected.setTags(Stream.of(EmployeeTag.FULLSTACK_DEV, EmployeeTag.JAVA_DEV).collect(Collectors.toSet()));
        expected.setStatus(EmployeeStatus.INACTIVE);
        expected.setBankAccount(bankAccountEntityExpected);
        expected.setDepartments(null);

        EmployeeEntity actual = employeeRepository.getById(createdId);
        Assertions.assertThat(actual).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expected);
    }

    @Test
    void whenCreate_givenNewEmployeeWithAnEmptyName_thenHttpBadRequest() throws Exception {

        EmployeeRequest request = new EmployeeRequest();
        request.setBirthDate(LocalDate.of(2000, 1, 1));
        request.setCityId(1L);
        request.setBankAccountId(4L);
        request.setTags(Stream.of(EmployeeTag.FULLSTACK_DEV, EmployeeTag.JAVA_DEV).collect(Collectors.toSet()));

        MockMvc mvc = standaloneSetup().setControllerAdvice(new AppControllerAdvice()).build();

        mvc.perform(MockMvcRequestBuilders.post(baseUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        Matchers.equalTo("At least one field in the request is invalid")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors.names[0]",
                        Matchers.equalTo("must not be blank")));
    }

    @Test
    void whenDelete_givenEmployeeById_thenEmployeeEntityRemovedFromDatabase() throws Exception {

        long employeeId = 8L;
        MockMvc mvc = standaloneSetup().build();

        URI uri = UriComponentsBuilder.fromUri(baseUri)
                .path("/" + employeeId)
                .build()
                .toUri();

        mvc.perform(MockMvcRequestBuilders.delete(uri))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        boolean exists = employeeRepository.existsById(employeeId);
        Assertions.assertThat(exists).isFalse();
    }

    @Test
    void whenUpdate_givenEmployeeById_thenEmployeeNamesChangedInDatabase() throws Exception {

        final EmployeeEntity before = employeeRepository.getById(1L);

        MockMvc mvc = standaloneSetup().build();

        EmployeeRequest request = new EmployeeRequest();
        request.setNames("CHANGED");
        request.setBirthDate(before.getBirthDate());
        request.setTags(before.getTags());
        request.setCityId(before.getCity().getId());
        request.setBankAccountId(before.getBankAccount().getId());

        URI uri = UriComponentsBuilder.fromUri(baseUri)
                .path("/" + before.getId())
                .build()
                .toUri();

        mvc.perform(MockMvcRequestBuilders.put(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        final EmployeeEntity after = employeeRepository.getById(1L);

        Assertions.assertThat(after.getNames()).isEqualTo(request.getNames());
        Assertions.assertThat(after).usingRecursiveComparison().ignoringFields("names").isEqualTo(before);
    }

    private StandaloneMockMvcBuilder standaloneSetup() {

        BankMapper bankMapper = new BankMapper();
        CityMapper cityMapper = new CityMapper();
        EmployeeMapper employeeMapper = new EmployeeMapper(cityMapper);
        BankAccountMapper bankAccountMapper = new BankAccountMapper(bankMapper);

        BankAccountService bankAccountService = new BankAccountService(
                employeeRepository,
                bankRepository,
                bankAccountRepository,
                bankAccountMapper);

        EmployeeService employeeService = new EmployeeService(
                employeeRepository,
                employeeStatusLogRepository,
                cityRepository,
                bankAccountRepository,
                departmentRepository,
                employeeMapper,
                employeePageableConverter);

        return MockMvcBuilders.standaloneSetup(new EmployeeCtrl(employeeService, bankAccountService));
    }
}
