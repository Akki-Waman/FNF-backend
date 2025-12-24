package com.sipl.ticket.android.controller;

import com.sipl.ticket.core.dto.response.AndroidApiResponseDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/android")
@CrossOrigin(origins = "*")
@Api(tags = "AndroidVersions APIs")
public interface AndroidVersionController {

    @ApiOperation(
            value = "Check app version compatibility",
            notes =
                    "Checks if the specified app version is up-to-date or requires an update, based on the provided appId and version.",
            response = AndroidApiResponseDTO.class)
    @GetMapping("/check-version/{appId}/{version}")
    public ResponseEntity<AndroidApiResponseDTO> checkVersion(
            @PathVariable String appId, @PathVariable String version);

    @ApiOperation(
            value = "Download a file",
            notes =
                    "Downloads a specified file by providing its name. The response is a binary file download.",
            response = byte[].class)
    @GetMapping(value = "/download/{name}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<?> downloadFile(@PathVariable(value = "name") String fileName);
}
