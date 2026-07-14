package com.ensf.fnf.family.controller;

import com.ensf.fnf.core.dto.requestDto.CreateFamilyMemberRequestDto;
import com.ensf.fnf.core.dto.responseDto.CommonApiResponse;
import com.ensf.fnf.core.dto.responseDto.FamilyMemberResponseDto;
import com.ensf.fnf.core.dto.responseDto.HomeDashboardResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/family")
@CrossOrigin("*")
@Api(tags = "Family Network & Graph Tree APIs")
public interface FamilyController {

    @GetMapping("/tree")
    @ApiOperation("Fetch full dashboard hierarchy detailing linked family members graph nodes.")
    ResponseEntity<CommonApiResponse<HomeDashboardResponseDto>> getFamilyTree();

    @PostMapping("/node")
    @ApiOperation("Append a structural family relative relationship node component to the active tree graph.")
    ResponseEntity<CommonApiResponse<FamilyMemberResponseDto>> addFamilyMemberNode(@RequestBody CreateFamilyMemberRequestDto dto);

    @DeleteMapping("/node/{nodeId}")
    @ApiOperation("Remove a relative from the family tree")
    ResponseEntity<CommonApiResponse<String>> removeFamilyNode(@PathVariable Long nodeId);
}