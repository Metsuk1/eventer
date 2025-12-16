package org.eventer.service;

import org.eventer.dto.UserDto;
import org.eventer.entity.User;
import org.eventer.enums.UserRole;
import org.eventer.exceptions.DuplicateResourceException;
import org.eventer.exceptions.EntityNotFoundException;
import org.eventer.exceptions.ValidationException;
import org.eventer.repository.UserRepository;
import org.eventer.utils.Mapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final Mapper mapper;

    public UserService(UserRepository userRepository, Mapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public UserDto findById(Long id) {
        if(id == null){
            throw new ValidationException("id cannot be null");
        }
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        return mapper.toDto(user);
    }

    @Transactional(readOnly = true)
    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserDto findByEmail(String email) {
        if(email == null){
            throw new ValidationException("email cannot be null");
        }
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));

        return mapper.toDto(user);
    }

    @Transactional(readOnly = true)
    public List<UserDto> findByGroupId(Long groupId) {
        if(groupId == null){
            throw new ValidationException("groupId cannot be null");
        }

        return userRepository.findByGroupId(groupId)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Transactional
    public UserDto create(UserDto userDto) {
        if(userDto == null){
            throw new ValidationException("UserDto cannot be null");
        }

        if(userDto.getEmail() == null || userDto.getEmail().isBlank()){
            throw new ValidationException("email cannot be empty");
        }

        if(userRepository.findByEmail(userDto.getEmail()).isPresent()){
            throw new DuplicateResourceException("User with email: " + userDto.getEmail() + " already exists");
        }

        User user = mapper.toUser(userDto);
        user.setRole(UserRole.USER.name()); // it is by default

        Long generatedId = userRepository.save(user);
        user.setId(generatedId);

        return mapper.toDto(user);
    }

    @Transactional
    public void update(Long id,UserDto userDto) {
        if(id == null){
            throw new ValidationException("id cannot be null");
        }

        if (userDto == null){
            throw new ValidationException("userDto cannot be null");
        }

        userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        User user = mapper.toUser(userDto);
        userRepository.update(id, user);
    }

    @Transactional
    public void deleteById(Long id) {
        if(id == null){
            throw new ValidationException("id cannot be null");
        }

        userRepository.deleteById(id);
    }

    @Transactional
    public void updatePassword(Long id, String password) {
        if(id == null){
            throw new ValidationException("id cannot be null");
        }

        if(password == null || password.isBlank() || password.length() < 6){
            throw new ValidationException("password cannot be null or empty or less than 6 characters");
        }


        userRepository.updatePassword(id, password);
    }

    @Transactional
    public void updateRole(Long id, String role) {
        if(id == null){
            throw new ValidationException("id cannot be null");
        }

        if (role == null || role.isBlank()){
            throw new ValidationException("role cannot be empty");
        }

        UserRole parsedRole;
        try{
            parsedRole = UserRole.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e){
            throw new ValidationException("Invalid role: " + role);
        }

        userRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        userRepository.updateRole(id, parsedRole.name());
    }

}
