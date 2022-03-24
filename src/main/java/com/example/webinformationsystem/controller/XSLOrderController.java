package com.example.webinformationsystem.controller;

import com.example.webinformationsystem.bean.XSLOrderBean;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/xmlOrder")
public class XSLOrderController {

    @EJB
    XSLOrderBean xslBean;

    @GET
    public int createXLST() {
        return xslBean.createXSLOrder();
    }

    @POST
    @Path("/{index}")
    public int addXMLData(@PathParam("index") int index, String file) {
        switch (index){
            case 0:
                return xslBean.replaceXMLDataOrder(file);
            case 1:
                return xslBean.addXMLDataOrder(file);
        }
        return 0;
    }

}