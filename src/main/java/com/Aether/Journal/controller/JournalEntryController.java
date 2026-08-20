package com.Aether.Journal.controller;

import com.Aether.Journal.dto.JournalEntryRequestDto;
import com.Aether.Journal.dto.JournalEntryResponseDto;
import com.Aether.Journal.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @PostMapping()
    public ResponseEntity<JournalEntryResponseDto> createJournal(
            @RequestBody JournalEntryRequestDto entryRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(journalEntryService.createJournalEntry(entryRequestDto));


    }

    @GetMapping
    public ResponseEntity<List<JournalEntryResponseDto>> getAllEntriesOfAnUser(){
        return ResponseEntity.ok(journalEntryService.getAllEntriesOfAnUser());
    }

    @GetMapping("/{id}")
    public ResponseEntity<JournalEntryResponseDto> getById(@PathVariable ObjectId id){
        return ResponseEntity.ok(journalEntryService.getEntryById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBYId(@PathVariable ObjectId id){
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(journalEntryService.deleteById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JournalEntryResponseDto> updateEntry(@PathVariable ObjectId id,
                                                               @RequestBody JournalEntryRequestDto requestDto){
        return ResponseEntity.ok(journalEntryService.updateEntry(id,requestDto));
    }



}
