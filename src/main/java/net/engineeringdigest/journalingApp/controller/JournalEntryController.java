package net.engineeringdigest.journalingApp.controller;


import net.engineeringdigest.journalingApp.entity.JournalEntry;
import net.engineeringdigest.journalingApp.entity.User;
import net.engineeringdigest.journalingApp.service.JournalEntryService;
import net.engineeringdigest.journalingApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllJournalEntriesOfUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name  = authentication.getName();
        User user = userService.findByUserName(name);
        List<JournalEntry> list =  user.getJournalEntries();
        if(list != null && !list.isEmpty()){
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry journalEntry){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String name  = authentication.getName();
            journalEntryService.saveEntry(journalEntry,name);
            return new ResponseEntity<>(journalEntry, HttpStatus.CREATED);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("id/{myId}")
    public ResponseEntity<JournalEntry> getJournalEntryByID(@PathVariable ObjectId myId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name  = authentication.getName();
        User user = userService.findByUserName(name);
        List<JournalEntry> journalEntries = user.getJournalEntries().stream().filter(x-> x.getId().equals(myId)).toList();
        if(!journalEntries.isEmpty()){
            Optional<JournalEntry> entry = journalEntryService.findById(myId);
            if(entry.isPresent()){
                return new ResponseEntity<>(entry.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("id/{myId}")
    public ResponseEntity<?> deleteEntryById(@PathVariable ObjectId myId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        boolean removed = journalEntryService.deleteById(myId, name);
        if (removed) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("id/{myId}")
    public ResponseEntity<JournalEntry> updateJournalByID(@PathVariable ObjectId myId, @RequestBody JournalEntry myEntry){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(myId)).toList();
        if (!collect.isEmpty()) {
            Optional<JournalEntry> journalEntry = journalEntryService.findById(myId);
            if (journalEntry.isPresent()) {
                JournalEntry old = journalEntry.get();
                old.setTitle(!myEntry.getTitle().isEmpty() ? myEntry.getTitle() : old.getTitle());
                old.setContent(myEntry.getContent() != null && !myEntry.getContent().isEmpty() ? myEntry.getContent() : old.getContent());
                journalEntryService.saveEntry(old);
                return new ResponseEntity<>(old, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }


}
