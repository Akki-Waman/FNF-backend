package com.sipl.ticket.controller;


import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/tickets")
@RestController
@CrossOrigin(origins = "*")
@Api(tags = "Tickets APIs")
public interface TicketController {

}
