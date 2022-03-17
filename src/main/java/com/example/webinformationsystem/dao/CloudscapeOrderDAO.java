package com.example.webinformationsystem.dao;

import com.example.webinformationsystem.connection.JDBCConnection;
import com.example.webinformationsystem.connection.JDBCUtils;
import com.example.webinformationsystem.model.Customer;
import com.example.webinformationsystem.model.Order;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CloudscapeOrderDAO implements Repository<Order> {

    private static JDBCUtils jdbcUtils = JDBCConnection.getJDBCUtils();

    @Override
    public List<Order> get(){
        try (Connection connection = jdbcUtils.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM ORDERS");
            List<Order> orders = new ArrayList<>();
            while (resultSet.next()) {
                Order order = new Order();
                order.setOrderID(UUID.fromString(resultSet.getString("ID")));
                order.setCustomer(UUID.fromString(resultSet.getString("CUSTOMER")));
                order.setOrderDate(resultSet.getDate("DATA"));
                order.setOrderPrice(Double.parseDouble(resultSet.getString("PRICE")));
                orders.add(order);
            }
            return orders;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int add(Order order) {
        try (Connection connection = jdbcUtils.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO ORDERS VALUES (?, ?, ?, ?)");
            preparedStatement.setString(1, order.getOrderID().toString());
            preparedStatement.setString(2, order.getCustomer().toString());
            preparedStatement.setObject(3, order.getOrderDate().toLocalDate(), Types.DATE);
            preparedStatement.setDouble(4, order.getOrderPrice());
            preparedStatement.executeUpdate();
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int remove(String orderID) {
        try (Connection connection = jdbcUtils.getConnection()) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("DELETE FROM ORDERS WHERE id = ?");
            preparedStatement.setString(1, orderID);
            preparedStatement.executeUpdate();
            connection.close();
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int change(String orderID, Order newOrder) {
        try (Connection connection = jdbcUtils.getConnection()) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("UPDATE ORDERS SET CUSTOMER = ?, DATA = ?, PRICE = ? WHERE ID = ?");
            preparedStatement.setString(1, newOrder.getCustomer().toString());
            preparedStatement.setObject(2, newOrder.getOrderDate(), Types.DATE);
            preparedStatement.setDouble(3, newOrder.getOrderPrice());
            preparedStatement.setString(4, orderID);
            preparedStatement.executeUpdate();
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public boolean check(String orderID) {
        try (Connection connection = jdbcUtils.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT ID FROM ORDERS where ID = " + orderID);
            List<String> ordersID = new ArrayList<>();
            while (resultSet.next()) {
                ordersID.add(resultSet.getString("ID"));
            }
            if(ordersID.size() != 0){
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
