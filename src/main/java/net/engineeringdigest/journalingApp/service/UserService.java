package net.engineeringdigest.journalingApp.service;

import net.engineeringdigest.journalingApp.entity.User;
import net.engineeringdigest.journalingApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void saveAdmin(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER", "ADMIN"));
        userRepository.save(user);
    }

    public boolean saveNewUser(User user) {
        try{
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(List.of("USER"));
            userRepository.save(user);
            return true;
        }catch(Exception e) {
            logger.info("haha");
            logger.error("Error Occurred", e);
            logger.warn("Error Error");
            logger.trace("haha");
            logger.debug("haha");
            return false;
        }
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(ObjectId id) {
        return userRepository.findById(id);

    }

    public User findByUserName(String name){
        return userRepository.findByName(name);
    }

    public void deleteById(ObjectId id) {
        userRepository.deleteById(id);
    }

    public void deleteByName(String name){
        userRepository.deleteByName(name);
    }

    public List<User> addAll(List<User> users){
       return userRepository.saveAll(users);
    }

}
