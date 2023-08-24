package com.udacity.jwdnd.course1.cloudstorage.repository;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import com.udacity.jwdnd.course1.cloudstorage.model.File;

@Mapper
public interface FileMapper {
    @Select("SELECT * FROM FILES WHERE fileId = #{fileId} AND userId = #{userId}")
    Optional<File> getUserFileById(Integer fileId, Integer userId);

    @Select("SELECT * FROM FILES WHERE fileName = #{fileName} AND userId = #{userId}")
    Optional<File> getUserFileByName(String fileName, Integer userId);

    @Select("SELECT * FROM FILES WHERE userId = #{userId}")
    List<File> getUserFiles(Integer userId);

    @Insert("INSERT INTO FILES (fileName, contentType, fileSize, userId, fileData)" +
            "VALUES(#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insertFile(File file);

    @Delete("DELETE FROM FILES WHERE fileName = #{fileName} AND userId = #{userId}")
    int deleteFile(String fileName, Integer userId);
}
