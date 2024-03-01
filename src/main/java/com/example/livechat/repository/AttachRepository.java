package com.example.livechat.repository;

import com.example.livechat.domain.entity.Attach;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AttachRepository extends JpaRepository<Attach, Long> {

    Optional<Attach> findByStoreFileName(String storeFileName);

}
