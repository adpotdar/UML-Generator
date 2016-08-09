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
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import static jd.controller.pageEditController.selectedDiagram;
import static jd.data.DataManager.foreignBoxesPresent;
import jd.data.ForeignClassBox;
import static jd.gui.Workspace.space;
import jd.lines.InheritanceLines;
import static jf.settings.AppStartupConstants.CLOSE_BUTTON_LABEL;

/**
 * This class serves to present custom text messages to the user when
 * events occur.
 */
public class AppAddForeignInterfaceSingleton extends Stage {
    // HERE'S THE SINGLETON OBJECT
    static AppAddForeignInterfaceSingleton singleton = null;
    
    // HERE ARE THE DIALOG COMPONENTS
    VBox messagePane;
    Scene messageScene;
    Label messageLabel;
    Button closeButton;
    
    TextField textField1;
    VBox vbox1;
    
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
    private AppAddForeignInterfaceSingleton() {}
    
    /**
     * A static accessor method for getting the singleton object.
     * 
     * @return The one singleton dialog of this object type.
     */
    public static AppAddForeignInterfaceSingleton getSingleton() {
	if (singleton == null)
	    singleton = new AppAddForeignInterfaceSingleton();
	return singleton;
    }
    
    /**
     * This function fully initializes the singleton dialog for use.
     * 
     * @param owner The window above which this dialog will be centered.
     */
    public void init(Stage owner) {
        
        // LABEL TO DISPLAY THE CUSTOM MESSAGE
        messageLabel = new Label("To add more Interfaces, click on \"ADD MORE\" \n and if done adding click on \"DONE ADDING!\"");        

        // CLOSE BUTTON
        closeButton = new Button(CLOSE_BUTTON_LABEL);
        closeButton.setOnAction(e->{ AppAddForeignInterfaceSingleton.this.close(); });

        textField1 = new TextField();
        vbox1 = new VBox(5);
        doneAdding = new Button("DONE editing");
        
        vbox1.getChildren().add(textField1);
        addMore = new Button("ADD More");
        addMore.setOnAction(e ->{
            TextField temp = new TextField();
            vbox1.getChildren().add(temp);
        });
        
        doneAdding.setOnAction(e ->{
            boolean invalidName = false;
            
            for(Node inter:vbox1.getChildren()){
                if(((TextField)inter).getText().trim().equals("")){
                    invalidName = true;
                }
            }
            
            if(invalidName){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Incorret Input");
                alert.setHeaderText("One of the entered names is invalid. Recheck all entered names!!");
                String s ="Enter correct interface names.";
                alert.setContentText(s);
                alert.show();
            }else{
                // TO AVOID JAVA CONCURRENT EXCEPTION
                ArrayList<String> newInterfaces = new ArrayList<String>();

                for(Node node:vbox1.getChildren()){
                    if(((TextField)node).getText().isEmpty()){

                    }else{
                        newInterfaces.add(((TextField)node).getText());
                    }
                }

                selectedDiagram.setLocalInterfaces(newInterfaces);

                // THIS IS TO ADD THE FOREIGN INTERFACE BOX
                for(Node node:vbox1.getChildren()){

                    if(((TextField)node).getText().isEmpty()){

                    }else{

                        ForeignClassBox box = new ForeignClassBox(((TextField)node).getText(),space);
                        // TO AVOID CREATING SAME BOXES AGAIN AND AGAIN
                        foreignBoxesPresent.add(box);

                        InheritanceLines line = new InheritanceLines(selectedDiagram,box,selectedDiagram.getRootContainer(),box.getVBox(),space);
                        // ADD THERESPECTIVE LINES TO THEIR RESPECTIVE BOXES AND CLASS DIAGRAMS
                        box.addComingInLine(line);
                        selectedDiagram.addGoingOutLine(line);
                    }
                }
                
               AppAddForeignInterfaceSingleton.this.close(); 
            }
        });
        
        // WE'LL PUT EVERYTHING HERE
        messagePane = new VBox();
        messagePane.setAlignment(Pos.CENTER);
        messagePane.getChildren().add(messageLabel);
        messagePane.getChildren().add(vbox1);
        messagePane.getChildren().add(addMore);
        messagePane.getChildren().add(doneAdding);
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