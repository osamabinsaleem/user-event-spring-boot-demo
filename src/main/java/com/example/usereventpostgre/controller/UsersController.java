package com.example.usereventpostgre.controller;

import com.example.usereventpostgre.exception.ResourceNotFoundException;
import com.example.usereventpostgre.model.UserEvent;
import com.example.usereventpostgre.model.Users;
import com.example.usereventpostgre.repository.UserEventRepository;
import com.example.usereventpostgre.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UsersController {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    //private ApplicationUserRepository applicationUserRepository;
    @Autowired
    private UsersRepository usersRepository;

    @GetMapping("/user")
    public Page<Users> getUserEvent(Pageable pageable) {
        return usersRepository.findAll(pageable);
    }


    @PostMapping("/user")
    public Users createUserEvent(@Valid @RequestBody Users user) {

        return usersRepository.save(user);
    }
    @PostMapping("users/sign-up")
    public Users signUp(@Valid @RequestBody Users user) {
        System.out.println(user);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
       return usersRepository.save(user);
    }

    @PutMapping("/user/{userId}")
    public Users updateUserEvent(@PathVariable Long userId,
                                     @Valid @RequestBody Users userEventRequest) {
        return usersRepository.findById(userId)
                .map(question -> {
                    question.setUsername(userEventRequest.getUsername());
                    question.setLatitude(userEventRequest.getLatitude());
                    question.setLongitude(userEventRequest.getLongitude());
                    return usersRepository.save(question);
                }).orElseThrow(() -> new ResourceNotFoundException("Question not found with id " + userId));
    }
    @DeleteMapping("/user/{questionId}")
    public ResponseEntity<?> deleteQuestion(@PathVariable Long questionId) {
        return usersRepository.findById(questionId)
                .map(question -> {
                    usersRepository.delete(question);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("Question not found with id " + questionId));
    }

}
