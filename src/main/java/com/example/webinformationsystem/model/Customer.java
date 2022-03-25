package com.example.webinformationsystem.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;


@Entity
@Table(name = "CUSTOMERS", schema = "SYS")
public class Customer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private String customerID;

    @Column(name = "NAME")
    private String name;

    @Column(name = "TELEPHONE")
    private String phoneNumber;

    @Column(name = "ADDRESS")
    private String address;

    public Customer(){
        this.customerID = UUID.randomUUID().toString();
        this.address = null;
        this.name = null;
        this.phoneNumber = null;
    }

    public Customer(String name, String phoneNumber, String address){
        customerID = UUID.randomUUID().toString();
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public String getName(){
        return name;
    }

    public void setName(String newName){
        name = newName;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }

    public void setPhoneNumber(String newPhoneNumber){
        phoneNumber = newPhoneNumber;
    }

    public String getAddress(){
        return address;
    }

    public void setAddress(String newAddress){
        address = newAddress;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getCustomerID(){
        return customerID;
    }

    public String toString(){
        return "ID: " + customerID + "; Name: " + name + "; Phone: " + phoneNumber + "; Address: " + address;
    }
}
