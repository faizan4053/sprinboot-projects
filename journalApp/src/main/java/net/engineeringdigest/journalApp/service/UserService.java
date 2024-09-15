package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.Repository.JournalEntryRepository;
import net.engineeringdigest.journalApp.Repository.UserRepository;
import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User saveEntry(User entry){
        this.repository.save(entry);
        return entry;
    }

    public List<User> findAll(){
        return this.repository.findAll();
    }

    public Optional<User> getUserById(ObjectId objectId){
        return this.repository.findById(objectId);
    }

    public void deleteUserById(ObjectId objectId){
        this.repository.deleteById(objectId);
    }

    public User findByUserName(String userName){
        return this.repository.findByUserName(userName);
    }

}
