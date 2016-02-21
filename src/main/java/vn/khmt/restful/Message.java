package vn.khmt.restful;

/**
 * Created by VINH on 1/23/2016.
 */




import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Message {

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private int status;
    private String username;
    private String password;
    private String email;
    private String name;

    public Message() {

    }



    public Message(int status, String username, String password, String email, String name) {
        this.status = status;
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
    }




}

