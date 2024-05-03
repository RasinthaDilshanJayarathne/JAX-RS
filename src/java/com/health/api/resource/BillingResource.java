package com.health.api.resource;

import com.health.api.dao.BillingDAO;
import com.health.api.daoImpl.BillingDAOImpl;
import com.health.api.model.Billing;
import com.health.api.model.ResponseBean;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/billing")
public class BillingResource {

    private BillingDAO billingDAO = new BillingDAOImpl();
    ResponseBean responseBean = new ResponseBean();

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Billing getBillingById(@PathParam("id") int id) {
        Billing billing = new Billing();
        try {
            billing = billingDAO.getBillingById(id);
            if (billing == null) {
                responseBean.setResponseCode("404");
                responseBean.setResponseMsg("Doctor not found");
                return billing;
            }
            responseBean.setContent(billing);
            responseBean.setResponseCode("200");
            responseBean.setResponseMsg("Billing Retrieved Successfully");
            return billing;
        } catch (Exception e) {
            responseBean.setResponseCode("400");
            responseBean.setResponseMsg("Failed to retrieve billing: " + e.getMessage());
            return billing;
        }
    }

    @POST
    @Path("/post")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Billing createBilling(Billing billing) {
        Billing createBilling = new Billing();
        try {
            if (billing.getAppointment().getId() == 00) {
                Logger.getLogger("First name is required");
            }

            if (billingDAO == null) {
                Logger.getLogger("Billing DAO is not properly initialized");
            }

            createBilling = billingDAO.createBilling(billing);
            responseBean.setContent(createBilling);
            responseBean.setResponseCode("200");
            responseBean.setResponseMsg("Billing Successfully Created");
            return createBilling;
        } catch (Exception e) {
            responseBean.setContent(null);
            responseBean.setResponseCode("400");
            responseBean.setResponseMsg("Billing Unsuccessfully Created" + e.getMessage());
            return createBilling;
        }
    }

}
