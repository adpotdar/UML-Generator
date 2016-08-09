package jd.gui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import jd.controller.pageEditController;
import static jd.controller.pageEditController.diagramsList;
import static jd.controller.pageEditController.selectedDiagram;
import jd.data.ClassDiagram;
import jd.data.DataManager;
import static jd.data.DataManager.foreignBoxesPresent;
import jd.data.ForeignClassBox;
import jd.data.VariablesObject;
import static jd.gui.Workspace.space;
import jd.lines.InheritanceLines;
import jd.lines.UseLines;
import static jf.settings.AppStartupConstants.CLOSE_BUTTON_LABEL;

/**
 * This class serves to present custom text messages to the user when
 * events occur.
 */
public class AppAddVariableSingleton extends Stage {
    // HERE'S THE SINGLETON OBJECT
    static AppAddVariableSingleton singleton = null;
    
    // HERE ARE THE DIALOG COMPONENTS
    VBox messagePane;
    Scene messageScene;
    Label messageLabel;
    Button closeButton;
    
    Label label1 ;
    Label label2 ;
    Label label3 ;
    Label label4 ;
    Label label5 ;
    
    TextField textField1;
    TextField textField2;
    CheckBox checkBox1;
    CheckBox checkBox2;
    TextField textField5;

    Button button1;
    
    GridPane grid;
    /**
     * Initializes this dialog so that it can be used repeatedly
     * for all kinds of messages. Note this is a singleton design
     * pattern so the constructor is private.
     * 
     * @param owner The owner stage of this modal dialoge.
     * 
     * @param closeButtonText Text to appear on the close button.
     */
    private AppAddVariableSingleton() {}
    
    /**
     * A static accessor method for getting the singleton object.
     * 
     * @return The one singleton dialog of this object type.
     */
    public static AppAddVariableSingleton getSingleton() {
	if (singleton == null)
	    singleton = new AppAddVariableSingleton();
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
        closeButton.setOnAction(e->{ AppAddVariableSingleton.this.close(); });

        // WE'LL PUT EVERYTHING HERE
        messagePane = new VBox(5);
        messagePane.setAlignment(Pos.CENTER);
        
        
        // HERE I WILL ADD CUSTOM FIELDS
        label1 = new Label("Name");
        label2 = new Label("Type");
        label3 = new Label("isStatic");
        label4 = new Label("isFinal");
        label5 = new Label("Access");
        
        textField1 = new TextField();
        textField2 = new TextField();
        checkBox1 = new CheckBox();
        checkBox2 = new CheckBox();
        textField5 = new TextField();
        
        button1 = new Button("ADD");
        
        if(!selectedDiagram.getClassType()){
            checkBox1.setDisable(true);
            checkBox2.setDisable(true);
        }
        
        button1.setOnAction(e ->{
            
            String name = textField1.getText();
            String returnType = textField2.getText();
            
            boolean isStatic;
            if(checkBox1.isSelected()){
                isStatic = true;
            }else{
                isStatic = false;
            }
            
            boolean isFinal;
            if(checkBox2.isSelected()){
                isFinal = true;
            }else{
                isFinal = false;
            }
            
            String access = textField5.getText();
            if(access.equals("")){ // SET THE DEAFULT VALUE
                access ="public";
            }
            
            if(name.trim().equals("") || returnType.trim().equals("")){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Incorret Input");
                alert.setHeaderText("One of the entered names is invalid. Recheck all entered names!!");
                String s ="Enter correct values!!";
                alert.setContentText(s);
                alert.show();
            }else{
                VariablesObject var;
                if(selectedDiagram.getClassType()){
                    // THIS IS FOR A NORMAL CLASS
                    var = new VariablesObject(name, returnType, isStatic,isFinal ,access);
                }else{
                    // THIS IS FOR INTERFACE
                    var = new VariablesObject(name, returnType,true ,true ,access);
                }
                // THIS WILL ADD THE VARIABLE TO THE DIAGRAM
                pageEditController.selectedDiagram.addVariable(var);

                if(!(returnType.equals("byte")||returnType.equals("short")||returnType.equals("int")||returnType.equals("long")
                        ||returnType.equals("float")||returnType.equals("double")
                        ||returnType.equals("boolean")||returnType.equals("char"))){

                    boolean isLocal = false;
                    String className;
                    ClassDiagram tempDiagram = null;

                    for(ClassDiagram diagram: diagramsList){
                        if(diagram.getClassName().equals(returnType)){
                            tempDiagram = diagram;
                            className = returnType;
                            isLocal = true;
                        }
                    }

                    if(isLocal){
                        UseLines line = new UseLines(pageEditController.selectedDiagram,tempDiagram,pageEditController.selectedDiagram.getRootContainer(),tempDiagram.getRootContainer(),space);
                        // ADD THERESPECTIVE LINES TO THEIR RESPECTIVE BOXES AND CLASS DIAGRAMS
                        tempDiagram.addComingInLine(line);
                        selectedDiagram.addGoingOutLine(line);
                    }else{

                        //TO USE ISlocalBOX
                        DataManager dataManager = new DataManager();
                        ForeignClassBox box  = null;

                        if(dataManager.isLocalBox(returnType) != null){
                            box =  dataManager.isLocalBox(returnType);                   
                        }else{
                            box = new ForeignClassBox(returnType,space);
                            // TO AVOID CREATING SAME BOXES AGAIN AND AGAIN
                            foreignBoxesPresent.add(box);
                        }
                        // ADD THE LINE
                        UseLines line = new UseLines(pageEditController.selectedDiagram,box ,pageEditController.selectedDiagram.getRootContainer(),box.getVBox(),space);
                        // ADD THERESPECTIVE LINES TO THEIR RESPECTIVE BOXES AND CLASS DIAGRAMS
                        box.addComingInLine(line);
                        selectedDiagram.addGoingOutLine(line);
                    }
                    AppAddVariableSingleton.this.close();
                }
            }
        });
        
        
        grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(label1,1,1);
        grid.add(textField1,2,1);
        grid.add(label2,1,2);
        grid.add(textField2,2,2);
        grid.add(label3,1 ,3 );
        grid.add(checkBox1, 2, 3);
        grid.add(label4,1,4);
        grid.add(checkBox2,2,4);
        grid.add(label5, 1, 5);
        grid.add(textField5,2,5);
        grid.add(button1,2,6);
        
        // adding everything to the message pane
        messagePane.getChildren().add(grid);
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