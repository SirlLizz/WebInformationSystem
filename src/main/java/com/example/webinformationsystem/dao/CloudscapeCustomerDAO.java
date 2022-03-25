package com.example.webinformationsystem.dao;

import com.example.webinformationsystem.connection.HibernateConnection;
import com.example.webinformationsystem.model.Customer;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class CloudscapeCustomerDAO implements Repository<Customer> {

    @Override
    public List<Customer> get(){
        try (Session session = HibernateConnection.getSession()) {
            Query query = session.createQuery("FROM Customer");
            return (List<Customer>) query.list();
        }catch (HibernateException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int add(Customer customer){
        try (Session session = HibernateConnection.getSession()) {
            session.beginTransaction();
            session.save(customer);
            session.getTransaction().commit();
            session.close();
            return 1;
        }catch (HibernateException e){
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int remove(String customerID){
        try (Session session = HibernateConnection.getSession()) {
            session.beginTransaction();
            Customer customer = new Customer();
            customer.setCustomerID(customerID);
            session.delete(customer);
            session.getTransaction().commit();
            session.close();
            return 1;
        }catch (HibernateException e){
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int change(String customerID, Customer newCustomer){
        try (Session session = HibernateConnection.getSession()) {
            session.beginTransaction();
            Customer customer = session.load(Customer.class, customerID);
            customer.setName(newCustomer.getName());
            customer.setPhoneNumber(newCustomer.getPhoneNumber());
            customer.setAddress(newCustomer.getAddress());
            session.update(customer);
            session.getTransaction().commit();
            session.close();
            return 1;
        }catch (HibernateException e){
            e.printStackTrace();
            return 0;
        }
    }

}
