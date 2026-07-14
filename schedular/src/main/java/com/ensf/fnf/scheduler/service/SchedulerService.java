package com.ensf.fnf.scheduler.service;

import com.ensf.fnf.core.dto.requestDto.ScheduleWishRequestDto;
import com.ensf.fnf.core.dto.responseDto.CommonApiResponse;
import com.ensf.fnf.core.dto.responseDto.CursorPageResponse;
import com.ensf.fnf.core.dto.responseDto.ScheduledWishResponseDto;

import java.util.List;

public interface SchedulerService {
    CommonApiResponse<String> createScheduledWish(ScheduleWishRequestDto requestDto);
    CommonApiResponse<CursorPageResponse<ScheduledWishResponseDto>> fetchPendingWishes(Long cursorId, int limit);
    CommonApiResponse<String> deleteScheduledWish(Long wishId);
    CommonApiResponse<List<Object>> getTemplates();
}