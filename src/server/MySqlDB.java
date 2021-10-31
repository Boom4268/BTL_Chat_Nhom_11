/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.sql.*;
import model.Account;

/**
 *
 * @author Cuong
 */
public class MySqlDB{
    static final String dbUrl = "jdbc:mysql://localhost:3306/chat";
    static final String dbUsername = "root";
    static final String jdbcDriver = "com.mysql.cj.jdbc.Driver";
    static final String dbPassword = "";
    private Connection con = null;
    
    public MySqlDB(){
        
    }
    
    //Thiet lap trang thai sau khi dang nhap    
    public static void setOnline(String username, Boolean isOnline) throws SQLException, ClassNotFoundException{
        Connection con = null;
        Class.forName(jdbcDriver);
        con = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
        
        String query = "UPDATE account SET isOnline = 0 WHERE username = ?";
        if(isOnline)
            query = "UPDATE account SET isOnline = 1 WHERE username = ?";
        
        PreparedStatement ps = con.prepareStatement(query);    
        ps.setString(1, username);
        ps.executeUpdate();
        
        ps.close();
        con.close();
    }
    //Lay password de check
    public static String getPassword(String username) throws SQLException, ClassNotFoundException{
        Connection con = null;
        String pass="";
        Class.forName(jdbcDriver);
        con = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
        
        String query = "SELECT password FROM account where username = ?;";
        
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, username);
        ResultSet resultSet = ps.executeQuery();
        
        while(resultSet.next()){
            pass = resultSet.getString(1);
        }
        resultSet.close();
        ps.close();
        con.close();
        
        return pass;
    }
    //Dang ki
    public static Boolean addAccount(String username, String password) throws SQLException, ClassNotFoundException{
        Connection con = null;
        
        try {
            Class.forName(jdbcDriver);
            con = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);

            String query = "INSERT INTO account (username, password, isOnline) VALUES (?, ?, 0)";

            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);
            int res = ps.executeUpdate();

            ps.close();
            con.close();

            if(res == 0) 
                return false; 
            else 
                return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (con != null) con.close();
            } catch(SQLException se) {
                se.printStackTrace();
            }
        }
    }
    //K hieu
    public static String[] getAccount(String username) throws SQLException, ClassNotFoundException{
        Connection con = null;
        Class.forName(jdbcDriver);
        con = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
        
        String query = "SELECT username FROM account , friendship"
                + "WHERE username != 'admin' "
                + "AND username != ? "
                + "AND isOnline = 1 "
                + "AND username = user1 "
                + "AND user2 = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, username);
        ps.setString(2, username);  
        
        ResultSet result =  ps.executeQuery();
        
        int count = 0;
        result.last();
        count = result.getRow();
        result.beforeFirst();

        String[] usersArr = new String[count];
        int i = 0;
        while(result.next()) {			
            usersArr[i] = result.getString(1);
            i++;
        }

        ps.close();
        con.close();

        return usersArr;
    }
    //K hieu
    public static String[] getAccountFriends(String user) throws SQLException, ClassNotFoundException{
        Connection con = null;
        Class.forName(jdbcDriver);
        con = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
        
        String query = "SELECT username FROM account WHERE "
                + "username NOT IN (SELECT user2 FROM friendship WHERE user1 = ?) "
                + "AND username != ? "
                + "AND username != 'GLOBAL' "
                + "AND username != 'ADMIN';";
        PreparedStatement statement = con.prepareStatement(query);
        statement.setString(1, user);
        statement.setString(2, user);
        
        ResultSet result =  statement.executeQuery();

        int count = 0;
        result.last();
        count = result.getRow();
        result.beforeFirst();

        String[] usersArr = new String[count];
        int i = 0;
        while(result.next()) {                
                usersArr[i] = result.getString(1);
                i++;
        }

        statement.close();
        con.close();
        return usersArr;
    }
    
    //Them ban 
    public static Boolean addFriendDB(String user1, String user2) {
        Connection con = null;
        try {   
            Class.forName(jdbcDriver);
            con = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
        
            String query = "INSERT INTO frendship (user1, user2) VALUES (?,?), (?,?)";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, user1);
            statement.setString(2, user2);
            statement.setString(3, user2);
            statement.setString(4, user1);

            statement.executeUpdate();

            statement.close();
            con.close();

            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (con != null) con.close();
            } catch(SQLException se) {
                se.printStackTrace();
            }
        }		
    }
}




































