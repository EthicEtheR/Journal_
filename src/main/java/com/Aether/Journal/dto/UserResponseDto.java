package com.Aether.Journal.dto;

import com.Aether.Journal.entity.JournalEntry;
import lombok.Data;
import org.bson.types.ObjectId;

import java.util.List;

@Data
public class UserResponseDto {
    private ObjectId id;
    private String username;
    private List<JournalEntry> journalEntries;
}
