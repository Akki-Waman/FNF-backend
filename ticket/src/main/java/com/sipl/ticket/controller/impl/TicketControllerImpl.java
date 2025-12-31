package com.sipl.ticket.controller.impl;

import com.sipl.ticket.controller.TicketController;
import com.sipl.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TicketControllerImpl implements TicketController {
    private final TicketService ticketService;
}
