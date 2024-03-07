package com.cairone.pg.core.service;

import com.cairone.pg.base.exception.AppClientException;
import com.cairone.pg.data.dao.EmployeeRepository;
import com.cairone.pg.data.domain.EmployeeEntity;
import com.cairone.pg.storage.ContentStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class EmployeeAvatarService {

    private final ContentStorage contentStorage;
    private final EmployeeRepository employeeRepository;

    public URL uploadAvatar(Long employeeId, byte[] avatar) {
        return handleAvatar(employeeId, avatar, employeeEntity -> {
            if (contentStorage.existsById(employeeEntity.getGlobalId())) {
                throw new AppClientException(
                        AppClientException.DATA_INTEGRITY,
                        error -> error.put("id", "Provided employee ID already has an avatar image associated with it"),
                        "Avatar already exists for employee with ID %s", employeeId);
            }
        });
    }

    public URL replaceAvatar(Long id, byte[] avatar) {
        return handleAvatar(id, avatar, employeeEntity -> {
            if (contentStorage.nonExistsById(employeeEntity.getGlobalId())) {
                throw new AppClientException(
                        AppClientException.DATA_INTEGRITY,
                        error -> error.put("id", "Provided employee ID doest not have an avatar image associated with it"),
                        "Avatar does not exist for employee with ID %s", id);
            }
        });
    }

    public Optional<ContentStorage.ContentHolder> downloadAvatar(Long id) {
        return employeeRepository.findById(id)
                .flatMap(employeeEntity -> contentStorage.downloadContent(employeeEntity.getGlobalId()));
    }

    public void removeAvatar(Long id) {
        employeeRepository.findById(id)
                .ifPresentOrElse(
                        employeeEntity -> contentStorage.removeById(employeeEntity.getGlobalId()),
                        () -> {
                            throw new AppClientException(
                                    AppClientException.NOT_FOUND,
                                    error -> error.put("id", "Invalid Employee ID provided"),
                                    "Employee with ID %s does not exist in our database", id);
                        });
    }

    private URL handleAvatar(Long employeeId, byte[] avatar, Consumer<EmployeeEntity> consumer) {

        if (Objects.isNull(avatar)) {
            throw new AppClientException("Avatar image is empty!");
        }

        EmployeeEntity employeeEntity = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new AppClientException(
                        AppClientException.NOT_FOUND,
                        error -> error.put("id", "Invalid Employee ID provided"),
                        "Employee with ID %s does not exist in our database", employeeId));

        if (consumer != null) {
            consumer.accept(employeeEntity);
        }

        return contentStorage.uploadContent(
                employeeEntity.getGlobalId(),
                avatar);
    }
}
