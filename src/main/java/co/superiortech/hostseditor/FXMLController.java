package co.superiortech.hostseditor;

import co.superiortech.hostseditor.model.HostsRecord;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class FXMLController implements Initializable {

    @FXML
    private TableColumn<HostsRecord, String> colAddresses, colNames;
    @FXML
    private TableView<HostsRecord> tblRecords;
    @FXML
    private CheckBox chkBackup;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //context menu initialization
        ContextMenu cmTableMenu = new ContextMenu();
        MenuItem addRowItem = new MenuItem();
        MenuItem deleteRowItem = new MenuItem();
        addRowItem.setText("Add Row");
        addRowItem.setOnAction((ActionEvent c) -> {
            FXMLController.this.addRow();
        });
        deleteRowItem.setText("Delete Row(s)");
        deleteRowItem.setOnAction((ActionEvent c) -> {
            FXMLController.this.deleteRow();
        });

        cmTableMenu.getItems().addAll(addRowItem, deleteRowItem);

        //columns width and value factories
        this.colAddresses.prefWidthProperty().bind(this.tblRecords.widthProperty().divide(2));
        this.colNames.prefWidthProperty().bind(this.tblRecords.widthProperty().divide(2));
        this.colAddresses.setCellValueFactory(c -> {
            return new SimpleStringProperty(c.getValue().getIpAddress());
        });
        this.colNames.setCellValueFactory(c -> {
            return new SimpleStringProperty(c.getValue().getHostname());
        });

        this.colAddresses.setCellFactory(TextFieldTableCell.forTableColumn());
        this.colNames.setCellFactory(TextFieldTableCell.forTableColumn());

        //table Events and data population
        this.tblRecords.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent evt) -> {
            if (evt.getButton() == MouseButton.SECONDARY) {
                cmTableMenu.show(tblRecords, evt.getScreenX(), evt.getScreenY());
            }
        });

        try {
            this.tblRecords.setItems(FXCollections.observableList(MainApp.hosts.getHostRecords()));
        } catch (IOException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void btnSaveAction(ActionEvent evt) {
        Alert alert = new Alert(AlertType.NONE);
        alert.setHeaderText(null);
        try {
            MainApp.hosts.saveHostRecords(this.tblRecords.getItems(), this.chkBackup.isSelected());

            alert.setAlertType(AlertType.INFORMATION);
            alert.setTitle("Success!");
            alert.setContentText("Your changes have been saved successfully!");

        } catch (IOException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);

            alert.setAlertType(AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setContentText("An Error Occurred while saving your file:\n" + ex.getMessage());
        }

        alert.show();
    }

    private void addRow() {
        this.tblRecords.getItems().add(new HostsRecord("127.0.0.1","localhost"));        
    }

    private void deleteRow() {
        this.tblRecords.getItems().remove(this.tblRecords.getSelectionModel().getSelectedIndex());
    }
}
