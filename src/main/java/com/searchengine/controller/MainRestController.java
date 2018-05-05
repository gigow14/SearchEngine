package com.searchengine.controller;

import com.searchengine.config.FileStorageConfig;
import com.searchengine.model.SearchPhrase;
import com.searchengine.searchAlgorithms.Indexer;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Paths;
import java.util.List;

@RestController
public class MainRestController {

    @Autowired
    private FileStorageConfig fileStorageConfig;

    @RequestMapping("/searchApi")
    public List<SearchPhrase> getFiles(@RequestParam(value = "phrase", required = true) String phrase) throws IOException {

        System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");
        System.setProperty("org.apache.pdfbox.rendering.UsePureJavaCMYKConversion", "true");

        Indexer i = new Indexer();
        i.mains();
        System.out.println();
        return i.searchIndex(phrase);
    }

    // Using ResponseEntity<InputStreamResource>
    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable("fileName") String fileName) throws IOException {

        File file = new File(fileStorageConfig.getPath()+fileName);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment;filename=" + file.getName())
                .contentType(MediaType.APPLICATION_PDF).contentLength(file.length())
                .body(resource);
    }

    @Autowired
    private Environment readProperty;

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> uploadFile(
            @RequestParam("uploadfile") MultipartFile uploadfile) {

        try {
            // Get the filename and build the local file path
            String filename = uploadfile.getOriginalFilename();
            String directory = readProperty.getProperty("file.storage.path");
            String filepath = Paths.get(directory, filename).toString();

            // Save the file locally
            BufferedOutputStream stream =
                    new BufferedOutputStream(new FileOutputStream(new File(filepath)));
            stream.write(uploadfile.getBytes());
            stream.close();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
