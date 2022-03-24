package com.example.webinformationsystem.model;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.UUID;

@Entity
@Table(name = "ORDERS")
public class Order implements Serializable{

    @Id
    @Column(name = "ID")
    private UUID orderID;

    //@OneToOne (optional=false, cascade=CascadeType.ALL)
    @JoinColumn(name = "CUSTOMER")
    private UUID customer;

    @Column(name = "DATA")
    private Date orderDate;

    @Column(name = "PRICE")
    private double orderPrice;

    public Order(){
        this.orderID = UUID.randomUUID();
        this.customer = null;
        this.orderDate = null;
        this.orderPrice = 0;
    }

    public Order(UUID customer, Date orderDate, double orderPrice){
        this.orderID = UUID.randomUUID();
        this.customer = customer;
        this.orderDate = orderDate;
        this.orderPrice = orderPrice;
    }

    public UUID getCustomer(){
        return customer;
    }

    public void setCustomer(UUID newCustomer){
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

    public UUID getOrderID(){
        return orderID;
    }

    public void setOrderID(UUID orderID) {
        this.orderID = orderID;
    }

    public String toString(){
        return "ID: " + orderID + "; model.Customer: (" + customer.toString() + "); Date: " + orderDate + "; Price: " + orderPrice + "\n";
    }
}
