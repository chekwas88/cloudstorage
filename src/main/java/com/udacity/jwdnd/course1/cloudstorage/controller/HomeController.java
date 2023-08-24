package com.udacity.jwdnd.course1.cloudstorage.controller;

import java.io.IOException;
import java.security.Principal;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class HomeController {
    private UserService userService;
    private FileService fileService;
    private NoteService noteService;
    private CredentialService credentialService;

    @GetMapping(value = "/result")
    public String displayResult(Model model) {

        return "result";
    }

    @GetMapping(value = "/home")
    public String showNotes(Model model, @ModelAttribute("note") Note note,
            @ModelAttribute("credential") Credential credential, Principal principal) {
        Integer userId = userService.findUserByUsername(principal.getName()).getUserId();
        model.addAttribute("files", fileService.getUserFiles(userId));
        model.addAttribute("notes", noteService.getUserNotes(userId));

        model.addAttribute("credentials", credentialService.getUserCredentials(userId));
        return "home";
    }

    // Notes

    @PostMapping(value = "/saveNote")

    public String saveNote(@ModelAttribute("note") Note note,
            Principal principal, RedirectAttributes attributes) {
        if (note.getNoteId() != null) {
            noteService.update(note);
        } else {
            Integer userId = userService.findUserByUsername(principal.getName()).getUserId();
            note.setUserId(userId);
            noteService.save(note);
        }

        attributes.addFlashAttribute("success", true);
        return "redirect:/result";
    }

    @GetMapping(value = "/deleteNote")
    public String deleteNote(@RequestParam("id") String id, RedirectAttributes attributes, Principal principal) {
        Integer userId = userService.findUserByUsername(principal.getName()).getUserId();
        noteService.delete(Integer.parseInt(id), userId);
        attributes.addFlashAttribute("success", true);
        return "redirect:/result";
    }

    // Credentials
    @PostMapping(value = "/saveCredential")

    public String saveCredential(@ModelAttribute("credential") Credential credential,
            Principal principal, RedirectAttributes attributes) {
        if (credential.getCredentialId() != null) {
            credentialService.update(credential);
        } else {
            Integer userId = userService.findUserByUsername(principal.getName()).getUserId();
            credential.setUserId(userId);
            credentialService.save(credential);
        }

        attributes.addFlashAttribute("success", true);
        return "redirect:/result";
    }

    @GetMapping(value = "/deleteCredential")
    public String deleteCredential(@RequestParam("id") String id,
            RedirectAttributes attributes, Principal principal) {
        Integer userId = userService.findUserByUsername(principal.getName()).getUserId();
        credentialService.delete(Integer.parseInt(id), userId);
        attributes.addFlashAttribute("success", true);
        return "redirect:/result";
    }

    // Files

    @PostMapping(value = "/upload")
    public String uploadFile(@RequestParam("fileUpload") MultipartFile file, Principal principal,
            RedirectAttributes attributes) {
        Integer userId = userService.findUserByUsername(principal.getName()).getUserId();
        try {
            File dfile = new File(null, file.getOriginalFilename(), file.getContentType(),
                    String.valueOf(file.getSize()), userId, file.getBytes());
            fileService.uploadFile(dfile);

        } catch (IOException e) {
            e.printStackTrace();
        }
        attributes.addFlashAttribute("success", true);

        return "redirect:/result";
    }

    @GetMapping(value = "/deleteFile")
    public String deleteFile(@RequestParam("fileName") String fileName, RedirectAttributes attributes,
            Principal principal) {
        Integer userId = userService.findUserByUsername(principal.getName()).getUserId();

        fileService.deleteFile(fileName, userId);
        attributes.addFlashAttribute("success", true);

        return "redirect:/result";
    }

    @GetMapping(value = "/download")
    public ResponseEntity<byte[]> viewFile(@RequestParam("fileName") String fileName, Principal principal) {
        HttpHeaders headers = new HttpHeaders();
        Integer userId = userService.findUserByUsername(principal.getName()).getUserId();
        File file = fileService.getUserFile(fileName, userId);

        headers.setContentType(MediaType.parseMediaType(file.getContentType()));

        headers.add("content-disposition", "inline;filename=" + file.getFileName());

        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(file.getFileData(), headers, HttpStatus.OK);
        return response;
    }

}
