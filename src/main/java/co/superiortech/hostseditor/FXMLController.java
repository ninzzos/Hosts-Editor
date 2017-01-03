package co.superiortech.hostseditor;

import co.superiortech.hostseditor.model.HostsRecord;
import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;

public class FXMLController implements Initializable {
    
    @FXML private TableColumn<HostsRecord, String> colAddresses, colNames;
    @FXML private TableView<HostsRecord> tblRecords;        
    @FXML private CheckBox chkBackup;
        
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
                
        try {
            this.tblRecords.setItems(FXCollections.observableList(MainApp.hosts.getHostRecords()));
        } catch (IOException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void btnSaveAction(ActionEvent evt){
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
            alert.setContentText("An Error Occurred while saving your file:\n"+ex.getMessage());
        }   
        
        alert.show();
    }
   
}
