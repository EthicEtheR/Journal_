package com.Aether.Journal.service;

import com.Aether.Journal.dto.JournalEntryRequestDto;
import com.Aether.Journal.dto.JournalEntryResponseDto;
import com.Aether.Journal.entity.JournalEntry;
import com.Aether.Journal.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.jspecify.annotations.Nullable;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class JouralEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public JournalEntryResponseDto createJournalEntry(JournalEntryRequestDto entryRequestDto) {

        JournalEntry entry=new JournalEntry();
        entry.setTitle(entryRequestDto.getTitle());
        entry.setContent(entryRequestDto.getContent());
        entry.setDate(LocalDateTime.now());

        entry=journalEntryRepository.save(entry);

       JournalEntryResponseDto dto=new JournalEntryResponseDto();
       dto.setContent(entry.getContent());
       dto.setDate(entry.getDate());
       dto.setTitle(entry.getTitle());
       dto.setId(entry.getId());
        return dto;

    }

    public  List<JournalEntryResponseDto> getAllEntries() {
        List<JournalEntry> entryList=journalEntryRepository.findAll();

        return entryList.stream()
                .map(entry->modelMapper.map(entry,JournalEntryResponseDto.class))
                .toList();
    }

    public  JournalEntryResponseDto getEntryById(ObjectId id) {
        JournalEntry journalEntry=journalEntryRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Entry is not found by id: "+id));

        return modelMapper.map(journalEntry,JournalEntryResponseDto.class);
    }

    public  String deleteById(ObjectId id) {
        if(journalEntryRepository.existsById(id)){
            journalEntryRepository.deleteById(id);
        }
        //should throw excetion from here;

        return "Entry deleted";
    }

    public  JournalEntryResponseDto updateEntry(ObjectId id, JournalEntryRequestDto requestDto) {
        JournalEntry journalEntry=journalEntryRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Not found"));
        if(requestDto!=null && !requestDto.getTitle().isEmpty()){
            journalEntry.setTitle(requestDto.getTitle());
        }
        if(requestDto!=null && !requestDto.getContent().isEmpty()){
            journalEntry.setContent(requestDto.getContent());
        }

        journalEntryRepository.save(journalEntry);

            return modelMapper.map(journalEntry,JournalEntryResponseDto.class);
    }
}
