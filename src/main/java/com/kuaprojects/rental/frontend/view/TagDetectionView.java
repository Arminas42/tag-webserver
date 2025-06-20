package com.kuaprojects.rental.frontend.view;

import com.kuaprojects.rental.frontend.utils.PagingObj;
import com.kuaprojects.rental.frontend.utils.TagInfoPanel;
import com.kuaprojects.rental.frontend.model.TagDataViewModel;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.kuaprojects.rental.frontend.utils.TagDetectionComponents.createUniqueTagDiv;
import static com.kuaprojects.rental.frontend.utils.Utils.contentStyling;
import static com.kuaprojects.rental.frontend.utils.Utils.createButtonLayout;

@Route(value = "", layout = AppLayoutAdmin.class)
//https://stackoverflow.com/questions/20053462/excluding-grantedauthority-from-userdetails-in-spring-security
//https://medium.com/@ardagosti/spring-security-roles-a-comprehensive-guide-e134eafaabef
@RolesAllowed("ROLE_ADMIN")
public class TagDetectionView extends VerticalLayout {
    private final TagDetectionRepository repository;
    private final TagRepository tagRepository;
    final Grid<TagDataViewModel> uniqueTagViewGrid;
    final Grid<TagDetection> recentTagViewGrid;
    private final Button previousButton = new Button("Buvęs");
    private final Button nextButton = new Button("Kitas");

    private final TagInfoPanel tagInfoPanel = new TagInfoPanel();
    private PagingObj pagingObj = new PagingObj();
    private Div tagDetectionDiv = new Div(new H3("Paskutiniai nuskaityti žymekliai"));


    public TagDetectionView(TagDetectionRepository repository, TagRepository tagRepository) {
        this.repository = repository;
        this.tagRepository = tagRepository;
        //Unique Tag View setup
        List<TagDetection> tags = this.repository.findAll();
        this.uniqueTagViewGrid = new Grid<>(TagDataViewModel.class);
        uniqueTagViewGrid.setItems(getTagsData(tags));
        //
        //Recent Tag View setup
        this.recentTagViewGrid = new Grid<>(TagDetection.class);
        var buttonLayout = createButtonLayout(previousButton, nextButton);
        tagDetectionDiv.add(buttonLayout);
        loadRecentTagViewPage(pagingObj.getCurrentPage());
        prevAndNextBtnSetup(previousButton, nextButton, pagingObj);
        setupTagDetectionDiv(recentTagViewGrid, tagDetectionDiv);
        //

        uniqueTagViewGrid.addSelectionListener(event -> {
            event.getFirstSelectedItem().ifPresentOrElse(selected -> {
                Notification.show("Selected: " + selected.getTagCode() + ", Count: " + selected.getDetectionCount());
                updateTagInfoPanel(selected);
            }, () -> updateTagInfoPanel(null));
        });

        var interactivePanel = new Div();
        contentStyling(interactivePanel);
        interactivePanel.add(createUniqueTagDiv(uniqueTagViewGrid), tagInfoPanel);

        var historicDataPanel = new Div();
        contentStyling(historicDataPanel);
        historicDataPanel.add(tagDetectionDiv);

        add(
                new H1("Administravimas"),
                interactivePanel,
                historicDataPanel
        );

    }


    public List<TagDataViewModel> getTagsData(List<TagDetection> tags) {
        var uniqueTagCodes = new HashSet<String>(tags.stream().map(TagDetection::getTagCode).toList());
        List<TagDataViewModel> listOfTags = new ArrayList<>();
        for (String tag : uniqueTagCodes) {
            listOfTags.add(new TagDataViewModel(tag, tags.stream().filter(x -> x.getTagCode().equals(tag)).count()));

        }
        return listOfTags;
    }

    private void prevAndNextBtnSetup(Button prev, Button next, PagingObj pagingObj) {
        prev.addClickListener(e -> {
            if (pagingObj.getCurrentPage() > 0) {
                pagingObj.decrementCurrentPage();
                loadRecentTagViewPage(pagingObj.getCurrentPage());
            }
        });

        next.addClickListener(e -> {
            pagingObj.incrementCurrentPage();
            loadRecentTagViewPage(pagingObj.getCurrentPage());
        });
    }

    private void loadRecentTagViewPage(int pageIndex) {
        Page<TagDetection> page = repository.findAllByOrderByDetectionTimeDesc(PageRequest.of(pageIndex, pagingObj.getPageSize()));
        recentTagViewGrid.setItems(page.getContent());

        previousButton.setEnabled(pageIndex > 0);
        nextButton.setEnabled(page.hasNext());
    }
    public static void setupTagDetectionDiv(Grid grid, Div tagDetectionDiv) {
        grid.setHeight("300px");
        grid.setColumns("id", "tagCode", "detectionTime");
        grid.getColumnByKey("id").setWidth("200px").setFlexGrow(0);
        tagDetectionDiv.add(grid);
        tagDetectionDiv.getStyle().set("flex", "1 1 50%");
        tagDetectionDiv.getStyle().setPadding("5px");
        tagDetectionDiv.getStyle().setMinWidth("300px");
        tagDetectionDiv.getStyle().setBoxSizing(Style.BoxSizing.BORDER_BOX);
    }

    private void updateTagInfoPanel(TagDataViewModel selected) {
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
                    new Div(new Text("Veiksmai: ")),
                    new Div(buttons)
            );
        }
    }

    private void createTagEntityEvent(TagDataViewModel selectedTagDetection) {
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
