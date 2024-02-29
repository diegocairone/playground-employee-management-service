package com.cairone.pg.core.service;

import com.cairone.pg.base.exception.AppClientException;
import com.cairone.pg.core.form.DepartmentForm;
import com.cairone.pg.core.mapper.DepartmentMapper;
import com.cairone.pg.core.mapper.DepartmentMapperCfg;
import com.cairone.pg.core.model.DepartmentModel;
import com.cairone.pg.data.dao.DepartmentRepository;
import com.cairone.pg.data.dao.EmployeeRepository;
import com.cairone.pg.data.domain.DepartmentEntity;
import com.cairone.pg.data.domain.EmployeeEntity;
import com.cairone.pg.data.domain.QDepartmentEntity;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final DepartmentMapper departmentMapper;

    @Transactional(readOnly = true)
    public Optional<DepartmentModel> findById(Long id, DepartmentMapperCfg mapperCfg) {
        return departmentRepository.findById(id)
                .map(entity -> departmentMapper.convert(entity, mapperCfg));
    }

    @Transactional(readOnly = true)
    public List<DepartmentModel> findAll(DepartmentMapperCfg mapperCfg) {
        return departmentRepository.findAll()
                .stream()
                .map(entity -> departmentMapper.convert(entity, mapperCfg))
                .collect(Collectors.toList());
    }

    @Transactional
    public DepartmentModel create(DepartmentForm form) {

        // check business rules
        String name = form.getName().trim().toUpperCase();
        verifyDuplicatedName(name, q -> q.name.eq(form.getName()));

        final EmployeeEntity manager = employeeRepository.findById(form.getManagerId())
                .orElseThrow(() -> getManagerNotFoundException(form.getManagerId()));

        final Set<EmployeeEntity> employees = form.getEmployeeIDs().stream()
                .map(employeeId -> employeeRepository.findById(employeeId)
                        .orElseThrow(() -> getEmployeeNotFoundException(employeeId)))
                .collect(Collectors.toSet());

        DepartmentEntity departmentEntity = new DepartmentEntity();
        departmentEntity.setName(name);
        departmentEntity.setManager(manager);
        departmentEntity.setEmployees(employees);

        return departmentMapper.convert(departmentRepository.save(departmentEntity));
    }

    @Transactional
    public DepartmentModel update(Long id, DepartmentForm form) {

        DepartmentEntity departmentEntity = departmentRepository.findById(id)
                .orElseThrow(() -> new AppClientException(
                        AppClientException.NOT_FOUND,
                        error -> error.put("departmentId", "Invalid Department ID provided"),
                        "Department with ID %s could not be updated", id));

        // check business rules
        String name = form.getName().trim().toUpperCase();
        verifyDuplicatedName(name, q -> q.name.eq(form.getName()).and(q.id.ne(id)));

        departmentEntity.setName(name);

        if (!departmentEntity.getManager().getId().equals(form.getManagerId())) {
            final EmployeeEntity manager = employeeRepository.findById(form.getManagerId())
                    .orElseThrow(() -> getManagerNotFoundException(form.getManagerId()));
            departmentEntity.setManager(manager);
        }

        final Set<Long> actualEmployeeIds = departmentEntity.getEmployees().stream()
                .map(EmployeeEntity::getId)
                .collect(Collectors.toSet());

        final Set<Long> intersection = form.getEmployeeIDs().stream()
                .filter(actualEmployeeIds::contains)
                .collect(Collectors.toSet());

        final Set<EmployeeEntity> toBeAdded = form.getEmployeeIDs().stream()
                .filter(e -> !intersection.contains(e))
                .map(employeeId -> employeeRepository.findById(employeeId)
                        .orElseThrow(() -> getEmployeeNotFoundException(employeeId)))
                .collect(Collectors.toSet());

        final Set<EmployeeEntity> toBeRemoved = actualEmployeeIds.stream()
                .filter(e -> !intersection.contains(e))
                .map(employeeId -> employeeRepository.findById(employeeId)
                        .orElseThrow(() -> getEmployeeNotFoundException(employeeId)))
                .collect(Collectors.toSet());

        departmentEntity.getEmployees().addAll(toBeAdded);
        departmentEntity.getEmployees().removeAll(toBeRemoved);

        return departmentMapper.convert(departmentRepository.save(departmentEntity));
    }

    @Transactional
    public void delete(Long id) {
        DepartmentEntity departmentEntity = departmentRepository.findById(id)
                .orElseThrow(() -> new AppClientException(
                        AppClientException.NOT_FOUND,
                        "Department with ID %s could not be deleted", id));
        departmentRepository.delete(departmentEntity);
    }

    private void verifyDuplicatedName(String name, Function<QDepartmentEntity, BooleanExpression> predicate) {
        boolean existsByName = exists(predicate);
        if (existsByName) {
            throw new AppClientException(
                    AppClientException.DATA_INTEGRITY,
                    error -> error.put("name", "Provided department name is already in use"),
                    "Department with name %s already exists", name);
        }
    }

    private Boolean exists(Function<QDepartmentEntity, BooleanExpression> predicate) {
        QDepartmentEntity qDepartmentEntity = QDepartmentEntity.departmentEntity;
        BooleanExpression booleanExpression = predicate.apply(qDepartmentEntity);
        return departmentRepository.exists(booleanExpression);
    }

    private AppClientException getManagerNotFoundException(Long managerId) {
        return new AppClientException(
                AppClientException.NOT_FOUND,
                error -> error.put("managerId", "Invalid Manager ID provided"),
                "Manager with ID %s could not be found in the database",
                managerId);
    }

    private AppClientException getEmployeeNotFoundException(Long employeeId) {
        return new AppClientException(
                AppClientException.NOT_FOUND,
                error -> error.put("employeeId", "Invalid Employee ID provided"),
                "Employee with ID %s could not be found in the database",
                employeeId);
    }
}
