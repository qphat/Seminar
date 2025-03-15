package com.seminar.seminar.repository;

import com.seminar.seminar.domain.Role;
import com.seminar.seminar.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

     User findByEmail(String email);

     List<User> findByRole(Role role);

     boolean existsByEmail(String email); // Kiểm tra email có tồn tại không

     @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role")
     long countByRole(@Param("role") Role role); // Đếm số lượng user theo role
}
