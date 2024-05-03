
import com.health.api.resource.AppointmentResource;
import com.health.api.resource.BillingResource;
import com.health.api.resource.DoctorResource;
import com.health.api.resource.MedicalRecordResource;
import com.health.api.resource.PatientResource;
import com.health.api.resource.PersonResource;
import com.health.api.resource.PrescriptionResource;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author rasin
 */
@ApplicationPath("/") 
public class MyApplication extends Application{
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();
        resources.add(AppointmentResource.class); 
        resources.add(BillingResource.class);  
        resources.add(DoctorResource.class); 
        resources.add(MedicalRecordResource.class);
        resources.add(PatientResource.class);
        resources.add(PersonResource.class);  
        resources.add(PrescriptionResource.class);
        return resources;
    }
}
