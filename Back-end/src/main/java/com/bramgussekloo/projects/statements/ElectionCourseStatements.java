package com.bramgussekloo.projects.statements;

import com.bramgussekloo.projects.FileHandling.FileService;
import com.bramgussekloo.projects.Properties.GetPropertyValues;
import com.bramgussekloo.projects.database.DatabaseConnection;
import com.bramgussekloo.projects.dataclasses.ElectionCourse;
import com.bramgussekloo.projects.dataclasses.ElectionCourseDescription;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * PDO implementation
 * source: https://javaconceptoftheday.com/statement-vs-preparedstatement-vs-callablestatement-in-java/
 */
public class ElectionCourseStatements {
    private static String fileNameVar = "kv-lijst.xlsx";
    public static void uploadFile(MultipartFile file) throws IOException {
        File f = GetPropertyValues.getResourcePath("ElectionCourse", fileNameVar);
        if (!f.exists()) {
            FileService.uploadFile(file, "ElectionCourse", fileNameVar);
        } else {
            throw new IOException("File already exists. Try using PUT if you want to update it.");
        }
    }

    public static List<ElectionCourse> getExcelContent() throws IOException {
        try {
            Workbook workbook = null;
            File f = GetPropertyValues.getResourcePath("ElectionCourse", fileNameVar);
            if (f.exists()) {
                FileInputStream excelFile = new FileInputStream(f);

                //Find the file extension by splitting file name in substring  and getting only extension name
//            String fileExtensionName = fileName.substring(fileName.indexOf("."));

                //Check condition if the file is a .xls file or .xlsx file
//            if(fileExtensionName.equals(".xls")){
//                //If it is .xls file then create object of HSSFWorkbook class
//                workbook = new HSSFWorkbook(excelFile);
//            }
//            else if(fileExtensionName.equals(".xlsx")) {
                //If it is .xlsx file then create object of XSSFWorkbook class
                workbook = new XSSFWorkbook(excelFile);
//            }

                Sheet worksheet = workbook.getSheetAt(0);

                DataFormatter formatter = new DataFormatter();
                List<ElectionCourse> rows = new ArrayList<>();
                //Create a loop to get the cell values of a row for one iteration
                for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
                    Row row = worksheet.getRow(i);
                    if (row.getLastCellNum() < row.getPhysicalNumberOfCells()) continue;

                    String courseCode = formatter.formatCellValue(row.getCell(0));
                    String name = formatter.formatCellValue(row.getCell(1));
                    String period = formatter.formatCellValue(row.getCell(2));
                    String groupNumber = formatter.formatCellValue(row.getCell(3));
                    String teacher = formatter.formatCellValue(row.getCell(4));
                    String dayOfTheWeek = formatter.formatCellValue(row.getCell(5));
                    String startTime = formatter.formatCellValue(row.getCell(6));
                    String endTime = formatter.formatCellValue(row.getCell(7));
                    String location = formatter.formatCellValue(row.getCell(8));
                    String classroom = formatter.formatCellValue(row.getCell(9));

                    rows.add(new ElectionCourse(
                            courseCode,
                            name,
                            period,
                            groupNumber,
                            teacher,
                            dayOfTheWeek,
                            startTime,
                            endTime,
                            location,
                            classroom
                    ));
                }
                excelFile.close();
                return rows;
            } else {
                throw new IOException("File does not exist.");
            }
        } catch(IOException e){
            throw new IOException("File not found");
        }
    }

    public static void updateFile(MultipartFile file) throws IOException {
        File folder = GetPropertyValues.getResourcePath("ElectionCourse", "");
        File[] files = folder.listFiles();
        assert files != null;
        if(files.length <= 2) {
            for (File f : files) {
                if (f.exists() && !f.toString().contains(".gitkeep")) {
                    if (f.delete()) {
                        FileService.uploadFile(file, "ElectionCourse", fileNameVar);
                    } else {
                        throw new IOException("File deletion failed");
                    }
                }
            }
        } else {
            throw new IOException("There are more than one files on the server. Try to fix that.");
        }
    }

    public static ElectionCourseDescription createElectionCourseDescription(ElectionCourseDescription electionCourseDescription) throws SQLException{
        Connection conn = new DatabaseConnection().getConnection();

        PreparedStatement pstmt = conn.prepareStatement("INSERT INTO election_course (electioncoursecode, electioncoursename, description) VALUES (?,?,?);");
        pstmt.setString(1,electionCourseDescription.getCourseCode());
        pstmt.setString(2,electionCourseDescription.getName());
        pstmt.setString(3,electionCourseDescription.getDescription());
        pstmt.executeQuery();
        return getElectionCourseDescription(electionCourseDescription.getCourseCode());
    }

    public static List<ElectionCourseDescription> getAllElectionCourseDescription()throws SQLException {
        Connection conn = new DatabaseConnection().getConnection();
        List<ElectionCourseDescription> allElectionCourseDescriptions = new ArrayList<>();
        ResultSet result = conn.createStatement().executeQuery("SELECT * FROM election_course");
        while (result.next()) {
            allElectionCourseDescriptions.add(getResult(result));
        }
        return allElectionCourseDescriptions;
    }

    public static ElectionCourseDescription getElectionCourseDescription(String courseCode) throws SQLException {
        Connection conn = new DatabaseConnection().getConnection();
        PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM election_course WHERE electioncoursecode=?;");
        pstmt.setString(1,courseCode);
        ResultSet result = pstmt.executeQuery();
        result.next();
        return getResult(result);
    }

    public static ElectionCourseDescription getElectionCourseDescriptionByName(String name) throws SQLException {
        Connection conn = new DatabaseConnection().getConnection();
        PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM election_course WHERE electioncoursename=?;");
        pstmt.setString(1,name);
        ResultSet result = pstmt.executeQuery();
        result.next();
        return getResult(result);
    }

    public static ElectionCourseDescription updateElectionCourseDescription(ElectionCourseDescription electionCourseDescription) throws SQLException {
        Connection conn = new DatabaseConnection().getConnection();
        String ElectionCourseCode = electionCourseDescription.getCourseCode();
        PreparedStatement pstmt = conn.prepareStatement("UPDATE election_course SET electioncoursename =?, description=? WHERE electioncoursecode=?;");
        pstmt.setString(1,electionCourseDescription.getName());
        pstmt.setString(2,electionCourseDescription.getDescription());
        pstmt.setString(3,ElectionCourseCode);
        pstmt.executeQuery();
        return getElectionCourseDescription(ElectionCourseCode);
    }

    public static ElectionCourseDescription deleteElectionCourseDescription(String courseCode) throws SQLException {
        Connection conn = new DatabaseConnection().getConnection();
        ElectionCourseDescription deletedElectionCourseDescription = getElectionCourseDescription(courseCode);
        PreparedStatement deletePstmt = conn.prepareStatement("DELETE FROM election_course WHERE electioncoursecode =?;");
        deletePstmt.setString(1,courseCode);
        deletePstmt.executeQuery();
        return deletedElectionCourseDescription;
    }
// TESTING
    public static ArrayList ifExist(ElectionCourseDescription electionCourseDescription) throws SQLException {
        ArrayList<Boolean> exists = new ArrayList();
        String ElectionCourseCode = electionCourseDescription.getCourseCode();
        String ElectionCourseName = electionCourseDescription.getName();
        boolean check1 = getElectionCourseDescription(ElectionCourseCode).getCourseCode().isEmpty();
        boolean check2 = getElectionCourseDescriptionByName(ElectionCourseName).getName().isEmpty();
        exists.add(check1);
        exists.add(check2);
        return exists;
    }

    private static ElectionCourseDescription getResult (ResultSet result) throws SQLException {
        String ElectionCourseCode = result.getString("electioncoursecode");
        String ElectionCourseName = result.getString("electioncoursename");
        String ElectionCourseDescription = result.getString("description");
        return new ElectionCourseDescription(ElectionCourseCode, ElectionCourseName, ElectionCourseDescription);
    }
}
