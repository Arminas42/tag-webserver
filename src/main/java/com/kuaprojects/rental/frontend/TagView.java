package com.kuaprojects.rental.frontend;

import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.builder.ChartBuilder;
import com.github.appreciated.apexcharts.config.builder.DataLabelsBuilder;
import com.github.appreciated.apexcharts.config.builder.PlotOptionsBuilder;
import com.github.appreciated.apexcharts.config.builder.XAxisBuilder;
import com.github.appreciated.apexcharts.config.builder.YAxisBuilder;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.plotoptions.builder.BarBuilder;
import com.github.appreciated.apexcharts.config.xaxis.XAxisType;
import com.github.appreciated.apexcharts.helper.ColorDateTimeCoordinate;
import com.github.appreciated.apexcharts.helper.Series;
import com.kuaprojects.rental.tag.Tag;
import com.kuaprojects.rental.tag.TagDetectionFacade;
import com.kuaprojects.rental.tag.TagDetectionRepository;
import com.kuaprojects.rental.tag.TagRepository;
import com.kuaprojects.rental.tag.TagVacantRepository;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.kuaprojects.rental.frontend.Utils.contentStyling;
import static com.kuaprojects.rental.frontend.Utils.generateRandomHexColor;
import static com.kuaprojects.rental.frontend.Utils.getRange;

@Route(value = "tags", layout = AppLayoutAdmin.class)
@RolesAllowed("ROLE_ADMIN")
public class TagView extends VerticalLayout {
    private final TagDetectionRepository repository;
    private final TagRepository tagRepository;
    private final TagVacantRepository tagVacantRepository;
    private final TagDetectionFacade tagDetectionFacade;
    private final Grid<Tag> tagGrid;
    private final Grid<TagVacantView> vacancyGrid;

    private int currentPage = 0;
    private final int pageSize = 5;

    private final Button previousButton = new Button("Previous");
    private final Button nextButton = new Button("Next");
    private final Button processVacancy = new Button("Process vacancy");
    private final Button displayResults = new Button("Display results");
    private Utils.DateTimeRange selectedRange;
    private Tag selectedTag;
    private Set<Tag> multipleSelectionTags = new HashSet<>();
    Div chartDiv = createChartDiv();

    public TagView(TagDetectionRepository repository, TagRepository tagRepository, TagVacantRepository tagVacantRepository, TagDetectionFacade tagDetectionFacade) {
        this.repository = repository;
        this.tagRepository = tagRepository;
        this.tagVacantRepository = tagVacantRepository;
        this.tagDetectionFacade = tagDetectionFacade;
        this.tagGrid = new Grid<>(Tag.class);
        this.vacancyGrid = new Grid<>(TagVacantView.class);
        //styling
        tagGrid.setHeight("200px");
        vacancyGrid.setHeight("300px");
        loadPage(currentPage);

        TextField searchField = new TextField();
        searchField.setPlaceholder("Search by Tag Code");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setClearButtonVisible(true);

        ComboBox<String> dateRangeComboBox = new ComboBox<>("Select Date Range");
        dateRangeComboBox.setItems("Yesterday", "Day Before Yesterday", "This week", "Last Week");

        Text selectionTagDivText = new Text("");
        Text selectionDateDivText = new Text("");
        Div selectionDiv = new Div();
        Div selectedTagDiv = new Div(selectionTagDivText);
        Div selectedDateDiv = new Div(selectionDateDivText);
        selectionDiv.add(selectedTagDiv, selectedDateDiv);
        tagGrid.setSelectionMode(Grid.SelectionMode.MULTI);

        //listeners
        prevAndNextBtnSetup();

        processVacancyListenerSetup();

        displayResultsListenerSetup();

        dateRangeComboBox.addValueChangeListener(event -> selectionDateDivText.setText("Selected: " + getRange(event.getValue())));

        searchFieldSetup(searchField);

        tagGridSelectionListenerSetup(selectionTagDivText);

        //layout
        var buttonLayout = createButtonLayout(previousButton, nextButton, searchField);
        var dateSelectionLayout = createButtonLayout(dateRangeComboBox, selectionDiv);
        var tagDiv = new Div(
                new H3("Žymekliai"),
                buttonLayout,
                tagGrid,
                dateSelectionLayout
        );
        tagDivStyling(tagDiv);

        var vacancyDiv = new Div(
                new H3("Paliko vietą"),
                createButtonLayout(displayResults, processVacancy),
                vacancyGrid
        );
        tagDivStyling(vacancyDiv);
        var content = new Div();
        contentStyling(content);
        content.add(tagDiv, vacancyDiv);

        add(content , chartDiv);
    }

    private void tagGridSelectionListenerSetup(Text selectionTagDivText) {
        tagGrid.addSelectionListener(event -> {
            var items = event.getAllSelectedItems();
            multipleSelectionTags.clear();
            if (items.size() > 1) {
                Notification.show("Selected multiple tags");
                selectionTagDivText.setText("Multiple selected");
                multipleSelectionTags.addAll(items);
            } else if (items.size() == 1) {
                multipleSelectionTags.addAll(items);
                event.getFirstSelectedItem().ifPresentOrElse(selected -> {
                    Notification.show("Selected: " + selected.getTagCode());
                    selectionTagDivText.setText("Tag kodas: " + selected.getTagCode());
                    setSelectedTag(selected);
                }, () -> setSelectedTag(null));
            }
        });
    }

    private void searchFieldSetup(TextField searchField) {
        searchField.addValueChangeListener(e -> {
            String searchTerm = e.getValue();
            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                var results = tagRepository.findByTagCodeContainingIgnoreCase(searchTerm.trim());
                tagGrid.setItems(results);
                previousButton.setEnabled(false);
                nextButton.setEnabled(false);
            } else {
                loadPage(currentPage);
                previousButton.setEnabled(currentPage > 0);
                nextButton.setEnabled(tagRepository.findAll(PageRequest.of(currentPage, pageSize)).hasNext());
            }
        });
    }

    private void displayResultsListenerSetup() {
        displayResults.addClickListener(event -> {
            if (ObjectUtils.isNotEmpty(selectedTag) && ObjectUtils.isNotEmpty(selectedRange)) {
                var items =
                        tagVacantRepository
                                .findByTagCodeAndVacantFromBetween(selectedTag.getTagCode(), selectedRange.getFrom(), selectedRange.getTo());
                var mappedItems = items
                        .stream()
                        .map(tagVacant -> {
                                    var viewItem = TagVacantView.builder()
                                            .tagCode(tagVacant.getTagCode())
                                            .status(tagVacant.getStatus());
                                    if (ObjectUtils.isEmpty(tagVacant.getVacantTo())) {
                                        return viewItem.elapsedMinutes(null).build();
                                    }

                                    return viewItem.elapsedMinutes(Duration.between(tagVacant.getVacantFrom(), tagVacant.getVacantTo()).toMinutes()).build();
                                }
                        ).toList();
                vacancyGrid.setItems(mappedItems);
            }

            if (multipleSelectionTags.size() > 1) {
                displayChart(multipleSelectionTags);
            }
        });
    }

    private void processVacancyListenerSetup() {
        processVacancy.addClickListener(event -> {
            if (ObjectUtils.isNotEmpty(multipleSelectionTags) && ObjectUtils.isNotEmpty(selectedRange)) {
                Notification.show("Using processVacancy not intended");
                tagDetectionFacade.processDetections(selectedRange.getFrom(), selectedRange.getTo(), selectedRange.getTo().withHour(20));
                Notification.show("Processed");
            }
        });
    }

    private void prevAndNextBtnSetup() {
        previousButton.addClickListener(e -> {
            if (currentPage > 0) {
                currentPage--;
                loadPage(currentPage);
            }
        });

        nextButton.addClickListener(e -> {
            currentPage++;
            loadPage(currentPage);
        });
    }

    private void setSelectedTag(Tag selected) {
        this.selectedTag = selected;
    }

    private HorizontalLayout createButtonLayout(Component... children) {
        var layout = new HorizontalLayout(children);
        layout.getStyle()
                .set("border", "1px solid #ccc")
                .set("background-color", "#f9f9f9")
                .set("padding", "10px")
                .set("border-radius", "5px")
                .set("gap", "10px");
        return layout;
    }

    private void loadPage(int pageIndex) {
        Page<Tag> page = tagRepository.findAll(PageRequest.of(pageIndex, pageSize));
        tagGrid.setItems(page.getContent());

        previousButton.setEnabled(pageIndex > 0);
        nextButton.setEnabled(page.hasNext());
    }

    private void tagDivStyling(Div uniqueTagDiv) {
        uniqueTagDiv.getStyle().set("flex", "1 1 50%");
        uniqueTagDiv.getStyle().setPadding("5px");
        uniqueTagDiv.getStyle().setMinWidth("300px");
        uniqueTagDiv.getStyle().setBoxSizing(Style.BoxSizing.BORDER_BOX);
    }

    // Optional getter if needed elsewhere
    public Utils.DateTimeRange getSelectedRange() {
        return selectedRange;
    }


    public class TimeLineChart extends ApexChartsBuilder {
        public TimeLineChart(Series<ColorDateTimeCoordinate>... coordinateSeries) {
            withChart(ChartBuilder.get()
                    .withType(Type.RANGEBAR)
                    .build())
                    .withPlotOptions(PlotOptionsBuilder.get()
                            .withBar(BarBuilder.get()
                                    .withHorizontal(true)
                                    .withDistributed(true)
                                    .build())
                            .build())
                    .withDataLabels(DataLabelsBuilder.get()
                            .withEnabled(false)
                            .build())
                    .withSeries(coordinateSeries)
                    .withYaxis(YAxisBuilder.get()
                            .withMin(selectedRange.getFrom().toLocalDate())
                            .withMax(selectedRange.getTo().toLocalDate().plusDays(1))
                            .build())
                    .withXaxis(XAxisBuilder.get()
                            .withType(XAxisType.DATETIME)
                            .build());
        }
    }

    public void displayChart(Set<Tag> selection) {
        var tagCodes = selection.stream().map(tag -> tag.getTagCode()).toList();
        var vacancies = tagVacantRepository.findByTagCodeInAndVacantFromBetween(tagCodes, selectedRange.getFrom(), selectedRange.getTo());
        List<Series<ColorDateTimeCoordinate>> seriesList = new ArrayList<>();
        for (String code : tagCodes) {
            var color = generateRandomHexColor();
            var coords = vacancies.stream()
                    .filter(x -> StringUtils.equals(x.getTagCode(), code))
                    .filter(x -> ObjectUtils.isNotEmpty(x.getVacantTo()))
                    .map(item -> new ColorDateTimeCoordinate<>(color, "vacant", item.getVacantFrom(), item.getVacantTo())).toArray(ColorDateTimeCoordinate[]::new);

            var series = new Series<>(code, coords);
            seriesList.add(series);
        }
        var chart = new TimeLineChart(seriesList.toArray(new Series[0])).build();
        chartDiv.removeAll();
        chartDiv.add(chart);
    }

    private Div createChartDiv(){
        var div = new Div();
        div.setWidth("2.5em");
        div.setHeight("2.5em");
        return div;
    }
}
