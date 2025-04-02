package com.project.sodam365.repository;

import com.project.sodam365.dto.ProductDto;
import com.project.sodam365.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByUser_Userid(String userid); // 특정 사용자(User)가 등록한 상품 조회
    @Query("SELECT new com.project.sodam365.dto.ProductDto( " +
            "p.no, p.p_title, p.p_contents, p.p_price, p.p_link, p.createdAt, u.name, u.userid, u.ownerloc) " +
            "FROM Product p JOIN p.user u ORDER BY p.no")
    List<ProductDto> findAllProductsWithUsername();
    Optional<Product> findByNo(Long no);
    List<Product> findTop3ByOrderByNoDesc(); // ✅ 최근 3개 조회 (no 내림차순)
    @Query("SELECT p FROM Product p WHERE p.p_title LIKE %:title%")
    List<Product> findByTitleContaining(@Param("title") String title);


}
