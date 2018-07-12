import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainScreen implements Initializable {
    private Main mainClass;
    private Stage stage;

    private Rounder rounder = new Rounder();

    private double offX = 0;
    private double offY = 0;

    @FXML Pane exitButton;
    @FXML Pane minimizeButton;
    @FXML Pane dragPane;
    @FXML Label minimizeLabel;
    @FXML Label lblSupport;
    @FXML Label title;
    @FXML TextField txtHealth;
    @FXML TextField txtPhysDef;
    @FXML TextField txtMagDef;
    @FXML TextField txtPhysFlatPen;
    @FXML TextField txtPhysPercentPen;
    @FXML TextField txtMagFlatPen;
    @FXML TextField txtMagPercentPen;
    @FXML TextField txtDR;
    @FXML Label lblPhysHealth;
    @FXML Label lblMagHealth;
    @FXML Label lblPhysHealthPen;
    @FXML Label lblMagHealthPen;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainClass = Main.getInstance();
        stage = mainClass.getStage();

        initializeMinimizeExit();
        initializeTextFields();
    }

    @FXML private void minimizePressed(){
        mainClass.minimize();
    }

    @FXML private void closePressed(){
        mainClass.getStage().close();
    }

    @FXML private void supportPressed(){
        if(Desktop.isDesktopSupported()){
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(new URI("https://www.paypal.me/thume02"));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }else {
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec("xdg-open " + "https://www.paypal.me/thume02");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateBaseHealth(){
        int health;
        int physDef;
        int magDef;
        double damageMitigation;

        if(txtHealth.getText().equals(""))health = 0;
        else health = Integer.valueOf(txtHealth.getText());

        if(txtPhysDef.getText().equals(""))physDef = 0;
        else physDef = Integer.valueOf(txtPhysDef.getText());

        if(txtMagDef.getText().equals(""))magDef = 0;
        else magDef = Integer.valueOf(txtMagDef.getText());

        if(txtDR.getText().equals(""))damageMitigation = 0;
        else damageMitigation = Double.valueOf(txtDR.getText()) / 100;

        double basePhysMult = (100.0/(physDef + 100));
        double physMult = basePhysMult - (basePhysMult * damageMitigation);
        int physMit = rounder.toWhole(100 * (1-physMult));

        double baseMagMult = (100.0/(magDef + 100));
        double magMult = baseMagMult - (baseMagMult * damageMitigation);
        int magMit = rounder.toWhole(100 * (1-magMult));

        int basePhysHealth = rounder.toWhole(health / physMult);
        int baseMagHealth = rounder.toWhole(health / magMult);

        lblPhysHealth.setText(String.valueOf(basePhysHealth) + " (" + physMit + "% mit)");
        lblMagHealth.setText(String.valueOf(baseMagHealth) + " (" + magMit + "% mit)");

        updateHealthVsPen();
    }

    private void updateHealthVsPen(){
        int health;
        double physDef;
        double magDef;
        int physFlatPen;
        double physPercentPen;
        int magFlatPen;
        double magPercentPen;
        double damageMitigation;

        if(txtHealth.getText().equals(""))health = 0;
        else health = Integer.valueOf(txtHealth.getText());

        if(txtPhysDef.getText().equals(""))physDef = 0;
        else physDef = Integer.valueOf(txtPhysDef.getText());

        if(txtMagDef.getText().equals(""))magDef = 0;
        else magDef = Integer.valueOf(txtMagDef.getText());

        if(txtDR.getText().equals(""))damageMitigation = 0;
        else damageMitigation = Double.valueOf(txtDR.getText()) / 100;

        if(txtPhysFlatPen.getText().equals(""))physFlatPen = 0;
        else physFlatPen = Integer.valueOf(txtPhysFlatPen.getText());

        if(txtPhysPercentPen.getText().equals(""))physPercentPen = 0;
        else physPercentPen = 1 - Double.valueOf(txtPhysPercentPen.getText())/100;

        if(txtMagFlatPen.getText().equals(""))magFlatPen = 0;
        else magFlatPen = Integer.valueOf(txtMagFlatPen.getText());

        if(txtMagPercentPen.getText().equals(""))magPercentPen = 0;
        else magPercentPen = 1 - Double.valueOf(txtMagPercentPen.getText())/100;

        if (txtPhysPercentPen.getText().equals("100")) physDef = 0;
        else if(physPercentPen != 0)physDef *= physPercentPen;
        physDef -= physFlatPen;
        if(physDef < 0) physDef = 0;

        if(txtMagPercentPen.getText().equals("100")) magDef = 0;
        else if(magPercentPen != 0)magDef *= magPercentPen;
        magDef -= magFlatPen;
        if(magDef < 0) magDef = 0;

        double basePhysMult = (100.0/(physDef + 100));
        double physMult = basePhysMult - (basePhysMult * damageMitigation);
        int physMit = rounder.toWhole(100 * (1-physMult));

        double baseMagMult = (100.0/(magDef + 100));
        double magMult = baseMagMult - (baseMagMult * damageMitigation);
        int magMit = rounder.toWhole(100 * (1-magMult));

        int penPhysHealth = rounder.toWhole(health / physMult);
        int penMagHealth = rounder.toWhole(health / magMult);

        lblPhysHealthPen.setText(String.valueOf(penPhysHealth) + " (" + physMit + "% mit)");
        lblMagHealthPen.setText(String.valueOf(penMagHealth) + " (" + magMit + "% mit)");

    }

    //Initializes elements in the top pane of the window.
    private void initializeMinimizeExit(){
        minimizeLabel.setOpacity(1);
        title.setOpacity(1);

        lblSupport.addEventFilter(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) { lblSupport.setTextFill(Color.LIGHTGREY); }
        });
        lblSupport.addEventFilter(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) { lblSupport.setTextFill(Color.web("#e7e7e7")); }
        });

        exitButton.addEventFilter(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) { exitButton.setStyle("-fx-background-color: firebrick;"); }
        });

        exitButton.addEventFilter(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) { exitButton.setStyle("-fx-background-color: dimgrey;"); }
        });

        minimizeButton.addEventFilter(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) { minimizeButton.setStyle("-fx-background-color: grey;"); }
        });
        minimizeButton.addEventFilter(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(stage.isIconified()) return;
                minimizeButton.setStyle("-fx-background-color: dimgrey;");
            }
        });

        stage.iconifiedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                if(!t1){
                    minimizeButton.setStyle("-fx-background-color: dimgrey;");
                }
            }
        });

        dragPane.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                offX = stage.getX() - event.getScreenX();
                offY = stage.getY() - event.getScreenY();
            }
        });

        dragPane.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.setX(event.getScreenX() + offX);
                stage.setY(event.getScreenY() + offY);
            }
        });
    }

    //Initializes all TextFields to 0 and adds listeners that makes them numeric only.
    private void initializeTextFields(){
        txtHealth.setText("0");
        txtPhysDef.setText("0");
        txtMagDef.setText("0");
        txtPhysFlatPen.setText("0");
        txtPhysPercentPen.setText("0");
        txtMagFlatPen.setText("0");
        txtDR.setText("0");
        txtMagPercentPen.setText("0");
        lblPhysHealth.setText("0 (0% mit)");
        lblMagHealth.setText("0 (0% mit)");
        lblPhysHealthPen.setText("0 (0% mit)");
        lblMagHealthPen.setText("0 (0% mit)");

        txtHealth.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtHealth.setText(newValue.replaceAll("[^\\d]", ""));
                }
                else if(newValue.length() > 6){
                    txtHealth.setText(oldValue);
                }
                else if(newValue.equals("")) return;
                else{
                    updateBaseHealth();
                }
            }
        });
        txtHealth.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                if (!newPropertyValue)
                {
                    if(txtHealth.getText().equals("")) txtHealth.setText("0");
                }
            }
        });

        txtPhysDef.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtPhysDef.setText(newValue.replaceAll("[^\\d]", ""));
                }
                else if(newValue.length() > 6){
                    txtPhysDef.setText(oldValue);
                }
                else if(newValue.equals("")) return;
                else{
                    updateBaseHealth();
                }
            }
        });
        txtPhysDef.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                if (!newPropertyValue)
                {
                    if(txtPhysDef.getText().equals("")) txtPhysDef.setText("0");
                }
            }
        });

        txtMagDef.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtMagDef.setText(newValue.replaceAll("[^\\d]", ""));
                }
                else if(newValue.length() > 6){
                    txtMagDef.setText(oldValue);
                }
                else if(newValue.equals("")) return;
                else{
                    updateBaseHealth();
                }
            }
        });
        txtMagDef.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                if (!newPropertyValue)
                {
                    if(txtMagDef.getText().equals("")) txtMagDef.setText("0");
                }
            }
        });

        txtPhysFlatPen.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtPhysFlatPen.setText(newValue.replaceAll("[^\\d]", ""));
                }
                else if(newValue.length() > 6){
                    txtPhysFlatPen.setText(oldValue);
                }
                else if(newValue.equals("")) return;
                else{
                    updateHealthVsPen();
                }
            }
        });
        txtPhysFlatPen.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                if (!newPropertyValue)
                {
                    if(txtPhysFlatPen.getText().equals("")) txtPhysFlatPen.setText("0");
                }
            }
        });

        txtPhysPercentPen.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtPhysPercentPen.setText(newValue.replaceAll("[^\\d]", ""));
                }
                else if(newValue.equals("")) return;
                else if(newValue.length() > 3 || (Integer.valueOf(newValue) > 100)){
                    txtPhysPercentPen.setText(oldValue);
                }
                else{
                    updateHealthVsPen();
                }
            }
        });
        txtPhysPercentPen.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                if (!newPropertyValue)
                {
                    if(txtPhysPercentPen.getText().equals("")) txtPhysPercentPen.setText("0");
                }
            }
        });

        txtMagFlatPen.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtMagFlatPen.setText(newValue.replaceAll("[^\\d]", ""));
                }
                else if(newValue.length() > 6){
                    txtMagFlatPen.setText(oldValue);
                }
                else if(newValue.equals("")) return;
                else{
                    updateHealthVsPen();
                }
            }
        });
        txtMagFlatPen.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                if (!newPropertyValue)
                {
                    if(txtMagFlatPen.getText().equals("")) txtMagFlatPen.setText("0");
                }
            }
        });

        txtMagPercentPen.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtMagPercentPen.setText(newValue.replaceAll("[^\\d]", ""));
                }
                else if(newValue.equals("")) return;
                else if(newValue.length() > 3 || (Integer.valueOf(newValue) > 100)){
                    txtMagPercentPen.setText(oldValue);
                }
                else{
                    updateHealthVsPen();
                }
            }
        });
        txtMagPercentPen.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                if (!newPropertyValue)
                {
                    if(txtMagPercentPen.getText().equals("")) txtMagPercentPen.setText("0");
                }
            }
        });

        txtDR.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtDR.setText(newValue.replaceAll("[^\\d]", ""));
                }
                else if(newValue.equals("")) return;
                else if(Integer.valueOf(newValue) > 99){
                    txtDR.setText(oldValue);
                }
                else{
                    updateBaseHealth();
                }
            }
        });
        txtDR.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                if (!newPropertyValue)
                {
                    if(txtDR.getText().equals("")) txtDR.setText("0");
                }
            }
        });
    }
}
