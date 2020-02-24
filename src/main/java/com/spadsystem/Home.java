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
import java.util.ArrayList;
import java.util.List;

@Path("/")
public class Home {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String test() {
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
            if (new JDBC().checkUser(user)) {
                if (user.getType() != null)
                    return Response.seeOther(URI.create("/spadsystem/admin.html")).build();
                else
                    return Response.seeOther(URI.create("/spadsystem/main.html?id=" + id)).build();
            } else
                return Response.seeOther(URI.create("/spadsystem/index.jsp?text=auth")).build();
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
            return new JDBC().getFirstData();
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
            return new JDBC().addUser(user);
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
            return new JDBC().deleteUser(user);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Path("/image")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String uploadImage(@FormDataParam("file") InputStream uploadedInputStream,
                              @FormDataParam("file") FormDataContentDisposition fileDetails,
                              @FormDataParam("id") String id) {
        String uploadedFileLocation = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath().substring(0,
                this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath().indexOf("WEB-INF")) +
                "temp/img_" + id + fileDetails.getFileName().substring(fileDetails.getFileName().lastIndexOf("."));
        // save it
        try {
            OutputStream out = new FileOutputStream(new File(uploadedFileLocation));
            int read;
            byte[] bytes = new byte[1024];
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            new JDBC().saveImage(id, uploadedFileLocation);
//            Files.copy(new File(uploadedFileLocation).toPath(),
//                    new File(url).toPath(), StandardCopyOption.REPLACE_EXISTING);
            out.flush();
            out.close();
            return "File uploaded to : " + uploadedFileLocation;
        } catch (IOException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return "Error";
    }

    @Path("/search")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<User> search(String data) {
        String[] datas = data.substring(9, data.length() - 2).split(",");
        try {
            return new JDBC().searchDb(datas);
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
            return new JDBC().getUser(user);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
