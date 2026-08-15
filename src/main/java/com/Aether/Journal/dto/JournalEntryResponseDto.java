package com.Aether.Journal.dto;

import lombok.Data;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

@Data
public class JournalEntryResponseDto {
    private ObjectId id;

    private  String title;
    private String content;
    private LocalDateTime date;
}
