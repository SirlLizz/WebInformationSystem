package com.example.webinformationsystem.controller;

import com.example.webinformationsystem.model.Customer;
import com.example.webinformationsystem.reference.ReferenceSystem;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerController {
    @Inject
    ReferenceSystem department;

    @GET
    public List<Customer> selectAllItems() {
        return department.getCustomers();
    }

    @POST
    public int addCustomer(Customer item) {
        return department.addCustomer(item);
    }

    @PUT
    @Path("/{id}")
    public int changeCustomerInformation(@PathParam("id") String id, Customer customer) {
        return department.changeCustomerInformation(id, customer);
    }

    @DELETE
    @Path("/{id}")
    public int deleteItem(@PathParam("id") String id) {
        return department.removeCustomer(id);
    }
}
