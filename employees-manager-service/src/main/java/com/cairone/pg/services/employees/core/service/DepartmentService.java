package com.cairone.pg.services.employees.core.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.cairone.pg.services.employees.core.exception.EntityNotFoundException;
import com.cairone.pg.services.employees.data.dao.DepartmentRepository;
import com.cairone.pg.services.employees.data.dao.EmployeeRepository;
import com.cairone.pg.services.employees.data.domain.DepartmentEntity;
import com.cairone.pg.services.employees.data.domain.EmployeeEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cairone.pg.services.employees.core.form.DepartmentForm;
import com.cairone.pg.services.employees.core.mapper.DepartmentMapper;
import com.cairone.pg.services.employees.core.mapper.DepartmentMapperCfg;
import com.cairone.pg.services.employees.core.model.DepartmentModel;

import lombok.RequiredArgsConstructor;

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
        departmentEntity.setName(form.getName().trim().toUpperCase());
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

        departmentEntity.setName(form.getName());

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

}
