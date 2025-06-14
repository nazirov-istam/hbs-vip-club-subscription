package org.example.grand_education.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.grand_education.enums.Role;
import org.example.grand_education.enums.Step;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Location;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = ("bot_user"))
public class User {
    @Id
    @Column(name = ("chat_id"))
    Long chatId;
    @Column(name = ("first_name"))
    String firstName;
    @Column(name = ("last_name"))
    String lastName;
    @Column(name = ("username"))
    String username;
    @Column(name = ("contact"))
    Contact contact;
    @Column(name = ("location"))
    Location location;
    @Column(name = ("created_at"))
    LocalDateTime createdAt;
    @Enumerated(EnumType.STRING)
    @Column(name = ("step"))
    Step step;
    @Column(name = ("role"))
    @Enumerated(EnumType.STRING)
    Role role;
}
