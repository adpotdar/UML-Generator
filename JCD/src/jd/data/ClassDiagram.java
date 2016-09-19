package jd.data;

import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import static jd.controller.pageEditController.isSelecting;
import static jd.controller.pageEditController.selectedDiagram;
import jd.gui.Workspace;
import jd.lines.InheritanceLines;
import jd.lines.MethodLines;
import jd.lines.UseLines;
import jf.AppTemplate;
import static jf.components.AppStyleArbiter.CLASS_DIAGRAM_VBOX;
import static jf.components.AppStyleArbiter.DIAGRAM_VBOX_SELECTED;

public class ClassDiagram extends Diagram{
        
    public Text className;
    public String classPackage = "";
    public String classParent  = "" ;
    
    // TRUE IF DIAGRAM IS CLASS AND FALSE IF THE DIAGRAM IS INTERFACE
    public  boolean classType;
    
    // NUMBER OF CLASS DIAGRAMS PRESENT
    private static int counter=0;
    
    AppTemplate app;
    
    // COORDINATES OF THE CLASS DIAGRAM
    double x;
    double y;
    
    // GUI OF CLASS DIAGRAM
    VBox rootContainer;
    
    // THIS IS FOR THE ONLY PARENT
    InheritanceLines parentLine = null;
    
    public void setParentLine(InheritanceLines line){
        parentLine = line;
    }
    
    public InheritanceLines getParentLine(){
        return parentLine;
    }
    
    //  INNER VBOXES
    VBox one;
    VBox two;
    VBox three;
    
    ArrayList<String> localInterfaces;
    ArrayList<String> externalInterfaces;
    ArrayList<String> importedPackages;
    
    // LINES WHICH WILL BE USED FOR RESIZING
    Line leftLine;
    Line rightLine;
    
    // LIST OF METHODS
    ArrayList<MethodsObject> methodsList;
    
    // LIST OF VARIABLES
    ArrayList<VariablesObject> variablesList;
    
    // TO KEEP TRACK OF LINES
    ArrayList<InheritanceLines> inheritanceLinesComingIn = new ArrayList<InheritanceLines>();
    ArrayList<InheritanceLines> inheritanceLinesGoingOut = new ArrayList<InheritanceLines>();
    
    ArrayList<MethodLines> methodLinesComingIn = new ArrayList<MethodLines>();
    ArrayList<MethodLines> methodLinesGoingOut = new ArrayList<MethodLines>();
 
    ArrayList<UseLines> useLinesComingIn = new ArrayList<UseLines>();
    ArrayList<UseLines> useLinesGoingOut    = new ArrayList<UseLines>();

    // CONSTRUCTOR 1
    public ClassDiagram(Pane pane, boolean isClass){
       
        // DEFAULT NAME
        className = new Text("name"+counter++);  
        
        // INIT CLASS TYPE
        classType = isClass; 
                
        // INIT THE METHODS LIST
        methodsList = new ArrayList<MethodsObject>();
        
        // INIT VARIABLES LIST
        variablesList = new ArrayList<VariablesObject>();
        
        //INIT THE INTERFACES BEING IMPLEMENTED LIST
        localInterfaces = new ArrayList<String>();
        externalInterfaces = new ArrayList<String>();
        importedPackages = new ArrayList<String>();
        
        
        // INIT ALL VBOXES
        rootContainer = new VBox(5);
        one = new VBox();
        two = new VBox();
        three = new VBox();
 
        one.getChildren().add(className);
        
        // MAKE THE FULL DIAGRAM
        if(classType){
            rootContainer.getChildren().addAll(one,two,three);
        }else{
            Label intLabel = new Label("<<interface>>");
            rootContainer.getChildren().addAll(intLabel,one,two,three);
        }
        // ADD THE LINES TO THE DIAGRAM
        leftLine = new Line();
        rightLine = new Line();

        
        setFeatures();
         
        // ADD THE ROOT CONTAINER TO space
        pane.getChildren().add(rootContainer);
        pane.getChildren().add(leftLine);
        pane.getChildren().add(rightLine);
        
        // SET THE DEFAULT POSITOIN OF DIAGRAM
        rootContainer.setLayoutX(100);
        rootContainer.setLayoutY(100);         
        
        
        // SET THE STYLE OF VBOX more like ADD BORDER
        initStyle();
        
    }
    
    // CONSTRUCTOR 2
    public ClassDiagram(Pane pane, double x, double y,boolean isClass) {
       // DEFAULT NAME
        className = new Text("name"+counter++);  
        
        // INIT CLASS TYPE
        classType = isClass;  
        
        // INIT THE METHODS LIST
        methodsList = new ArrayList<MethodsObject>();
        
        // INIT VARIABLES LIST
        variablesList = new ArrayList<VariablesObject>();
        
        //INIT THE INTERFACES BEING IMPLEMENTED LIST
        localInterfaces = new ArrayList<String>();
        externalInterfaces = new ArrayList<String>();
        importedPackages = new ArrayList<String>();
        
        // INIT ALL VBOXES
        rootContainer = new VBox(5);
        one = new VBox();
        two = new VBox();
        three = new VBox();
 
        one.getChildren().add(className);
        
        // MAKE THE FULL DIAGRAM
        rootContainer.getChildren().addAll(one,two,three);
        
         // ADD THE LINES TO THE DIAGRAM
        leftLine = new Line();
        rightLine = new Line();
        
        setFeatures();
        
        pane.getChildren().add(rootContainer);
        pane.getChildren().add(leftLine);
        pane.getChildren().add(rightLine);
        
        // SET THE DEFAULT POSITOIN OF DIAGRAM
        rootContainer.setLayoutX(x);
        rootContainer.setLayoutY(y);         
        
        // SET THE STYLE OF VBOX more like ADD BORDER
        initStyle(); 
    }
    
    // GETTERS
    public ArrayList<String> getLocalInterfaces() {
        return localInterfaces;
    }

    public ArrayList<String> getExternalInterfaces() {
        return externalInterfaces;
    }

    
    public ArrayList<String> getImportedPackages() {
        return importedPackages;
    }

    public void setImportedPackages(ArrayList<String> s){
        this.importedPackages = s;
    }
    
    public ArrayList<InheritanceLines> getInheritanceLinesComingIn() {
        return inheritanceLinesComingIn;
    }

    public ArrayList<InheritanceLines> getInheritanceLinesGoingOut() {
        return inheritanceLinesGoingOut;
    }

    public ArrayList<MethodLines> getMethodLinesComingIn() {
        return methodLinesComingIn;
    }

    public ArrayList<MethodLines> getMethodLinesGoingOut() {
        return methodLinesGoingOut;
    }

    public ArrayList<UseLines> getUseLinesComingIn() {
        return useLinesComingIn;
    }

    public ArrayList<UseLines> getUseLinesGoingOut() {
        return useLinesGoingOut;
    }
    
    // ADD A CONNECTOR GOING IN
    public void addComingInLine(Line line){
        if(line instanceof InheritanceLines){
            inheritanceLinesComingIn.add((InheritanceLines)line);
        }else if(line instanceof MethodLines){
            methodLinesComingIn.add((MethodLines)line);
        }else if(line instanceof UseLines){
            useLinesComingIn.add((UseLines)line);
        }
    }
    
    // ADD A CONNECTOR GOING OUT
    public void addGoingOutLine(Line line){
        if(line instanceof InheritanceLines){
            inheritanceLinesGoingOut.add((InheritanceLines)line);
        }else if(line instanceof MethodLines){
            methodLinesGoingOut.add((MethodLines)line);
        }else if(line instanceof UseLines){
            useLinesGoingOut.add((UseLines)line);
        }
    }
        
    // SET THE FEATURES OF THE CLASS DIAGRAM
    public void setFeatures(){
        // SET FEATURES OF ONE
        one.setMinHeight(50);
        one.setMinWidth(100);
        
        // SET FEATURES OF TWO
        two.setMinHeight(50);
        two.setMinWidth(100);
        
        // SET FEATURES OF THREE
        three.setMinHeight(50);
        three.setMinWidth(100);
        
        // BINDING THE LEFT LINE
        leftLine.startXProperty().bind(rootContainer.layoutXProperty());
        leftLine.startYProperty().bind(rootContainer.layoutYProperty());

        leftLine.endXProperty().bind(leftLine.startXProperty());
        leftLine.endYProperty().bind(leftLine.startYProperty().add(rootContainer.heightProperty()));

        leftLine.setStroke(Color.WHITE);
        leftLine.setStrokeWidth(3);
                                             
        // BINDING THE RIGHT LINE
        rightLine.startXProperty().bind(rootContainer.layoutXProperty().add(rootContainer.widthProperty()));
        rightLine.startYProperty().bind(rootContainer.layoutYProperty());

        rightLine.endXProperty().bind(rightLine.startXProperty());
        rightLine.endYProperty().bind(rightLine.startYProperty().add(rootContainer.heightProperty()));

        rightLine.setStroke(Color.WHITE);
        rightLine.setStrokeWidth(3);
        
        // BINDING THE INNER VBOXES TO THE ROOT CONTAINER
        one.minWidthProperty().bind(rootContainer.minWidthProperty());
        one.maxWidthProperty().bind(rootContainer.maxWidthProperty());
        one.prefWidthProperty().bind(rootContainer.prefWidthProperty());

        three.minWidthProperty().bind(rootContainer.minWidthProperty());
        two.maxWidthProperty().bind(rootContainer.maxWidthProperty());
        two.prefWidthProperty().bind(rootContainer.prefWidthProperty());
        
        one.minWidthProperty().bind(rootContainer.minWidthProperty());
        one.maxWidthProperty().bind(rootContainer.maxWidthProperty());
        one.prefWidthProperty().bind(rootContainer.prefWidthProperty());
        
    }
    // INIT THE CONTRUCTOR FOR TEST SAVE
    

    // TO ADD VARIABLE TO THE CLASS
    public void addVariable(VariablesObject var){
        variablesList.add(var);
        Text temp = new Text(var.toString());
        two.getChildren().add(temp);
    }
    // TO ADD METHOD TO THE CLASS
    public void addMethod(MethodsObject method){
        methodsList.add(method);
        Text temp = new Text(method.toString());
        three.getChildren().add(temp);
    }
    
    // HELPER FOR REMOVING THE VARAINLES IN TWO VBOX WHEN THE 
    // VARIABLES ARE UPDATES
    public void updateTwo(ArrayList<VariablesObject> li){
        two.getChildren().clear();
        for(VariablesObject var: li){
            Text temp = new Text(var.toString());
            two.getChildren().add(temp);
        }
    }
    
    public void updateThree(ArrayList<MethodsObject> li){
        three.getChildren().clear();
        for(MethodsObject var: li){
            Text temp = new Text(var.toString());
            three.getChildren().add(temp);
        }
    }
    
    // GETTERS GETTERS
    public boolean getClassType() {
        return classType;
    }
    
    public ArrayList<MethodsObject> getMethodsList() {
        return methodsList;
    }
    public ArrayList<VariablesObject> getVariablesList() {
        return variablesList;
    }
    public void setClassType(boolean classType) {
        this.classType = classType;
    }
    // RETURNS classPackage
    public String getPackage(){
        return classPackage;
    } 
    // RETURNS classParent
    public String getClassParent(){
        return classParent;
    }
    // GET rootContainer
    public VBox getRootContainer(){
        return rootContainer;
    }
    
    // THIS IS A HELPER METHOD FOR ADDING LOCAL INTERFACES DIALOG
    public void addALocalInterface(String s){
        boolean present =false;
        // CHECK IF THE NAME ALREDY EXISTS
        for(String temp: localInterfaces){
            if(temp.equals(s)){
                present = true;
            }
        }
        
        if(!present){
            localInterfaces.add(s);
        }
    }
    public void setLocalInterfaces(ArrayList<String> localInterfaces) {
        this.localInterfaces = localInterfaces;
    }

    public void setExternalInterfaces(ArrayList<String> externalInterfaces) {
        this.externalInterfaces = externalInterfaces;
    }
    
    //  RETURNS className
    public String getClassName(){
       return className.getText();
    }
    // RETURNS leftLine
    public Line getLeftLine() {
        return leftLine;
    }
    // RETURNS rightLine
    public Line getRightLine() {
        return rightLine;
    }
    
    // SET className
    public void setClassName(String s){
        className.setText(s);
    }
    
    public void setMethodsList(ArrayList<MethodsObject> methodsList) {
        this.methodsList = methodsList;
    }

    public void setVariablesList(ArrayList<VariablesObject> variablesList) {
        this.variablesList = variablesList;
    }
    
    // SET classParent
    public void setParentName(String s){
        classParent = s;
    }
    
    // SETS classPackage
    public void setPackageName(String s){
        classPackage = s;
    }
      
    
    public void initStyle(){
        one.getStyleClass().add(CLASS_DIAGRAM_VBOX);
        two.getStyleClass().add(CLASS_DIAGRAM_VBOX);
        three.getStyleClass().add(CLASS_DIAGRAM_VBOX);
        rootContainer.getStyleClass().add(CLASS_DIAGRAM_VBOX);
    }
    
    // HELPER FOR REMOVING VARIABLES
    public ArrayList<String> getVariableNames(){
        
        ArrayList<String> ar = new ArrayList<String>();
        for(VariablesObject var: getVariablesList()){
            ar.add(var.getName());
        }
        return ar;
    }
   
    // HELPER FOR REMOVING METHODS
    public ArrayList<String> getMethodNames(){
        ArrayList<String> ar = new ArrayList<String>();
        for(MethodsObject method: getMethodsList()){
            ar.add(method.getName());
        }
        return ar;
    }
    
    // CONVERTS THE CLASS DIAGRAM INTO EXECUTABLE CODE
    public String toCode(){
        
        String classType = "class";
        if(!getClassType() == true){
            classType = " interface ";
        }
        
        String header="";
                
        for(String s: getImportedPackages()){
            header += "import "+s+" ;\n";
        }
        
        header += "public "+ classType + " "+ getClassName();
        
        // SET UP EXTENDS
        if(!classParent.equals("")){
            header = header + " extends "+ classParent;
        }
        
        // SET UP IMPLEMENTS
        if(!(localInterfaces.size()==0 && externalInterfaces.size()==0)){
            header = header + " implements ";
            for(String name : localInterfaces){
                header += name+",";
            }
            for(String name: externalInterfaces){
                header += name+",";
            }
            header = header.substring(0, header.length()-1);
        }
        
        header = header+ "{ \n";
        
        String vars = "";
        
        for(VariablesObject var :getVariablesList()){
            vars += var.toCode()+" \n ";
        }
        
        String mets ="";
        for(MethodsObject met : getMethodsList()){
            mets += met.toCode();
        }
     
        header = header+vars+mets+"\n}";
        
        return header;
    }
    
    // HELPER FOR SNAPPING
    public void setPosition(double layX,double layY){
        rootContainer.setLayoutX(layX);
        rootContainer.setLayoutY(layY);
    }
    
    // MAKE SURE IT WORKS
    // HELPER FOR REMOVING DIAGRAM
    public void removeLineFromParentHalf(Line line){
        if(line instanceof InheritanceLines){
            this.getInheritanceLinesComingIn().remove(line);
        }else if(line instanceof MethodLines){
            this.getMethodLinesComingIn().remove(line);
        }else if(line instanceof UseLines){
            this.getUseLinesComingIn().remove(line);
        }
    }
    
    // HELPER FOR REMOVING DIAGRAM
    public void removeLineFromChildHalf(Line line){
        if(line instanceof InheritanceLines){
            this.getInheritanceLinesGoingOut().remove(line);
        }else if(line instanceof MethodLines){
            this.getMethodLinesGoingOut().remove(line);
        }else if(line instanceof UseLines){
            this.getUseLinesGoingOut().remove(line);
        }
    }
    
    
}
