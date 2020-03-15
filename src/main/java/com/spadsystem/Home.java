package com.spadsystem;

import com.spadsystem.controller.JDBC;
import com.spadsystem.model.User;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.net.URI;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Path("/")
public class Home {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String test() {
//        JDBC.fromExcel();
        JDBC.clearDB();
        return "server is GOOD!";
    }

    @Path("/login")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response login(@FormParam("user_code") String id, @FormParam("password") String password) {
        User user = new User();
        user.setUser_id(id);
        user.setPassword(password);
        try {
            if (JDBC.getInstance().checkUser(user)) {
                if (user.getType() != null && user.getType().equals("true"))
                    return Response.seeOther(URI.create("/spadsystem/admin.html")).build();
                else
                    return Response.seeOther(URI.create("/spadsystem/?id=" + id)).build();
            } else
                return Response.seeOther(URI.create("/spadsystem/login.html?text=auth")).build();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return Response.seeOther(URI.create("/spadsystem/index.jsp?text=error")).build();
    }

    @Path("/first_data")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> firstData() {
        try {
            return JDBC.getInstance().getFirstData();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Path("/add_user")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public boolean addUser(User user) {
        try {
            return JDBC.getInstance().addUser(user);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Path("/delete_user")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public boolean deleteUser(User user) {
        try {
            return JDBC.getInstance().deleteUser(user);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Path("/image")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public JSONObject uploadImage(@FormDataParam("file") InputStream uploadedInputStream,
                                  @FormDataParam("file") FormDataContentDisposition fileDetails,
                                  @FormDataParam("id") String id) {
        String uploadedFileLocation = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath().substring(0,
                this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath().indexOf("WEB-INF")) +
                "temp/img_" + id + ".png";//fileDetails.getFileName().substring(fileDetails.getFileName().lastIndexOf("."));
        // save it
        try {
            OutputStream out = new FileOutputStream(new File(uploadedFileLocation));
            int read;
            byte[] bytes = new byte[1024];
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            JDBC.getInstance().saveImage(id, uploadedFileLocation);
//            Files.copy(new File(uploadedFileLocation).toPath(),
//                    new File(url).toPath(), StandardCopyOption.REPLACE_EXISTING);
            out.flush();
            out.close();
            return new JSONObject("{\"File uploaded to\" : \"" + uploadedFileLocation + "\"}");
        } catch (IOException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new JSONObject("detail:Error");
    }

    @Path("/search")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<User> search(String data) {
        String[] datas = data.substring(9, data.length() - 2).split(",");
        try {
            return JDBC.getInstance().searchDb(datas);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Path("/get_user")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public User getUser(String string) {
        User user = new User();
        user.setUser_id(new JSONObject(string).getString("user_id"));
        try {
            return JDBC.getInstance().getUser(user);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Path("/forgot")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean sendForgottenPassword(String string) {
        User user = new User();
        user.setMobile(new JSONObject(string.replace("\"", "")).getString("mobile"));
        try {
            return JDBC.getInstance().sendForgottenPassword(user);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        Date date = new Date(1583929383000L);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd 'T'HH:mm:ss.SSSZ");
        format.setTimeZone(TimeZone.getTimeZone("Asia/Tehran"));
        System.out.println(format.format(date));
    }
}
