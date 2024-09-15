package net.engineeringdigest.journalApp.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@Document(collection = "journal_entries")
public class JournalEntry {

    @Id
    private ObjectId id;
    private String title;
    @NonNull
    private String content;
    private LocalDateTime date;
}
