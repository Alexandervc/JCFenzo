/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stamboom.gui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;
import javafx.collections.*;;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import stamboom.controller.StamboomController;
import stamboom.domain.*;
import stamboom.util.StringUtilities;

/**
 *
 * @author frankpeeters
 */
public class StamboomFXController extends StamboomController implements Initializable {

    //MENUs en TABs
    @FXML MenuBar menuBar;
    @FXML MenuItem miNew;
    @FXML MenuItem miOpen;
    @FXML MenuItem miSave;
    @FXML CheckMenuItem cmDatabase;
    @FXML MenuItem miClose;
    @FXML Tab tabPersoon;
    @FXML Tab tabGezin;
    @FXML Tab tabPersoonInvoer;
    @FXML Tab tabGezinInvoer;
    @FXML Tab tabStamboom;    
    
    //PERSOON JCF
    @FXML TreeView trvStamboom;
    @FXML TableView tavPersonen;
    @FXML TableColumn tcOuderKind;
    @FXML TableColumn tcVoornaam;
    @FXML TableColumn tcAchternaam;
    @FXML TableColumn tcGeboortedatum;
    @FXML TableColumn tcGeslacht;
    
    //INVOER PERSOON
    @FXML TextField tfVoornamen2;
    @FXML TextField tfTussenvoegsel2;
    @FXML TextField tfAchternaam2;
    @FXML TextField tfGeslacht2;
    @FXML TextField tfGebDatum2;
    @FXML TextField tfGebPlaats2;
    @FXML ComboBox cbOuderlijkGezin2;
    @FXML Button btAddPersoon;
    @FXML Button btCancelPersoon;

    //GEZIN
    @FXML ComboBox cbGezin;
    @FXML ComboBox cbOuder1Invoer2;
    @FXML ComboBox cbOuder2Invoer2;
    @FXML TextField tfHuwelijkInvoer2;
    @FXML TextField tfScheidingInvoer2;
    @FXML ListView lvKinderen;
    @FXML Button btSetHuwelijk;
    @FXML Button btSetScheiding;
    
    //INVOER GEZIN
    @FXML ComboBox cbOuder1Invoer;
    @FXML ComboBox cbOuder2Invoer;
    @FXML TextField tfHuwelijkInvoer;
    @FXML TextField tfScheidingInvoer;
    @FXML Button btOKGezinInvoer;
    @FXML Button btCancelGezinInvoer;
    
    //STAMBOOM
    @FXML Button btCreateStamboom;
    @FXML Button btOpenStamboom;
    @FXML Button btSaveStamboom;
    
    //opgave 4
    private boolean withDatabase;
    private StamboomController controller;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {       
        withDatabase = true;        
        controller = new StamboomController();
        initComboboxes();
    }

    private void initComboboxes()
    {      
        Administratie admin = this.getAdministratie();
        
        if(admin.getPersonen() != null)
        {
            cbOuder1Invoer.setItems(admin.getPersonen());
            cbOuder2Invoer.setItems(admin.getPersonen());
            cbOuder1Invoer2.setItems(admin.getPersonen());
            cbOuder2Invoer2.setItems(admin.getPersonen());
        }
        else
        {
            cbOuder1Invoer.setItems(null);
            cbOuder2Invoer.setItems(null);
            cbOuder1Invoer2.setItems(null);
            cbOuder2Invoer2.setItems(null);
        }
        
        if (admin.getGezinnen() != null)
        {
            cbGezin.setItems(admin.getGezinnen());        
            cbOuderlijkGezin2.setItems(admin.getGezinnen());
        }
        else
        {
            cbGezin.setItems(null);
            cbOuderlijkGezin2.setItems(null);
        }
    }

    public void selectPersoon(Event evt)
    {
        //TODO (select persoon in treeview)
        //Persoon persoon = (Persoon) cbPersonen.getSelectionModel().getSelectedItem();
        //showPersoon(persoon);
    }

    private void showPersoon(Persoon persoon)
    {
        //TODO (selected in treeview)
    }

    public void selectGezin(Event evt)
    {
        Gezin gezin = (Gezin) cbGezin.getSelectionModel().getSelectedItem();
        showGezin(gezin);
    }

    private void showGezin(Gezin gezin)
    {
        if (gezin == null)
        {
            clearTabGezin();
        }
        else
        {
            if (gezin.getOuder1() != null)
            {
                cbOuder1Invoer2.getSelectionModel().select(gezin.getOuder1());
            }
            else
            {
                cbOuder1Invoer2.getSelectionModel().clearSelection();
            }
            
            if (gezin.getOuder1() != null)
            {
                cbOuder2Invoer2.getSelectionModel().select(gezin.getOuder2());
            }
            else
            {
                cbOuder2Invoer2.getSelectionModel().clearSelection();
            }
            
            if (gezin.getHuwelijksdatum() != null)
            {                
                tfHuwelijkInvoer2.setText(StringUtilities.datumString(gezin.getHuwelijksdatum()));
            }
            
            if (gezin.getScheidingsdatum() != null)
            {
                tfScheidingInvoer2.setText(StringUtilities.datumString(gezin.getScheidingsdatum()));
            }
            
            if (gezin.getKinderen() != null)
            {
                lvKinderen.setItems(gezin.getKinderen());
            }
        }
    }

    public void setHuwdatum(Event evt)
    {
        if (tfHuwelijkInvoer2.getText().isEmpty() || tfHuwelijkInvoer2.getText().equals("")
                || tfHuwelijkInvoer2.getText().equals("dd-mm-jjjj"))
        {
            return;
        }  
        
        Gezin gezin = (Gezin) cbGezin.getSelectionModel().getSelectedItem();
        
        if (gezin == null)
        {
            showDialog("Warning", "geen gezin geselecteerd");
            return;
        }
        
        if (gezin.getOuder2() == null)
        {
            showDialog("Warning", "kan geen huwelijksdatum instellen bij eenoudergezin");
            return;
        }
        
        Calendar cal = null;
                
        try
        {
            cal = StringUtilities.datum(tfHuwelijkInvoer2.getText());
        }
        catch (IllegalArgumentException ex)
        {
            showDialog("Warning", ex.getMessage());
        }      
        
        boolean bool = this.getAdministratie().setHuwelijk(gezin, cal);  
        
        if (bool == false)
        {
            showDialog("Warning", "Invoer huwelijksdatum is niet geaccepteerd");
        }
        else
        {
            showDialog("Geslaagd", "Invoer huwelijksdatum geslaagd");
            clearTabs();
            initComboboxes();
        }
    }

    public void setScheidingsdatum(Event evt)
    {
        if (tfScheidingInvoer2.getText().isEmpty() || tfScheidingInvoer2.getText().equals("")
                || tfScheidingInvoer2.getText().equals("dd-mm-jjjj"))
        {
            return;
        }
        
        Gezin gezin = (Gezin) cbGezin.getSelectionModel().getSelectedItem();
        
        if (gezin == null)
        {
            showDialog("Warning", "geen gezin geselecteerd");
            return;
        }
        
        if (gezin.getOuder2() == null)
        {
            showDialog("Warning", "kan geen scheidingsdatum instellen bij eenoudergezin");
            return;
        } 
        
        Calendar cal = null;
                
        try
        {
            cal = StringUtilities.datum(tfScheidingInvoer2.getText()); 
        }
        catch (IllegalArgumentException ex)
        {
            showDialog("Warning", ex.getMessage());
        }
        
        boolean bool = this.getAdministratie().setScheiding(gezin, cal);
        
        if (bool == false)
        {
            showDialog("Warning", "Invoer scheidingsdatum is niet geaccepteerd");
        }
        else
        {
            showDialog("Geslaagd", "Invoer scheidingsdatum geslaagd");
            clearTabs();
            initComboboxes();
        }        
    }

    public void cancelPersoonInvoer(Event evt)
    { 
        clearTabPersoonInvoer();
    }

    public void okPersoonInvoer(Event evt)
    {
        // todo opgave 3
        String[] vnamen = tfVoornamen2.getText().split(" ");        
        String tvoegsel = tfTussenvoegsel2.getText();
        String anaam = tfAchternaam2.getText();        
        String gebplaats = tfGebPlaats2.getText();         
        Calendar gebdat = null;

        try
        {
            gebdat = StringUtilities.datum(tfGebDatum2.getText());
        }
        catch (IllegalArgumentException ex)
        {
            showDialog("Warning", ex.getMessage());
        }
        
        Geslacht geslacht = null;
        
        if (tfGeslacht2.getText().toLowerCase().equals("man"))
        {
            geslacht = Geslacht.MAN;
        }
        else if (tfGeslacht2.getText().toLowerCase().equals("vrouw"))
        {
            geslacht = Geslacht.VROUW;
        }
        
        Gezin gezin = (Gezin) cbOuderlijkGezin2.getSelectionModel().getSelectedItem();
        
        if (vnamen == null)
        {
            showDialog("Warning", "Voornamen niet ingevuld");
            return;
        }
        
        if (anaam.isEmpty())
        {
            showDialog("Warning", "Achternaam niet ingevuld");
            return;
        }
        
        if (gebdat == null)
        {
            showDialog("Warning", "Geboortedatum niet ingevuld");
            return;
        }
        
        if (gebplaats.isEmpty())
        {
            showDialog("Warning", "Geboorteplaats niet ingevuld");
            return;
        }
        
        Persoon persoon = null;
        
        try
        {
            persoon = this.getAdministratie().addPersoon(geslacht, vnamen, anaam, tvoegsel, gebdat, gebplaats, gezin);
        }
        catch (IllegalArgumentException ex)
        {
            showDialog("Warning", ex.getMessage());
        }
        
        if (persoon == null)
        {
            showDialog("Warning", "Invoer persoon is niet geaccepteerd");
        }
        else
        {
            showDialog("Geslaagd", "Invoer persoon geslaagd");
        }
        
        clearTabs();
        initComboboxes();
    }

    public void okGezinInvoer(Event evt)
    {
        Persoon ouder1 = (Persoon) cbOuder1Invoer.getSelectionModel().getSelectedItem();
        
        if (ouder1 == null)
        {
            showDialog("Warning", "eerste ouder is niet ingevoerd");
            return;
        }
        
        Persoon ouder2 = null;
        
        if (cbOuder2Invoer.getSelectionModel().getSelectedItem() != null)
        {
            ouder2 = (Persoon) cbOuder2Invoer.getSelectionModel().getSelectedItem();
        }
        
        if (ouder1 == ouder2)
        {
            showDialog("Warning", "ouders kunnen niet hetzelfde zijn");
            return;
        }
        
        String huwdat = tfHuwelijkInvoer.getText();        
        Gezin g = null;
        
        if (!huwdat.equals("") && !huwdat.isEmpty() && !huwdat.equals("dd-mm-jjjj"))
        {
            Calendar huwdatum = null;
            
            try
            {
                huwdatum = StringUtilities.datum(huwdat);
            }
            catch (IllegalArgumentException exc) 
            {
                showDialog("Warning", "huwelijksdatum :" + exc.getMessage());
                return;
            }
            
            g = this.getAdministratie().addHuwelijk(ouder1, ouder2, huwdatum);
            
            if (g == null) 
            {
                showDialog("Warning", "Invoer huwelijk is niet geaccepteerd");
                return;
            } 
            else 
            {
                String scheidat = tfScheidingInvoer.getText();
                
                if (!scheidat.equals("") && !scheidat.isEmpty() && !scheidat.equals("dd-mm-jjjj"))
                {
                    Calendar scheidingsdatum = null;
                    
                    try 
                    {
                        scheidingsdatum = StringUtilities.datum(tfScheidingInvoer.getText());
                        this.getAdministratie().setScheiding(g, scheidingsdatum);
                    } 
                    catch (IllegalArgumentException exc) 
                    {
                        showDialog("Warning", "scheidingsdatum :" + exc.getMessage());
                        return;
                    }
                }
                
                showDialog("Geslaagd", "Invoer gezin geslaagd");
                clearTabs();
                initComboboxes();  
            }
        } 
        else 
        {
            g = this.getAdministratie().addOngehuwdGezin(ouder1, ouder2);
            
            if (g == null) 
            {
                showDialog("Warning", "Invoer ongehuwd gezin is niet geaccepteerd");
                return;
            }
            else
            {
                showDialog("Geslaagd", "Invoer gezin geslaagd");
                clearTabs();
                initComboboxes();
            }
        }      
    }

    public void cancelGezinInvoer(Event evt) 
    {
        clearTabGezinInvoer();
    }
    
    public void showStamboom(Event evt) 
    {
        //TODO (laten zien bij opstarten, voor alle personen)
//        Persoon persoon = (Persoon) cbPersonen.getSelectionModel().getSelectedItem();
//        
//        if (persoon != null)
//        {
//            taStamboom.setText(persoon.stamboomAlsString());
//        }
    }

    public void createEmptyStamboom(Event evt) 
    {
        this.clearAdministratie();
        clearTabs();
        initComboboxes();
    }
    
    public void openStamboom(Event evt) 
    {
        if (withDatabase == false)
        {        
            FileChooser chooser = new FileChooser();
            File bestand = chooser.showOpenDialog(getStage());
            
            if (bestand == null)
            {
                return;
            }
            
            try
            {
                this.deserialize(bestand);
                showDialog("Geslaagd", "Stamboom is geladen");
            }
            catch (IOException ex)
            {
                showDialog("Warning", "Stamboom kon niet geladen worden");
            }
        }
        else
        {
            try
            {
                this.loadFromDatabase();
                if (this.getAdministratie().getGezinnen() == null && this.getAdministratie().getPersonen() == null)
                {
                    showDialog("Warning", "Stamboom is leeg");
                }
                else
                {
                    showDialog("Geslaagd", "Stamboom is geladen");
                }
            }
            catch (IOException ex)
            {
                showDialog("Warning", "Stamboom kon niet geladen worden");
            }
        }
        
        clearTabs();
        initComboboxes();
    }
    
    public void saveStamboom(Event evt) 
    {
        if (withDatabase == false)
        {
            FileChooser chooser = new FileChooser();
            File bestand = chooser.showSaveDialog(getStage());

            if (bestand == null)
            {
                return;
            }
            
            try
            {
                this.serialize(bestand);
                showDialog("Geslaagd", "Stamboom is opgeslagen");
            }
            catch (IOException ex)
            {
                showDialog("Warning", "Stamboom kon niet opgeslagen worden");
            }
        }
        else
        {
            if (this.getAdministratie().aantalGeregistreerdePersonen() > 0)
            {            
                try
                {
                    this.saveToDatabase();
                    showDialog("Geslaagd", "Stamboom is opgeslagen");
                }
                catch (IOException ex)
                {
                    showDialog("Warning", "Stamboom kon niet opgeslagen worden");
                }
            }
            else
            {
                showDialog("Warning", "Stamboom is leeg");
            }
        }
        
        clearTabs();
        initComboboxes();
    }
    
    public void closeApplication(Event evt) 
    {
        saveStamboom(evt);
        getStage().close();
    }
   
    public void configureStorage(Event evt) 
    {
        withDatabase = cmDatabase.isSelected();
    }
 
    public void selectTab(Event evt) 
    {
        Object source = evt.getSource();
        
        if (source == tabPersoon) 
        {
            clearTabPersoon();
        } 
        else if (source == tabGezin) 
        {
            clearTabGezin();
        } 
        else if (source == tabPersoonInvoer) 
        {
            clearTabPersoonInvoer();
        } 
        else if (source == tabGezinInvoer) 
        {
            clearTabGezinInvoer();
        }
    }

    private void clearTabs() 
    {
        clearTabPersoon();
        clearTabPersoonInvoer();
        clearTabGezin();
        clearTabGezinInvoer();
    }
    
    private void clearTabPersoonInvoer() 
    {
        tfVoornamen2.clear();
        tfTussenvoegsel2.clear();
        tfAchternaam2.clear();
        tfGeslacht2.clear();
        tfGebDatum2.clear();
        tfGebPlaats2.clear();
        cbOuderlijkGezin2.getSelectionModel().clearSelection();  
    }
    
    private void clearTabGezinInvoer() 
    {
        cbOuder1Invoer.getSelectionModel().clearSelection();
        cbOuder2Invoer.getSelectionModel().clearSelection();
        tfHuwelijkInvoer.clear();
        tfScheidingInvoer.clear();        
    }

    private void clearTabPersoon() 
    {
        //trvStamboom
        //tavPersonen
    }
    
    private void clearTabGezin() 
    {
        cbGezin.getSelectionModel().clearSelection();
        cbOuder1Invoer2.getSelectionModel().clearSelection();
        cbOuder2Invoer2.getSelectionModel().clearSelection();
        lvKinderen.setItems(FXCollections.emptyObservableList());
        tfHuwelijkInvoer2.clear();
        tfScheidingInvoer2.clear();       
    }

    private void showDialog(String type, String message) 
    {
        Stage myDialog = new Dialog(getStage(), type, message);
        myDialog.show();
    }

    private Stage getStage() 
    {
        return (Stage) menuBar.getScene().getWindow();
    }
}
