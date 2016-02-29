package vn.khmt.restful;

import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author TheNhan
 */
@Path("/user")
public class HelloService {
    final String AUTHENTICATION_HEADER = "Authorization";
    
    
    String host = "ec2-54-227-253-228.compute-1.amazonaws.com";
    String user = "uzufecmqojhnyx";
    String port = "5432";
    String pass = "WPJGueUbd3npLKslU2BEUOmMHx";
    String dbname = "d8viikojj42e3b";

//    @GET
//    @Path("/{param}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getMsg(@PathParam("param") int id) {
//        String output = "Hello ";
//
//        ConnectToSQL mConnection = new ConnectToSQL(ConnectToSQL.POSTGRESQL, host, dbname, user, pass);
//        String usrName = mConnection.getUserInfo(id);
//        if (usrName.equals(""))
//            output = "User not found.";
//        else output += usrName ;
//
//        return Response.status(200).entity(output).build();
//    }
    
    @GET
    @Path("/{param}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("param") int id , @Context HttpHeaders httpHeaders) {
        
        // Basic Authentication
        String authCredentials = httpHeaders.getRequestHeaders().getFirst(AUTHENTICATION_HEADER);
        boolean authenStatus = AuthenticationService.authenticate(authCredentials);

        ConnectToSQL mConnection = new ConnectToSQL(ConnectToSQL.POSTGRESQL, host, dbname, user, pass);

        Object[] obj = mConnection.getUser(id);
        User user = new User((int) obj[0], obj[1].toString(), obj[2].toString(), obj[3].toString(), (int)obj[4], obj[5].toString());

        Response res = null;
        
        // If authen succeed , retrieve data from DB
        if (authenStatus || (AuthenticationService.getUser().getUserName().equals(user.getUserName())
                && AuthenticationService.getUser().getPassword().equals(user.getPassword()))) {
            res = Response.status(200).entity(user).build();
        } // Otherwise return UNAUTHORIZED response
        else {
            res = Response.status(Response.Status.UNAUTHORIZED).build();
        } 
        
        mConnection.closeDBConnection();
        return res;
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUser(@Context HttpHeaders httpHeaders) {

        // Authentication first
        // Basic Authentication
        String authCredentials = httpHeaders.getRequestHeaders().getFirst(AUTHENTICATION_HEADER);
        boolean authenStatus = AuthenticationService.authenticate(authCredentials);

        // Connect to DB
        ConnectToSQL mConnection = new ConnectToSQL(ConnectToSQL.POSTGRESQL, host, dbname, user, pass);
        
        // Check status of user 
        // 1. is admin -> don't need to check status
        // 2. otherwise -> check whether status equals 3 or not
        authenStatus = ("admin".equals(AuthenticationService.getUser().getUserName()) && "admin".equals(AuthenticationService.getUser().getPassword()))
                || mConnection.checkStatusEqual(3, AuthenticationService.getUser().getUserName(), AuthenticationService.getUser().getPassword());

        Response res = null;
        // If user was authorized , retrieve data from DB
        if (authenStatus) {
            
            List<User> users = mConnection.getAllUser();
            res =  Response.status(200).entity(new GenericEntity<List<User>>(users) {}).build();
            
        } // Otherwise return UNAUTHORIZED response
        else {
            res = Response.status(Response.Status.UNAUTHORIZED).build();
        } 
      
        mConnection.closeDBConnection();
        return res;
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



        return Response.status(200).entity(db.addUser(username, password, email, status, name)).build();

    }

    
    @PUT
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUser(User updatedUser , @Context HttpHeaders httpHeaders){
        
         // Basic Authentication
        String authCredentials = httpHeaders.getRequestHeaders().getFirst(AUTHENTICATION_HEADER);
        AuthenticationService.authenticate(authCredentials);

        ConnectToSQL mConnection = new ConnectToSQL(ConnectToSQL.POSTGRESQL, host, dbname, user, pass);

        int userId = mConnection.checkUser(AuthenticationService.getUser().getUserName(),AuthenticationService.getUser().getPassword());
        
        Response res = null;
        
        // If user existed
        if ( userId != -1 ) {
            mConnection.updateUser(userId, updatedUser);
            res = Response.status(200).build();
        } // Otherwise return UNAUTHORIZED response
        else {
            res = Response.status(Response.Status.UNAUTHORIZED).build();
        } 
        
        mConnection.closeDBConnection();
        return res;
    }
    
//    @POST
//    @Path("/add")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response createTrackInJSON(Message message) {
//
//
//        //ConnectToSQL db = new ConnectToSQL(ConnectToSQL.POSTGRESQL, host, dbname, user, pass);
//
//
//
//        //String s = db.addUser(message.getUsername(), message.getPassword(), message.getEmail(), message.getStatus(), message.getName());
//        //return s.equals("Added successfully");
//        return Response.status(200).build();
//    }
}
