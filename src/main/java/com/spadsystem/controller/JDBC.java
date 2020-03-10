package com.spadsystem.controller;

import com.spadsystem.model.User;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JDBC {
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/spadsystem?useUnicode=yes&characterEncoding=UTF-8&user=root&password=";
    //  Database credentials
    static final String USER = "root";
    //    static final String PASS = "R@njbar1";
    static final String PASS = "";
    private static JDBC instance;
    private Connection connection;

    public JDBC() throws ClassNotFoundException, SQLException {
        Class.forName(JDBC_DRIVER);
        connection = DriverManager.getConnection(DB_URL);
//        instance = this;
    }

    public JDBC getInstance() throws SQLException, ClassNotFoundException {
        if (instance == null)
            instance = new JDBC();
        return instance;
    }

    public boolean addUser(User user) {
        if (user.getUser_id().length() > 1)
            try {
                String sql;
                PreparedStatement preparedStatement;
                sql = "Select * from user where id=?";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, user.getUser_id());
                if (preparedStatement.executeQuery().next())
                    return updateUser(user);
                else {
                    sql = "INSERT INTO user VALUES (?,?,'')";
                    preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, user.getUser_id());
                    preparedStatement.setString(2, user.getPassword() == null ? user.getUser_id() : user.getPassword());
                    preparedStatement.executeUpdate();
                    sql = "INSERT INTO user_data VALUES (?,?,?,'',?,?)";
                    preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, user.getUser_id());
                    preparedStatement.setString(2, user.getFirst_name());
                    preparedStatement.setString(3, user.getLast_name());
                    preparedStatement.setString(4, user.getGender());
                    preparedStatement.setString(5, user.getNational_code());
                    preparedStatement.executeUpdate();
                    sql = "INSERT INTO position VALUES (?,?,?)";
                    preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, user.getUser_id());
                    preparedStatement.setString(2, user.getMaster_id());
                    preparedStatement.setString(3, user.getPosition());
                    preparedStatement.executeUpdate();
                    sql = "INSERT INTO address VALUES (?,?,?,?,'',?)";
                    preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, user.getUser_id());
                    preparedStatement.setString(2, user.getState());
                    preparedStatement.setString(3, user.getCity());
                    preparedStatement.setString(4, user.getAddress());
                    preparedStatement.setString(5, user.getService_table());
                    preparedStatement.executeUpdate();
                    sql = "INSERT  INTO telephone VALUES (?,?,?,?,?,?,?)";
                    preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, user.getUser_id());
                    preparedStatement.setString(2, user.getTelephone());
                    preparedStatement.setString(3, user.getMobile());
                    preparedStatement.setString(4, user.getInternalTel1());
                    preparedStatement.setString(5, user.getInternalTel2());
                    preparedStatement.setString(6, user.getFax());
                    preparedStatement.setString(7, user.getPreIntTel());
                    preparedStatement.executeUpdate();
                    preparedStatement.close();
                    return true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        return false;
    }

    public boolean updateUser(User user) {
        if (user.getUser_id().length() > 1)
            try {
                String sql;
                PreparedStatement preparedStatement;
                if (user.getPassword() != null && user.getPassword().length() > 5) {
                    sql = "UPDATE user SET password=? WHERE id=?";
                    preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, user.getPassword());
                    preparedStatement.setString(2, user.getUser_id());
                    preparedStatement.executeUpdate();
                }
                sql = "UPDATE user_data SET first_name=?, last_name=?, gender=?, national_code=? WHERE id=?";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(5, user.getUser_id());
                preparedStatement.setString(1, user.getFirst_name());
                preparedStatement.setString(2, user.getLast_name());
                preparedStatement.setString(3, user.getGender());
                preparedStatement.setString(4, user.getNational_code());
                preparedStatement.executeUpdate();
                sql = "UPDATE position SET master_id=?, position=? WHERE id=?";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(3, user.getUser_id());
                preparedStatement.setString(1, user.getMaster_id());
                preparedStatement.setString(2, user.getPosition());
                preparedStatement.executeUpdate();
                sql = "UPDATE address SET state=?, city=?, address=?, service_table=? WHERE id=?";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(5, user.getUser_id());
                preparedStatement.setString(1, user.getState());
                preparedStatement.setString(2, user.getCity());
                preparedStatement.setString(3, user.getAddress());
                preparedStatement.setString(4, user.getService_table());
                preparedStatement.executeUpdate();
                sql = "UPDATE telephone SET tel=?,mobile=?,intTel1=?,intTel2=?,fax=?,preIntTel=? WHERE id=?";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(7, user.getUser_id());
                preparedStatement.setString(1, user.getTelephone());
                preparedStatement.setString(2, user.getMobile());
                preparedStatement.setString(3, user.getInternalTel1());
                preparedStatement.setString(4, user.getInternalTel2());
                preparedStatement.setString(5, user.getFax());
                preparedStatement.setString(6, user.getPreIntTel());
                preparedStatement.executeUpdate();
                preparedStatement.close();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        return false;
    }

    public boolean checkUser(User user) {
        try {
            String sql = "Select type from user where id=? and password=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getUser_id());
            preparedStatement.setString(2, user.getPassword());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user.setType(resultSet.getString(1));
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<User> searchDb(String[] datas) {
        List<User> list = new ArrayList<>();
        if (datas[0].length() > 0) {
            String city = null, state = null, pos = null, dat = null;
            if (datas[0].equals("all"))
                dat = "";
            else
                for (String data : datas) {
                    if (data.contains("شهر"))
                        city = data.replace("شهر ", "");
                    else if (data.contains("استان"))
                        state = data.replace("استان ", "");
                    else if (data.contains("سمت"))
                        pos = data.replace("سمت ", "");
                    else if (data.length() > 0)
                        dat = data;
                }
            User user;
            try {
                String sql = "SELECT first_name,last_name,tel,mobile,intTel1,intTel2,fax,preIntTel,position,state,city," +
                        "address,ud.id,gender,service_table,type,national_code FROM user_data AS ud JOIN user ON ud.id = user.id JOIN telephone AS tel " +
                        "ON tel.id = ud.id JOIN position AS pos ON pos.id = ud.id JOIN address AS ad ON ad.id = ud.id " +
                        "WHERE " + (dat == null ? "1" : " (ud.first_name LIKE ? OR " +
                        "ud.last_name LIKE ?)") + (pos == null ? "" : " AND position LIKE ?") + (state == null ? "" :
                        " AND state LIKE ?") + (city == null ? "" : " AND city LIKE ?");
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                int i = 1;
                if (dat != null) {
                    preparedStatement.setString(i++, '%' + dat + '%');
                    preparedStatement.setString(i++, '%' + dat + '%');
                }
                if (pos != null)
                    preparedStatement.setString(i++, '%' + pos + '%');
                if (state != null)
                    preparedStatement.setString(i++, '%' + state + '%');
                if (city != null)
                    preparedStatement.setString(i++, '%' + city + '%');
                System.out.println(preparedStatement.toString());
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    user = new User();
                    user.setUser_id(resultSet.getString(13));
                    sql = "SELECT * FROM image WHERE id=" + user.getUser_id();
                    ResultSet resultSet0 = connection.prepareStatement(sql).executeQuery();
                    if (resultSet0.next())
                        user.setImage(resultSet0.getString(2));
                    user.setFirst_name(resultSet.getString(1));
                    user.setLast_name(resultSet.getString(2));
                    user.setTelephone(resultSet.getString(3));
                    user.setMobile(resultSet.getString(4));
                    user.setInternalTel1(resultSet.getString(5));
                    user.setInternalTel2(resultSet.getString(6));
                    user.setFax(resultSet.getString(7));
                    user.setPreIntTel(resultSet.getString(8));
                    user.setPosition(resultSet.getString(9));
                    user.setState(resultSet.getString(10));
                    if (user.getState().length() > 1) {
                        sql = "SELECT * FROM state WHERE name='" + resultSet.getString(10) + "'";
                        resultSet0 = connection.prepareStatement(sql).executeQuery();
                        if (resultSet0.next())
                            user.setPreTel(resultSet0.getString(3));
                    }
                    user.setCity(resultSet.getString(11));
                    user.setAddress(resultSet.getString(12));
                    user.setGender(resultSet.getString(14));
                    user.setService_table(resultSet.getString(15));
                    user.setType(resultSet.getString(16));
                    user.setNational_code(resultSet.getString(17));
                    list.add(user);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return list;
        }
        return null;
    }

    public boolean saveImage(String id, String uploadedFileLocation) {
        try {
            String sql;
            PreparedStatement preparedStatement;
            sql = "Select * from image where id=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, id);
            if (preparedStatement.executeQuery().next()) {
                sql = "UPDATE image set location=? WHERE id=?";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(2, id);
                preparedStatement.setString(1, uploadedFileLocation);
                preparedStatement.executeUpdate();
            } else {
                sql = "INSERT INTO image VALUES (?,?)";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, id);
                preparedStatement.setString(2, uploadedFileLocation);
                preparedStatement.executeUpdate();
            }
            preparedStatement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void fromExcel() {
        String excelFilePath = "/home/mahdi/Documents/persons.xlsx";
        try {
            long start = System.currentTimeMillis();
            FileInputStream inputStream = new FileInputStream(excelFilePath);
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet firstSheet = workbook.getSheetAt(0);
            for (Row nextRow : firstSheet) {
                Iterator<Cell> cellIterator = nextRow.cellIterator();
                Cell nextCell = cellIterator.next();
                User user = new User();
                user.setFirst_name(nextCell.getStringCellValue());
                nextCell = cellIterator.next();
                user.setLast_name(nextCell.getStringCellValue());
                nextCell = cellIterator.next();
                user.setUser_id(String.valueOf((int) nextCell.getNumericCellValue()));
                nextCell = cellIterator.next();
                user.setAddress(nextCell.getStringCellValue());
                nextCell = cellIterator.next();
                user.setPosition(nextCell.getStringCellValue());
                nextCell = cellIterator.next();
                user.setTelephone(String.valueOf((int) nextCell.getNumericCellValue()));
                nextCell = cellIterator.next();
                user.setInternalTel1(String.valueOf((int) nextCell.getNumericCellValue()));
                nextCell = cellIterator.next();
                user.setInternalTel2(String.valueOf((int) nextCell.getNumericCellValue()));
                nextCell = cellIterator.next();
                user.setFax(String.valueOf((int) nextCell.getNumericCellValue()));
                nextCell = cellIterator.next();
                user.setPreIntTel(String.valueOf((int) nextCell.getNumericCellValue()));
                new JDBC().addUser(user);
            }
            workbook.close();
            long end = System.currentTimeMillis();
            System.out.printf("Import done in %d ms\n", (end - start));

        } catch (IOException ex1) {
            System.out.println("Error reading file");
            ex1.printStackTrace();
        } catch (SQLException ex2) {
            System.out.println("Database error");
            ex2.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<String> getFirstData() {
        List<String> list = new ArrayList<>();
        try {
            String sql = "Select name from state";
            ResultSet resultSet = connection.prepareStatement(sql).executeQuery();
            while (resultSet.next())
                list.add(resultSet.getString(1));
            sql = "Select name, state_id from city";
            resultSet = connection.prepareStatement(sql).executeQuery();
            while (resultSet.next())
                list.add(resultSet.getString(1) + ',' + resultSet.getString(2));
            sql = "Select position from position group by 1";
            resultSet = connection.prepareStatement(sql).executeQuery();
            while (resultSet.next())
                list.add("pos:" + resultSet.getString(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public User getUser(User user) throws SQLException {
        String sql = "SELECT first_name,last_name,gender FROM user_data WHERE id=" + user.getUser_id();
        ResultSet resultSet = connection.prepareStatement(sql).executeQuery();
        if (resultSet.next()) {
            user.setFirst_name(resultSet.getString(1));
            user.setLast_name(resultSet.getString(2));
            user.setGender(resultSet.getString(3));
        }
        sql = "SELECT * FROM telephone WHERE id=" + user.getUser_id();
        resultSet = connection.prepareStatement(sql).executeQuery();
        if (resultSet.next()) {
            user.setTelephone(resultSet.getString(2));
            user.setMobile(resultSet.getString(3));
            user.setInternalTel1(resultSet.getString(4));
            user.setInternalTel2(resultSet.getString(5));
            user.setFax(resultSet.getString(6));
            user.setPreIntTel(resultSet.getString(7));
        }
        sql = "SELECT * FROM position WHERE id=" + user.getUser_id();
        resultSet = connection.prepareStatement(sql).executeQuery();
        if (resultSet.next()) {
            user.setMaster_id(resultSet.getString(2));
            user.setPosition(resultSet.getString(3));
        }
        sql = "SELECT * FROM address WHERE id=" + user.getUser_id();
        resultSet = connection.prepareStatement(sql).executeQuery();
        if (resultSet.next()) {
            user.setState(resultSet.getString(2));
            user.setCity(resultSet.getString(3));
            user.setAddress(resultSet.getString(4));
            user.setService_table(resultSet.getString(6));
        }
        sql = "SELECT * FROM image WHERE id=" + user.getUser_id();
        resultSet = connection.prepareStatement(sql).executeQuery();
        if (resultSet.next())
            user.setImage(resultSet.getString(2));
        return user;
    }

    public boolean deleteUser(User user) throws SQLException {
        String sql = "DELETE FROM user WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, user.getUser_id());
        preparedStatement.executeUpdate();
        sql = "DELETE FROM user_data WHERE id=?";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, user.getUser_id());
        preparedStatement.executeUpdate();
        sql = "DELETE FROM telephone WHERE id=?";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, user.getUser_id());
        preparedStatement.executeUpdate();
        sql = "DELETE FROM position WHERE id=?";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, user.getUser_id());
        preparedStatement.executeUpdate();
        sql = "DELETE FROM address WHERE id=?";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, user.getUser_id());
        preparedStatement.executeUpdate();
        sql = "DELETE FROM image WHERE id=?";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, user.getUser_id());
        preparedStatement.executeUpdate();
        return true;
    }

    public boolean sendForgottenPassword(User user) throws SQLException {
        String sql = "select password FROM telephone AS tel JOIN user ON user.id = tel.id WHERE mobile=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, user.getMobile());
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next())
            return SMS.sendSms(user.getMobile(),
                    "رمز عبور شما در سامانه اسپادسیستم " + resultSet.getString(1) + " است");
        else
            return false;
    }
//    public static void main(String[] args) {
//        fromExcel();
//    }
}
