package com.kuaprojects.rental.frontend.utils;

import com.kuaprojects.rental.frontend.model.TagDataViewModel;
import com.kuaprojects.rental.tag.TagDetection;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.dom.Style;

import java.util.List;

public class TagDetectionComponents {


    public static Div createUniqueTagDiv(Grid<TagDataViewModel> dataViewGrid) {
        dataViewGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        dataViewGrid.setHeight("300px");
        var div = new Div(new H3("Unikalūs žymeklių kodai ir nuskaitymų skaičius"),
                dataViewGrid);
        div.getStyle().set("flex", "1 1 70%");
        div.getStyle().setPadding("5px");
        div.getStyle().setMinWidth("300px");
        div.getStyle().setBoxSizing(Style.BoxSizing.BORDER_BOX);
        return div;
    }
}