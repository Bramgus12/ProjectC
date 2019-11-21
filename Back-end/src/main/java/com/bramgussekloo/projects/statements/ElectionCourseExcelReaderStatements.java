package com.bramgussekloo.projects.statements;

import com.bramgussekloo.projects.FileHandling.FileService;
import com.bramgussekloo.projects.dataclasses.ElectionCourse;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ElectionCourseExcelReaderStatements {
    public static void uploadFile(MultipartFile file) throws IOException {
        try{
            FileService.uploadFile(file, "ElectionCourse");
        }
        catch (IOException e){
            throw new IOException(e.getMessage());
        }
    }

    private static File getFile(String resourceFolderName, String fileName, String statementClassName) throws IOException {
        ClassLoader classLoader = ElectionCourseExcelReaderStatements.class.getClassLoader();
        if (ElectionCourseExcelReaderStatements.class.getResource(statementClassName + ".class").toString().contains("jar")) {
            File file = new File("/usr/share/hr-routeplanner/ProjectC/Back-end/src/main/resources/" + resourceFolderName + "/" + fileName);
            if (!file.exists()) {
                throw new IOException("File not found");
            } else {
                return file;
            }
        }
        else {
            File resource = new File("src/main/resources/" + resourceFolderName + "/" + fileName);
            System.out.println(resource.getAbsolutePath());
            if (!resource.exists()) {
                throw new IOException("File not found");
            } else {
                return resource;
            }
        }
    }

    public static List<ElectionCourse> getExcelContent() throws IOException{
        try {
            String fileName = "kv-lijst.xlsx";
            Workbook workbook = null;
            FileInputStream excelFile = new FileInputStream(getFile("ElectionCourse", fileName, "ElectionCourseExcelReaderStatements"));

            //Find the file extension by splitting file name in substring  and getting only extension name
            String fileExtensionName = fileName.substring(fileName.indexOf("."));

            //Check condition if the file is a .xls file or .xlsx file
            if(fileExtensionName.equals(".xls")){
                //If it is .xls file then create object of HSSFWorkbook class
                workbook = new HSSFWorkbook(excelFile);
            }
            else if(fileExtensionName.equals(".xlsx")) {
                //If it is .xlsx file then create object of XSSFWorkbook class
                workbook = new XSSFWorkbook(excelFile);
            }

            Sheet worksheet = workbook.getSheetAt(0);

            /**
             * Works up to this point.
             *
             */
            DataFormatter formatter = new DataFormatter();
            List<ElectionCourse> rows = new ArrayList<>();
            //Create a loop to get the cell values of a row for one iteration
            for(int i=1; i<worksheet.getPhysicalNumberOfRows(); i++){
                Row row = worksheet.getRow(i);
                if(row.getLastCellNum() < row.getPhysicalNumberOfCells()) continue;

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
            return rows;
        } catch (IOException e) {
            throw new IOException("File not found");
        }
    }
}
