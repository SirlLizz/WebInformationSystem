package com.example.webinformationsystem.controller;

import com.example.webinformationsystem.bean.XSLOrderBean;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.List;

@Path("/xmlOrder")
public class XSLOrderController {

    @EJB
    XSLOrderBean xslBean;

    @GET
    public int createXLST() {
        return xslBean.createXSLOrder();
    }

}