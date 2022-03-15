package com.example.webinformationsystem.model;

import com.example.webinformationsystem.reference.LocalDateAdapter;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.UUID;

@XmlRootElement
@XmlType(name = "order")
public class Order implements Serializable{

    @XmlAttribute(name = "ID")
    private UUID orderID;
    private UUID customer;
    private String orderDate;
    private double orderPrice;

    public Order(){
        this.orderID = UUID.randomUUID();
        this.customer = null;
        this.orderDate = null;
        this.orderPrice = 0;
    }

    public Order(UUID customer, String orderDate, double orderPrice){
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

    public String getOrderDate(){
        return orderDate;
    }

    public void setOrderDate(String newOrderDate)
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
