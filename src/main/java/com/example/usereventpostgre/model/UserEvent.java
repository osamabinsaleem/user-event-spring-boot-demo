package com.example.usereventpostgre.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.tomcat.jni.User;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NamedQueries({
        @NamedQuery(name = UserEvent.NAMED_QUERY_GET_BY_STATUS, query = "Select e from UserEvent e where e.user =:user")
})

@Entity
@Table(name = "user_event_table")
public class UserEvent extends AuditModel {
    public static final String NAMED_QUERY_GET_BY_STATUS = "UserEvent.getByUserId";
    @Id
    @GeneratedValue(generator = "question_generator")
    @SequenceGenerator(
            name = "question_generator",
            sequenceName = "question_sequence",
            initialValue = 1000
    )
    private Long id;

    private String name;

    @Column(columnDefinition = "text")
    private String event;

    public String getEvent() {
        return event;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    //@OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Users user;

    public void setEvent(String event) {
        this.event = event;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
}
