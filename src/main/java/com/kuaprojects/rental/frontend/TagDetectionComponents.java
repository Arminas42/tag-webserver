package com.kuaprojects.rental.frontend;

import com.kuaprojects.rental.tag.TagDetection;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.dom.Style;

import java.util.List;

public class TagDetectionComponents {

    //TODO: Make static method
    public static Div createTagDetectionDiv(List<TagDetection> tagDetections) {
        var grid = new Grid<>(TagDetection.class);
        grid.setHeight("300px");
        grid.setColumns("id", "tagCode", "detectionTime");
        grid.getColumnByKey("id").setWidth("200px").setFlexGrow(0);
        grid.setItems(tagDetections);

        var tagDetectionDiv = new Div(new H3("Nuskaityti žymekliai"),
                grid);
        tagDetectionDiv.getStyle().set("flex", "1 1 50%");
        tagDetectionDiv.getStyle().setPadding("5px");
        tagDetectionDiv.getStyle().setMinWidth("300px");
        tagDetectionDiv.getStyle().setBoxSizing(Style.BoxSizing.BORDER_BOX);
        return tagDetectionDiv;
    }

    public static Div createUniqueTagDiv(Grid<TagDataView> dataViewGrid) {
        dataViewGrid.setSelectionMode(Grid.SelectionMode.SINGLE);

        dataViewGrid.setHeight("300px");
//        dataViewGrid.setItems(getTagsData(tags));

        var div = new Div(new H3("Unikalūs žymekliai"),
                dataViewGrid);
        div.getStyle().set("flex", "1 1 50%");
        div.getStyle().setPadding("5px");
        div.getStyle().setMinWidth("300px");
        div.getStyle().setBoxSizing(Style.BoxSizing.BORDER_BOX);
        return div;
    }
}