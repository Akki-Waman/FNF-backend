package com.ensf.fnf.app.service.impl;

import com.ensf.fnf.app.service.UserProfileService;
import com.ensf.fnf.core.dao.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserProfileServiceImpl implements UserProfileService {

    private final UserRepository userRepository;


}
