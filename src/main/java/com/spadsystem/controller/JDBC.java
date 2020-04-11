package com.spadsystem.controller;

import com.spadsystem.model.Mail;
import com.spadsystem.model.User;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.sql.*;
import java.util.*;

public class JDBC {
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/spadsystem?useUnicode=yes&characterEncoding=UTF-8&autoReconnect=true&user=root&password=";
    private static JDBC instance;
    private Connection connection;

    private JDBC() throws ClassNotFoundException, SQLException {
        Class.forName(JDBC_DRIVER);
        connection = DriverManager.getConnection(DB_URL);
    }

    public static JDBC getInstance() throws SQLException, ClassNotFoundException {
        if (instance == null)
            instance = new JDBC();
        return instance;
    }

    public static void clearDB() {
        String sql;
        sql = "Select ud.id,first_name,last_name,position,address,service_table,fullAddress from user_data AS ud " +
                "JOIN position AS pos ON ud.id=pos.id JOIN address AS ad ON ad.id=ud.id";
        try {
            PreparedStatement preparedStatement = getInstance().connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                sql = "UPDATE user_data SET first_name=?, last_name=? WHERE id=?";
                preparedStatement = getInstance().connection.prepareStatement(sql);
                preparedStatement.setString(1, User.setPersianText(resultSet.getString(2)));
                preparedStatement.setString(2, User.setPersianText(resultSet.getString(3)));
                preparedStatement.setString(3, resultSet.getString(1));
                preparedStatement.executeUpdate();
                sql = "UPDATE position SET position=? WHERE id=?";
                preparedStatement = getInstance().connection.prepareStatement(sql);
                preparedStatement.setString(2, User.setPersianText(resultSet.getString(1)));
                preparedStatement.setString(1, User.setPersianText(resultSet.getString(4)));
                preparedStatement.executeUpdate();
                sql = "UPDATE address SET address=?, service_table=?, fullAddress=? WHERE id=?";
                preparedStatement = getInstance().connection.prepareStatement(sql);
                preparedStatement.setString(4, User.setPersianText(resultSet.getString(1)));
                preparedStatement.setString(1, User.setPersianText(resultSet.getString(5)));
                preparedStatement.setString(2, User.setPersianText(resultSet.getString(6)));
                preparedStatement.setString(3, User.setPersianText(resultSet.getString(7)));
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
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
                    sql = "INSERT INTO address VALUES (?,?,?,?,'',?,?,?)";
                    preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, user.getUser_id());
                    preparedStatement.setString(2, user.getState());
                    preparedStatement.setString(3, user.getCity());
                    preparedStatement.setString(4, user.getAddress());
                    preparedStatement.setString(5, user.getService_table());
                    preparedStatement.setString(6, user.getFullAddress());
                    preparedStatement.setString(7, user.getService_unit());
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
                    sql = "UPDATE user SET password=?, type=? WHERE id=?";
                    preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, user.getPassword());
                    preparedStatement.setString(2, user.getType());
                    preparedStatement.setString(3, user.getUser_id());
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
                sql = "UPDATE address SET state=?, city=?, address=?, service_table=?, fullAddress=?, service_unit=? WHERE id=?";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(7, user.getUser_id());
                preparedStatement.setString(1, user.getState());
                preparedStatement.setString(2, user.getCity());
                preparedStatement.setString(3, user.getAddress());
                preparedStatement.setString(4, user.getService_table());
                preparedStatement.setString(5, user.getFullAddress());
                preparedStatement.setString(6, user.getService_unit());
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

    public List<User> searchDb(String[] data) {
        System.out.println(Arrays.toString(data));
        List<User> list = new ArrayList<>();
        if (data.length > 0) {
            StringBuilder where = new StringBuilder();
            if (data[0].equals("all")) {
                where.append("1");
            } else {
                for (String datum : data) {
                    where.append("first_name LIKE \"%").append(datum)
                            .append("%\" OR last_name LIKE \"%").append(datum)
                            .append("%\" OR address LIKE \"%").append(datum)
                            .append("%\" OR service_table LIKE \"%").append(datum)
                            .append("%\" OR service_unit LIKE \"%").append(datum)
                            .append("%\" OR ");
                }
                where.setLength(where.length() - 4);
            }
            User user;
            try {
                String sql = "SELECT first_name,last_name,tel,mobile,intTel1,intTel2,fax,preIntTel,position,state,city," +
                        "address,ud.id,gender,service_table,type,national_code,fullAddress,service_unit FROM user_data " +
                        "AS ud JOIN user ON ud.id = user.id JOIN telephone AS tel " +
                        "ON tel.id = ud.id JOIN position AS pos ON pos.id = ud.id JOIN address AS ad ON ad.id = ud.id " +
                        "WHERE " + where;
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
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
                    user.setFullAddress(resultSet.getString(18));
                    user.setService_unit(resultSet.getString(19));
                    list.add(user);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return list;
        }
        return null;
    }

    public void saveImage(String id, String uploadedFileLocation) {
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
            } else {
                sql = "INSERT INTO image VALUES (?,?)";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, id);
                preparedStatement.setString(2, uploadedFileLocation);
            }
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void fromExcel() {
        String excelFilePath = JDBC.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "990117.xlsx";
        try {
            long start = System.currentTimeMillis();
            FileInputStream inputStream = new FileInputStream(excelFilePath);
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet firstSheet = workbook.getSheetAt(0);
            for (Row nextRow : firstSheet) {
                Iterator<Cell> cellIterator = nextRow.cellIterator();
                Cell nextCell = cellIterator.next();
                User user = new User();
                try {
                    user.setUser_id(String.valueOf((int) nextCell.getNumericCellValue()));
                } catch (Exception ignored) {
                    continue;
                }
                nextCell = cellIterator.next();
                user.setFirst_name(nextCell.getStringCellValue());
                nextCell = cellIterator.next();
                user.setLast_name(nextCell.getStringCellValue());
                nextCell = cellIterator.next();
                user.setAddress(nextCell.getStringCellValue());
                nextCell = cellIterator.next();
                user.setService_unit(nextCell.getStringCellValue());
                nextCell = cellIterator.next();
                user.setPosition(nextCell.getStringCellValue());
                nextCell = cellIterator.next();
                user.setService_table(nextCell.getStringCellValue());
//                nextCell = cellIterator.next();
//                user.setTelephone(String.valueOf((int) nextCell.getNumericCellValue()));
                nextCell = cellIterator.next();
                try {
                    user.setInternalTel1(String.valueOf((int) nextCell.getNumericCellValue()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                nextCell = cellIterator.next();
//                user.setInternalTel2(String.valueOf((int) nextCell.getNumericCellValue()));
//                nextCell = cellIterator.next();
//                user.setFax(String.valueOf((int) nextCell.getNumericCellValue()));
//                nextCell = cellIterator.next();
//                user.setPreIntTel(String.valueOf((int) nextCell.getNumericCellValue()));
                getInstance().addUser(user);
//                new JDBC().addUser(user);
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
            user.setFullAddress(resultSet.getString(7));
            user.setService_unit(resultSet.getString(8));
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

    public static void main(String[] args) {
//        fromExcel();
        try {
            System.out.println(new JDBC().exportExcel());
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public Mail saveMail(Mail mail) throws SQLException {
        String sql = "INSERT INTO mail (sender,receiver,time,title,body,attach) VALUES (?,?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, mail.getSender().getUser_id());
        preparedStatement.setString(2, mail.getReceiver().getUser_id());
        preparedStatement.setString(3, mail.getTime());
        preparedStatement.setString(4, mail.getTitle());
        preparedStatement.setString(5, mail.getBody());
        preparedStatement.setString(6, mail.getAttach());
        if (preparedStatement.executeUpdate() != -1) {
            sql = "SELECT pk FROM mail ORDER BY pk DESC LIMIT 1";
            preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            mail.setPk(Long.parseLong(resultSet.getString(1)));
        }
        return mail;
    }

    public List<Mail> getMail(String id) throws SQLException {
        String sql = "select * FROM mail WHERE (receiver=? OR sender=?) AND NOT `delete`";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, id);
        preparedStatement.setString(2, id);
        final ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<Mail> result = new ArrayList<Mail>();
        String dir = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        while (resultSet.next()) {
            Mail mail = new Mail();
            mail.setPk(Long.parseLong(resultSet.getString("pk")));
            User user = new User();
            user.setUser_id(resultSet.getString("sender"));
            if (!resultSet.getString("sender").equals(id))
                getName(resultSet.getString("sender"), user);
            mail.setSender(user);
            user = new User();
            user.setUser_id(resultSet.getString("receiver"));
            if (!resultSet.getString("receiver").equals(id))
                getName(resultSet.getString("receiver"), user);
            mail.setReceiver(user);
            mail.setTime(resultSet.getString("time"));
            mail.setTitle(resultSet.getString("title"));
            mail.setBody(resultSet.getString("body"));
            mail.setSend(resultSet.getBoolean("send"));
            mail.setRead(resultSet.getBoolean("read"));
            mail.setStar(resultSet.getBoolean("star"));
            mail.setTrash(resultSet.getBoolean("trash"));
            mail.setDelete(resultSet.getBoolean("delete"));
            if (resultSet.getString("attach") != null && resultSet.getString("attach").length() > 0) {
                File[] matchingFiles = new File(dir.substring(0, dir.indexOf("WEB-INF")) + "temp")
                        .listFiles(new FilenameFilter() {
                            public boolean accept(File dir, String name) {
                                try {
                                    return name.startsWith(resultSet.getString("attach"));
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                return false;
                            }
                        });
                mail.setAttach(Arrays.toString(matchingFiles));
            }
            result.add(mail);
        }
        return result;
    }

    private void getName(String id, User user) throws SQLException {
        String sql = "select first_name, last_name, position FROM user_data AS ud JOIN position AS pos ON pos.id=ud.id WHERE ud.id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            user.setFirst_name(resultSet.getString(1));
            user.setLast_name(resultSet.getString(2));
            user.setPosition(resultSet.getString(3));
        }
    }

    public Mail updateMail(Mail mail) throws SQLException {
        String sql = "UPDATE mail SET title=?,body=?,`read`=?,`send`=?,`star`=?,`trash`=?,`delete`=? WHERE pk=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, mail.getTitle());
        preparedStatement.setString(2, mail.getBody());
        preparedStatement.setBoolean(3, mail.isRead());
        preparedStatement.setBoolean(4, mail.isSend());
        preparedStatement.setBoolean(5, mail.isStar());
        preparedStatement.setBoolean(6, mail.isTrash());
        preparedStatement.setBoolean(7, mail.isDelete());
        preparedStatement.setLong(8, mail.getPk());
        preparedStatement.executeUpdate();
        return mail;
    }

    public User updateUserPassword(User user) throws SQLException {
        String sql = "UPDATE user SET password=? WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, user.getPassword());
        preparedStatement.setString(2, user.getUser_id());
        preparedStatement.executeUpdate();
        return user;
    }

    public String exportExcel() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Employee Data");
        List<User> users = searchDb(new String[]{"all"});
        Map<String, Object[]> data = new TreeMap<String, Object[]>();
        data.put("1", new Object[]{"کد پرسنلی", "نام", "نام خانوادگی", "سمت سازمانی", "واحد سازمانی", "محل خدمت", "میز خدمت", "جنسیت", "کدملی", "آدرس کامل", "تلفن همراه", "تلفن ثابت", "داخلی اول", "داخلی دوم", "پیش شماره", "دورنگار", "رییس"});
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            data.put(String.valueOf(i + 2), new Object[]{user.getUser_id(), user.getFirst_name(), user.getLast_name(), user.getPosition(), user.getAddress(), user.getService_unit(), user.getService_table(), user.getGender(), user.getNational_code(), user.getFullAddress(), user.getMobile(), user.getTelephone(), user.getInternalTel1(), user.getInternalTel2(), user.getPreIntTel(), user.getFax(), user.getType()});
        }
        //Iterate over data and write to sheet
        Set<String> keySet = data.keySet();
        int rowNum = 0;
        for (String key : keySet) {
            Row row = sheet.createRow(rowNum++);
            Object[] objArr = data.get(key);
            int cellNum = 0;
            for (Object obj : objArr) {
                Cell cell = row.createCell(cellNum++);
                if (obj instanceof String)
                    cell.setCellValue((String) obj);
                else if (obj instanceof Integer)
                    cell.setCellValue((Integer) obj);
            }
        }
        try {
            String dir = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
            FileOutputStream out = new FileOutputStream(new File(dir.substring(0, dir.indexOf("WEB-INF")) + "temp/export.xlsx"));
            workbook.write(out);
            out.close();
            return "/spadsystem/temp/export.xlsx";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Error";
    }
}

