package com.health.api.dao;

import com.health.api.model.Billing;
import java.util.List;


public interface BillingDAO {
    public Billing getBillingById(int id);

    public List<Billing> getBillingByAppointmentId(int appointmentId);

    public Billing createBilling(Billing billing);

    public Billing updateBilling(Billing billing);

    public void deleteBilling(int id);
}
