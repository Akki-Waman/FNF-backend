package com.ensf.fnf.chat.controller.impl;

import com.ensf.fnf.chat.controller.ChatController;
import com.ensf.fnf.chat.service.ChatService;
import com.ensf.fnf.core.dto.responseDto.CommonApiResponse;
import com.ensf.fnf.core.dto.responseDto.CursorPageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatControllerImpl implements ChatController {

    private final ChatService chatService;

    @Override
    public ResponseEntity<CommonApiResponse<String>> sendMessage(Long roomId, String messageText, String mediaUrl) {
        log.info("<<START>> ChatControllerImpl :: sendMessage <<START>>");
        CommonApiResponse<String> response = chatService.processIncomingMessage(roomId, messageText, mediaUrl);
        log.info("<<END>> ChatControllerImpl :: sendMessage <<END>>");
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CommonApiResponse<List<Object>>> getActiveChats() {
        log.info("<<START>> ChatControllerImpl :: getActiveChats <<START>>");
        CommonApiResponse<List<Object>> response = chatService.fetchUserInbox();
        log.info("<<END>> ChatControllerImpl :: getActiveChats <<END>>");
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CommonApiResponse<CursorPageResponse<Object>>> getMessageHistory(Long roomId, Long cursorId, int limit) {
        log.info("<<START>> ChatControllerImpl :: getMessageHistory <<START>>");
        CommonApiResponse<CursorPageResponse<Object>> response = chatService.fetchRoomMessages(roomId, cursorId, limit);
        log.info("<<END>> ChatControllerImpl :: getMessageHistory <<END>>");
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CommonApiResponse<String>> createGroupChat(String groupName, List<Long> participantIds) {
        log.info("<<START>> ChatControllerImpl :: createGroupChat <<START>>");
        CommonApiResponse<String> response = chatService.buildGroupChannel(groupName, participantIds);
        log.info("<<END>> ChatControllerImpl :: createGroupChat <<END>>");
        return ResponseEntity.ok(response);
    }
}