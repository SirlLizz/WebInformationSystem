package com.example.webinformationsystem.dao;

import com.example.webinformationsystem.connection.HibernateConnection;
import com.example.webinformationsystem.model.Order;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class CloudscapeOrderDAO implements Repository<Order> {

    @Override
    public List<Order> get(){
        try (Session session = HibernateConnection.getSession()) {
            Query query = session.createQuery("FROM Order");
            return (List<Order>) query.list();
        }catch (HibernateException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int add(Order order) {
        try (Session session = HibernateConnection.getSession()) {
            session.beginTransaction();
            session.save(order);
            session.getTransaction().commit();
            session.close();
            return 1;
        }catch (HibernateException e){
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int remove(String orderID) {
        try (Session session = HibernateConnection.getSession()) {
            session.beginTransaction();
            Order order = new Order();
            order.setOrderID(orderID);
            session.delete(order);
            session.getTransaction().commit();
            session.close();
            return 1;
        }catch (HibernateException e){
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int change(String orderID, Order newOrder) {
        try (Session session = HibernateConnection.getSession()) {
            session.beginTransaction();
            Order order = session.load(Order.class, orderID);
            order.setCustomer(newOrder.getCustomer());
            order.setOrderDate(newOrder.getOrderDate());
            order.setOrderPrice(newOrder.getOrderPrice());
            session.update(order);
            session.getTransaction().commit();
            session.close();
            return 1;
        }catch (HibernateException e){
            e.printStackTrace();
            return 0;
        }
    }
}
