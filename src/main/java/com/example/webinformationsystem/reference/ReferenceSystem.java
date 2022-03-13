package com.example.webinformationsystem.reference;

import com.example.webinformationsystem.connection.JDBCConnection;
import com.example.webinformationsystem.model.Customer;
import com.example.webinformationsystem.model.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReferenceSystem{

    public List<Customer> getCustomers(){
        try (Connection connection = JDBCConnection.get()) {
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

    /*
    public List<Order> getOrders(){
        return orders;
    }
     */

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

    public int addCustomer(Customer customer){
        System.out.println(customer.toString());
        try (Connection connection = JDBCConnection.get()) {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into customers values (?, ?, ?, ?)");
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

    public int removeCustomer(String customerID){
        try (Connection connection = JDBCConnection.get()) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("delete from customers where id = ?");
            preparedStatement.setString(1, customerID);
            preparedStatement.executeUpdate();
            connection.close();
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /*
    public void removeOrder(String orderID){
        for(int i =0; i< orders.size();i++){
            if(Objects.equals(orders.get(i).getOrderID(), orderID)){
                orders.remove(i);
            }
        }
    }
     */

    public int changeCustomerInformation(String customerID, Customer newCustomer){
        try (Connection connection = JDBCConnection.get()) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("update customers set name = ?, telephone = ?, address = ? where id = ?");
            preparedStatement.setString(1, UUID.randomUUID().toString());
            preparedStatement.setString(2, newCustomer.getName());
            preparedStatement.setString(3, newCustomer.getPhoneNumber());
            preparedStatement.setString(4, newCustomer.getAddress());
            preparedStatement.executeUpdate();
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

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
