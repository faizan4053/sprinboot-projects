package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.JournalEntryService;
import net.engineeringdigest.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        List<User> users = this.userService.findAll();
        if(users !=null && !users.isEmpty()){
            return new ResponseEntity<>(users,HttpStatus.OK);
        }
        return  new ResponseEntity<>(users,HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user){

         try{
             this.userService.saveEntry(user);
             return  new ResponseEntity<>(user,HttpStatus.CREATED);
         }catch (Exception e){
             return  new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
         }
    }

    @GetMapping("id/{id}")
    public ResponseEntity<User> getJournalEntryById(@PathVariable("id") ObjectId id){
        Optional<User> user = this.userService.getUserById(id);
        return user.map(entry -> new ResponseEntity<>(entry, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));

    }

    @DeleteMapping("id/{id}")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable("id") ObjectId id){
        this.userService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping()
    public ResponseEntity<JournalEntry> deleteJournalEntryById(@RequestBody User user){
        User userInDb=this.userService.findByUserName(user.getUserName());
        if(userInDb !=null){
            userInDb.setUserName(user.getUserName());
            userInDb.setPassword(user.getPassword());
            this.userService.saveEntry(userInDb);
            new ResponseEntity<>(userInDb,HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
