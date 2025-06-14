package org.example.grand_education.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.grand_education.model.VideoEntity;
import org.example.grand_education.repository.VideosRepository;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {
    private final VideosRepository videosRepository;

    public void saveVideo(Message message) {
        String fileId = message.getVideo().getFileId();
        VideoEntity video = new VideoEntity();
        video.setFileId(fileId);
        videosRepository.save(video);
    }

    public void deleteAllVideos() {
        videosRepository.deleteAll();
    }
}
