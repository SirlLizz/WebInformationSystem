package com.example.webinformationsystem.dao;

import com.example.webinformationsystem.connection.JDBCConnection;
import com.example.webinformationsystem.connection.JDBCUtils;
import com.example.webinformationsystem.model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CloudscapeCustomerDAO implements Repository<Customer> {

    private static JDBCUtils jdbcUtils = JDBCConnection.getJDBCUtils();

    @Override
    public List<Customer> get(){
        try (Connection connection = jdbcUtils.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM CUSTOMERS");
            List<Customer> customers = new ArrayList<>();
            while (resultSet.next()) {
                Customer customer = new Customer();
                customer.setCustomerID(UUID.fromString(resultSet.getString("ID")));
                customer.setName(resultSet.getString("NAME"));
                customer.setPhoneNumber(resultSet.getString("TELEPHONE"));
                customer.setAddress(resultSet.getString("ADDRESS"));
                customers.add(customer);
            }
            return customers;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int add(Customer customer){
        try (Connection connection = jdbcUtils.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO CUSTOMERS VALUES (?, ?, ?, ?)");
            preparedStatement.setString(1, UUID.randomUUID().toString());
            preparedStatement.setString(2, customer.getName());
            preparedStatement.setString(3, customer.getPhoneNumber());
            preparedStatement.setString(4, customer.getAddress());
            preparedStatement.executeUpdate();
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int remove(String customerID){
        try (Connection connection = jdbcUtils.getConnection()) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("DELETE FROM CUSTOMERS WHERE ID = ?");
            preparedStatement.setString(1, customerID);
            preparedStatement.executeUpdate();
            connection.close();
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int change(String customerID, Customer newCustomer){
        try (Connection connection = jdbcUtils.getConnection()) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("UPDATE CUSTOMERS SET NAME = ?, TELEPHONE = ?, ADDRESS = ? WHERE ID = ?");
            preparedStatement.setString(1, newCustomer.getName());
            preparedStatement.setString(2, newCustomer.getPhoneNumber());
            preparedStatement.setString(3, newCustomer.getAddress());
            preparedStatement.setString(4, customerID);
            preparedStatement.executeUpdate();
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

}
