package org.example.grand_education;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.grand_education.configuration.BotConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
@EnableJpaRepositories
@Slf4j
public class GrandEducationApplication {
    public static void main(String[] args) {
        SpringApplication.run(GrandEducationApplication.class, args);
    }

    @Configuration
    @RequiredArgsConstructor
    static class RegisterBot {

        private final BotConfig botConfig;


        @EventListener(ContextRefreshedEvent.class)
        public void init() {
            try {
                TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
                telegramBotsApi.registerBot(botConfig);
            } catch (TelegramApiException e) {
                log.error(e.getMessage());
            }
        }
    }
}
