package org.example.modernapp;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;

@Route("")


public class MainView extends VerticalLayout {

    private PersonRepository personRepository;
    private TextField firstName = new TextField("Name");
    private TextField lastName = new TextField("Surname");
    private TextField email = new TextField("Email");
    private Binder<Person> binder = new Binder<>(Person.class);
    private Grid<Person> grid = new Grid<>(Person.class);

    public MainView(PersonRepository personRepository) {
        this.personRepository = personRepository;
        grid.setColumns("firstName","lastName","email");
        add(getForm(), grid);
        refreshGrid();

    }
    private Component getForm() {
        var layout = new HorizontalLayout();
        layout.setAlignItems(Alignment.BASELINE);
        var addButton = new Button("Add");
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addButton.addClickShortcut(Key.ENTER);

        layout.add(firstName, lastName, email, addButton);

        binder.bindInstanceFields(this);

        addButton.addClickListener(click -> {
           try {
               var person = new Person();
               binder.writeBean(person);
               personRepository.save(person);
               binder.readBean(new Person());
               refreshGrid();
           }catch (ValidationException e) {
               //
           }
        });

        return layout;
    }
    private void refreshGrid() {
        grid.setItems(personRepository.findAll());
    }
}
