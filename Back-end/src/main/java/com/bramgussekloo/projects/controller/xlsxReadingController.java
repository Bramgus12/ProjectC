package com.bramgussekloo.projects.controller;

import com.bramgussekloo.projects.FileHandling.FileException;
import com.bramgussekloo.projects.dataclasses.XlsxReader;
import com.bramgussekloo.projects.statements.XlsxReadingStatements;
import io.swagger.annotations.Api;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;


// Controller for xlsx reader
@Api (value = "Keuzevakken lijst")
@RestController
@RequestMapping("/api/kv-lijst")
public class xlsxReadingController {

    /**
     * Gets all Election Course as an Object and save them into a List
     *
     * @return List of Objects
     */
    @ApiOperation(value = "Get a list of all Election Course from the excel file")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list", response = XlsxReader.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @GetMapping
    private ResponseEntity getElectionCourseList() {
        try {
            //WIP
//            return ResponseEntity.status(HttpStatus.OK).body(XlsxReadingStatements.());
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @PostMapping("/upload")
    private ResponseEntity uploadFile (@RequestParam("file") MultipartFile file){
        try {
            XlsxReadingStatements.uploadFile(file);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (FileException e){
            throw new IllegalArgumentException(e.getMsg());
        }
    }
}
