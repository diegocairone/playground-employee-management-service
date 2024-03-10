package com.cairone.pg.rest.endpoints;

import com.cairone.pg.base.vo.ErrorValue;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/api/employees/{id}/avatar")
public interface EmployeeAvatarEndpoints {

    @PostMapping(consumes = "multipart/form-data")
    @Operation(summary = "Upload employee avatar", description = "Upload employee avatar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Employee avatar uploaded",
                    headers = { @Header(
                            name = "Location",
                            description = "The URL to get avatar image from content storage")
                    }),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = ErrorValue.class))
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Void.class)))
            )
    })
    ResponseEntity<Void> uploadAvatar(Long id, @RequestParam("file") MultipartFile avatar);

    @PutMapping(consumes = "multipart/form-data")
    @Operation(summary = "Replace employee avatar", description = "Replace employee avatar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Employee avatar uploaded",
                    headers = { @Header(
                            name = "Location",
                            description = "The URL to get avatar image from content storage")
                    }),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = ErrorValue.class))
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Void.class)))
            )
    })
    ResponseEntity<Void> replaceAvatar(Long id, @RequestParam("file") MultipartFile avatar);

    @DeleteMapping
    @Operation(summary = "Remove employee avatar", description = "Remove employee avatar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Employee avatar removed"),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied",
                    content = @Content(schema = @Schema(implementation = ErrorValue.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Void.class)))
            ),
            @ApiResponse(responseCode = "404", description = "Employee avatar not found",
                    content = @Content(schema = @Schema(implementation = ErrorValue.class)))
    })
    ResponseEntity<Void> removeAvatar(Long id);

    @GetMapping
    @Operation(summary = "Download employee avatar", description = "Download employee avatar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved employee avatar",
                    content = @Content(schema = @Schema(implementation = Resource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied",
                    content = @Content(schema = @Schema(implementation = ErrorValue.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Void.class)))
            ),
            @ApiResponse(responseCode = "404", description = "Employee avatar not found",
                    content = @Content(schema = @Schema(implementation = ErrorValue.class)))
    })
    ResponseEntity<Resource> downloadAvatar(Long id);

}
