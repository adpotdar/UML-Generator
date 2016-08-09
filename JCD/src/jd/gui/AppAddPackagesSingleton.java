package jd.gui;

import java.util.ArrayList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import static jd.controller.pageEditController.selectedDiagram;
import static jf.settings.AppStartupConstants.CLOSE_BUTTON_LABEL;

/**
 * This class serves to present custom text messages to the user when
 * events occur.
 */
public class AppAddPackagesSingleton extends Stage {
    // HERE'S THE SINGLETON OBJECT
    static AppAddPackagesSingleton singleton = null;
    
    // HERE ARE THE DIALOG COMPONENTS
    VBox messagePane;
    Scene messageScene;
    Label messageLabel;
    Button closeButton;
    
    VBox vbox1;
    TextField textfield1;
    Button addMore;
    Button doneAdding;
    /**
     * Initializes this dialog so that it can be used repeatedly
     * for all kinds of messages. Note this is a singleton design
     * pattern so the constructor is private.
     * 
     * @param owner The owner stage of this modal dialoge.
     * 
     * @param closeButtonText Text to appear on the close button.
     */
    private AppAddPackagesSingleton() {}
    
    /**
     * A static accessor method for getting the singleton object.
     * 
     * @return The one singleton dialog of this object type.
     */
    public static AppAddPackagesSingleton getSingleton() {
	if (singleton == null)
	    singleton = new AppAddPackagesSingleton();
	return singleton;
    }
    
    /**
     * This function fully initializes the singleton dialog for use.
     * 
     * @param owner The window above which this dialog will be centered.
     */
    public void init(Stage owner) {
        
        // LABEL TO DISPLAY THE CUSTOM MESSAGE
        messageLabel = new Label("Add the packages you are going to use in the class.");        
        
        // CLOSE BUTTON
        closeButton = new Button(CLOSE_BUTTON_LABEL);
        closeButton.setOnAction(e->{ AppAddPackagesSingleton.this.close(); });

        vbox1 = new VBox();
        textfield1 = new TextField();
        vbox1.getChildren().add(textfield1);
        
        addMore = new Button("ADD more packages");
        addMore.setOnMouseClicked(e ->{
            TextField tmp = new TextField();
            vbox1.getChildren().add(tmp);
        });
        
        doneAdding = new Button("DONE adding packages");
        doneAdding.setOnMouseClicked(e ->{
            ArrayList<String> temp = new ArrayList<String>();
            for(Node txt : vbox1.getChildren()){
                String s = ((TextField)txt).getText();
                if(!s.trim().equals("")){
                    temp.add(s);
                }
            }
            selectedDiagram.setImportedPackages(temp);
            AppAddPackagesSingleton.this.close();
        });
        
        
        // WE'LL PUT EVERYTHING HERE
        messagePane = new VBox();
        messagePane.setAlignment(Pos.CENTER);
        messagePane.getChildren().add(vbox1);
        messagePane.getChildren().add(addMore);
        messagePane.getChildren().add(doneAdding);
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