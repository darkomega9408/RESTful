package vn.khmt.restful;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
//import org.postgresql.Driver;

/**
 *
 * @author LUONG The Nhan
 * @version 0.1
 */
public class ConnectToSQL {

    public static final String ERROR = "Error";
    public static final String NOTMATCH = "NotMatch";
    public static final String SQLSERVER = "sqlserver";
    public static final String SQLSERVERDRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    public static final String MYSQL = "mysql";
    public static final String MYSQLDRIVER = "com.mysql.jdbc.Driver";
    public static final String POSTGRESQL = "postgresql";
    public static final String POSTGRESQLDRIVER = "org.postgresql.Driver";

    Connection dbConnection = null;

    public ConnectToSQL(String type, String host, String dbname, String user, String pwd) {
        this.dbConnection = getDBConnection(type, host, dbname, user, pwd);
    }

    private Connection getDBConnection(String type, String host, String dbname, String user, String pwd) {
        if (type != null && !type.isEmpty()) {
            try {
                if (type.equalsIgnoreCase(SQLSERVER)) {
                    Class.forName(SQLSERVERDRIVER);
                    dbConnection = DriverManager.getConnection(host + ";database=" + dbname + ";sendStringParametersAsUnicode=true;useUnicode=true;characterEncoding=UTF-8;", user, pwd);
                } else if (type.equalsIgnoreCase(MYSQL)) {
                    Class.forName(MYSQLDRIVER);
                    dbConnection = DriverManager.getConnection(host + "/" + dbname, user, pwd);
                } else if (type.equalsIgnoreCase(POSTGRESQL)) {
                    Class.forName(POSTGRESQLDRIVER);
                    dbConnection = DriverManager.getConnection("jdbc:postgresql://ec2-54-227-253-228.compute-1.amazonaws.com:5432/d8viikojj42e3b?sslmode=require&user="+user+"&password="+pwd/*host + "/" + dbname + ";sslmode=require", user, pwd*/);
                }
                return dbConnection;
            } catch (ClassNotFoundException | SQLException ex) {
                System.err.println(ex.getMessage());
            }
        }
        return dbConnection;
    }
    
    public boolean closeDBConnection(){
        if (this.dbConnection != null) {
            try {
                this.dbConnection.close();
                return true;
            } catch (SQLException sqle) {
                System.err.println(sqle.getMessage());
                return false;
            }
        }
        
        return true;
    }
    
    public String getUserInfo(int id ) {
        try {
            String SQL = "SELECT username,email FROM public.user WHERE id = " + id ;
            Statement stmt = this.dbConnection.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);

            // Iterate through the data in the result set and display it.  
            if (rs.next()) {
                System.out.println("User exists");
                return rs.getString("username") + ". Your email is "+ rs.getString("email"); 
            }
        } catch (SQLException sqle) {
            System.err.println(sqle.getMessage());
        } finally {
            if (this.dbConnection != null) {
                try {
                    this.dbConnection.close();
                } catch (SQLException sqle) {
                    System.err.println(sqle.getMessage());
                }
            }
        }
        return "";
    }

    /**
     *
     * @param value Status you want to check
     * @param userName User name
     * @param pwd Password
     * @return
     */
    public boolean checkStatusEqual(int value, String userName , String pwd){
        try {
            String SQL = "SELECT status FROM public.user WHERE username = '" + userName + "' AND password = '" + pwd + "';";
            Statement stmt = this.dbConnection.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);

            // Iterate through the data in the result set and display it.  
            if (rs.next()) {
                System.out.println("User exists"); 
                return value == rs.getInt("status");
            }
        } catch (SQLException sqle) {
            System.err.println(sqle.getMessage());
        } finally {
            
        }
        return false;
    }
    
    public Object[] getUser(int id) {
        try {
            String SQL = "SELECT id, username, password, email, status, name FROM public.user WHERE id = " + id + ";";
            Statement stmt = this.dbConnection.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);

            // Iterate through the data in the result set and display it.  
            if (rs.next()) {
                System.out.println("User exists"); 
                Object[] res = new Object[6];
                res[0] = rs.getInt(1); 
                res[1] = rs.getString(2);
                res[2] = rs.getString(3);
                res[3] = rs.getString(4);
                res[4] = rs.getInt(5);
                res[5] = rs.getString(6);
                return res;
            }
        } catch (SQLException sqle) {
            System.err.println(sqle.getMessage());
        } 
        return null;
    }
    
    public List<User> getAllUser(){
        try {
            String SQL = "SELECT id, username, password, email, status , name FROM public.user";
            Statement stmt = this.dbConnection.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            
            List<User> usrList = new ArrayList<User>();
            // Iterate through the data in the result set and display it.  
            while (rs.next()) {
                System.out.println("User exists");
                User usr = new User(rs.getInt("id"),rs.getString("username"),rs.getString("password"),
                        rs.getString("email"),rs.getInt("status"),rs.getString("name"));
                usrList.add(usr);
            }
            
            return usrList;
        } catch (SQLException sqle) {
            System.err.println(sqle.getMessage());
        } 
        return null;
    }
    
    public String addUser(String username, String password, String email, int status, String name) {
        try {
            String SQL = "INSERT INTO public.user VALUES ((SELECT MAX(id) + 1 FROM public.user),'" + username + "','" + password + "','" + email + "'," + status + ",'" + name + "')";
            System.out.println(SQL);
            Statement stmt = this.dbConnection.createStatement();
            boolean rs = stmt.execute(SQL);
            if (rs)
                return "Added successfully";
        }catch (SQLException sqle) {
            System.err.println(sqle.getMessage());
            return ERROR;
        } 
        return "ADDED " + username + " SUCCESSFULLY";
    }

    public int checkUser(String username, String password) {
        try {
            if (username != null && password != null) {
                String SQL = "SELECT u.id FROM public.user u WHERE u.username = '" + username + "' AND u.password = '" + password + "'";
                Statement stmt = this.dbConnection.createStatement();
                ResultSet rs = stmt.executeQuery(SQL);

                // Iterate through the data in the result set and display it.  
                if (rs.next()) {
//                    if (rs.getInt(2) == 1) {
//                        return rs.getString(1);
//                    } else {
//                        return ERROR;
//                    
                    return rs.getInt(1);
                } else {
                    //return NOTMATCH;
                    return -1;
                }
            }
        } catch (SQLException sqle) {
            System.err.println(sqle.getMessage());
        } 
        return -1;
    }

    private static Timestamp getTimeStampOfDate(Date date) {
        if (date != null) {
            return new Timestamp(date.getTime());
        }
        return null;
    }

    public boolean executeSQL(String sql) {
        Connection con = null;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = this.dbConnection.prepareStatement(sql);
            // execute insert SQL stetement
            if (preparedStatement != null) {
                int res = preparedStatement.executeUpdate();
                return res == 1;
            }
        } catch (SQLException sqle) {
            System.err.println(sqle.getMessage());
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException sqle) {
                    System.err.println(sqle.getMessage());
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException sqle) {
                    System.err.println(sqle.getMessage());
                }
            }
        }
        return false;
    }

    public boolean updateUser(int id, User updatedUser){
        try {
            String SQL = "UPDATE public.user " 
                    +    "SET username = '" + updatedUser.getUserName() + "', password = '" + updatedUser.getPassword() + "', email= '" + updatedUser.getEmail() + "', status = " + updatedUser.getStatus() + ", name = '" + updatedUser.getName() + "'"
                    +    "WHERE id = " + updatedUser.getId() + ";";
            System.out.println(SQL);
            Statement stmt = this.dbConnection.createStatement();
            boolean rs = stmt.execute(SQL);
            return rs;
        }catch (SQLException sqle) {
            System.err.println(sqle.getMessage());
        } 
        return false;
    }
}
