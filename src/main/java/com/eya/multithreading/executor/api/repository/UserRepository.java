package com.eya.multithreading.executor.api.repository;

import com.eya.multithreading.executor.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
