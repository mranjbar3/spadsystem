package com.spadsystem;

import com.spadsystem.controller.JDBC;
import com.spadsystem.model.Mail;
import com.spadsystem.model.User;
import org.glassfish.jersey.media.multipart.*;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Path("/")
public class Home {
    private String dir = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String test() {
        JDBC.fromExcel();
//        JDBC.clearDB();
        return "server is GOOD!";
    }

    @GET
    @Path("/get-all-data")
    @Produces(MediaType.TEXT_PLAIN)
    public String exportData() {
        try {
            return JDBC.getInstance().exportExcel();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return "Error";
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
                    return Response.seeOther(URI.create("/spadsystem/index.jsp?id=" + id + "&type=" + user.getType())).build();
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

    @Path("/update-password")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public User updateUserPassword(User user) {
        try {
            return JDBC.getInstance().updateUserPassword(user);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
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
        String uploadedFileLocation = dir.substring(0, dir.indexOf("WEB-INF")) + "temp/img_" + id + ".png";
        // save it
        try {
            OutputStream out = new FileOutputStream(new File(uploadedFileLocation));
            int read;
            byte[] bytes = new byte[1024];
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            JDBC.getInstance().saveImage(id, uploadedFileLocation);
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
        try {
            return JDBC.getInstance().searchDb(new JSONObject(data).getString("data").split(" "));
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

    @Path("/send_mail")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Mail sendMail(Mail mail) {
        try {
            return JDBC.getInstance().saveMail(mail);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Path("/send_file")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public String sendImage(@FormDataParam("file") FormDataBodyPart files) {
        String obj = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        OutputStream out;
        int read;
        byte[] bytes = new byte[1024];
        for (BodyPart part : files.getParent().getBodyParts()) {
            InputStream is = part.getEntityAs(InputStream.class);
            ContentDisposition meta = part.getContentDisposition();

            String uploadedFileLocation = obj.substring(0, obj.indexOf("WEB-INF")) + "temp/" + meta.getFileName();
            try {
                out = new FileOutputStream(new File(uploadedFileLocation));
                while ((read = is.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "image saved";
    }

    @Path("/get_mail")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<Mail> getMail(Mail mail) {
        try {
            return JDBC.getInstance().getMail(mail.getReceiver().getUser_id());
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Path("/update_mail")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Mail updateMail(Mail mail) {
        try {
            return JDBC.getInstance().updateMail(mail);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
