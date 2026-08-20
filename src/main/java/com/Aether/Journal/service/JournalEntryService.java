package com.Aether.Journal.service;

import com.Aether.Journal.dto.JournalEntryRequestDto;
import com.Aether.Journal.dto.JournalEntryResponseDto;
import com.Aether.Journal.entity.JournalEntry;
import com.Aether.Journal.entity.User;
import com.Aether.Journal.exception.GenericNotFoundException;
import com.Aether.Journal.exception.IllegalEntryAccessException;
import com.Aether.Journal.repository.JournalEntryRepository;
import com.Aether.Journal.repository.UserRepository;
import org.bson.types.ObjectId;
import org.jspecify.annotations.Nullable;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private  UserRepository userRepository;

    @Transactional
    public JournalEntryResponseDto createJournalEntry(JournalEntryRequestDto entryRequestDto) {

        String username=SecurityContextHolder.getContext().getAuthentication().getName();

        User user=userRepository.findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("User not found "));

        JournalEntry entry=new JournalEntry();
        entry.setTitle(entryRequestDto.getTitle());
        entry.setContent(entryRequestDto.getContent());
        entry.setDate(LocalDateTime.now());

        entry=journalEntryRepository.save(entry);

         user.getJournalEntries().add(entry);

        userRepository.save(user);

       JournalEntryResponseDto dto=new JournalEntryResponseDto();
       dto.setContent(entry.getContent());
       dto.setDate(entry.getDate());
       dto.setUsername(username);
       dto.setTitle(entry.getTitle());
       dto.setId(entry.getId());
        return dto;

    }

    public  List<JournalEntryResponseDto> getAllEntriesOfAnUser() {

        String username=SecurityContextHolder.getContext().getAuthentication().getName();

        User user=userRepository.findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("User not found"));

        List<JournalEntry> entryList=user.getJournalEntries();
         //TODO usename is going null need fixing

        return entryList.stream()
                .map(entry -> {
                    JournalEntryResponseDto dto =
                            modelMapper.map(entry, JournalEntryResponseDto.class);
                    dto.setUsername(username);
                    return dto;
                })
                .toList();
    }


    public  JournalEntryResponseDto getEntryById(ObjectId id) {
        String username=SecurityContextHolder.getContext().getAuthentication().getName();

        User user=userRepository.findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("User not found"));

        if(!journalEntryRepository.existsById(id))
             throw new GenericNotFoundException("Entry is not found or deleted");

        JournalEntry entry = user.getJournalEntries().stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElse(null);
        if(entry==null)
             throw new IllegalEntryAccessException("You cannot get others JournalEntry");

        JournalEntryResponseDto dto= modelMapper.map(entry,JournalEntryResponseDto.class);
        dto.setUsername(username);

        return dto;

    }

    @Transactional
    public  String deleteById(ObjectId id) {

        String username=SecurityContextHolder.getContext().getAuthentication().getName();

        User user=userRepository.findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("User not found"));

        if(!journalEntryRepository.existsById(id))
            throw new GenericNotFoundException("Entry is not found or deleted");

        JournalEntry entry = user.getJournalEntries().stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElse(null);
        if(entry==null)
            throw new IllegalEntryAccessException("You cannot delete others JournalEntry");

             journalEntryRepository.deleteById(id);


            user.getJournalEntries().removeIf(ent->ent.getId().equals(id));
            userRepository.save(user);

        return "Entry deleted ";
    }

    @Transactional
    public  JournalEntryResponseDto updateEntry(ObjectId id, JournalEntryRequestDto requestDto) {

        String username=SecurityContextHolder.getContext().getAuthentication().getName();

        User user=userRepository.findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("User not found"));

        if(!journalEntryRepository.existsById(id))
            throw new GenericNotFoundException("Entry is not found or deleted");

        JournalEntry journalEntry = user.getJournalEntries().stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElse(null);

        if(journalEntry==null)
            throw new IllegalEntryAccessException("You cannot delete others JournalEntry");

        if(requestDto!=null && !requestDto.getTitle().isEmpty()){
            journalEntry.setTitle(requestDto.getTitle());
        }
        if(requestDto!=null && !requestDto.getContent().isEmpty()){
            journalEntry.setContent(requestDto.getContent());
        }

        journalEntry= journalEntryRepository.save(journalEntry);


        JournalEntryResponseDto dto= modelMapper.map(journalEntry,JournalEntryResponseDto.class);
        dto.setUsername(user.getUsername());
            return dto;
    }

    public @Nullable List<JournalEntryResponseDto> getAllEntries() {
        List<JournalEntry> journalEntries=journalEntryRepository.findAll();
        return journalEntries.stream()
                .map(entry->modelMapper.map(entry,JournalEntryResponseDto.class))
                .toList();
    }
}
