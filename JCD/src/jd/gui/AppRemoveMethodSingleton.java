package jd.gui;

import java.util.ArrayList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import jd.controller.pageEditController;
import static jd.controller.pageEditController.selectedDiagram;
import jd.data.MethodsObject;
import static jf.settings.AppStartupConstants.CLOSE_BUTTON_LABEL;

/**
 * This class serves to present custom text messages to the user when
 * events occur. 
 */
public class AppRemoveMethodSingleton extends Stage {
    // HERE'S THE SINGLETON OBJECT
    static AppRemoveMethodSingleton singleton = null;
    
    // HERE ARE THE DIALOG COMPONENTS
    VBox messagePane;
    Scene messageScene;
    Label messageLabel;
    Button closeButton;
    
    Label label1;
    ComboBox<String> comboBox;
    Button button1;
    
    /**
     * Initializes this dialog so that it can be used repeatedly
     * for all kinds of messages. Note this is a singleton design
     * pattern so the constructor is private.
     * 
     * @param owner The owner stage of this modal dialoge.
     * 
     * @param closeButtonText Text to appear on the close button.
     */
    private AppRemoveMethodSingleton() {}
    
    /**
     * A static accessor method for getting the singleton object.
     * 
     * @return The one singleton dialog of this object type.
     */
    public static AppRemoveMethodSingleton getSingleton() {
	if (singleton == null)
	    singleton = new AppRemoveMethodSingleton();
	return singleton;
    }
    
    /**
     * This function fully initializes the singleton dialog for use.
     * 
     * @param owner The window above which this dialog will be centered.
     */
    public void init(Stage owner) {

        // LABEL TO DISPLAY THE CUSTOM MESSAGE
        messageLabel = new Label();        
 
        // CLOSE BUTTON
        closeButton = new Button(CLOSE_BUTTON_LABEL);
        closeButton.setOnAction(e->{ AppRemoveMethodSingleton.this.close(); });

        // WE'LL PUT EVERYTHING HERE
        messagePane = new VBox(5);
        messagePane.setAlignment(Pos.CENTER);
        
        label1 = new Label("The following methods are present in "+ selectedDiagram.getClassName()+": ");
        comboBox = new ComboBox<>();
        comboBox.getItems().addAll(selectedDiagram.getMethodNames());
        button1 = new Button("REMOVE");
        
        // TO AVOID java.util.ConcurrentModificationException
        ArrayList<MethodsObject> varCopy = new ArrayList<MethodsObject>(selectedDiagram.getMethodsList());
        
        button1.setOnAction(e ->{
            
            for(MethodsObject var: selectedDiagram.getMethodsList()){
                // THIS WILL HOLD THE VALUE OF VARIABLE TO BE REMOVED FROM THE LIST
                int i;

                if(var.getName().equals(comboBox.getValue())){
                    i = selectedDiagram.getMethodsList().indexOf(var);
                    // REMOVEING THE ELEMENT FROM THE COPY
                    varCopy.remove(i);
                }
            }
            selectedDiagram.setMethodsList(varCopy);
            // TO UPDATE THE three VBOX
            selectedDiagram.updateThree(varCopy);
            AppRemoveMethodSingleton.this.close();
           
        });
        
        // adding everything to the message pane
        messagePane.getChildren().add(label1);
        messagePane.getChildren().add(comboBox);
        messagePane.getChildren().add(button1);
        messagePane.getChildren().add(messageLabel);
        messagePane.getChildren().add(closeButton);
        
        // MAKE IT LOOK NICE
        messagePane.setPadding(new Insets(80, 60, 80, 60));
        messagePane.setSpacing(20);

        // AND PUT IT IN THE WINDOW
        messageScene = new Scene(messagePane);
        this.setScene(messageScene);
    }
 
    /**
     * This method loads a custom message into the label and
     * then pops open the dialog.
     * 
     * @param title The title to appear in the dialog window.
     * 
     * @param message Message to appear inside the dialog.
     */
    public void show(String title, String message) {
	// SET THE DIALOG TITLE BAR TITLE
	setTitle(title);

        // SET THE MESSAGE TO DISPLAY TO THE USER
        messageLabel.setText(message);
	
	// AND OPEN UP THIS DIALOG, MAKING SURE THE APPLICATION
	// WAITS FOR IT TO BE RESOLVED BEFORE LETTING THE USER
	// DO MORE WORK.
        showAndWait();
    }
}