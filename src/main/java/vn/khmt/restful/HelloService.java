package vn.khmt.restful;

import java.net.URISyntaxException;
import java.sql.SQLException;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author TheNhan
 */

@Path("hello")
public class HelloService {
    
    @GET
    @Path("/{param}")
    public Response getMsg(@PathParam("param") String msg) {

        String output = "Hello " + msg;

        return Response.status(200).entity(output).build();
    }
    
    @GET
    @Path("/haha")
    public Response retrieve() throws URISyntaxException, SQLException, ClassNotFoundException{
        String host = "ec2-54-227-253-228.compute-1.amazonaws.com";
        String user = "uzufecmqojhnyx";
        String port = "5432";
        String password = "WPJGueUbd3npLKslU2BEUOmMHx";
        String dbname = "d8viikojj42e3b";
        ConnectToSQL db = new ConnectToSQL(ConnectToSQL.POSTGRESQL, host, dbname, user, password);
        
        return Response.status(200).entity(db.checkUser("dino", "saurus")).build();
    }

	@POST
	@Path("/post")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response createTrackInJSON(@FormParam("username") String username,
                @FormParam("password") String password,
                @FormParam("email") String email,
                @FormParam("status") int status,
                @FormParam("name") String name) {
            
                    String host = "ec2-54-227-253-228.compute-1.amazonaws.com";
        String user = "uzufecmqojhnyx";
        String port = "5432";
        String pass = "WPJGueUbd3npLKslU2BEUOmMHx";
        String dbname = "d8viikojj42e3b";
        ConnectToSQL db = new ConnectToSQL(ConnectToSQL.POSTGRESQL, host, dbname, user, pass);
        
        

		return Response.status(201).entity(db.addUser(username, password, email, status, name)).build();
		
	}
}
