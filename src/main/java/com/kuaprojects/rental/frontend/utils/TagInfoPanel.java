package com.kuaprojects.rental.frontend.utils;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class TagInfoPanel extends VerticalLayout {
    public TagInfoPanel() {
        this.add(new H3("Pasirinktas žymeklis"));
        this.add(new Div(new Text("Žymeklis nepasirinktas")));
        this.setPadding(true);
        this.setSpacing(true);
        this.getStyle()
                .set("border", "1px solid lightgray")
                .set("flex", "1 1 30%")
                .set("padding", "1rem")
                .set("background-color", "#f9f9f9")
                .set("border", "1px solid #ccc")
                .set("border-radius", "8px")
                .set("margin-top", "1rem");
        this.setWidthFull();
    }
}
