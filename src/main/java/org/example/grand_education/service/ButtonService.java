package org.example.grand_education.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.grand_education.application.Commands;
import org.example.grand_education.application.Messages;
import org.example.grand_education.enums.Role;
import org.example.grand_education.model.Setting;
import org.example.grand_education.model.User;
import org.example.grand_education.repository.SettingRepository;
import org.example.grand_education.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ButtonService {
    private final UserRepository userRepository;
    private final SettingRepository settingRepository;

    public SendMessage checkChannelSubscription(long chatId, String text) {
        try {
            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rows = new ArrayList<>();

            List<Setting> channels = settingRepository.findAll();

            for (Setting channel : channels) {
                InlineKeyboardButton channelButton = new InlineKeyboardButton();
                channelButton.setText("➕ " + channel.getChannelName() + " ➕");
                channelButton.setUrl(channel.getChannelUrl());
                rows.add(List.of(channelButton));
            }

            InlineKeyboardButton checkButton = new InlineKeyboardButton();
            checkButton.setText(text.split("00001")[1]);
            checkButton.setCallbackData(Commands.JOIN_CHANNEL);
            rows.add(List.of(checkButton));

            markupInline.setKeyboard(rows);

            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText(text.split("00001")[0]);
            message.setReplyMarkup(markupInline);
            return message;
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public ReplyKeyboardMarkup settingSectionButtons() {
        KeyboardButton addChannel = new KeyboardButton();
        KeyboardButton deleteChannel = new KeyboardButton();
        KeyboardButton allChannels = new KeyboardButton();
        KeyboardButton back = new KeyboardButton();

        addChannel.setText(Messages.addChannel);
        deleteChannel.setText(Messages.deleteChannel);
        allChannels.setText(Messages.allChannels);
        back.setText(Messages.back);

        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();
        KeyboardRow row4 = new KeyboardRow();

        row1.add(addChannel);
        row2.add(deleteChannel);
        row3.add(allChannels);
        row4.add(back);

        ArrayList<KeyboardRow> rows = new ArrayList<>();
        rows.add(row1);
        rows.add(row2);
        rows.add(row3);
        rows.add(row4);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(rows);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        return replyKeyboardMarkup;
    }

    public ReplyKeyboardMarkup adminSectionButtons() {
        KeyboardButton settingsSection = new KeyboardButton();
        KeyboardButton statsSection = new KeyboardButton();
        KeyboardButton addVideo = new KeyboardButton();
        KeyboardButton allVideos = new KeyboardButton();
        KeyboardButton changeRole = new KeyboardButton();
        KeyboardButton deleteVideo = new KeyboardButton();

        settingsSection.setText(Messages.settingsSection);
        statsSection.setText(Messages.statsSection);
        addVideo.setText(Messages.addVideo);
        allVideos.setText(Messages.viewAllVideos);
        changeRole.setText(Messages.changeRoleToManager);
        deleteVideo.setText(Messages.deleteVideo);

        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();
        KeyboardRow row4 = new KeyboardRow();

        row1.add(statsSection);
        row2.add(addVideo);
        row2.add(deleteVideo);
        row3.add(allVideos);
        row3.add(settingsSection);
        row4.add(changeRole);

        ArrayList<KeyboardRow> rows = new ArrayList<>();
        rows.add(row1);
        rows.add(row2);
        rows.add(row3);
        rows.add(row4);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(rows);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        return replyKeyboardMarkup;
    }

    public ReplyKeyboardMarkup buttonsForManager() {
        KeyboardButton addAdmin = new KeyboardButton();
        KeyboardButton deleteAdmin = new KeyboardButton();
        KeyboardButton changeRole = new KeyboardButton();

        addAdmin.setText(Messages.addAdmin);
        deleteAdmin.setText(Messages.deleteAdmin);
        changeRole.setText(Messages.changeRoleToAdmin);

        KeyboardRow row = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        row.add(addAdmin);
        row.add(deleteAdmin);
        row.add(changeRole);

        ArrayList<KeyboardRow> rows = new ArrayList<>();
        rows.add(row);
        rows.add(row2);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(rows);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        return replyKeyboardMarkup;
    }

    public ReplyKeyboardMarkup adminList() {
        List<User> allAdmins = userRepository.findAllByRole(Role.ADMIN);
        ArrayList<KeyboardRow> rows = new ArrayList<>();

        for (User allAdmin : allAdmins) {
            KeyboardButton admin = new KeyboardButton();
            admin.setText(allAdmin.getFirstName());
            KeyboardRow row = new KeyboardRow();
            row.add(admin);
            rows.add(row);
        }

        KeyboardButton back = new KeyboardButton();
        back.setText(Messages.back);
        KeyboardRow row = new KeyboardRow();
        row.add(back);
        rows.add(row);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(rows);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        return replyKeyboardMarkup;
    }

    public ReplyKeyboardMarkup channelList() {
        List<Setting> allChannels = settingRepository.findAll();
        ArrayList<KeyboardRow> rows = new ArrayList<>();

        for (Setting allAdmin : allChannels) {
            KeyboardButton admin = new KeyboardButton();
            admin.setText(allAdmin.getChannelName());
            KeyboardRow row = new KeyboardRow();
            row.add(admin);
            rows.add(row);
        }
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(rows);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        return replyKeyboardMarkup;
    }

    public ReplyKeyboardMarkup twoButtons() {
        KeyboardButton confirmButton = new KeyboardButton();
        KeyboardButton declineButton = new KeyboardButton();

        confirmButton.setText(Messages.confirmMessageUz);
        declineButton.setText(Messages.declineMessageUz);


        KeyboardRow row1 = new KeyboardRow();
        row1.add(confirmButton);
        row1.add(declineButton);
        ArrayList<KeyboardRow> rows = new ArrayList<>();
        rows.add(row1);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(rows);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        return replyKeyboardMarkup;
    }

    public ReplyKeyboardMarkup userButton() {
        KeyboardButton getAllVideos = new KeyboardButton();

        getAllVideos.setText(Messages.getAllVideoLessons);

        KeyboardRow row = new KeyboardRow();
        row.add(getAllVideos);

        ArrayList<KeyboardRow> rows = new ArrayList<>();
        rows.add(row);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(rows);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        return replyKeyboardMarkup;
    }

    public ReplyKeyboardMarkup twoButtons2() {
        KeyboardButton confirmButton = new KeyboardButton();
        KeyboardButton declineButton = new KeyboardButton();

        confirmButton.setText(Messages.deleteAllVideos);
        declineButton.setText(Messages.deleteSeparately);

        KeyboardRow row1 = new KeyboardRow();
        row1.add(confirmButton);
        row1.add(declineButton);
        ArrayList<KeyboardRow> rows = new ArrayList<>();
        rows.add(row1);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(rows);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        return replyKeyboardMarkup;
    }

    public ReplyKeyboardMarkup finishButton() {
        KeyboardButton finish = new KeyboardButton();

        finish.setText(Messages.finish);

        KeyboardRow row = new KeyboardRow();
        row.add(finish);

        ArrayList<KeyboardRow> rows = new ArrayList<>();
        rows.add(row);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(rows);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        return replyKeyboardMarkup;
    }
}