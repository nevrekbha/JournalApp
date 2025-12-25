package net.engineeringdigest.journalingApp.controller;


import net.engineeringdigest.journalingApp.entity.User;
import net.engineeringdigest.journalingApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/allUsers")
    public ResponseEntity<?> getAllUsers(){
        List<User> userList = userService.getAll();
        if(userList!= null && !userList.isEmpty()){
            return new ResponseEntity<>(userList, HttpStatus.OK);
        }
        return new ResponseEntity<>(userList, HttpStatus.NOT_FOUND);
    }

    @PutMapping("/createAdmin")
    public ResponseEntity<?> createAdmin(@RequestBody User user){
            userService.saveAdmin(user);
            return new ResponseEntity<>(HttpStatus.OK);
    }
}
