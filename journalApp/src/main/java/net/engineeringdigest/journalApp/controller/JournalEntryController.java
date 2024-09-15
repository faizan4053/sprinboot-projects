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
@RequestMapping("/journal")
public class JournalEntryController {

    private final JournalEntryService journalEntryService;
    private final UserService userService;

    public JournalEntryController(JournalEntryService journalEntryService, UserService userService) {
        this.journalEntryService = journalEntryService;
        this.userService = userService;
    }


    @GetMapping("{userName}")
    public ResponseEntity<?> getAllJournalEntriesOfUser(@PathVariable String userName) {
        User user=this.userService.findByUserName(userName);
        List<JournalEntry> journalEntries = user.getJournalEntries();
        if(journalEntries !=null && !journalEntries.isEmpty()){
            return new ResponseEntity<>(journalEntries,HttpStatus.OK);
        }
        return  new ResponseEntity<>(journalEntries,HttpStatus.NOT_FOUND);
    }

    @PostMapping("{userName}")
    public ResponseEntity<JournalEntry> createJournalEntryForUser(@RequestBody JournalEntry journalEntry, @PathVariable String userName){

         try{
             this.journalEntryService.saveEntry(journalEntry, userName);
             return  new ResponseEntity<>(journalEntry,HttpStatus.CREATED);
         }catch (Exception e){
             return  new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
         }
    }

    @GetMapping("id/{id}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable("id") ObjectId id){
        Optional<JournalEntry> journalEntry = this.journalEntryService.getEntryById(id);
        return journalEntry.map(entry -> new ResponseEntity<>(entry, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));

    }

    @DeleteMapping("username/{userName}/id/{id}")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable("userName") String userName, @PathVariable("id") ObjectId id){
        this.journalEntryService.deleteEntryById(id, userName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("username/{userName}/id/{id}")
    public ResponseEntity<JournalEntry> deleteJournalEntryById(@PathVariable("userName") String userName,
                                                               @PathVariable("id") ObjectId id,
                                                               @RequestBody JournalEntry journalEntry
    ){
        JournalEntry old=this.journalEntryService.getEntryById(id).orElse(null);
        if(old !=null){
            old.setTitle(journalEntry.getTitle() != null && !journalEntry.getTitle().equals("") ? journalEntry.getTitle() : old.getTitle());
            old.setContent(journalEntry.getContent() != null && !journalEntry.getContent().equals("") ? journalEntry.getContent() : old.getContent());
            old.setDate(LocalDateTime.now());
            JournalEntry  savedEntry = this.journalEntryService.saveEntry(old);
            new ResponseEntity<>(savedEntry,HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
