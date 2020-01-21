package com.example.usereventpostgre.controller;

import com.example.usereventpostgre.exception.ResourceNotFoundException;
import com.example.usereventpostgre.model.UserEvent;
import com.example.usereventpostgre.model.Users;
import com.example.usereventpostgre.repository.UserEventRepository;
import com.example.usereventpostgre.repository.UsersRepository;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.*;

import static com.example.usereventpostgre.model.UserEvent.NAMED_QUERY_GET_BY_STATUS;


@RestController
public class UserEventController {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserEventRepository userEventRepository;

    @Autowired
    private UsersRepository usersRepository;

    /*@GetMapping("/user_event")
    public Page<UserEvent> getUserEvent(Pageable pageable) {
        return userEventRepository.findAll(pageable);
    }*/

    private static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        }
        else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            if (unit.equals("K")) {
                dist = dist * 1.609344;
            } else if (unit.equals("N")) {
                dist = dist * 0.8684;
            }
            return (dist);
        }
    }

    @GetMapping("/user_event/{userId}")
    public List<UserEvent> getEventsByUserId(@PathVariable Long userId) {
        return userEventRepository.findByUserId(userId);
    }

    public Optional<Users> findUser(Long userId){
       return usersRepository.findById(userId);

    }

    @GetMapping("/near_user_events/{userId}")
    public List<UserEvent> getAllNearEventsByUserId(@PathVariable Long userId) {
        List<UserEvent> temp = userEventRepository.findAll();
        List<Users> allUsers = usersRepository.findAll();
        Optional<Users> curr_user = usersRepository.findById(userId);
        
        List<UserEvent> allEvents = new ArrayList<UserEvent>();
        usersRepository.findById(userId)
                .map(user -> {
                    for (int i = 0; i < allUsers.size(); i++) {
                        Users element = allUsers.get(i);
                        if(userId != element.getId()){

                            double dist = distance(element.getLatitude(), element.getLongitude(), user.getLatitude(), user.getLongitude(), "K");
                            if(dist < 10){

                                List<UserEvent> event1 =  userEventRepository.findByUserId(element.getId());
                                //System.out.print(element.getName());
                                for (int a = 0; a < event1.size(); a++) {
                                    //System.out.println(event1.get(a).getName());
                                    allEvents.add(event1.get(a));
                                }
                            }

                        }
                    }

                    return true;
                }).orElseThrow(() -> new ResourceNotFoundException("Question not found with id " + userId));

        return allEvents;
    }


    /*@PostMapping("/user_event")
    public UserEvent createUserEvent(@Valid @RequestBody UserEvent userEvent) {
        return userEventRepository.save(userEvent);
    }*/

    @PostMapping("/user_event/{userId}")
    public @Valid UserEvent addUserEvent(@PathVariable Long userId,
                                         @Valid @RequestBody UserEvent event) {
        return usersRepository.findById(userId)
                .map(user -> {
                    event.setUser(user);
                    return userEventRepository.save(event);
                }).orElseThrow(() -> new ResourceNotFoundException("Question not found with id " + userId));
    }

   /* @PutMapping("/user_event/{userId}")
    public UserEvent updateUserEvent(@PathVariable Long userId,
                                   @Valid @RequestBody UserEvent userEventRequest) {
        return userEventRepository.findById(userId)
                .map(question -> {
                    question.setName(userEventRequest.getName());
                    question.setEvent(userEventRequest.getEvent());
                    return userEventRepository.save(question);
                }).orElseThrow(() -> new ResourceNotFoundException("Question not found with id " + userId));
    }


    @DeleteMapping("/questions/{userId}")
    public ResponseEntity<?> deleteUserEvent(@PathVariable Long userId) {
        return userEventRepository.findById(userId)
                .map(question -> {
                    userEventRepository.delete(question);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("Question not found with id " + userId));
    }*/
}
