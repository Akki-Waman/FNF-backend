package com.ensf.fnf.scheduler.controller.impl;

import com.ensf.fnf.core.dto.requestDto.ScheduleWishRequestDto;
import com.ensf.fnf.core.dto.responseDto.CommonApiResponse;
import com.ensf.fnf.core.dto.responseDto.CursorPageResponse;
import com.ensf.fnf.core.dto.responseDto.ScheduledWishResponseDto;
import com.ensf.fnf.scheduler.controller.SchedulerController;
import com.ensf.fnf.scheduler.service.SchedulerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SchedulerControllerImpl implements SchedulerController {

    private final SchedulerService schedulerService;

    @Override
    public ResponseEntity<CommonApiResponse<String>> scheduleWish(ScheduleWishRequestDto requestDto) {
        log.info("<<START>> SchedulerControllerImpl :: scheduleWish <<START>>");
        CommonApiResponse<String> response = schedulerService.createScheduledWish(requestDto);
        log.info("<<END>> SchedulerControllerImpl :: scheduleWish <<END>>");
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CommonApiResponse<CursorPageResponse<ScheduledWishResponseDto>>> getPendingWishes(Long cursorId, int limit) {
        log.info("<<START>> SchedulerControllerImpl :: getPendingWishes <<START>>");
        CommonApiResponse<CursorPageResponse<ScheduledWishResponseDto>> response = schedulerService.fetchPendingWishes(cursorId, limit);
        log.info("<<END>> SchedulerControllerImpl :: getPendingWishes <<END>>");
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CommonApiResponse<String>> cancelWish(Long wishId) {
        log.info("<<START>> SchedulerControllerImpl :: cancelWish <<START>>");
        CommonApiResponse<String> response = schedulerService.deleteScheduledWish(wishId);
        log.info("<<END>> SchedulerControllerImpl :: cancelWish <<END>>");
        return ResponseEntity.ok(response);
    }
    @Override
    public ResponseEntity<CommonApiResponse<List<Object>>> getTemplates() {
        log.info("<<START>> SchedulerControllerImpl :: getTemplates <<START>>");
        CommonApiResponse<List<Object>> response = schedulerService.getTemplates();
        log.info("<<END>> SchedulerControllerImpl :: getTemplates <<END>>");
        return ResponseEntity.ok(response);
    }
}