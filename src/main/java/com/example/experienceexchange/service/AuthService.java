package com.example.experienceexchange.service;

import com.example.experienceexchange.constant.Role;
import com.example.experienceexchange.constant.Status;
import com.example.experienceexchange.dto.UserDto;
import com.example.experienceexchange.dto.LoginDto;
import com.example.experienceexchange.dto.TokenDto;
import com.example.experienceexchange.exception.EmailNotUniqueException;
import com.example.experienceexchange.model.User;
import com.example.experienceexchange.repository.interfaceRepo.IUserRepository;
import com.example.experienceexchange.security.JwtUserDetails;
import com.example.experienceexchange.security.provider.IJwtTokenProvider;
import com.example.experienceexchange.service.interfaceService.IAuthService;
import com.example.experienceexchange.util.date.DateUtil;
import com.example.experienceexchange.util.factory.TokenDtoFactory;
import com.example.experienceexchange.util.mapper.UserMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService implements IAuthService {

    private final AuthenticationManager manager;
    private final IJwtTokenProvider jwtTokenProvider;
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public AuthService(AuthenticationManager manager,
                       IJwtTokenProvider jwtTokenProvider,
                       IUserRepository userRepository,
                       PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.manager = manager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Transactional
    @Override
    public TokenDto authentication(LoginDto loginDto) {
        Authentication authentication = manager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
        JwtUserDetails userDetails = (JwtUserDetails) authentication.getPrincipal();
        String token = jwtTokenProvider.createToken(userDetails.getUsername(), userDetails.getRole());
        return TokenDtoFactory.createTokenDto(token, userDetails.getUsername());
    }

    @Transactional
    @Override
    public void registrationUser(UserDto registrationDto) {
        registrationDto.setRole(Role.USER);
        registration(registrationDto);
    }

    @Transactional
    @Override
    public void registrationAdmin(UserDto registrationDto) {
        registrationDto.setRole(Role.ADMIN);
        registration(registrationDto);
    }

    private void registration(UserDto userDto) {
        User account = userRepository.findByEmail(userDto.getEmail());
        if (account != null) {
            throw new EmailNotUniqueException();
        }
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userDto.setStatus(Status.ACTIVE);
        userDto.setCreated(DateUtil.dateTimeNow());
        userDto.setUpdated(DateUtil.dateTimeNow());
        User newUser = userMapper.userDtoToUser(userDto);
        userRepository.save(newUser);
    }
}
