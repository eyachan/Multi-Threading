package com.eya.multithreading.executor.api.servie;

import com.eya.multithreading.executor.api.model.User;
import com.eya.multithreading.executor.api.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class UserService {
    private UserRepository userRepository;
    Object target;
    Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Async
    public CompletableFuture<List<User>> saveUsers(MultipartFile file) throws Exception {
        long start = System.currentTimeMillis();
        List<User> users = parseCsvFile(file);
        logger.info("saving ...." + Thread.currentThread().getName());
        List<User> usersResult = userRepository.saveAll(users);
        long end = System.currentTimeMillis();
        logger.info("total time {}", (end - start));
        return CompletableFuture.completedFuture(usersResult);
    }

    public CompletableFuture<List<User>> findAllUser() {
        logger.info("get all users by " + Thread.currentThread().getName());
        List<User> users = userRepository.findAll();
        return CompletableFuture.completedFuture(users);
    }

    private List<User> parseCsvFile(final MultipartFile file) throws Exception {
        final List<User> users = new ArrayList<>();
        try {
            try (final BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    final String[] data = line.split(",");
                    final User user = new User();
                    user.setName((data[0]));
                    user.setEmail(data[1]);
                    user.setGendre(data[2]);
                    users.add(user);
                }
                return users;
            }
        } catch (Exception exception) {
            logger.error("failed to parse csv {}", exception);
            throw new Exception("failed to parse csv file");
        }
    }


}
