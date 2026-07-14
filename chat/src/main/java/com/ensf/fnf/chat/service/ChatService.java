package com.ensf.fnf.chat.service;

import com.ensf.fnf.core.dto.responseDto.CommonApiResponse;
import com.ensf.fnf.core.dto.responseDto.CursorPageResponse;

import java.util.List;

public interface ChatService {
    CommonApiResponse<String> processIncomingMessage(Long roomId, String messageText, String mediaUrl);
    CommonApiResponse<List<Object>> fetchUserInbox();
    CommonApiResponse<CursorPageResponse<Object>> fetchRoomMessages(Long roomId, Long cursorId, int limit);
    CommonApiResponse<String> buildGroupChannel(String groupName, List<Long> participantIds);
}