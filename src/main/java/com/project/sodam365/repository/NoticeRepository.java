package com.project.sodam365.repository;

import com.project.sodam365.entity.Notice;
import com.project.sodam365.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    List<Notice> findTop3ByOrderByIdDesc();

}
