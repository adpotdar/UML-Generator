package jd.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.stage.Modality;
import static jd.controller.pageEditController.diagramsList;
import static jd.controller.pageEditController.selectedDiagram;
import jd.data.ClassDiagram;
import static jd.data.DataManager.interfacesPresent;
import static jd.gui.Workspace.space;
import jd.lines.InheritanceLines;
import static jf.settings.AppStartupConstants.CLOSE_BUTTON_LABEL;

/**
 * This class serves to present custom text messages to the user when
 * events occur. 
 */
public class AppAddLocalInterfaceSingleton extends Stage {
    // HERE'S THE SINGLETON OBJECT
    static AppAddLocalInterfaceSingleton singleton = null;
    
    // HERE ARE THE DIALOG COMPONENTS
    VBox messagePane;
    Scene messageScene;
    Label messageLabel;
    Button closeButton;
    
    ComboBox localInterfacesBox;
    Button add;
    /**
     * Initializes this dialog so that it can be used repeatedly
     * for all kinds of messages. Note this is a singleton design
     * pattern so the constructor is private.
     * 
     * @param owner The owner stage of this modal dialoge.
     * 
     * @param closeButtonText Text to appear on the close button.
     */
    private AppAddLocalInterfaceSingleton() {}
    
    /**
     * A static accessor method for getting the singleton object.
     * 
     * @return The one singleton dialog of this object type.
     */
    public static AppAddLocalInterfaceSingleton getSingleton() {
	if (singleton == null)
	    singleton = new AppAddLocalInterfaceSingleton();
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
        closeButton.setOnAction(e->{ AppAddLocalInterfaceSingleton.this.close(); });

        // PUT THE LIST OF INTERFACES PRESENT IN COMBOBOX
        ObservableList<String> locals = FXCollections.observableArrayList(interfacesPresent);
        localInterfacesBox = new ComboBox(locals);
        
        
        add = new Button("ADD");
        add.setOnAction(e ->{
            // ADD THE INTERFACE TO DIAGRAM
            selectedDiagram.addALocalInterface((String) localInterfacesBox.getValue());
            
            ClassDiagram parent = null;
            
            for(ClassDiagram diagram: diagramsList){
                if(((String) localInterfacesBox.getValue()).equals(diagram.getClassName())){
                    parent = diagram;
                }
            }
            
            // ADD THE CONNNECTOR LINE 
            if(parent != null){
            InheritanceLines line = new InheritanceLines(selectedDiagram,parent,selectedDiagram.getRootContainer(),parent.getRootContainer(),space);
            // ADD THERESPECTIVE LINES TO THEIR RESPECTIVE BOXES AND CLASS DIAGRAMS
            parent.addComingInLine(line);
            selectedDiagram.addGoingOutLine(line);
            }
            
            AppAddLocalInterfaceSingleton.this.close();
        });
        
        // WE'LL PUT EVERYTHING HERE
        messagePane = new VBox();
        messagePane.setAlignment(Pos.CENTER);
        messagePane.getChildren().add(localInterfacesBox);
        messagePane.getChildren().add(add);
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