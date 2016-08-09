package jd.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.stage.DirectoryChooser;
import javax.imageio.ImageIO;
import jd.data.ClassDiagram;
import jd.data.DataManager;
import static jd.data.DataManager.interfacesPresent;
import jd.data.ForeignClassBox;
import jd.data.VariablesObject;
import jd.gui.Workspace;
import static jd.gui.Workspace.space;
import jd.lines.GridLines;
import jd.lines.InheritanceLines;
import jd.lines.MethodLines;
import jd.lines.UseLines;
import jf.AppTemplate;
import static jf.components.AppStyleArbiter.CLASS_DIAGRAM_VBOX;
import static jf.components.AppStyleArbiter.DIAGRAM_VBOX_SELECTED;

public class pageEditController {
    
    // THIS REPRESENTS THE SELECTED DIAGRAM
    public static ClassDiagram selectedDiagram;
    
    // IS SELECTING OR NOT
    public static boolean isSelecting;         
    
    // TO KEEP TRACK LIST OF ADDED CLASS DIAGRAMS
    public static ArrayList<ClassDiagram> diagramsList = new ArrayList<ClassDiagram>();
    
    // IS RESIZING ?
    public static boolean isResizing;
    
    // FOR CALLING METHODS IN OTHER CLASSES
    AppTemplate app;
    DataManager dataManager;
    
    public  pageEditController(AppTemplate initApp){
        app = initApp;
        
        selectedDiagram = null;
    }
   
    // ADD THE DIAGRAM(BOX) TO PANE
    public void handleAddClassRequest(Pane pane){
        
        // TO MAKE SURE THAT YOU CAN SELECT
        isSelecting = false;
                
        // INIT DATA MANAGER OBJECT
        dataManager = (DataManager) app.getDataComponent();
        
        // CREATE A NEW CLASS DIAGRAM OBJECT
        ClassDiagram temp = new ClassDiagram(pane,true);
     
        // ADD THE CLASS DIAGRAM TO THE LIST
        diagramsList.add(temp);
        
        // ADD THE EVENT HANDLER OF 
        dataManager.addEventHandler(temp);
        
        // ADD THE PACKAGE TO LIST OF PACKAGES
        dataManager.addToPackageList(temp.getPackage());
 
    }
    
    // ADD THE INTERFACE DIAGRAM(BOX) TO THE PANE
    public void handleAddInterfaceRequest(Pane pane){
        
        // TO MAKE SURE THAT YOU CAN SELECT
        isSelecting = false;
        
        // INIT DATA MANAGER OBJECT
        dataManager = (DataManager) app.getDataComponent();
        
        // CREATE A NEW CLASS DIAGRAM OBJECT
        ClassDiagram temp = new ClassDiagram(pane,false);
     
        // TO ADD INTERFACE TO LIST OF INTERFACES PRESENT ON THE CANVAS
        dataManager.addToInterfacesList(temp.getClassName());
        
        // ADD THE CLASS DIAGRAM TO THE LIST
        diagramsList.add(temp);
        
        // ADD THE EVENT HANDLER OF 
        dataManager.addEventHandler(temp);
        
        // ADD THE PACKAGE TO LIST OF PACKAGES
        dataManager.addToPackageList(temp.getPackage());
    }
    
    // MAKE SELECTING POSSIBLE
    public void handleSelectRequest(){
        isSelecting = true;
        isResizing = false;
    }
    
    // MAKE RESIZING POSSIBLE
    public void handleResizeRequest(){
        isResizing = true;
    }
    
    //  TO CHANGE THE NAME OF CLASS THROUGH THE TEXT FIELD
    public void handleChangeClassNameRequest(String s){
        if(s.trim().equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorret Input");
            alert.setHeaderText("Class name can't be left empty!!");
            alert.setContentText("Enter a valid parent name.");
            alert.show();
        }else{
            selectedDiagram.setClassName(s);
        }
    }
    
    // TO CHANGE THE PACKAGE NAME THROUGH THE TEXT FIELD
    public void handleChangePackageNameRequest(String s){
        
        if(s.trim().equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorret Input");
            alert.setHeaderText("Package name can't be left empty!!");
            alert.setContentText("Enter a valid parent name.");
            alert.show();
        }else{
            // THIS IS TO ADD THE NEW PACKAGE NAME TO THE LIST AND DELETE THE OLD ONE
            dataManager.addToPackageList(selectedDiagram.getPackage(), s);

            // THIS SETS THE PACKAGE NAME IN CLAS OBJECT
            selectedDiagram.setPackageName(s);        }
        
    }
    
    // HANDLE THE ZOOM IN REQUEST
    public void handleZoomInRequest(Pane pane){
        if(pane.getScaleX()+0.2 > 2 ){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Maximum zoom out Limit reaches");
            alert.setHeaderText("NOW zoom out only!!");
            alert.show();
        }else{
            pane.setScaleX(pane.getScaleX()+0.2);
            pane.setScaleY(pane.getScaleY()+0.2); 
        }
    }
    
    public void handleZoomOutRequest(Pane pane){
        if(pane.getScaleX()-0.2 < 1){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Maximum zoom in Limit reaches");
            alert.setHeaderText("NOW zoom in only");
            alert.show();
        }else{
            pane.setScaleX(pane.getScaleX()-0.2);
            pane.setScaleY(pane.getScaleY()-0.2);
        }
    }
    
    // TO ADD THE GRIDS IN THE BACKGROUND
    public void handleAddGridRequest(Pane pane){
        
        for(double i=0;i<pane.getWidth();i=i+15){
            GridLines line = new GridLines();
            line.setStroke(Color.CADETBLUE);
            pane.getChildren().add(line);
            line.setStartX(i);
            line.setStartY(0);
            line.setEndX(i);
            line.setEndY(pane.getHeight());
        }
        
        for(double i=0;i<pane.getHeight();i+=15){
            GridLines line = new GridLines();
            pane.getChildren().add(line);
            line.setStroke(Color.ALICEBLUE);
            line.setStartX(0);
            line.setStartY(i);
            line.setEndX(pane.getWidth());
            line.setEndY(i);
        }
    }
    //TO REMOVE THE GRIDS
    public void handleRemoveGridRequest(Pane pane){
        
        ArrayList<Line> toRemove = new ArrayList<Line>();
        
        for(Node line : pane.getChildren()){
            if(line instanceof GridLines){
                toRemove.add((Line)line);
            }
        }
        
        pane.getChildren().removeAll(toRemove);
    }
    // FOR SNAPPING ALL THE DIAGRAMS
    public void snapDiagrams(){
        for(ClassDiagram dig: diagramsList){
            double layX = dig.getRootContainer().getLayoutX()-(dig.getRootContainer().getLayoutX()%15);
            double layY = dig.getRootContainer().getLayoutY()-(dig.getRootContainer().getLayoutY()%15);
            dig.setPosition(layX, layY);
        }
    }
    
    // TAKE A SNAPSHOT OF THE UML DIAGRAM AND SAVE IT
    public void handleSnapshotRequest(Pane pane){
        Workspace work  = (Workspace)app.getWorkspaceComponent();
        
        if(selectedDiagram != null){
            selectedDiagram.getRootContainer().getStyleClass().remove("diagram_vbox_selected");
            selectedDiagram.getRootContainer().getStyleClass().add(CLASS_DIAGRAM_VBOX);
        }
        
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle("Save the screeshot");
        File d = dc.showDialog(app.getGUI().getWindow());
        File file = new File(d.getPath()+ "//screenShot.png");
        
        Pane canvas = pane;
        
	WritableImage image = canvas.snapshot(new SnapshotParameters(), null);
	try {
	    ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
	}
	catch(IOException ioe) {
	    ioe.printStackTrace();
	}
    } 
    
    // THIS METHOD REMOVES A CLASS DIAGRAM AND ALL THE CONNECTORS THAT THE CLASS DIAGRAM WAS CONNECTED TO
    public void removeClassDiagram(ClassDiagram diagram,Pane pane){
        
        // REMOVE THE INHERITANCE LINES COMING IN THE DIAGRAM
        for(Line line: diagram.getInheritanceLinesComingIn()){
            
            if(((InheritanceLines)line).getChildDiagram() instanceof ClassDiagram){
                ClassDiagram dig = (ClassDiagram)((InheritanceLines)line).getChildDiagram();
                dig.removeLineFromChildHalf(line);
                
            }else if(((InheritanceLines)line).getChildDiagram() instanceof ForeignClassBox){

            }
            
            pane.getChildren().remove(line);
            pane.getChildren().remove(((InheritanceLines)line).getArrowContainer());
        }
        
        // REMOVE THE INHERITANCE LINES GOING OUT OF THE DIAGRAM
        for(Line line: diagram.getInheritanceLinesGoingOut()){
            
            if(((InheritanceLines)line).getChildDiagram() instanceof ClassDiagram){
                ClassDiagram dig = (ClassDiagram)((InheritanceLines)line).getChildDiagram();
                dig.removeLineFromParentHalf(line);
            }else if(((InheritanceLines)line).getChildDiagram() instanceof ForeignClassBox){
                ForeignClassBox dig = (ForeignClassBox)((InheritanceLines)line).getChildDiagram();
                dig.removeLineFromParentHalf(line);
            }
            pane.getChildren().remove(line);
            pane.getChildren().remove(((InheritanceLines)line).getArrowContainer());
        }
        
        // REMOVE THE METHOD LINES COMING INTO THE CLASS DIAGRAM
        for(Line line: diagram.getMethodLinesComingIn()){
            
            if(((MethodLines)line).getChildDiagram() instanceof ClassDiagram){
                ClassDiagram dig = (ClassDiagram)((MethodLines)line).getChildDiagram();
                dig.removeLineFromChildHalf(line);
            }else if(((MethodLines)line).getChildDiagram() instanceof ForeignClassBox){

            }
            
            pane.getChildren().remove(line);
            pane.getChildren().remove(((MethodLines)line).getArrowContainer());
        }
        
        // REMOVE THE METHOD LINES GOING OUT OF THE CLASS DIAGRAM
        for(Line line: diagram.getMethodLinesGoingOut()){
            if(((MethodLines)line).getChildDiagram() instanceof ClassDiagram){
                ClassDiagram dig = (ClassDiagram)((MethodLines)line).getChildDiagram();
                dig.removeLineFromParentHalf(line);
            }else if(((MethodLines)line).getChildDiagram() instanceof ForeignClassBox){
                ForeignClassBox dig = (ForeignClassBox)((MethodLines)line).getChildDiagram();
                dig.removeLineFromParentHalf(line);
            }
            
            pane.getChildren().remove(line);
            pane.getChildren().remove(((MethodLines)line).getArrowContainer());
        }
        
        // REMOVE THE USELINES COMING IN
        for(Line line: diagram.getUseLinesComingIn()){
            
            if(((UseLines)line).getChildDiagram() instanceof ClassDiagram){
                ClassDiagram dig = (ClassDiagram)((UseLines)line).getChildDiagram();
                dig.removeLineFromChildHalf(line);
            }else if(((UseLines)line).getChildDiagram() instanceof ForeignClassBox){

            }
            
            pane.getChildren().remove(line); 
            pane.getChildren().remove(((UseLines)line).getArrowContainer());
        }
        
        // REMOVE THE USE LINES GOING OUT
        for(Line line: diagram.getUseLinesGoingOut()){
            if(((UseLines)line).getChildDiagram() instanceof ClassDiagram){
                ClassDiagram dig = (ClassDiagram)((UseLines)line).getChildDiagram();
                dig.removeLineFromParentHalf(line);
            }else if(((UseLines)line).getChildDiagram() instanceof ForeignClassBox){
                ForeignClassBox dig = (ForeignClassBox)((UseLines)line).getChildDiagram();
                dig.removeLineFromParentHalf(line);
            }
            
            pane.getChildren().remove(line);
            pane.getChildren().remove(((UseLines)line).getArrowContainer());
        }
        
        // REMOVE THE INTERFACE FROM THE LIST OF INTERFACES
        interfacesPresent.remove(diagram.getClassName());
        
        // REMOVE THE DIAGRAM FROM THE LIST OF DIAGRAMS
        diagramsList.remove(diagram);
        
        // REMOVE THE MAIN CLASS DIAGRAM
        pane.getChildren().remove(diagram.getRootContainer());
        pane.getChildren().remove(diagram.getRightLine());
        pane.getChildren().remove(diagram.getLeftLine());
    }
}

