package com.example.webinformationsystem.controller;

import com.example.webinformationsystem.model.Customer;
import com.example.webinformationsystem.dao.CloudscapeCustomerDAO;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerController {
    @Inject
    CloudscapeCustomerDAO customers;

    @GET
    public List<Customer> selectAllItems() {
        return customers.get();
    }

    @POST
    public int addCustomer(Customer customer) {
        return customers.add(customer);
    }

    @PUT
    @Path("/{id}")
    public int changeCustomerInformation(@PathParam("id") String id, Customer customer) {
        return customers.change(id, customer);
    }

    @DELETE
    @Path("/{id}")
    public int deleteItem(@PathParam("id") String id) {
        return customers.remove(id);
    }
}
