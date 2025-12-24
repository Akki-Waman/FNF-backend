package com.sipl.ticket.core.helper;

import com.sipl.ticket.core.dao.entity.Users;
import com.sipl.ticket.core.dao.repository.UsersRepository;
import com.sipl.ticket.core.util.JwtUtil;
import com.sipl.ticket.core.util.TokenFromUserHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Slf4j
@Component("utilUserManager")
@RequiredArgsConstructor
public class UserManager {

    private final JwtUtil jwtTokenHelper;
    private final UsersRepository userRepository;

    public Users getUser(HttpServletRequest httpRequest) {
        try {
            String userToken = TokenFromUserHelper.getTokenFromRequest(httpRequest);
            String userName = jwtTokenHelper.extractUsername(userToken);
            Optional<Users> userFromDb = userRepository.findByUserName(userName);
            log.info("userFromDb : " + userFromDb);
            if (userFromDb.isPresent()) {
                return userFromDb.get();
            }
        } catch (Exception e) {
            log.error("Exception in UserMasterManager : ", e);
        }
        return null;
    }
}
