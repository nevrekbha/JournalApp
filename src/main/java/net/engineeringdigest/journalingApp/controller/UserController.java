package net.engineeringdigest.journalingApp.controller;


import net.engineeringdigest.journalingApp.entity.User;
import net.engineeringdigest.journalingApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /*@GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = userService.getAll();
        if(users!=null && !users.isEmpty()){
            return new ResponseEntity<>(users, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }*/



    @GetMapping("id/{userId}")
    public ResponseEntity<User> getByID(@PathVariable ObjectId userId){
        Optional<User> userObj = userService.findById(userId);
        if(userObj.isPresent()){
            return new ResponseEntity<>(userObj.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteById(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        userService.deleteByName(name);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping
    public ResponseEntity<User> updateDataById(@RequestBody User newUser){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name  = authentication.getName();
        User exitingUser = userService.findByUserName(name);
        exitingUser.setName(newUser.getName());
        exitingUser.setPassword(newUser.getPassword());
        userService.saveNewUser(exitingUser);
        return new ResponseEntity<>(exitingUser, HttpStatus.OK);

    }



}
