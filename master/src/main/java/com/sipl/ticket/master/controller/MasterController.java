package com.sipl.ticket.master.controller;


import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.MastersResponseDTO;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/master")
@CrossOrigin("*")
@Api(tags = "Master")
public interface MasterController {
    @GetMapping("/findByColumnCode")
    public ResponseEntity<ApiResponseDTO<List<MastersResponseDTO>>> findByColumnCode(@RequestParam Integer columnCode);

}
