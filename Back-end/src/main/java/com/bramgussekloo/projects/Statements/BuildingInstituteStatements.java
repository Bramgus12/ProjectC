package com.bramgussekloo.projects.Statements;

import com.bramgussekloo.projects.DataClasses.Building;
import com.bramgussekloo.projects.DataClasses.BuildingInstitute;
import com.bramgussekloo.projects.DataClasses.Error;
import com.bramgussekloo.projects.Database.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BuildingInstituteStatements {
    public static ArrayList<BuildingInstitute> getAllBuildingInstitutes(){
        Connection conn = new DatabaseConnection().getConnection();
        ArrayList<BuildingInstitute> list = new ArrayList<>();
        try {
            ResultSet result = conn.createStatement().executeQuery("SELECT * FROM building_institute");
            while (result.next()){
                Integer id = result.getInt("id");
                Integer building_id = result.getInt("building_id");
                Integer institute_id = result.getInt("institute_id");
                BuildingInstitute buildingInstitute = new BuildingInstitute(id, building_id, institute_id);
                list.add(buildingInstitute);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return list;
    }

    public static BuildingInstitute getBuildingInstitute(Integer id){
        Connection conn = new DatabaseConnection().getConnection();
        try {
            ResultSet result = conn.createStatement().executeQuery("SELECT * FROM building_institute WHERE id=" + id);
            while (result.next()){
                Integer building_id = result.getInt("building_id");
                Integer institute_id = result.getInt("institute_id");
                 return new BuildingInstitute(id, building_id, institute_id);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return new BuildingInstitute();
    }

    public static String createBuildingInstitute(BuildingInstitute buildingInstitute){
        Connection conn = new DatabaseConnection().getConnection();
        Integer id = buildingInstitute.getId();
        Integer building_id = buildingInstitute.getBuilding_id();
        Integer institute_id = buildingInstitute.getInstitute_id();
        try{
            conn.createStatement().execute("INSERT INTO building_institute VALUES (" + id + ", " + building_id + ", " + institute_id + ");");
            return "yes";
        } catch (SQLException e){
            e.printStackTrace();
            return e.getMessage();
        }
    }
    public static String deleteBuildingInstitute(Integer id){
        Connection conn = new DatabaseConnection().getConnection();
        try{
            conn.createStatement().execute("DELETE FROM building_institute WHERE id=" + id);
            return "yes";
        } catch (SQLException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public static String updateBuildingInstitute(BuildingInstitute buildingInstitute){
        Connection conn = new DatabaseConnection().getConnection();
        Integer id = buildingInstitute.getId();
        Integer building_id = buildingInstitute.getBuilding_id();
        Integer institute_id = buildingInstitute.getInstitute_id();
        String output;
        try{
            conn.createStatement().execute("UPDATE building_institute SET building_id=" + building_id + ", institute_id=" + institute_id + " WHERE id=" + id );
            output = "yes";
        } catch (SQLException e) {
            e.printStackTrace();
            output = e.getMessage();
        }
        return output;
    }
}