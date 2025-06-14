package org.example.grand_education.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.grand_education.application.Commands;
import org.example.grand_education.application.Messages;
import org.example.grand_education.configuration.StaticVariables;
import org.example.grand_education.enums.Role;
import org.example.grand_education.enums.Step;
import org.example.grand_education.model.User;
import org.example.grand_education.model.VideoEntity;
import org.example.grand_education.repository.SettingRepository;
import org.example.grand_education.repository.UserRepository;
import org.example.grand_education.repository.VideosRepository;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MainService {
    private final UserService userService;
    private final CheckService checkService;
    private final ButtonService buttonService;
    private final SenderService senderService;
    private final SettingsService settingsService;
    private final UserRepository userRepository;
    private final SettingRepository settingRepository;
    private final AdminService adminService;
    private final VideosRepository videosRepository;

    public List<PartialBotApiMethod<?>> mainBot(Update update) {
        List<PartialBotApiMethod<?>> responses = new ArrayList<>();
        SendMessage sendMessage = new SendMessage();

        if (update.hasMessage()) {
            long chatId = update.getMessage().getChatId();
            String userText;
            if (update.getMessage().hasVideo()) {
                userText = ":";
            } else {
                userText = update.getMessage().getText();
            }
            sendMessage.setChatId(chatId);
            User user = userService.getCurrentUser(chatId);

            if (userText.equals(Commands.START)) {
                if (chatId == StaticVariables.OWNER_CHAT_ID) {
                    userService.registerUser(update.getMessage());
                    userService.updateStep(chatId, Step.MANAGER_MAIN);
                    sendMessage.setText(Messages.managerSuccess);
                    sendMessage.setReplyMarkup(buttonService.buttonsForManager());
                } else if (userService.checkForAdmin(chatId)) {
                    sendMessage.setText(Messages.adminSuccess);
                    userService.updateStep(chatId, Step.ADMIN_MAIN);
                    sendMessage.setReplyMarkup(buttonService.adminSectionButtons());
                } else if (user == null) {
                    userService.updateStep(chatId, Step.START);
                    userService.registerUserUnauthorized(update.getMessage());
                    if (!checkService.checkChatMember(chatId)) {
                        sendMessage = buttonService.checkChannelSubscription(chatId, senderService.message("channelText", Messages.channelText));
                    } else {
                        userService.updateStep(chatId, Step.USER_MAIN);
                        sendMessage.setText(Messages.success);
                        userService.updateRole(chatId, Role.USER);
                        sendMessage.setReplyMarkup(buttonService.userButton());
                    }
                } else if (user.getRole() == Role.USER) {
                    sendMessage.setText(Messages.userAlreadyExist);
                    userService.updateStep(chatId, Step.USER_MAIN);
                    sendMessage.setReplyMarkup(buttonService.userButton());
                } else if (user.getRole() == Role.MANAGER) {
                    sendMessage.setText(Messages.userAlreadyExist);
                    userService.updateStep(chatId, Step.MANAGER_MAIN);
                    sendMessage.setReplyMarkup(buttonService.buttonsForManager());
                } else if (user.getRole() == Role.ADMIN) {
                    sendMessage.setText(Messages.userAlreadyExist);
                    userService.updateStep(chatId, Step.ADMIN_MAIN);
                    sendMessage.setReplyMarkup(buttonService.adminSectionButtons());
                }
            } else if (user.getRole() == Role.MANAGER) {
                if (userText.startsWith(Messages.addAdmin)) {
                    userService.updateStep(chatId, Step.ADD_ADMIN_CHAT_ID);
                    sendMessage.setText(Messages.addAdminChatId);
                } else if (user.getStep() == Step.ADD_ADMIN_CHAT_ID) {
                    userService.updateStep(chatId, Step.ADD_ADMIN_NAME);
                    userService.registerAdmin(userText);
                    sendMessage.setText(Messages.addAdminName);
                } else if (user.getStep() == Step.ADD_ADMIN_NAME) {
                    userService.updateStep(chatId, Step.MANAGER_MAIN);
                    userService.registerAdminName(userText);
                    sendMessage.setText(Messages.addAdminSuccess);
                    sendMessage.setReplyMarkup(buttonService.buttonsForManager());
                } else if (userText.startsWith(Messages.deleteAdmin)) {
                    if (userRepository.existsByRole(Role.ADMIN)) {
                        userService.updateStep(chatId, Step.DELETE_ADMIN_LIST);
                        sendMessage.setText(Messages.deleteAdminMessage);
                        sendMessage.setReplyMarkup(buttonService.adminList());
                    } else {
                        userService.updateStep(chatId, Step.MANAGER_MAIN);
                        sendMessage.setText(Messages.deleteAdminNo);
                        sendMessage.setReplyMarkup(buttonService.buttonsForManager());
                    }
                } else if (!userText.equals(Messages.back) && user.getStep() == Step.DELETE_ADMIN_LIST) {
                    userService.updateStep(chatId, Step.MANAGER_MAIN);
                    userService.deleteAdmin(userText);
                    sendMessage.setText(Messages.deleteAdminSuccess);
                    sendMessage.setReplyMarkup(buttonService.buttonsForManager());
                } else if (userText.equals(Messages.back)) {
                    if (user.getStep() == Step.DELETE_ADMIN_LIST) {
                        userService.updateStep(chatId, Step.MANAGER_MAIN);
                        sendMessage.setText(Messages.adminMainMenu);
                        sendMessage.setReplyMarkup(buttonService.buttonsForManager());
                    }
                } else if (userText.equals(Messages.changeRoleToAdmin)) {
                    userService.updateRole(chatId, Role.ADMIN);
                    userService.updateStep(chatId, Step.ADMIN_MAIN);
                    sendMessage.setText(Messages.adminSuccess);
                    sendMessage.setReplyMarkup(buttonService.adminSectionButtons());
                } else {
                    sendMessage.setText(Messages.noCommandPrompt);
                    userService.updateStep(chatId, Step.MANAGER_MAIN);
                    sendMessage.setReplyMarkup(buttonService.buttonsForManager());
                }
            }
            else if (user.getRole() == Role.ADMIN) {
                if (userText.equals(Messages.changeRoleToManager)) {
                    if (StaticVariables.OWNER_CHAT_ID.equals(chatId)) {
                        userService.updateRole(chatId, Role.MANAGER);
                        sendMessage.setText(Messages.managerSuccess);
                        userService.updateStep(chatId, Step.MANAGER_MAIN);
                        sendMessage.setReplyMarkup(buttonService.buttonsForManager());
                    } else {
                        sendMessage.setText(Messages.failChangeRole);
                        userService.updateStep(chatId, Step.ADMIN_MAIN);
                        sendMessage.setReplyMarkup(buttonService.adminSectionButtons());
                    }
                } else if (userText.equals(":") && update.getMessage().hasVideo()) {
                    adminService.saveVideo(update.getMessage());
                    sendMessage.setText(Messages.addVideoSuccess);
                } else if (userText.equals(Messages.settingsSection)) {
                    userService.updateStep(chatId, Step.ADMIN_SETTINGS);
                    sendMessage.setText(Messages.adminSettingsSection);
                    sendMessage.setReplyMarkup(buttonService.settingSectionButtons());
                } else if (userText.equals(Messages.addChannel)) {
                    userService.updateStep(chatId, Step.ADD_CHANNEL_NAME);
                    sendMessage.setText(Messages.addChannelName);
                } else if (user.getStep() == Step.ADD_CHANNEL_NAME) {
                    userService.updateStep(chatId, Step.ADD_CHANNEL_URL);
                    settingsService.createSetting(userText);
                    sendMessage.setText(Messages.addChannelUrl);
                } else if (user.getStep() == Step.ADD_CHANNEL_URL) {
                    userService.updateStep(chatId, Step.ADD_CHANNEL_ID);
                    settingsService.addChannelUrl(userText);
                    sendMessage.setText(Messages.addChannelId);
                } else if (user.getStep() == Step.ADD_CHANNEL_ID) {
                    userService.updateStep(chatId, Step.ASK_CONFIRMATION);
                    settingsService.addChannelId(userText);
                    sendMessage.setText(settingsService.confirmSetting());
                    sendMessage.setReplyMarkup(buttonService.twoButtons());
                } else if (user.getStep() == Step.ASK_CONFIRMATION) {
                    if (userText.equals(Messages.confirmMessageUz)) {
                        sendMessage.setText(Messages.addChannelSuccess);
                    } else {
                        settingsService.declineSetting();
                        sendMessage.setText(Messages.addChannelDecline);
                    }
                    userService.updateStep(chatId, Step.ADMIN_MAIN);
                    sendMessage.setReplyMarkup(buttonService.adminSectionButtons());
                } else if (userText.equals(Messages.deleteChannel)) {
                    if (settingRepository.count() > 0) {
                        userService.updateStep(chatId, Step.DELETE_CHANNEL_LIST);
                        sendMessage.setText(Messages.deleteChannelMessage);
                        sendMessage.setReplyMarkup(buttonService.channelList());
                    } else {
                        userService.updateStep(chatId, Step.ADMIN_MAIN);
                        sendMessage.setText(Messages.deleteChannelNo);
                        sendMessage.setReplyMarkup(buttonService.adminSectionButtons());
                    }
                } else if (user.getStep() == Step.DELETE_CHANNEL_LIST) {
                    userService.updateStep(chatId, Step.ADMIN_MAIN);
                    settingsService.deleteChannelByName(userText);
                    sendMessage.setText(Messages.deleteChannelSuccess);
                    sendMessage.setReplyMarkup(buttonService.adminSectionButtons());
                } else if (userText.equals(Messages.allChannels)) {
                    if (settingRepository.count() > 0) {
                        sendMessage.setText(userService.channelsInfo());
                        userService.updateStep(chatId, Step.ADMIN_MAIN);
                        sendMessage.setReplyMarkup(buttonService.adminSectionButtons());
                    } else {
                        sendMessage.setText(Messages.deleteChannelNo);
                    }
                    userService.updateStep(chatId, Step.ADMIN_MAIN);
                    sendMessage.setReplyMarkup(buttonService.adminSectionButtons());
                } else if (userText.equals(Messages.statsSection)) {
                    if (userRepository.existsByRole(Role.USER)) {
                        sendMessage.setText(Messages.noUserMessage);
                    } else {
                        sendMessage.setText(userService.usersInfo());
                    }
                    userService.updateStep(chatId, Step.ADMIN_MAIN);
                    sendMessage.setReplyMarkup(buttonService.adminSectionButtons());
                } else if (userText.equals(Messages.addVideo)) {
                    userService.updateStep(chatId, Step.ADD_VIDEO);
                    sendMessage.setText(Messages.addVideoMessage);
                } else if (userText.equals(Messages.viewAllVideos)) {
                    if (videosRepository.count() == 0) {
                        sendMessage.setText(Messages.noVideos);
                        userService.updateStep(chatId, Step.ADMIN_MAIN);
                        sendMessage.setReplyMarkup(buttonService.adminSectionButtons());
                    } else {
                        userService.updateStep(chatId, Step.ADMIN_MAIN);
                        List<VideoEntity> videos = videosRepository.findAll();
                        for (VideoEntity video : videos) {
                            SendVideo sendVideo = new SendVideo();
                            sendVideo.setChatId(chatId);
                            sendVideo.setVideo(new InputFile(video.getFileId()));
                            responses.add(sendVideo);
                        }
                        return responses;
                    }
                }
                // TODO
                else if (userText.equals(Messages.deleteVideo)) {
                    sendMessage.setText(Messages.deleteSectionMessage);
                    sendMessage.setReplyMarkup(buttonService.twoButtons2());
                } else if (userText.equals(Messages.deleteAllVideos)) {
                    if (videosRepository.count() == 0) {
                        sendMessage.setText(Messages.noVideos);
                    } else {
                        adminService.deleteAllVideos();
                        sendMessage.setText(Messages.deleteAllVideosSuccess);
                    }
                    userService.updateStep(chatId, Step.ADMIN_MAIN);
                    sendMessage.setReplyMarkup(buttonService.adminSectionButtons());
                } else if (userText.equals(Messages.deleteSeparately)) {
                    if (videosRepository.count() == 0) {
                        sendMessage.setText(Messages.noVideos);
                        userService.updateStep(chatId, Step.ADMIN_MAIN);
                        sendMessage.setReplyMarkup(buttonService.adminSectionButtons());
                    } else {
                        for (VideoEntity video : videosRepository.findAll()) {
                            SendVideo sendVideo = new SendVideo();
                            sendVideo.setChatId(chatId);
                            sendVideo.setVideo(new InputFile(video.getFileId()));

                            InlineKeyboardButton deleteButton = new InlineKeyboardButton();
                            deleteButton.setText(Messages.delete);
                            deleteButton.setCallbackData(Messages.callBackStart + video.getId());

                            List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
                            List<InlineKeyboardButton> row = new ArrayList<>();
                            row.add(deleteButton);
                            keyboard.add(row);

                            InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
                            markup.setKeyboard(keyboard);

                            sendVideo.setReplyMarkup(markup);

                            responses.add(sendVideo);
                        }
                    }
                } else if (userText.equals(Messages.finish)) {
                    sendMessage.setText(Messages.adminMainMenu);
                    userService.updateStep(chatId, Step.ADMIN_MAIN);
                    sendMessage.setReplyMarkup(buttonService.adminSectionButtons());
                } else if (userText.equals(Messages.back)) {
                    if (user.getStep() == Step.ADMIN_SETTINGS) {
                        sendMessage.setText(Messages.adminMainMenu);
                        userService.updateStep(chatId, Step.ADMIN_MAIN);
                        sendMessage.setReplyMarkup(buttonService.adminSectionButtons());
                    }
                } else {
                    sendMessage.setText(Messages.noCommandPrompt);
                    userService.updateStep(chatId, Step.ADMIN_MAIN);
                    sendMessage.setReplyMarkup(buttonService.adminSectionButtons());
                }
            } else if (user.getRole() == Role.USER) {
                if (userText.equals(Messages.getAllVideoLessons)) {
                    if (videosRepository.count() == 0) {
                        sendMessage.setText(Messages.noVideos);
                        userService.updateStep(chatId, Step.USER_MAIN);
                        sendMessage.setReplyMarkup(buttonService.userButton());
                        responses.add(sendMessage);
                    } else {
                        List<VideoEntity> videos = videosRepository.findAll();
                        userService.updateRole(chatId, Role.USER);
                        for (VideoEntity video : videos) {
                            SendVideo sendVideo = new SendVideo();
                            sendVideo.setChatId(chatId);
                            sendVideo.setVideo(new InputFile(video.getFileId()));
                            responses.add(sendVideo);
                        }
                    }
                    return responses;
                } else {
                    sendMessage.setText(Messages.noCommandPrompt);
                    userService.updateStep(chatId, Step.USER_MAIN);
                    sendMessage.setReplyMarkup(buttonService.userButton());
                }
            } else if (user.getRole() == Role.UNAUTHORIZED) {
                sendMessage = buttonService.checkChannelSubscription(chatId, senderService.message("channelText", Messages.channelTextAgain));
            }
        } else if (update.hasCallbackQuery()) {
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            String callbackData = update.getCallbackQuery().getData();
            Integer messageId = update.getCallbackQuery().getMessage().getMessageId();

            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setChatId(chatId);
            deleteMessage.setMessageId(messageId);

            if (callbackData.equals(Commands.JOIN_CHANNEL)) {
                if (checkService.checkChatMember(chatId)) {
                    userService.updateRole(chatId, Role.USER);
                    sendMessage.setChatId(chatId);
                    sendMessage.setText(Messages.success);
                    sendMessage.setReplyMarkup(buttonService.userButton());
                } else {
                    userService.updateRole(chatId, Role.UNAUTHORIZED);
                    sendMessage = buttonService.checkChannelSubscription(chatId, senderService.message("channelText", Messages.channelTextAgain));
                }
            } else if (callbackData.startsWith(Messages.callBackStart)) {
                Long videoId = Long.parseLong(callbackData.substring(Messages.callBackStart.length()));
                videosRepository.deleteById(videoId);
                sendMessage.setChatId(chatId);
                sendMessage.setText(Messages.deleteSuccessMessage);
                sendMessage.setReplyMarkup(buttonService.finishButton());
            }

            responses.add(sendMessage);
            responses.add(deleteMessage);
            return responses;
        }
        responses.add(sendMessage);
        return responses;
    }
}
