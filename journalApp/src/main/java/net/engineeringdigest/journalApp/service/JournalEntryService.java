package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.Repository.JournalEntryRepository;
import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class JournalEntryService {

    private final JournalEntryRepository repository;
    private final UserService userService;

    public JournalEntryService(JournalEntryRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    public JournalEntry saveEntry(JournalEntry journalEntry, String userName){
        User user=this.userService.findByUserName(userName);
        journalEntry.setDate(LocalDateTime.now());
        JournalEntry saved = this.repository.save(journalEntry);
        user.getJournalEntries().add(saved);
        userService.saveEntry(user);
        return saved;
    }

    public JournalEntry saveEntry(JournalEntry journalEntry){
        journalEntry.setDate(LocalDateTime.now());
        JournalEntry saved = this.repository.save(journalEntry);
        return saved;
    }

    public List<JournalEntry> findAll(){
        return this.repository.findAll();
    }

    public Optional<JournalEntry> getEntryById(ObjectId objectId){
        return this.repository.findById(objectId);
    }

    public void deleteEntryById(ObjectId objectId, String userName){
        User user=this.userService.findByUserName(userName);
        user.getJournalEntries().removeIf(journalEntry -> journalEntry.getId().equals(objectId));
        userService.saveEntry(user);
        this.repository.deleteById(objectId);
    }

}
