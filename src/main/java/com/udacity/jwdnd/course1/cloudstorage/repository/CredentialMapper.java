package com.udacity.jwdnd.course1.cloudstorage.repository;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;

@Mapper
public interface CredentialMapper {

    @Select("SELECT * FROM CREDENTIALS WHERE userId = #{userId} ")
    public List<Credential> getUserCredentials(Integer userId);

    @Select("SELECT * FROM CREDENTIALS where credentialId = #{credentialId} AND userId = #{userId} ")
    public Optional<Credential> getUserCredentialById(Integer credentialId, Integer userId);

    @Insert("INSERT INTO CREDENTIALS (url,username, key, password, userId) VALUES(#{url},#{username}, #{key}, #{password}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    public int insert(Credential credential);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialId= #{credentialId} AND userId = #{userId}")
    public int deleteCredential(Integer credentialId, Integer userId);

    @Update("UPDATE CREDENTIALS SET url = #{url}, username = #{username}, password = #{password} where credentialId = #{credentialId} AND userId = #{userId}")
    public int updateCredential(Credential credential);
}
