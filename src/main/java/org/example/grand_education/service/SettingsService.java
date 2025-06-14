package org.example.grand_education.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.grand_education.application.Messages;
import org.example.grand_education.model.Setting;
import org.example.grand_education.repository.SettingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class SettingsService {
    private final SettingRepository settingRepository;

    public void createSetting(String channelName) {
        try {
            Setting setting = new Setting();
            setting.setCreatedAt(LocalDateTime.now());
            setting.setChannelName(channelName);
            settingRepository.save(setting);
            log.info("Setting created with channel name: {}", channelName);
        } catch (Exception e) {
            log.info("Setting creation failed");
            throw new RuntimeException("Setting creation failed");
        }
    }

    public void addChannelUrl(String channelUrl) {
        try {
            Setting setting = settingRepository.findTopByOrderByCreatedAtDesc();
            if (channelUrl != null) {
                setting.setChannelUrl(channelUrl);
                settingRepository.save(setting);
                log.info("Setting channelUrl updated");
            }
        } catch (Exception e) {
            log.info("Setting channelUrl update failed");
        }
    }

    public void addChannelId(String channelId) {
        try {
            Setting setting = settingRepository.findTopByOrderByCreatedAtDesc();
            if (channelId != null) {
                setting.setChannelId(channelId);
                settingRepository.save(setting);
                log.info("Setting channelUrl updated");
            }
        } catch (Exception e) {
            log.info("Setting channelUrl update failed");
        }
    }

    public void deleteChannelByName(String channelName) {
        Setting setting = settingRepository.findByChannelName(channelName);
        if (setting != null) {
            settingRepository.delete(setting);
            log.info("Setting deleted");
        } else {
            log.warn("Setting not found");
        }
    }

    public String confirmSetting() {
        Setting setting = settingRepository.findTopByOrderByCreatedAtDesc();
        if (setting != null) {
            return Messages.confirmationMessageForSetting.formatted(
                    setting.getChannelName(),
                    setting.getChannelUrl(),
                    setting.getChannelId()
            );
        }
        return "";
    }


    public void declineSetting() {
        Setting setting = settingRepository.findTopByOrderByCreatedAtDesc();
        if (setting != null) {
            settingRepository.delete(setting);
            log.info("Setting deleted");
        } else {
            log.warn("Setting not found");
        }
    }
}
