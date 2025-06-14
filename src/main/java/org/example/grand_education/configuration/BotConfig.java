package org.example.grand_education.configuration;

import lombok.extern.slf4j.Slf4j;
import org.example.grand_education.service.MainService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static org.example.grand_education.configuration.StaticVariables.BOT_TOKEN;
import static org.example.grand_education.configuration.StaticVariables.BOT_USERNAME;

@Slf4j
@Component
public class BotConfig extends TelegramLongPollingBot {

    private final MainService mainService;

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    public BotConfig(MainService mainService) {
        super(BOT_TOKEN);
        this.mainService = mainService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            List<PartialBotApiMethod<?>> responses = mainService.mainBot(update);

            if (responses != null && !responses.isEmpty()) {
                for (PartialBotApiMethod<?> response : responses) {
                    if (response instanceof SendMessage sendMessage) {
                        execute(sendMessage);
                    } else if (response instanceof SendVideo sendVideo) {
                        execute(sendVideo);
                    } else if (response instanceof DeleteMessage deleteMessage) {
                        execute(deleteMessage);
                    }
                    Thread.sleep(1000);
                }
            } else {
                log.warn("main service returned null or empty");
                System.err.println("main service returned null or empty");
            }
        } catch (Exception e) {
            log.error("Error occurred while processing update: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}