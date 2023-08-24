package com.udacity.jwdnd.course1.cloudstorage.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.udacity.jwdnd.course1.cloudstorage.exception.EntityNotFoundException;
import com.udacity.jwdnd.course1.cloudstorage.exception.ErrorException;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.repository.NoteMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class NoteService {
    private NoteMapper noteMapper;

    public Note getUserNote(Integer noteId, Integer userId) {
        Optional<Note> note = noteMapper.getNoteById(noteId, userId);
        return unwrapNote(note, noteId);
    }

    public List<Note> getUserNotes(Integer userId) {
        return noteMapper.getUserNotes(userId);

    }

    public void save(Note note) {
        int result = noteMapper.insert(new Note(null, note.getNoteTitle(), note.getNoteDescription(),
                note.getUserId()));

        if (result != 1)
            throw new ErrorException("An error ocurred while trying to save note");
    }

    public void delete(Integer noteId, Integer userId) {
        int result = noteMapper.delete(noteId, userId);
        if (result != 1)
            throw new ErrorException("An error occurred while trying to delete this note");

    }

    public void update(Note note) {
        int result = noteMapper.updateNote(note);

        if (result != 1)
            throw new ErrorException("Unable to Update Note: " + note.getNoteId());

    }

    static Note unwrapNote(Optional<Note> entity, Integer id) {
        if (entity.isPresent())
            return entity.get();
        else
            throw new EntityNotFoundException(id, Note.class);
    }

}
