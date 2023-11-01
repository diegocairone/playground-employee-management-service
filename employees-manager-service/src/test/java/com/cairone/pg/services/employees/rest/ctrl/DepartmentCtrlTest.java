package com.cairone.pg.services.employees.rest.ctrl;

import com.cairone.pg.services.employees.core.mapper.CityMapper;
import com.cairone.pg.services.employees.core.mapper.DepartmentMapper;
import com.cairone.pg.services.employees.core.mapper.EmployeeMapper;
import com.cairone.pg.services.employees.core.service.DepartmentService;
import com.cairone.pg.services.employees.data.dao.DepartmentRepository;
import com.cairone.pg.services.employees.data.dao.EmployeeRepository;
import com.cairone.pg.services.employees.data.domain.DepartmentEntity;
import com.cairone.pg.services.employees.data.domain.EmployeeEntity;
import com.cairone.pg.services.employees.rest.ctrl.request.DepartmentRequest;
import com.cairone.pg.services.employees.rest.valid.AppControllerAdvice;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
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
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

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
public class DepartmentCtrlTest extends AbstractCtrlTest {

    private URI baseUri;
    private DepartmentRepository departmentRepository;
    private EmployeeRepository employeeRepository;

    @Autowired
    public DepartmentCtrlTest(
            DepartmentRepository departmentRepository,
            EmployeeRepository employeeRepository,
            @LocalServerPort int port) {

        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;

        this.baseUri = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(port)
                .path("/api/departments")
                .build()
                .toUri();
    }

    @Test
    void whenFindAll_thenAllDepartmentsAndHttpOk() throws Exception {

        MockMvc mvc = standaloneSetup().build();

        mvc.perform(get(baseUri))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void whenFindById_givenDepartmentId_thenHttpOk() throws Exception {

        DepartmentEntity expected = departmentRepository.getById(1L);

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
    void whenCreate_givenNewDepartment_thenHttpCreated() throws Exception {

        DepartmentRequest request = new DepartmentRequest();
        request.setName("test");
        request.setManagerId(1L);
        request.setEmployeeIDs(Arrays.asList(2L, 3L).stream().collect(Collectors.toSet()));

        MockMvc mvc = standaloneSetup().build();

        MvcResult mvcResult = mvc.perform(post(baseUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        Long createdId = findIdInResponse(mvcResult);

        DepartmentEntity expected = new DepartmentEntity();
        expected.setId(createdId);
        expected.setName("TEST");

        DepartmentEntity actual = departmentRepository.getById(createdId);
        Assertions.assertThat(actual).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expected);

        Assertions.assertThat(actual.getManager().getId()).isEqualTo(1L);
        Assertions.assertThat(
                    actual.getEmployees().stream()
                            .map(EmployeeEntity::getId)
                            .collect(Collectors.toSet()))
                .contains(2L, 3L);
    }

    @Test
    void whenCreate_givenNewDepartmentWithAnExistingName_thenHttpBadRequest() throws Exception {

        DepartmentRequest request = new DepartmentRequest();
        request.setName("DEPARTMENT-1");
        request.setManagerId(1L);
        request.setEmployeeIDs(Arrays.asList(2L, 3L).stream().collect(Collectors.toSet()));

        MockMvc mvc = standaloneSetup().setControllerAdvice(new AppControllerAdvice()).build();

        mvc.perform(MockMvcRequestBuilders.post(baseUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        Matchers.equalTo("Data integrity violation")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors.name[0]",
                        Matchers.equalTo("Department with name DEPARTMENT-1 already exists")));
    }

    @Test
    void whenCreate_givenNewDepartmentWithAnEmptyName_thenHttpBadRequest() throws Exception {

        DepartmentRequest request = new DepartmentRequest();
        request.setName("");
        request.setManagerId(1L);
        request.setEmployeeIDs(Arrays.asList(2L, 3L).stream().collect(Collectors.toSet()));

        MockMvc mvc = standaloneSetup().setControllerAdvice(new AppControllerAdvice()).build();

        mvc.perform(MockMvcRequestBuilders.post(baseUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        Matchers.equalTo("At least one field in the request is invalid")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors.name[0]",
                        Matchers.equalTo("must not be blank")));
    }

    @Test
    void whenCreate_givenNewDepartmentWithAExtraLargeName_thenHttpBadRequest() throws Exception {

        DepartmentRequest request = new DepartmentRequest();
        request.setName("123456789012345678901234567890123456789012345678901");
        request.setManagerId(1L);
        request.setEmployeeIDs(Arrays.asList(2L, 3L).stream().collect(Collectors.toSet()));

        MockMvc mvc = standaloneSetup().setControllerAdvice(new AppControllerAdvice()).build();

        mvc.perform(MockMvcRequestBuilders.post(baseUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        Matchers.equalTo("At least one field in the request is invalid")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors.name[0]",
                        Matchers.equalTo("size must be between 0 and 50")));
    }

    @Test
    void whenCreate_givenManagerIsInEmployeeList_thenHttpBadRequest() throws Exception {

        DepartmentRequest request = new DepartmentRequest();
        request.setName("DEPARTMENT-1");
        request.setManagerId(1L);
        request.setEmployeeIDs(Arrays.asList(1L, 2L).stream().collect(Collectors.toSet()));

        MockMvc mvc = standaloneSetup().setControllerAdvice(new AppControllerAdvice()).build();

        mvc.perform(MockMvcRequestBuilders.post(baseUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        Matchers.equalTo("At least one field in the request is invalid")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.reason",
                        Matchers.equalTo("Manager with ID 1 cannot be included in the employee list")));
    }

    @Test
    void whenDelete_givenDepartmentById_thenDepartmentEntityRemovedFromDatabase() throws Exception {

        EmployeeEntity manager = employeeRepository.getById(1L);
        EmployeeEntity employee2 = employeeRepository.getById(2L);
        EmployeeEntity employee3 = employeeRepository.getById(3L);

        DepartmentEntity departmentEntity = new DepartmentEntity();
        departmentEntity.setName("TEST");
        departmentEntity.setManager(manager);
        departmentEntity.setEmployees(Arrays.asList(employee2, employee3).stream().collect(Collectors.toSet()));

        final DepartmentEntity actual = departmentRepository.save(departmentEntity);

        MockMvc mvc = standaloneSetup().build();

        URI uri = UriComponentsBuilder.fromUri(baseUri)
                .path("/" + actual.getId())
                .build()
                .toUri();

        mvc.perform(MockMvcRequestBuilders.delete(uri))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        boolean exists = departmentRepository.existsById(actual.getId());
        Assertions.assertThat(exists).isFalse();
    }

    @Test
    void whenUpdate_givenDepartmentById_thenDepartmentNameChangedInDatabase() throws Exception {

        final DepartmentEntity before = departmentRepository.getById(1L);

        MockMvc mvc = standaloneSetup().build();

        DepartmentRequest request = new DepartmentRequest();
        request.setName("CHANGED");
        request.setManagerId(before.getManager().getId());
        request.setEmployeeIDs(before.getEmployees().stream().map(EmployeeEntity::getId).collect(Collectors.toSet()));

        URI uri = UriComponentsBuilder.fromUri(baseUri)
                .path("/" + before.getId())
                .build()
                .toUri();

        mvc.perform(MockMvcRequestBuilders.put(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        final DepartmentEntity after = departmentRepository.getById(1L);

        Assertions.assertThat(after.getName()).isEqualTo(request.getName());
        Assertions.assertThat(after).usingRecursiveComparison().ignoringFields("name").isEqualTo(before);
    }

    @Test
    void whenUpdate_givenDepartmentById_thenEmployeeListChangedInDatabase() throws Exception {

        final DepartmentEntity before = departmentRepository.getById(1L);

        MockMvc mvc = standaloneSetup().build();

        DepartmentRequest request = new DepartmentRequest();
        request.setName("DEPARTMENT-1");
        request.setManagerId(before.getManager().getId());
        request.setEmployeeIDs(Arrays.asList(3l, 4l).stream().collect(Collectors.toSet()));

        URI uri = UriComponentsBuilder.fromUri(baseUri)
                .path("/" + before.getId())
                .build()
                .toUri();

        mvc.perform(MockMvcRequestBuilders.put(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        final DepartmentEntity after = departmentRepository.getById(1L);

        Assertions.assertThat(after).usingRecursiveComparison().ignoringFields("employees").isEqualTo(before);

        Assertions.assertThat(after.getEmployees().stream().map(EmployeeEntity::getId)).doesNotContain(2l);
        Assertions.assertThat(after.getEmployees().stream().map(EmployeeEntity::getId)).contains(3l, 4l);
    }

    private StandaloneMockMvcBuilder standaloneSetup() {
        CityMapper cityMapper = new CityMapper();
        EmployeeMapper employeeMapper = new EmployeeMapper(cityMapper);
        DepartmentMapper departmentMapper = new DepartmentMapper(employeeMapper);

        DepartmentService departmentService = new DepartmentService(
                departmentRepository, employeeRepository, departmentMapper);

        return MockMvcBuilders.standaloneSetup(new DepartmentCtrl(departmentService));
    }
}
