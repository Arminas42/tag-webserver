package com.kuaprojects.rental.frontend;

import com.kuaprojects.rental.tag.TagDetection;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.dom.Style;

import java.util.List;

public class TagDetectionComponents {

    private final List<TagDetection> tagDetections;

    public TagDetectionComponents(List<TagDetection> tagDetections) {
        this.tagDetections = tagDetections;
    }

    public Div getTagDetectionDiv(){
        var grid = new Grid<>(TagDetection.class);
        grid.setHeight("300px");
        grid.setColumns("id", "tagCode", "detectionTime");
        grid.getColumnByKey("id").setWidth("200px").setFlexGrow(0);
        grid.setItems(tagDetections);

        var tagDetectionDiv = new Div( new H3("Nuskaityti žymekliai"),
                grid);
        tagDetectionDiv.getStyle().set("flex", "1 1 50%");
        tagDetectionDiv.getStyle().setPadding("5px");
        tagDetectionDiv.getStyle().setMinWidth("300px");
        tagDetectionDiv.getStyle().setBoxSizing(Style.BoxSizing.BORDER_BOX);
        return tagDetectionDiv;
    }
}
