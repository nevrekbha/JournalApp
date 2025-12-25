package net.engineeringdigest.journalingApp.service;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalingApp.entity.JournalEntry;
import net.engineeringdigest.journalingApp.entity.User;
import net.engineeringdigest.journalingApp.repository.JournalEntryRepository;
import net.engineeringdigest.journalingApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;


    @Transactional
    public void saveEntry(JournalEntry journalEntry, String name) {
        try{
            User user = userService.findByUserName(name);
            journalEntry.setDate(LocalDateTime.now());
            JournalEntry entry = journalEntryRepository.save(journalEntry);
            user.getJournalEntries().add(entry);
            userService.saveUser(user);
        }catch (Exception e){
            System.out.println(e);
            throw new RuntimeException("Error occurred while saving data to user");
        }
    }

    public void saveEntry(JournalEntry journalEntry) {
        journalEntryRepository.save(journalEntry);
    }


    public List<JournalEntry> getAll() {
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> findById(ObjectId id) {
        return journalEntryRepository.findById(id);

    }

    @Transactional
    public boolean deleteById(ObjectId id, String name) {
        boolean removed = false;
        try {
            User user = userService.findByUserName(name);
            removed = user.getJournalEntries().removeIf(x -> x.getId().equals(id));
            if (removed) {
                userService.saveUser(user);
                journalEntryRepository.deleteById(id);
            }
        } catch (Exception e) {
            log.info("Exception Occurred ", e);
            throw new RuntimeException("An error occurred while deleting the entry.", e);
        }
        return removed;
    }

}
