package vn.khmt.restful;


import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author TheNhan
 */

@Path("user")
public class HelloService {
    
    String host = "ec2-54-227-253-228.compute-1.amazonaws.com";
    String user = "uzufecmqojhnyx";
    String port = "5432";
    String pass = "WPJGueUbd3npLKslU2BEUOmMHx";
    String dbname = "d8viikojj42e3b";
    
    @GET
    @Path("/{param}")
    public Response getUser(@PathParam("param") int id) {

        ConnectToSQL db = new ConnectToSQL(ConnectToSQL.POSTGRESQL, host, dbname, user, pass);
        Object[] user = db.getUser(id);
        String res = "Username: " + user[1] + " - Email: " + user[2] + " - Name: " + user[3];

        return Response.status(200).entity(res).build();
    }

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response createTrackInJSON(@FormParam("username") String username,
            @FormParam("password") String password,
            @FormParam("email") String email,
            @FormParam("status") int status,
            @FormParam("name") String name) {

        ConnectToSQL db = new ConnectToSQL(ConnectToSQL.POSTGRESQL, host, dbname, user, pass);
        
        

        return Response.status(201).entity(db.addUser(username, password, email, status, name)).build();

    }
}
