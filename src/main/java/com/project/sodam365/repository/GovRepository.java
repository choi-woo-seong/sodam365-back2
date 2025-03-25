package com.project.sodam365.repository;

import com.project.sodam365.entity.Gov;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface GovRepository extends JpaRepository<Gov, Long> {
    List<Gov> findTop3ByOrderByNoDesc();
}
