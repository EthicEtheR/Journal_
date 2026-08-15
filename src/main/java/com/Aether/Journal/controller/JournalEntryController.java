package com.Aether.Journal.controller;

import com.Aether.Journal.dto.JournalEntryRequestDto;
import com.Aether.Journal.dto.JournalEntryResponseDto;
import com.Aether.Journal.entity.JournalEntry;
import com.Aether.Journal.service.JouralEntryService;
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
    private JouralEntryService jouralEntryService;

    @PostMapping
    public ResponseEntity<JournalEntryResponseDto> createJournal(
            @RequestBody JournalEntryRequestDto entryRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(jouralEntryService.createJournalEntry(entryRequestDto));


    }

    @GetMapping
    public ResponseEntity<List<JournalEntryResponseDto>> getAllEntries(){
        return ResponseEntity.ok(jouralEntryService.getAllEntries());
    }

    @GetMapping("/{id}")
    public ResponseEntity<JournalEntryResponseDto> getById(@PathVariable ObjectId id){
        return ResponseEntity.ok(jouralEntryService.getEntryById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBYId(@PathVariable ObjectId id){
        return ResponseEntity.ok(jouralEntryService.deleteById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JournalEntryResponseDto> updateEntry(
            @PathVariable ObjectId id,@RequestBody JournalEntryRequestDto requestDto){
        return ResponseEntity.ok(jouralEntryService.updateEntry(id,requestDto));
    }



}
