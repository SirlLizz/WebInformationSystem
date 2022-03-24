package com.example.webinformationsystem.controller;

import com.example.webinformationsystem.dao.CloudscapeOrderDAO;
import com.example.webinformationsystem.model.Order;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderController {
    @Inject
    CloudscapeOrderDAO orders;

    @GET
    public List<Order> selectAllItems() {
        return orders.get();
    }

    @POST
    public int addCustomer(Order order) {
        return orders.add(order);
    }

    @PUT
    @Path("/{id}")
    public int changeCustomerInformation(@PathParam("id") String id, Order order) {
        return orders.change(id, order);
    }

    @DELETE
    @Path("/{id}")
    public int deleteItem(@PathParam("id") String id) {
        return orders.remove(id);
    }
}


