package com.kuaprojects.rental.frontend;

import com.kuaprojects.rental.tag.Tag;
import com.kuaprojects.rental.tag.TagDetection;
import com.kuaprojects.rental.tag.TagDetectionRepository;
import com.kuaprojects.rental.tag.TagRepository;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.kuaprojects.rental.frontend.TagDetectionComponents.createTagDetectionDiv;
import static com.kuaprojects.rental.frontend.TagDetectionComponents.createUniqueTagDiv;
import static com.kuaprojects.rental.frontend.Utils.contentStyling;

@Route(value = "ui", layout = AppLayoutAdmin.class)
//https://stackoverflow.com/questions/20053462/excluding-grantedauthority-from-userdetails-in-spring-security
//https://medium.com/@ardagosti/spring-security-roles-a-comprehensive-guide-e134eafaabef
@RolesAllowed("ROLE_ADMIN")
public class TagDetectionView extends VerticalLayout {
    private final TagDetectionRepository repository;
    private final TagRepository tagRepository;
    final Grid<TagDataView> dataViewGrid;
    private final TagInfoPanel tagInfoPanel = new TagInfoPanel();

    public TagDetectionView(TagDetectionRepository repository, TagRepository tagRepository) {
        this.repository = repository;
        this.tagRepository = tagRepository;
        List<TagDetection> tags = this.repository.findAll();
        this.dataViewGrid = new Grid<>(TagDataView.class);

        dataViewGrid.setItems(getTagsData(tags));

        dataViewGrid.addSelectionListener(event -> {
            event.getFirstSelectedItem().ifPresentOrElse(selected -> {
                Notification.show("Selected: " + selected.getTagCode() + ", Count: " + selected.getDetectionCount());
                updateTagInfoPanel(selected);
            }, () -> updateTagInfoPanel(null));
        });

        var interactivePanel = new Div();
        contentStyling(interactivePanel);
        interactivePanel.add(createUniqueTagDiv(dataViewGrid), tagInfoPanel);

        var historicDataPanel = new Div();
        contentStyling(historicDataPanel);
        historicDataPanel.add(createTagDetectionDiv(tags));

        add(
                new H1("Administravimas"),
                interactivePanel,
                historicDataPanel
        );

    }


    public List<TagDataView> getTagsData(List<TagDetection> tags) {
        var uniqueTagCodes = new HashSet<String>(tags.stream().map(TagDetection::getTagCode).toList());
        List<TagDataView> listOfTags = new ArrayList<>();
        for (String tag : uniqueTagCodes) {
            listOfTags.add(new TagDataView(tag, tags.stream().filter(x -> x.getTagCode().equals(tag)).count()));

        }
        return listOfTags;
    }


    private void updateTagInfoPanel(TagDataView selected) {
        tagInfoPanel.removeAll();

        tagInfoPanel.add(new H3("Pasirinktas žymeklis"));

        if (selected == null) {
            tagInfoPanel.add(new Div(new Text("Žymeklis nepasirinktas")));
        } else {
            var tag = tagRepository.findByTagCode(selected.getTagCode());
            var createTagButton = new Button("Sukurti tag'ą", event -> createTagEntityEvent(selected));
            if (tag.isPresent()) createTagButton.setEnabled(false);
            HorizontalLayout buttons = new HorizontalLayout(
                    createTagButton
            );
            buttons.setSpacing(true);
            tagInfoPanel.add(
                    new Div(new Text("Tag kodas: " + selected.getTagCode())),
                    new Div(new Text("Nuskaitymų skaičius: " + selected.getDetectionCount())),
                    new Div(buttons)
            );
        }
    }

    private void createTagEntityEvent(TagDataView selectedTagDetection) {
        var tagCode = selectedTagDetection.getTagCode();
        if (tagRepository.findByTagCode(tagCode).isPresent()) {
            Notification.show("Tag is already created: " + tagCode);
            return;
        }
        var savedEntity = tagRepository.save(Tag.builder()
                .tagCode(tagCode)
                .trailer(null)
                .build());

        Notification.show("Created tag successfully: " + savedEntity.getTagCode());

    }

}
