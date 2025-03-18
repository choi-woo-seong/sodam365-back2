package com.project.sodam365.repository;

import com.project.sodam365.entity.Nuser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NuserRepository extends JpaRepository<Nuser, String> {
    @Query("SELECT n FROM Nuser n WHERE n.nUserid = :nUserid")
    Optional<Nuser> findByNUserid(@Param("nUserid") String nUserid);
}

