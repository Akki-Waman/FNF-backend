package com.ensf.fnf.chat.service.impl;

import com.ensf.fnf.chat.service.ChatService;
import com.ensf.fnf.core.dao.entity.ChatMessageEntity;
import com.ensf.fnf.core.dao.entity.ChatParticipantEntity;
import com.ensf.fnf.core.dao.entity.ChatRoomEntity;
import com.ensf.fnf.core.dao.entity.UserEntity;
import com.ensf.fnf.core.dao.repository.ChatMessageRepository;
import com.ensf.fnf.core.dao.repository.ChatParticipantRepository;
import com.ensf.fnf.core.dao.repository.ChatRoomRepository;
import com.ensf.fnf.core.dao.repository.UserRepository;
import com.ensf.fnf.core.dto.responseDto.CommonApiResponse;
import com.ensf.fnf.core.dto.responseDto.CursorPageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ChatServiceImpl implements ChatService {

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final ChatMessageRepository chatMessageRepository;

    // ==========================================
    // PRIVATE HELPER METHODS (SEGREGATION)
    // ==========================================

    private UserEntity getAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private void validateRoomAccess(UserEntity sender, Long roomId) {
        log.debug("Validating room access for userId: {}, roomId: {}", sender.getUserId(), roomId);
        // Logic to check if user is a participant in the room
    }

    private void saveMessageToDatabase(Long roomId, UserEntity sender, String messageText, String mediaUrl) {
        log.debug("Persisting message to DB. Sender: {}", sender.getUserId());
        // MessageEntity message = new MessageEntity(...);
        // messageRepository.save(message);
    }

    private void triggerPushNotificationToRoom(Long roomId, UserEntity sender) {
        log.debug("Dispatching background push notifications to offline users in roomId: {}", roomId);
        // Call notification facade or Kafka producer here
    }

    @Override
    public CommonApiResponse<String> processIncomingMessage(Long roomId, String messageText, String mediaUrl) {
        log.info("Processing incoming real-time message for roomId: {}", roomId);
        UserEntity sender = getAuthenticatedUser();

        ChatRoomEntity room = validateAndGetRoomAccess(sender.getUserId(), roomId);
        saveMessageToDatabase(room, sender, messageText, mediaUrl);

        return CommonApiResponse.<String>builder().success(true).message("Message delivered").build();
    }

    @Override
    @Transactional(readOnly = true)
    public CommonApiResponse<List<Object>> fetchUserInbox() {
        UserEntity user = getAuthenticatedUser();
        log.info("Fetching active inbox rooms for user: {}", user.getUserId());

        List<ChatParticipantEntity> participantRecords = chatParticipantRepository.findActiveRoomsByUserId(user.getUserId());

        // Extracting room definitions safely
        List<Object> inboxFeeds = participantRecords.stream()
                .map(record -> record.getRoom())
                .collect(Collectors.toList());

        return CommonApiResponse.<List<Object>>builder().success(true).data(inboxFeeds).build();
    }

    @Override
    @Transactional(readOnly = true)
    public CommonApiResponse<CursorPageResponse<Object>> fetchRoomMessages(Long roomId, Long cursorId, int limit) {
        UserEntity user = getAuthenticatedUser();
        log.info("Fetching message history for room: {} via Cursor: {}", roomId, cursorId);

        validateAndGetRoomAccess(user.getUserId(), roomId);
        CursorPageResponse<Object> messagePage = queryMessagesWithCursor(roomId, cursorId, limit);

        return CommonApiResponse.<CursorPageResponse<Object>>builder().success(true).data(messagePage).build();
    }

    @Override
    public CommonApiResponse<String> buildGroupChannel(String groupName, List<Long> participantIds) {
        log.info("Initiating dynamic group creation for: {}", groupName);
        UserEntity creator = getAuthenticatedUser();

        ChatRoomEntity newRoom = createBaseGroupRoom(groupName);
        linkParticipantsToRoom(newRoom, creator.getUserId(), participantIds);

        log.info("Dynamic Group '{}' created successfully with Database ID: {}", groupName, newRoom.getRoomId());
        return CommonApiResponse.<String>builder()
                .success(true)
                .message("Group chat configured and saved successfully")
                .build();
    }

    // ==========================================
    // PRIVATE HELPER METHODS (SEGREGATION)
    // ==========================================


    private ChatRoomEntity validateAndGetRoomAccess(Long userId, Long roomId) {
        log.debug("Verifying access control. UserId: {} -> RoomId: {}", userId, roomId);
        ChatParticipantEntity participant = chatParticipantRepository.verifyUserInRoom(userId, roomId)
                .orElseThrow(() -> new RuntimeException("User is not authorized to access this room."));
        return participant.getRoom();
    }

    private void saveMessageToDatabase(ChatRoomEntity room, UserEntity sender, String messageText, String mediaUrl) {
        log.debug("Persisting new message to DB. Sender: {}", sender.getUserId());
        ChatMessageEntity message = ChatMessageEntity.builder()
                .room(room)
                .sender(sender)
                .messageText(messageText)
                .mediaUrl(mediaUrl)
                .build();
        chatMessageRepository.save(message);
    }

    private CursorPageResponse<Object> queryMessagesWithCursor(Long roomId, Long cursorId, int limit) {
        log.debug("Running cursor pagination query. Limit: {}", limit);

        List<ChatMessageEntity> entities = chatMessageRepository.findMessagesByCursor(roomId, cursorId, PageRequest.of(0, limit + 1));

        boolean hasNext = entities.size() > limit;
        if (hasNext) {
            entities.remove(limit); // Pop the look-ahead record
        }

        Long nextCursor = entities.isEmpty() ? null : entities.get(entities.size() - 1).getMessageId();

        // Casting generic Objects to fulfill interface contract without exposing direct DB Entities
        List<Object> contentList = new ArrayList<>(entities);

        return new CursorPageResponse<>(contentList, nextCursor, hasNext);
    }

    private ChatRoomEntity createBaseGroupRoom(String groupName) {
        log.debug("Allocating database record for new group: {}", groupName);
        ChatRoomEntity room = ChatRoomEntity.builder()
                .roomName(groupName)
                .isGroup(true)
                .build();
        return chatRoomRepository.save(room);
    }

    private void linkParticipantsToRoom(ChatRoomEntity room, Long creatorId, List<Long> participantIds) {
        log.debug("Linking {} participants to database roomId: {}", participantIds.size() + 1, room.getRoomId());

        // Add Creator
        UserEntity creator = userRepository.findById(creatorId).orElseThrow();
        chatParticipantRepository.save(ChatParticipantEntity.builder().room(room).user(creator).build());

        // Add Target Participants
        for (Long targetId : participantIds) {
            userRepository.findById(targetId).ifPresent(targetUser -> {
                chatParticipantRepository.save(ChatParticipantEntity.builder().room(room).user(targetUser).build());
            });
        }
    }
}