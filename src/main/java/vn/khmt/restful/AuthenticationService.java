/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.khmt.restful;

import java.io.IOException;
import java.util.Base64;
import java.util.StringTokenizer;

/**
 *
 * @author VINH
 */
public class AuthenticationService {
    
    private static User user = new User();

    // Except for admin , all users can only retrieve their own info 
    public static boolean authenticate(String authCredentials){
        if( authCredentials == null )
            return false;
        
        final String encodedUserPwd = authCredentials.replaceFirst("Basic ", "");
        String userNameAndPwd = null;
        
        try{
            
            byte[] decodedBytes = Base64.getDecoder().decode(encodedUserPwd);
            userNameAndPwd = new String(decodedBytes,"UTF-8");   
        }
        catch(IOException e){
            e.printStackTrace();
        }
        
        final StringTokenizer tokenizer = new StringTokenizer(userNameAndPwd,":");
        final String userName = tokenizer.nextToken();
        final String pwd = tokenizer.nextToken();
        
        user.setUserName(userName);
        user.setPassword(pwd);
        
        // Check our username and password out
        boolean authenStatus = "admin".equals(userName) && "admin".equals(pwd);            
        return authenStatus;
    }
    
    public static User getUser(){
        return user;
    }
}
