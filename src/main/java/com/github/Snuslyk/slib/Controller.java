package com.github.Snuslyk.slib;

import com.github.Snuslyk.slib.electives.ManageableElectives;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private HBox rootContainer;

    @FXML
    private AnchorPane popUp;

    @FXML
    private ToggleButton comboBox;

    @FXML
    private VBox objectContainer;

    @FXML
    private VBox sectionsContainer;

    private Section selectedSection;

    private final ToggleGroup sectionToggleGroup = new ToggleGroup();
    private final ToggleGroup objectToggleGroup = new ToggleGroup();

    private List<List<ManageableElectives>> externalObjects;
    private List<ManageableElectives> externalSections;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupSections();  // СЕКЦИИ
        setupObjects(0);  // ОБЪЕКТЫ
        update();
    }

    // ПЕРЕДАТЬ СПИСОК ОБЪЕКТОВ И СЕКЦИЙ
    // Эти методы используются для объявления кастомных объектов и секций
    public void setSectionList(List<ManageableElectives> sections) {
        this.externalSections = sections;
    }

    public void setObjectsList(List<List<ManageableElectives>> objects) {
        this.externalObjects = objects;
    }


    // УСТАНОВКА СПИСОКОВ В ПРОГРАММУ
    private void setupSections() {
        boolean isFirst = true;
        if (externalSections != null) {
            for (ManageableElectives section : externalSections) {
                comboBox.setText(isFirst ? section.getDisplayName() : comboBox.getText()); // Текст combo box по умолчанию
                addSectionButton(section.getDisplayName(), isFirst);
                isFirst = false;
            }
        }
    }

    private void setupObjects(int sectionIndex) {
        objectContainer.getChildren().clear();
        objectToggleGroup.getToggles().clear();

        if (externalObjects != null && sectionIndex < externalObjects.size()) {
            List<ManageableElectives> sectionObjects = externalObjects.get(sectionIndex);
            boolean isFirst = true;
            for (ManageableElectives object : sectionObjects) {
                addObjectButton(object.getDisplayName(), isFirst);
                isFirst = false;
            }
        }
    }


    // МЕТОДЫ СОЗДАНИЯ КНОПОК СЕКЦИЙ И ОБЪЕКТОВ
    private void addSectionButton(String text, boolean isSelected) {
        RadioButton sectionButton = createRadioButton(sectionToggleGroup, text, isSelected);
        sectionButton.setOnAction(this::handleSectionSelection);
        sectionsContainer.getChildren().add(sectionButton);
    }

    private void addObjectButton(String text, boolean isSelected) {
        RadioButton objectButton = createRadioButton(objectToggleGroup, text, isSelected);
        objectContainer.getChildren().add(objectButton);
    }

    // Устанавливает для combo-box название выбраной секции
    private void handleSectionSelection(ActionEvent event) {
        RadioButton selectedButton = (RadioButton) sectionToggleGroup.getSelectedToggle();

        if (selectedButton == null) return;

        comboBox.setText(selectedButton.getText());
        int sectionIndex = sectionsContainer.getChildren().indexOf(selectedButton);
        setupObjects(sectionIndex);

    }


    // ОБЩАЯ ФОРМА КНОПОК
    private RadioButton createRadioButton(ToggleGroup group, String text, boolean isSelected) {
        RadioButton button = new RadioButton(text);
        button.setCursor(Cursor.HAND);
        button.getStyleClass().add(group == objectToggleGroup ? "object-button" : "sections-button");
        button.setToggleGroup(group);
        button.setPrefSize(320, 40);

        if (isSelected) {
            button.setSelected(true);
        }
        return button;
    }

    private void update() {
        rootContainer.setOnMouseClicked(event -> {
            if (!comboBox.isHover() && !popUp.isHover()) {
                comboBox.setSelected(false);
            }
        });

        popUp.visibleProperty().bind(comboBox.selectedProperty());
    }

    public Section getSelectedSection() {
        return selectedSection;
    }
}
