package com.example.webinformationsystem.dao;

import com.example.webinformationsystem.connection.JDBCConnection;
import com.example.webinformationsystem.connection.JDBCUtils;
import com.example.webinformationsystem.model.Customer;
import com.example.webinformationsystem.model.Order;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
                order.setOrderDate(LocalDate.parse(resultSet.getString("DATA")));
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
    public int add(Order obj) {
        return 0;
    }

    @Override
    public int remove(String id) {
        return 0;
    }

    @Override
    public int change(String id, Order obj) {
        return 0;
    }

    /*
    public int checkCustomer(Customer customer){
        int k = -1;
        for (int i = 0; i< customers.size();i++){
            if((customers.get(i).getName().equals(customer.getName()))&&
                    (customers.get(i).getAddress().equals(customer.getAddress()))&&
                    (customers.get(i).getPhoneNumber().equals(customer.getPhoneNumber()))){
                k=i;
            }
        }
        return k;
    }
     */

    /*
    public int checkOrder(Order order){
        int k = -1;
        for (int i = 0; i< orders.size();i++){
            if((orders.get(i).getOrderDate().equals(order.getOrderDate()))&&
                    (orders.get(i).getOrderPrice() == order.getOrderPrice())&&
                    (orders.get(i).getCustomer().getAddress().equals(order.getCustomer().getAddress()))&&
                    (orders.get(i).getCustomer().getPhoneNumber().equals(order.getCustomer().getPhoneNumber()))&&
                    (orders.get(i).getCustomer().getName().equals(order.getCustomer().getName()))){
                k = i;
            }
        }
        return k;
    }
     */


    /*
    public void addOrder(Customer customer, LocalDate date, double orderPrice){
        if(checkCustomer(customer) == -1){
            orders.add(new Order(customer, date, orderPrice));
        }else{
            orders.add(new Order(customers.get(checkCustomer(customer)), date, orderPrice));
        }
    }
     */

    /*
    public Customer getCustomerFromID(String customerID){
        for (Customer customer : customers) {
            if (Objects.equals(customer.getCustomerID(), customerID)) {
                return customer;
            }
        }
        return null;
    }
     */

    /*
    public Order getOrderFromID(String orderID){
        for (Order order : orders) {
            if (Objects.equals(order.getOrderID(), orderID)) {
                return order;
            }
        }
        return null;
    }
     */

    /*
    public void removeOrder(String orderID){
        for(int i =0; i< orders.size();i++){
            if(Objects.equals(orders.get(i).getOrderID(), orderID)){
                orders.remove(i);
            }
        }
    }
     */


    /*
    public void removeOrder(String orderID){
        for(int i =0; i< orders.size();i++){
            if(Objects.equals(orders.get(i).getOrderID(), orderID)){
                orders.remove(i);
            }
        }
    }
     */

    /*
    public void changeOrderInformation(String orderID, Customer customer, LocalDate orderDate, double orderPrice){
        if(checkCustomer(customer) == -1){
            getOrderFromID(orderID).setCustomer(customer);
        }else{
            getOrderFromID(orderID).setCustomer(customers.get(checkCustomer(customer)));
        }
        getOrderFromID(orderID).setOrderDate(orderDate);
        getOrderFromID(orderID).setOrderPrice(orderPrice);
    }
     */


}
