package com.example.webinformationsystem.controller;

import com.example.webinformationsystem.bean.XSLCustomerBean;
import com.example.webinformationsystem.model.Customer;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.List;

@Path("/xmlCustomer")
public class XSLCustomerController {

    @EJB
    XSLCustomerBean xslBean;

    @GET
    public int createXLST() {
        return xslBean.createXSLCustomer();
    }

}
