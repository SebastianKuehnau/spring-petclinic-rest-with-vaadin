package org.springframework.samples.petclinic.ui;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.ClinicService;

@Route("")
@RouteAlias("vets")
public class VetView extends VerticalLayout {
    public VetView(ClinicService clinicService) {
        var grid = new Grid<>(Vet.class);
        grid.setItems(clinicService.findVets());
        grid.setColumns("firstName", "lastName", "specialties");
        grid.setSizeFull();
        add(grid);

        setSizeFull();
    }
}
