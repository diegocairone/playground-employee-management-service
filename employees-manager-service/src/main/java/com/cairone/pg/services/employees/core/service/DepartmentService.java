package com.cairone.pg.services.employees.core.service;

import com.cairone.pg.services.employees.core.exception.EntityIntegrityException;
import com.cairone.pg.services.employees.core.exception.EntityNotFoundException;
import com.cairone.pg.services.employees.core.form.DepartmentForm;
import com.cairone.pg.services.employees.core.mapper.DepartmentMapper;
import com.cairone.pg.services.employees.core.mapper.DepartmentMapperCfg;
import com.cairone.pg.services.employees.core.model.DepartmentModel;
import com.cairone.pg.services.employees.data.dao.DepartmentRepository;
import com.cairone.pg.services.employees.data.dao.EmployeeRepository;
import com.cairone.pg.services.employees.data.domain.DepartmentEntity;
import com.cairone.pg.services.employees.data.domain.EmployeeEntity;
import com.cairone.pg.services.employees.data.domain.QDepartmentEntity;
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
                .orElseThrow(() -> new EntityNotFoundException(
                        "Department could not be created",
                        "Assigned manager with ID %s could not be found in the database",
                        form.getManagerId()));

        final Set<EmployeeEntity> employees = form.getEmployeeIDs().stream()
                .map(employeeId -> employeeRepository.findById(employeeId)
                        .orElseThrow(() -> new EntityNotFoundException(
                                "Department could not be created",
                                "Assigned employee with ID %s could not be found in the database",
                                employeeId)))
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
                .orElseThrow(() -> new EntityNotFoundException(
                        "Requested resource to be updated does not exist in the database",
                        "Department with ID %s could not be updated", id));

        // check business rules
        String name = form.getName().trim().toUpperCase();
        verifyDuplicatedName(name, q -> q.name.eq(form.getName()).and(q.id.ne(id)));

        departmentEntity.setName(name);

        if (!departmentEntity.getManager().getId().equals(form.getManagerId())) {
            final EmployeeEntity manager = employeeRepository.findById(form.getManagerId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Department could not be updated",
                            "Assigned manager with ID %s could not be found in the database",
                            form.getManagerId()));
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
                        .orElseThrow(() -> new EntityNotFoundException(
                                "Department could not be updated",
                                "Assigned employee with ID %s could not be found in the database",
                                employeeId)))
                .collect(Collectors.toSet());

        final Set<EmployeeEntity> toBeRemoved = actualEmployeeIds.stream()
                .filter(e -> !intersection.contains(e))
                .map(employeeId -> employeeRepository.findById(employeeId)
                        .orElseThrow(() -> new EntityNotFoundException(
                                "Department could not be updated",
                                "Assigned employee with ID %s could not be found in the database",
                                employeeId)))
                .collect(Collectors.toSet());

        departmentEntity.getEmployees().addAll(toBeAdded);
        departmentEntity.getEmployees().removeAll(toBeRemoved);

        return departmentMapper.convert(departmentRepository.save(departmentEntity));
    }

    @Transactional
    public void delete(Long id) {
        DepartmentEntity departmentEntity = departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Requested resource to be deleted does not exist in the database",
                        "Department with ID %s could not be deleted", id));
        departmentRepository.delete(departmentEntity);
    }

    private void verifyDuplicatedName(String name, Function<QDepartmentEntity, BooleanExpression> predicate) {
        Boolean existsByName = exists(predicate);
        if (existsByName) {
            throw new EntityIntegrityException("name", "Department with name %s already exists", name);
        }
    }

    private Boolean exists(Function<QDepartmentEntity, BooleanExpression> predicate) {
        QDepartmentEntity qDepartmentEntity = QDepartmentEntity.departmentEntity;
        BooleanExpression booleanExpression = predicate.apply(qDepartmentEntity);
        return departmentRepository.exists(booleanExpression);
    }
}
