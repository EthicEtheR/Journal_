package com.Aether.Journal.entity;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@Getter
@Setter
public class JournalEntry {
    @Id
    private ObjectId id;

    private  String title;
    private String content;
    private LocalDateTime date;

}
