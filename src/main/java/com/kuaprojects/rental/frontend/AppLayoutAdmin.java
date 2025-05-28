package com.kuaprojects.rental.frontend;

import com.kuaprojects.rental.security.SecurityService;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.beans.factory.annotation.Autowired;

public class AppLayoutAdmin extends AppLayout {

    public AppLayoutAdmin(@Autowired SecurityService securityService) {
        DrawerToggle toggle = new DrawerToggle();

        H1 title = new H1("Žymeklių informacinė sistema");
        title.getStyle().set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");

        SideNav nav = getSideNav();

        Scroller scroller = new Scroller(nav);
        scroller.setClassName(LumoUtility.Padding.SMALL);

        HorizontalLayout header;
        if (securityService.getAuthenticatedUser() != null) {
            Button logout = new Button("Atsijungti", click -> securityService.logout());

            header = new HorizontalLayout(title, logout);
            header.setWidthFull();
            header.expand(title);
            header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
            header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        } else {
            header = new HorizontalLayout(title);
            header.setWidthFull();
        }

        addToDrawer(scroller);
        addToNavbar(toggle, header);
    }

    SideNav getSideNav(){
        SideNav nav = new SideNav();

        SideNavItem tagDetectionLink = new SideNavItem("Aptikti žymekliai",
                TagDetectionView.class, VaadinIcon.ABACUS.create());
        SideNavItem tagLink = new SideNavItem("Žymekliai",
                TagView.class, VaadinIcon.AUTOMATION.create());
        SideNavItem ieskauPriekabosLink = new SideNavItem("Vartotojų puslapis",
                "https://ieskaupriekabos.lt", VaadinIcon.GLOBE_WIRE.create());

        nav.addItem(tagDetectionLink, tagLink, ieskauPriekabosLink);

        return nav;
    }
}
