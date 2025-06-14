package org.example.grand_education.repository;

import org.example.grand_education.model.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingRepository extends JpaRepository<Setting, Integer> {
    Setting findTopByOrderByCreatedAtDesc();
    Setting findByChannelName(String channelName);
}
