package com.cairone.pg.rest.ctrl;

import com.cairone.pg.base.exception.AppClientException;
import com.cairone.pg.base.exception.AppServerException;
import com.cairone.pg.core.service.EmployeeAvatarService;
import com.cairone.pg.core.service.EmployeeService;
import com.cairone.pg.rest.endpoints.EmployeeAvatarEndpoints;
import com.cairone.pg.util.MediaTypeToFileExtensionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequiredArgsConstructor
public class EmployeeAvatarCtrl implements EmployeeAvatarEndpoints {

    private final EmployeeService employeeService;
    private final EmployeeAvatarService employeeAvatarService;

    @Override
    public ResponseEntity<Void> uploadAvatar(Long id, MultipartFile avatar) {
        return handlerForPostAndPutAvatar(id, avatar, true);
    }

    @Override
    public ResponseEntity<Void> replaceAvatar(Long id, MultipartFile avatar) {
        return handlerForPostAndPutAvatar(id, avatar, false);
    }

    @Override
    public ResponseEntity<Void> removeAvatar(Long id) {
        employeeAvatarService.removeAvatar(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Resource> downloadAvatar(Long id) {
        return employeeAvatarService.downloadAvatar(id)
                .map(contentHolder -> {
                    String contentType = contentHolder.contentMetadata().contentType();
                    MediaType mediaType = MediaType.parseMediaType(contentType);
                    String extension = MediaTypeToFileExtensionUtil.getExtension(mediaType);
                    return ResponseEntity.ok()
                            .contentType(mediaType)
                            .header(
                                    HttpHeaders.CONTENT_DISPOSITION,
                                    "inline;filename=" + id.toString() + extension)
                            .body(contentHolder.resource());
                })
                .orElseThrow(() -> new AppClientException(
                        AppClientException.NOT_FOUND,
                        error -> error.put("id", "Invalid Employee ID provided"),
                        "Employee with ID %s does not exist in our database", id));
    }

    private ResponseEntity<Void> handlerForPostAndPutAvatar(Long id, MultipartFile avatar, boolean isPost) {

        try {

            byte[] data = avatar.getBytes();
            var noContent = ResponseEntity.noContent();

            if (isPost) {
                URI uri = employeeAvatarService.uploadAvatar(id, data).toURI();
                noContent.headers(h -> h.setLocation(uri));
            } else {
                URI uri = employeeAvatarService.replaceAvatar(id, data).toURI();
                noContent.headers(h -> h.setLocation(uri));
            }

            return noContent.build();

        } catch (URISyntaxException e) {
            throw new AppServerException.Builder()
                    .withMessage("Error trying to create avatar URL")
                    .withTechnicalMessage(e.getMessage())
                    .withCause(e)
                    .build();
        } catch (IOException e) {
            throw new AppServerException.Builder()
                    .withMessage("Error trying to read avatar file")
                    .withTechnicalMessage(e.getMessage())
                    .withCause(e)
                    .build();
        }
    }
}
