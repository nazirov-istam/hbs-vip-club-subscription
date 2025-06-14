package org.example.grand_education.repository;

import org.example.grand_education.model.VideoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideosRepository extends JpaRepository<VideoEntity, Long> {
}