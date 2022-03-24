package com.example.webinformationsystem.controller;

import com.example.webinformationsystem.bean.XSLCustomerBean;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/xmlCustomer")
public class XSLCustomerController {

    @EJB
    XSLCustomerBean xslBean;

    @GET
    public int createXLST() {
        return xslBean.createXSLCustomer();
    }

    @POST
    @Path("/{index}")
    public int addXMLData(@PathParam("index") int index, String file) {
        switch (index){
            case 0:
                return xslBean.replaceXMLDataCustomer(file);
            case 1:
                return xslBean.addXMLDataCustomer(file);
        }
        return 0;
    }

}
