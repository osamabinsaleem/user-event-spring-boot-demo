package com.example.usereventpostgre.repository;

import com.example.usereventpostgre.model.UserEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public  interface UserEventRepository extends JpaRepository<UserEvent, Long> {
    List<UserEvent> findByUserId(Long userId);

}
