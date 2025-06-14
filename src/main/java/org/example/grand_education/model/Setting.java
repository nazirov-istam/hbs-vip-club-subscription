package org.example.grand_education.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = ("settings"))
public class Setting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ("id"))
    private Integer id;
    @Column(name = ("channel_name"))
    private String channelName;
    @Column(name = ("channel_url"))
    private String channelUrl;
    @Column(name = ("channel_id"))
    private String channelId;
    @Column(name = ("created_at"))
    private LocalDateTime createdAt;
}
