package com.example.webinformationsystem.model;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.UUID;

@Entity
@Table(name = "ORDERS", schema = "SYS")
public class Order implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private String orderID;

    @OneToOne
    @JoinColumn(name = "CUSTOMER")
    private Customer customer;

    @Column(name = "DATA")
    private Date orderDate;

    @Column(name = "PRICE")
    private double orderPrice;

    public Order(){
        this.orderID = UUID.randomUUID().toString();
        this.customer = null;
        this.orderDate = null;
        this.orderPrice = 0;
    }

    public Order(Customer customer, Date orderDate, double orderPrice){
        this.orderID = UUID.randomUUID().toString();
        this.customer = customer;
        this.orderDate = orderDate;
        this.orderPrice = orderPrice;
    }

    public Customer getCustomer(){
        return customer;
    }

    public void setCustomer(Customer newCustomer){
        customer = newCustomer;
    }

    public Date getOrderDate(){
        return orderDate;
    }

    public void setOrderDate(Date newOrderDate)
    {
        orderDate = newOrderDate;
    }

    public double getOrderPrice(){
        return orderPrice;
    }

    public void setOrderPrice(double newOrderPrice){
        orderPrice = newOrderPrice;
    }

    public String getOrderID(){
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String toString(){
        return "ID: " + orderID + "; model.Customer: (" + customer.toString() + "); Date: " + orderDate + "; Price: " + orderPrice + "\n";
    }
}
