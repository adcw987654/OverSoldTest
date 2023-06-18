package com.ray.oversolddemo.dao;

import com.ray.oversolddemo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(value = "SELECT amount FROM Product WHERE id = :id")
    public Optional<Integer> findAmountById(@Param("id") Long id);

    @Modifying
    @Query(value = "UPDATE Product SET amount = :amount WHERE id = :id AND amount = :oldAmount", nativeQuery = true)
    public int updateAmountById(@Param("amount") Integer amount, @Param("oldAmount") Integer oldAmount, @Param("id") Long id);

    @Modifying
    @Query(value = "UPDATE Product SET amount = :amount", nativeQuery = true)
    public int updateAmount(@Param("amount") Integer amount);
}
