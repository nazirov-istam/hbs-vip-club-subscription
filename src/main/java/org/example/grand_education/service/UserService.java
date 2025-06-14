package org.example.grand_education.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.grand_education.configuration.StaticVariables;
import org.example.grand_education.enums.Role;
import org.example.grand_education.enums.Step;
import org.example.grand_education.model.Setting;
import org.example.grand_education.model.User;
import org.example.grand_education.repository.SettingRepository;
import org.example.grand_education.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final SettingRepository settingRepository;


    public User getCurrentUser(Long chatId) {
        return userRepository.findById(chatId).orElse(null);
    }

    public void registerUser(Message message) {
        try {
            Optional<User> optional = userRepository.findById(message.getFrom().getId());

            if (optional.isPresent()) {
                log.info("User with id {} already exists", message.getFrom().getId());
            }
            User user = new User();
            user.setChatId(message.getChatId());
            user.setFirstName(message.getFrom().getFirstName());
            user.setLastName(message.getFrom().getLastName());
            user.setUsername(message.getFrom().getUserName());
            user.setContact(message.getContact());
            user.setLocation((message.getLocation()));
            user.setCreatedAt(LocalDateTime.now());
            user.setRole(StaticVariables.OWNER_CHAT_ID.equals(message.getChatId()) ? Role.MANAGER : Role.USER);
            log.info("User with id {} registered", message.getFrom().getId());
            userRepository.save(user);
        } catch (Exception e) {
            log.info("Error  registering user with id, {}", message.getFrom().getId());
            throw new RuntimeException(e);
        }
    }

    public void registerUserUnauthorized(Message message) {
        try {
            User user = new User();
            user.setChatId(message.getChatId());
            user.setFirstName(message.getFrom().getFirstName());
            user.setLastName(message.getFrom().getLastName());
            user.setUsername(message.getFrom().getUserName());
            user.setContact(message.getContact());
            user.setLocation((message.getLocation()));
            user.setCreatedAt(LocalDateTime.now());
            user.setRole(Role.UNAUTHORIZED);
            log.info("User with id {} registered", message.getFrom().getId());
            userRepository.save(user);
        } catch (Exception e) {
            log.info("Error  registering user with id, {}", message.getFrom().getId());
            throw new RuntimeException(e);
        }
    }


    public void registerAdmin(String adminChatId) {
        try {
            Optional<User> optional = userRepository.findById(Long.parseLong(adminChatId));

            if (optional.isPresent()) {
                log.info("User with id already exists");
            }
            User user = new User();
            user.setChatId(Long.parseLong(adminChatId));
            user.setCreatedAt(LocalDateTime.now());
            user.setRole(Role.ADMIN);
            log.info("User with id registered.");
            userRepository.save(user);
        } catch (Exception e) {
            log.info("Error  registering user with id");
            throw new RuntimeException(e);
        }
    }

    public void registerAdminName(String name) {
        try {
            Optional<User> optional = userRepository.findTopByRoleOrderByCreatedAtDesc(Role.ADMIN);
            if (optional.isPresent()) {
                log.info("User with id already exists");
                optional.get().setFirstName(name);
                userRepository.save(optional.get());
            }
        } catch (Exception e) {
            log.info("Error  registering user with name, {}", name);
        }
    }

    public void updateStep(Long chatId, Step step) {
        userRepository.findById(chatId).ifPresentOrElse(
                user -> {
                    user.setStep(step);
                    userRepository.save(user);
                },
                () -> log.warn("User with id {} not found", chatId)
        );
    }

    public void updateRole(Long chatId, Role role) {
        userRepository.findById(chatId).ifPresentOrElse(
                user -> {
                    user.setRole(role);
                    userRepository.save(user);
                },
                () -> log.warn("User with id {} not found", chatId)
        );
    }

    public boolean checkForAdmin(long chatId) {
        try {
            Optional<User> optional = userRepository.findById(chatId);
            if (optional.isPresent()) {
                log.info("User with id {} is an ADMIN ", chatId);
                return optional.get().getRole().equals(Role.ADMIN);
            }
            return false;
        } catch (Exception e) {
            log.info("Error getting Manager with id");
            throw new RuntimeException(e);
        }
    }

    public void deleteAdmin(String name) {
        try {
            Optional<User> optional = userRepository.findByRole(Role.ADMIN);
            if (optional.isPresent()) {
                userRepository.delete(optional.get());
                log.info("User with id {} is deleted", name);
            }
        } catch (Exception e) {
            log.info("Error deleting user with name, {}", name);
            throw new RuntimeException(e);
        }
    }

    public String channelsInfo() {
        try {
            List<Setting> channels = settingRepository.findAll();
            StringBuilder result = new StringBuilder("Kanallar haqida ma'lumatlar \uD83D\uDCCC");
            for (int i = 0; i < channels.size(); i++) {
                String channel = """
                        %d) Kanal nomi: %s
                        Kanal qo'shilgan sana: %s
                        Kanal linki: %s
                        """.formatted(
                        i + 1,
                        channels.get(i).getChannelName(),
                        channels.get(i).getCreatedAt(),
                        channels.get(i).getChannelUrl());
                result.append(channel);
            }
            return result.toString();
        } catch (Exception e) {
            log.info("Error getting Manager with role");
        }
        return "";
    }

    public String usersInfo() {
        try {
            return """
                    Foydalanuvchilar haqida ma'lumatlar \uD83D\uDCCC
                    
                    🧮 Umumiy foydalanuvchilar soni: %d
                    """.formatted(userRepository.countAllByRole(Role.USER)

            );
        } catch (Exception e) {
            log.info("Error getting users info");
        }
        return "";
    }
}
