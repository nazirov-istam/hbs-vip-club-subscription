package org.example.grand_education.service;

import lombok.extern.slf4j.Slf4j;
import org.example.grand_education.configuration.BotConfig;
import org.example.grand_education.model.Setting;
import org.example.grand_education.repository.SettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.objects.chatmember.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Service
@Slf4j
public class CheckService {
    @Lazy
    @Autowired
    private BotConfig botConfig;
    @Autowired
    private SettingRepository settingRepository;

    public boolean checkChatMember(Long userId) {
        List<Setting> channels = settingRepository.findAll();
        for (Setting channel : channels) {
            if (!isSubscribed(channel.getChannelId(), userId)) {
                return false;
            }
        }
        return true;
    }

    private boolean isSubscribed(String chatId, Long userId) {
        try {
            System.out.println("Checking subscription for user: " + userId + " in chat: " + chatId);
            ChatMember chatMember = botConfig.execute(new GetChatMember(chatId, userId));

            if (chatMember instanceof ChatMemberAdministrator
                    || chatMember instanceof ChatMemberOwner
                    || chatMember instanceof ChatMemberRestricted
                    || chatMember instanceof ChatMemberMember) {
                return true;
            } else if (chatMember instanceof ChatMemberLeft || chatMember instanceof ChatMemberBanned) {
                return false;
            }
        } catch (TelegramApiException e) {
            System.err.println("Error occurred while checking chat membership for user: " + userId + " in chat: " + chatId);
        }
        return false;
    }
}
