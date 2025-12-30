package com.sipl.ticket.company.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/companies")
@RestController
@CrossOrigin(origins = "*")
@Api(tags = "Company APIs")
public interface CompanyController {

}
