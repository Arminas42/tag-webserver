package com.kuaprojects.rental.frontend.view;

import com.kuaprojects.rental.hardware.Hardware;
import com.kuaprojects.rental.hardware.HardwareRepository;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Route(value = "hardware", layout = AppLayoutAdmin.class)
@RolesAllowed("ROLE_ADMIN")
public class HardwareView extends VerticalLayout {
    private final HardwareRepository hardwareRepository;

    public HardwareView(HardwareRepository hardwareRepository) {
        this.hardwareRepository = hardwareRepository;
        List<Hardware> hardwareList = this.hardwareRepository.findAll();

        setSpacing(true);
        setPadding(true);

        hardwareList.forEach(hardware -> add(createHardwareCard(hardware)));
    }

    private Div createHardwareCard(Hardware hardware) {
        Div card = new Div();
        card.getStyle().set("border", "1px solid #ccc");
        card.getStyle().set("border-radius", "8px");
        card.getStyle().set("padding", "16px");
        card.getStyle().set("margin-bottom", "16px");
        card.getStyle().set("box-shadow", "0 2px 4px rgba(0, 0, 0, 0.1)");

        H4 title = new H4(hardware.getName());
        Span code = new Span("Irenginio kodas: " + hardware.getHardwareCode());
        Span location = new Span("Lokacija: " + hardware.getLocation());

        String lastChecked = hardware.getLastCheckedIn() != null
                ? hardware.getLastCheckedIn().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                : "Niekados";
        Span lastCheckedIn = new Span("Paskutinį kartą aptiktas: " + lastChecked);

        VerticalLayout content = new VerticalLayout(code, location, lastCheckedIn);
        content.setPadding(false);
        content.setSpacing(false);

        card.add(title, content);
        return card;
    }
}
