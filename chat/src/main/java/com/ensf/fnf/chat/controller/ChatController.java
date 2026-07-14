package com.ensf.fnf.chat.controller;


import com.ensf.fnf.core.dto.responseDto.CommonApiResponse;
import com.ensf.fnf.core.dto.responseDto.CursorPageResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/chats")
@Api(tags = "Chat & Messaging API")
public interface ChatController {

    @PostMapping("/{roomId}/messages")
    @ApiOperation("Send a real-time message to a specific room")
    ResponseEntity<CommonApiResponse<String>> sendMessage(
            @PathVariable Long roomId,
            @RequestParam String messageText,
            @RequestParam(required = false) String mediaUrl);

    @GetMapping
    @ApiOperation("Fetch list of all active chat rooms/inboxes with recent message snippets")
    ResponseEntity<CommonApiResponse<List<Object>>> getActiveChats();

    @GetMapping("/{roomId}/messages")
    @ApiOperation("Fetch message history for a specific room using cursor pagination")
    ResponseEntity<CommonApiResponse<CursorPageResponse<Object>>> getMessageHistory(
            @PathVariable Long roomId,
            @RequestParam(required = false) Long cursorId,
            @RequestParam(defaultValue = "30") int limit);

    @PostMapping("/group")
    @ApiOperation("Instantiate a new multi-participant family group chat")
    ResponseEntity<CommonApiResponse<String>> createGroupChat(
            @RequestParam String groupName,
            @RequestBody List<Long> participantIds);
}