package com.kuaprojects.rental.frontend;

import com.kuaprojects.rental.tag.TagDetection;
import com.kuaprojects.rental.tag.TagDetectionRepository;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Route(value = "ui", layout = AppLayoutAdmin.class)
//https://stackoverflow.com/questions/20053462/excluding-grantedauthority-from-userdetails-in-spring-security
//https://medium.com/@ardagosti/spring-security-roles-a-comprehensive-guide-e134eafaabef
@RolesAllowed("ROLE_ADMIN")
public class MainView extends VerticalLayout {
    private final TagDetectionRepository repository;
    final Grid<TagDetection> tagDetectionGrid;
    final Grid<TagDataView> dataViewGrid;

    public MainView(TagDetectionRepository repository) {
        this.repository = repository;
        List<TagDetection> tags = repository.findAll();
        this.tagDetectionGrid = new Grid<>(TagDetection.class);
        this.dataViewGrid = new Grid<>(TagDataView.class);

        tagDetectionGrid.setHeight("300px");
        tagDetectionGrid.setColumns("id", "tagCode", "timeOfDetection");
        tagDetectionGrid.getColumnByKey("id").setWidth("200px").setFlexGrow(0);
        tagDetectionGrid.setItems(tags);

        dataViewGrid.setHeight("300px");
        dataViewGrid.setItems(getTagsData(tags));

        var tagDetectionDiv = new Div( new H3("Unikalūs žymekliai"),
                dataViewGrid);
        tagDetectionDiv.getStyle().set("flex", "1 1 50%");
        tagDetectionDiv.getStyle().setPadding("5px");
        tagDetectionDiv.getStyle().setMinWidth("300px");
        tagDetectionDiv.getStyle().setBoxSizing(Style.BoxSizing.BORDER_BOX);

        var dataViewDiv = new Div( new H3("Nuskaityti žymekliai"),
                tagDetectionGrid);
        dataViewDiv.getStyle().set("flex", "1 1 50%");
        dataViewDiv.getStyle().setPadding("5px");
        dataViewDiv.getStyle().setMinWidth("300px");
        dataViewDiv.getStyle().setBoxSizing(Style.BoxSizing.BORDER_BOX);

        var content = new Div();
        content.setWidth("100%");
        content.getStyle().setDisplay(Style.Display.FLEX);
        content.getStyle().setFlexWrap(Style.FlexWrap.WRAP);
        content.add(tagDetectionDiv, dataViewDiv);

        add(
                new H1("Administravimas"),
                content
        );

    }

    public List<TagDataView> getTagsData(List<TagDetection> tags){
        var uniqueTagCodes = new HashSet<String>(tags.stream().map(TagDetection::getTagCode).toList());
        List<TagDataView> listOfTags = new ArrayList<>();
        for (String tag: uniqueTagCodes) {
            listOfTags.add(new TagDataView(tag,tags.stream().filter(x -> x.getTagCode().equals(tag)).count()));

        }
        return listOfTags;
    }
    @Data
    @AllArgsConstructor
    public class TagDataView {
        String tagCode;
        long detectionCount;
    }
}
