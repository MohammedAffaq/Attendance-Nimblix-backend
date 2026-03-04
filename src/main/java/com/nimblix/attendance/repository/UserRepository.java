package com.nimblix.attendance.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nimblix.attendance.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmailIgnoreCase(String email);

    // Optional<User> findByEmailIgnoreCaseAndEnabledTrueAndLockedFalse(String
    // email);
    Optional<User> findByEmailIgnoreCase(String email);

    Optional<User> findByIdAndEnabledTrueAndLockedFalse(Long id);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

    boolean existsByEmployeeIdIgnoreCaseAndIdNot(String employeeId, Long id);

    boolean existsByEmployeeIdIgnoreCase(String employeeId);

}
