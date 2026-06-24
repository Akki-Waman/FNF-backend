package com.ensf.fnf.app.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user/profile")
@CrossOrigin("*")
@Api(tags = "User Profile API")
public interface UserProfileController {


}
