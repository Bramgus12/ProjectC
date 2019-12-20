package com.bramgussekloo.projects.controller;

import com.bramgussekloo.projects.ProjectsApplication;
import com.bramgussekloo.projects.dataclasses.ElectiveCourse;
import com.bramgussekloo.projects.dataclasses.ElectiveCourseDescription;
import com.bramgussekloo.projects.statements.ElectiveCourseStatements;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

// Controller for xlsx reader
@Api(value = "Elective Course list")
@RestController
@RequestMapping("/api/elective-course")
public class ElectiveCourseController {

    /**
     * Gets all Elective Course as an Object and save them into a List
     *
     * @return List of Objects
     */
    @ApiOperation(value = "Get a list of all Elective Course from the excel file")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list", response = ElectiveCourse.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @GetMapping
    private ResponseEntity getElectiveCourseList() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(ElectiveCourseStatements.getExcelContent());
        } catch (IOException | SQLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Add a specific Elective Course description
     * Validate for duplicate before adding data.
     *
     * @param electiveCourseDescription
     * @return added ElectiveCourseDescription object
     */
    @ApiOperation(value = "Add a specific Elective Course with its description, use https://www.freeformatter.com/json-escape.html to escape text")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added an elective course description", response = ElectiveCourseDescription.class),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @PostMapping
    private ResponseEntity createElectiveCourseDescription(
            @ApiParam(value = "Add an Elective Course description.", required = true) @RequestBody ElectiveCourseDescription electiveCourseDescription
    ) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(ElectiveCourseStatements.createElectiveCourseDescription(electiveCourseDescription));
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Lookup a specific Elective Course for its description
     *
     * @param coursecode
     * @return ElectiveCourseDescription object
     */
    @ApiOperation(value = "Lookup a specific Elective Course for its description")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved course description", response = ElectiveCourseDescription.class),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @GetMapping("/{coursecode}")
    private ResponseEntity getElectiveCourseDescription(
            @ApiParam(value = "Course Code that you want to lookup.", required = true) @PathVariable String coursecode
    ) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(ElectiveCourseStatements.getElectiveCourseDescription(coursecode));
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Lookup all Elective Course with its description
     *
     * @return list of ElectiveCourseDescription object
     */
    @ApiOperation(value = "Lookup all Elective Course with its description")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved all existing courses with its description", response = ElectiveCourseDescription.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @GetMapping("/description")
    private ResponseEntity getAllElectiveCourseDescription() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(ElectiveCourseStatements.getAllElectiveCourseDescription());
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Update a specific Elective Course for its description
     *
     * @param coursecode
     * @return updated ElectiveCourseDescription object
     */
    @ApiOperation(value = "Update a specific Elective Course for its description")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated course description", response = ElectiveCourseDescription.class),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @PutMapping("/{coursecode}")
    private ResponseEntity updateElectiveCourseDescription(
            @ApiParam(value = "Course Code that you want to update.", required = true) @PathVariable String coursecode,
            @ApiParam(value = "The Object that you want to update", required = true) @RequestBody ElectiveCourseDescription electiveCourseDescription) {
        try {
            if (coursecode.equals(electiveCourseDescription.getCourseCode())) {
                ElectiveCourseDescription oldData = ElectiveCourseStatements.getElectiveCourseDescription(coursecode);
                ElectiveCourseDescription newData = new ElectiveCourseDescription();
                newData.setCourseCode(oldData.getCourseCode());
                if (!electiveCourseDescription.getDescription().equals(oldData.getDescription())) {
                    newData.setDescription(electiveCourseDescription.getDescription());

                } else {
                    newData.setDescription(oldData.getDescription());
                }

                if (!electiveCourseDescription.getName().equals(oldData.getName())) {
                    newData.setName(electiveCourseDescription.getName());
                } else {
                    newData.setName(oldData.getName());
                }
                return ResponseEntity.status(HttpStatus.OK).body(ElectiveCourseStatements.updateElectiveCourseDescription(newData));
            } else {
                throw new IllegalArgumentException("Elective Course Code doesn't exist!");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Delete a specific Elective Course with its description
     *
     * @param coursecode
     * @return ElectiveCourseDescription object of the deleted object
     */
    @ApiOperation(value = "Delete a specific Elective Course with its description")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted elective course description", response = ElectiveCourseDescription.class),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @DeleteMapping("/{coursecode}")
    private ResponseEntity deleteElectiveCourseDescription(
            @ApiParam(value = "Course Code that you want to delete.", required = true) @PathVariable String coursecode
    ) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(ElectiveCourseStatements.deleteElectiveCourseDescription(coursecode));
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }


    /**
     * Update Elective Course excel file in Elective Course folder by deleting the file first if exist then upload again.
     *
     * @return void
     */
    @ApiOperation(value = "Update Elective Course excel file in Elective Course folder by deleting the file first if exist then upload again.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully file replaced", response = ElectiveCourse.class),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @PutMapping
    private ResponseEntity updateFile(@RequestParam MultipartFile file) {
        try {
            ElectiveCourseStatements.updateFile(file);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Upload Excel file in Elective Course folder if file doesn't exist.
     *
     * @return void
     */
    @ApiOperation(value = "Upload Excel file in Elective Course folder if file doesn't exist.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully uploaded"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @PostMapping("/upload")
    private ResponseEntity uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            ElectiveCourseStatements.uploadFile(file);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    // puts the Error in the right format
    @ExceptionHandler
    void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) throws IOException {
        ProjectsApplication.printErrorInConsole(e.getMessage());
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }
}
