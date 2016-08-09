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
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import jd.controller.pageEditController;
import static jd.controller.pageEditController.selectedDiagram;
import jd.data.ArgumentObject;
import jd.data.ClassDiagram;
import jd.data.DataManager;
import static jd.data.DataManager.foreignBoxesPresent;
import jd.data.ForeignClassBox;
import jd.data.MethodsObject;
import jd.data.VariablesObject;
import static jd.gui.Workspace.space;
import jd.lines.MethodLines;
import jd.lines.UseLines;
import static jf.settings.AppStartupConstants.CLOSE_BUTTON_LABEL;

/**
 * This class serves to present custom text messages to the user when
 * events occur.
 */
public class AppAddMethodSingleton extends Stage {
    // HERE'S THE SINGLETON OBJECT
    static AppAddMethodSingleton singleton = null;
    
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
    Label label6 ;
    // RETURN TYPE AND NAME OF ARGUMENT
    Label label7;
    Label label8;
    
    TextField textField1;
    TextField textField2;
    CheckBox checkBox1;
    CheckBox checkBox2;
    TextField textField5;
    
    TextField textField6;
    TextField textField7;
    
    
    //THIS IS FOR THE FIRST ARGUMENT
    HBox hbox1;
    HBox hbox2;
    
    // THIS WILL HOLD THEARGUMENTS
    VBox vbox1;
    // THIS BUTTON IS FOR ADDING MORE ARGUMENTS
    Button button2;
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
    private AppAddMethodSingleton() {}
    
    /**
     * A static accessor method for getting the singleton object.
     * 
     * @return The one singleton dialog of this object type.
     */
    public static AppAddMethodSingleton getSingleton() {
	if (singleton == null)
	    singleton = new AppAddMethodSingleton();
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
        closeButton.setOnAction(e->{ AppAddMethodSingleton.this.close(); });

        // WE'LL PUT EVERYTHING HERE
        messagePane = new VBox(5);
        messagePane.setAlignment(Pos.CENTER);
        
        
        // HERE I WILL ADD CUSTOM FIELDS
        label1 = new Label("Name");
        label2 = new Label("Return Type");
        label3 = new Label("isStatic");
        label4 = new Label("isAbstract");
        label5 = new Label("Access");
        label6 = new Label("Add the arguments =>>");
        
        
        textField1 = new TextField();
        textField2 = new TextField();
        checkBox1 = new CheckBox();
        checkBox2 = new CheckBox();
        textField5 = new TextField();
        
        if(!selectedDiagram.getClassType()){
            checkBox2.setDisable(true);
        }
        
        textField6 = new TextField();
        textField7 = new TextField();
        hbox1 = new HBox();
        vbox1 = new VBox();
        label7 = new Label("Varaible Type                          ");
        label8 = new Label("Variable Name");
        hbox2 = new HBox();
        hbox2.getChildren().addAll(label7,label8);
        hbox1.getChildren().addAll(textField6,textField7);
        
        vbox1.getChildren().addAll(hbox2,hbox1);
                
        button2 = new Button("Add Argument");
        button2.setOnAction(e ->{
            TextField temp1 = new TextField();
            TextField temp2 = new TextField();
            HBox temp3 = new HBox();
            temp3.getChildren().addAll(temp1,temp2);
            vbox1.getChildren().add(temp3);
        });
        
        
        button1 = new Button("ADD");
        button1.setOnAction(e ->{
            
            String name = textField1.getText();
            String returnType = textField2.getText();
            
            boolean isStatic;
            if(checkBox1.isSelected()){
                isStatic = true;
            }else{
                isStatic = false;
            }
            
            // isFinal is isAbstract
            boolean isFinal;
            if(checkBox2.isSelected()){
                isFinal = true;
            }else{
                isFinal = false;
            }
            
            String access = textField5.getText();
            if(access.trim().equals("")){
                access = "public";
            }
                        
            ArrayList<ArgumentObject> args = new ArrayList<ArgumentObject>();
            
            //TO  MAKE THE LIST OF ARGUMENT TO ADD IN METHOD
            for(Node box: vbox1.getChildren()){
                if(((HBox)box).getChildren().get(0) instanceof TextField){
                String argType = ((TextField)((HBox)box).getChildren().get(0)).getText();
                String argName = ((TextField)((HBox)box).getChildren().get(1)).getText();
             
                if(argType.trim().equals("") || argName.trim().equals("")){

                }else{
                    ArgumentObject arg = new ArgumentObject(argType, argName);
                    args.add(arg);
                }
                }
            }
            
            ////////////////
            // THIS IS TO CREATE METHOD OBJECT
            MethodsObject method ;
                    
            if(selectedDiagram.getClassType()){
                method = new MethodsObject(name,returnType,isStatic,isFinal,access,args);
            }else{
                method = new MethodsObject(name,returnType,isStatic,true,access,args); 
            }
            
            if(name.trim().equals("") || returnType.trim().equals("")){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Incorret Input");
                alert.setHeaderText("One of the entered names is invalid. Recheck all entered names!!");
                String s ="Enter valid values.";
                alert.setContentText(s);
                alert.show();
            }else{
                // TO RENDER LINES FOR ARGUMENTS  
                if(args.size()!=0){
                    
                    
                    for(ArgumentObject arg: args){
                        
                        String argName = arg.getName();
                        boolean isLocal = false;
                        ClassDiagram dig = null;

                        for(ClassDiagram diagram: pageEditController.diagramsList){
                            if(diagram.getClassName().equals(arg.getName())){
                                isLocal = true;
                                dig = diagram;
                            }
                        }
                        
                        // RENDER LINE
                        if(isLocal){
                            MethodLines line = new MethodLines(pageEditController.selectedDiagram,dig,pageEditController.selectedDiagram.getRootContainer(),dig.getRootContainer(),space);
                            // ADD THE RESPECTIVE LINES TO THEIR RESPECTIVE BOXES AND CLASS DIAGRAMS
                            dig.addComingInLine(line);
                            selectedDiagram.addGoingOutLine(line);
                        }else{
                                                                                    
                            DataManager dataManager = new DataManager();
                            ForeignClassBox box  = null;

                            if(dataManager.isLocalBox(returnType) != null){
                                box =  dataManager.isLocalBox(arg.getDataType());                   
                            }else{
                                box = new ForeignClassBox(arg.getDataType(),space);
                                // TO AVOID CREATING SAME BOXES AGAIN AND AGAIN
                                foreignBoxesPresent.add(box);
                            }
                            
                            MethodLines line = new MethodLines(pageEditController.selectedDiagram,box,pageEditController.selectedDiagram.getRootContainer(),box.getVBox(),space);
                            // ADD THERESPECTIVE LINES TO THEIR RESPECTIVE BOXES AND CLASS DIAGRAMS
                            box.addComingInLine(line);
                            selectedDiagram.addGoingOutLine(line);
                        }
                    }
                }
            
                // TO RENDER LINES ================ SHOULD HAVE MADE A METHOD FOR THIS
                // THIS IS JUST TO ADD CONNECTOR LINE FOR  RETURN TYPE OF METHOD
                if(!(returnType .equals("") || returnType .equals("null")||returnType.equals("byte")
                        ||returnType.trim().equals("short")||returnType.equals("int")||returnType.equals("long")
                        ||returnType.equals("float")||returnType.equals("double")
                        ||returnType.equals("boolean")||returnType.equals("char"))){

                    boolean isLocal = false;
                    ClassDiagram tempDiagram = null;

                    for(ClassDiagram dig: pageEditController.diagramsList){
                        if(dig.getClassName().equals(returnType)){
                            isLocal = true;
                            tempDiagram = dig;
                        }
                    }

                    if(isLocal){
                        MethodLines line = new MethodLines(pageEditController.selectedDiagram,tempDiagram,pageEditController.selectedDiagram.getRootContainer(),tempDiagram.getRootContainer(),space);
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

                        MethodLines line = new MethodLines(pageEditController.selectedDiagram,box,pageEditController.selectedDiagram.getRootContainer(),box.getVBox(),space);
                        // ADD THERESPECTIVE LINES TO THEIR RESPECTIVE BOXES AND CLASS DIAGRAMS
                        box.addComingInLine(line);
                        selectedDiagram.addGoingOutLine(line);
                    }
                }
                // THIS WILL ADD THE VARIABLE TO THE DIAGRAM
                pageEditController.selectedDiagram.addMethod(method);
                AppAddMethodSingleton.this.close();
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
        grid.add(label6,1,6);
        grid.add(vbox1,1,7);
        grid.add(button2,2,6);
        grid.add(button1,1,8);
        
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