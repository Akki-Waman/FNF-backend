package com.ensf.fnf.scheduler.controller;

import com.ensf.fnf.core.dto.requestDto.ScheduleWishRequestDto;
import com.ensf.fnf.core.dto.responseDto.CommonApiResponse;
import com.ensf.fnf.core.dto.responseDto.CursorPageResponse;
import com.ensf.fnf.core.dto.responseDto.ScheduledWishResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/scheduler")
@Api(tags = "Wish Scheduler API")
public interface SchedulerController {

    @PostMapping("/wish")
    @ApiOperation("Schedule a proactive wish for a family member")
    ResponseEntity<CommonApiResponse<String>> scheduleWish(@RequestBody ScheduleWishRequestDto requestDto);

    @GetMapping("/pending")
    @ApiOperation("Retrieve the user's active pending scheduled wishes")
    ResponseEntity<CommonApiResponse<CursorPageResponse<ScheduledWishResponseDto>>> getPendingWishes(
            @RequestParam(required = false) Long cursorId,
            @RequestParam(defaultValue = "15") int limit);

    @DeleteMapping("/wish/{wishId}")
    @ApiOperation("Cancel and remove a pending scheduled wish")
    ResponseEntity<CommonApiResponse<String>> cancelWish(@PathVariable Long wishId);

    @GetMapping("/templates")
    @ApiOperation("Fetch pre-made greeting cards and text for wishes")
    ResponseEntity<CommonApiResponse<java.util.List<Object>>> getTemplates();
}