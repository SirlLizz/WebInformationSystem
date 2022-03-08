package com.example.webinformationsystem.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.UUID;

@XmlType(name = "customer")
@XmlRootElement
public class Customer implements Serializable {

    @XmlAttribute(name = "ID")
    private UUID customerID;
    private String name;
    private String phoneNumber;
    private String address;

    public Customer(){
        this.customerID = UUID.randomUUID();
        this.address = null;
        this.name = null;
        this.phoneNumber = null;
    }

    public Customer(String name, String phoneNumber, String address){
        customerID = UUID.randomUUID();
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

    public void setCustomerID(UUID customerID) {
        this.customerID = customerID;
    }

    public String getCustomerID(){
        return customerID.toString();
    }

    public String toString(){
        return "ID: " + customerID + "; Name: " + name + "; Phone: " + phoneNumber + "; Address: " + address;
    }
}