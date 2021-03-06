package com.showurskillz.repository;

/**
 * Created by nitin on 10/28/2016.
 */

import com.showurskillz.model.*;
import com.showurskillz.repository.IExecuteQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.showurskillz.model.UserAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Repository
public class ExecuteQuery implements IExecuteQuery  {

    private UserAuthorize authorize;
    private User user;
    private Address address;
    private String qry;

    public ExecuteQuery(){}

    @Autowired
    public ExecuteQuery(UserAuthorize authorize,User user,Address address)
    {
        this.authorize = authorize;
        this.user=user;
        this.address=address;
    }

    public UserAuthorize getAuthorize() {
        return authorize;
    }

    public void setAuthorize(UserAuthorize authorize) {
        this.authorize = authorize;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void clearAll(){
        user.setUsername(null);
        user.setAddress(null);
        user.setFname(null);
        user.setPassword(null);
        user.setPhoneNumber(null);
        user.setLname(null);
        address.setAddressLine1(null);
        address.setAddressLine2(null);
        address.setCity(null);
        address.setZip_code(null);
        address.setState(null);
        authorize.setAllow(false);
    }

    @Override
    public UserAuthorize loginQuery(String qry, Connection conn, User newUser) {

        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(qry);
            pst.setString(1, newUser.getUsername());
            pst.setString(2, newUser.getPassword());
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                authorize.setAllow(true);
            }
            else{
                authorize.setAllow(false);
            }
            return authorize;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int insertUser(Connection conn, User user) {

        clearAll();
        int result;
        qry="Insert into user VALUES (?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(qry);
            pst.setString(1, user.getUsername());
            pst.setString(2, user.getPassword());
            pst.setString(3, user.getFname());
            pst.setString(4, user.getLname());
            pst.setString(5, (user.getAddress()).getAddressLine1());
            pst.setString(6, (user.getAddress()).getAddressLine2());
            pst.setString(7, (user.getAddress()).getCity());
            pst.setString(8, (user.getAddress()).getZip_code());
            pst.setString(9, (user.getAddress()).getState());
            pst.setString(10, user.getPhoneNumber());
            result=pst.executeUpdate();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;

    }

    @Override
    public User retrieveUser(Connection conn, String username) {

        clearAll();
        qry="Select * from user where user_name=?";
        PreparedStatement pst = null;

        try {
            pst=conn.prepareStatement(qry);
            pst.setString(1,username);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                user.setUsername(username);
                user.setPassword(rs.getString("password"));
                user.setFname(rs.getString("fname"));
                user.setLname(rs.getString("lname"));
                user.setPhoneNumber(rs.getString("phone_no"));
                address.setAddressLine1(rs.getString("addr1"));
                address.setAddressLine2(rs.getString("addr2"));
                address.setCity(rs.getString("city"));
                address.setState(rs.getString("state"));
                address.setZip_code(rs.getString("zip_code"));
                user.setAddress(address);
                rs.close();
                conn.close();
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public void updateUser(Connection conn, User user) {
        qry="Update user set fname=?,lname=?,password=?,addr1=?,addr2=?,city=?,state=?,zip_code=?,phone_no=? where user_name=?";
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(qry);
            pst.setString(1, user.getFname());
            pst.setString(2, user.getLname());
            pst.setString(3, user.getPassword());
            pst.setString(4, (user.getAddress()).getAddressLine1());
            pst.setString(5, (user.getAddress()).getAddressLine2());
            pst.setString(6, (user.getAddress()).getCity());
            pst.setString(7, (user.getAddress()).getState());
            pst.setString(8, (user.getAddress()).getZip_code());
            pst.setString(9, user.getPhoneNumber());
            pst.setString(10, user.getUsername());
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
