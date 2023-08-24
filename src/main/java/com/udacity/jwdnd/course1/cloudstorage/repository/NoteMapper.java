package com.udacity.jwdnd.course1.cloudstorage.repository;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;

@Mapper
public interface NoteMapper {
    @Select("SELECT * FROM NOTES WHERE userId = #{userId}")
    List<Note> getUserNotes(Integer userId);

    @Select("SELECT * FROM NOTES  WHERE noteId = #{noteId} AND userId = #{userId}")
    public Optional<Note> getNoteById(Integer noteId, Integer userId);

    @Insert("INSERT INTO NOTES (noteTitle,noteDescription, userId ) VALUES(#{noteTitle},#{noteDescription}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    public int insert(Note note);

    @Delete("DELETE FROM NOTES WHERE noteId= #{noteId} AND userId = #{userId}")
    public int delete(Integer noteId, Integer userId);

    @Update("UPDATE NOTES SET noteTitle = #{noteTitle}, noteDescription = #{noteDescription} WHERE noteId = #{noteId} AND userId= #{userId}")
    public int updateNote(Note note);
}
