package jd.data;

import Actions.Action;
import Actions.DragDiagramAction;
import Actions.ResizeLeftAction;
import Actions.ResizeRightAction;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Stack;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;
import static jd.controller.pageEditController.diagramsList;
import static jd.controller.pageEditController.isResizing;
import static jd.controller.pageEditController.isSelecting;
import static jd.controller.pageEditController.selectedDiagram;
import jd.gui.Workspace;
import static jd.gui.Workspace.space;
 import jf.components.AppDataComponent;
import jf.AppTemplate;
import static jf.components.AppStyleArbiter.DIAGRAM_VBOX_SELECTED;

/**
 * This class serves as the data management component for this application.
 */
public class DataManager implements AppDataComponent {
    // THIS IS A SHARED REFERENCE TO THE APPLICATION
    AppTemplate app;

    // KEEPS TRACK OF PACKAGES ADDED
    ArrayList<String> packageNames = new ArrayList<String>();
    
    // STACK FOR UNDO
    Stack<Action> undoStack = new Stack<Action>();
    // STACK FOR REDO
    Stack<Action> redoStack = new Stack<Action>();
    
    // LIST FOR ALL THE INTERFACES PRESENT
    public static ArrayList<String>  interfacesPresent = new ArrayList<String>();
    
    // LIST OF ALL FOREIGN BOXES
    public static ArrayList<ForeignClassBox> foreignBoxesPresent = new ArrayList<ForeignClassBox>();
    
    // DRAG VALUE FOR X
    double dragDelX;
    
    // DRAG VALUE FOR Y
    double dragDelY;
    
    // FOR UNDO USE
    private boolean clicked = true;
    
    // THIS WILL UPDATE THE VARIABLES TABLE
    ObservableList<VariablesObject> variables;
    
    // THIS WILL UPDATE THE METHOD TABLE
    ObservableList<MethodsObject> methods;
    
    // HELPER FOR INTERFACE HANDLING
    public void addToInterfacesList(String name){
        interfacesPresent.add(name);
    }
    
    // HELPER FOR INTERFACE HANDLING
    public void removeFromInterfacesList(String name){
        ArrayList<String> tempList = interfacesPresent;
        for(String tempName: interfacesPresent){
            if(name.equals(tempName)){
                tempList.remove(tempName);
            }
        }
        interfacesPresent = tempList;
    }
    
     /**
     * THis constructor creates the data manager and sets up the
     *
     *
     * @param initApp The application within which this data manager is serving.
     */
    public DataManager(AppTemplate initApp) throws Exception {
	// KEEP THE APP FOR LATER
	app = initApp;
    }

    public DataManager() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * This function clears out the HTML tree and reloads it with the minimal
     * tags, like html, head, and body such that the user can begin editing a
     * page.
     */
    @Override
    public void reset() {
        space.getChildren().clear();
    }
    
    // ADDS TO PACKAGE LIST IF THE PACKAGE IS NOT ALREADY PRESENT
    public void addToPackageList(String name){
        packageNames.add(name);
    }
    
    // ADD THE NEWLY ADDED PACKAGE TO THE LIST OF PACKAGES
    public void addToPackageList(String oldName,String newName){
        for(int i=0;i<packageNames.size();i++){
            String s = packageNames.get(i);
            if(s.equals(oldName)){
                packageNames.set(i, newName);
            }
        }
    }
    
    // EXPORT TO EXECUTABLE CODE
    public void exportToCode(Window window){
        
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Export the UML diagram in java code");
        
        File file = directoryChooser.showDialog(window);
        
        ArrayList<String> distinctPackages = getDifferentPackages();
        for(String packageName: distinctPackages){
            
            // THIS LIST CONTAINS THE LIST OF CLASSES WITH SAME PACKAGE NAME
            ArrayList<ClassDiagram> classesWithSamePackages = findClassByPackage(packageName);
            
            // SMART MOVE
            packageName = packageName.replace(".", "/");
            
            File directory = new File(file.getPath() + "/src/" + packageName);
            
            // MAKE THE DIRECTORY , IF THE DIRECTORY ALREADY EXISTS IT
            // WILL OVERWRITE IT
            directory.mkdirs();
            
            for(ClassDiagram temp : classesWithSamePackages){
                
                File javaFile = new File( directory ,temp.getClassName() + ".java");
                    
                try {
                    
                    PrintWriter writer = new PrintWriter(javaFile.getPath(), "UTF-8");
                    writer.write(temp.toCode());
                    writer.close();
                    
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                } 
            }
        }
    }

    // GET LIST OF DIFFERENT PACKAGES PRESENT IN THE UML DIAGRAM
    private ArrayList<String> getDifferentPackages() {          
          
        ArrayList<String> diff = new ArrayList<String>();
        boolean present = false;
        for(String s: packageNames){
            present = false;
            for(String d: diff){
                if(s.equals(d)){
                    present = true;
                }
            }
            if(!present){
                diff.add(s);
            }
        }
        return diff;
    }

    // FIND CLASSES WHICH SHARE SAME PACKAGE NAME
    public ArrayList<ClassDiagram> findClassByPackage(String name){
       ArrayList<ClassDiagram> samePackage = new ArrayList<ClassDiagram>();
        
       for(ClassDiagram d: diagramsList){
           if(d.getPackage().equals(name)){
               samePackage.add(d);
           }
       }
       return samePackage;
    }
    
    // ADD THE EVENT HANDLERS TO THE CLASS DIAGRAM
    public void addEventHandler(ClassDiagram diagram){
        Workspace work = (Workspace) app.getWorkspaceComponent();
        
        // ADD MOUSE PRESS EVENTS
        diagram.getRootContainer().setOnMousePressed( new EventHandler<MouseEvent>(){
            
            @Override
            public  void handle(MouseEvent me){
            dragDelX = diagram.getRootContainer().getLayoutX()-me.getSceneX();
            dragDelY = diagram.getRootContainer().getLayoutY()-me.getSceneY();
            
                if(isSelecting){
                    // TO UPDATE THE CLASS NAME AND PACKAGE FIELD
                    work.getClassTextField().setText(diagram.getClassName());
                    work.getPackageTextField().setText(diagram.getPackage());
                    work.getParentTextField().setText(diagram.getClassParent());
                    
                    variables = FXCollections.observableArrayList(diagram.getVariablesList());
                    work.fillVarTable(variables);
                    
                    methods = FXCollections.observableArrayList(diagram.getMethodsList());
                    work.fillMethodTable(methods);
                    
                    // THIS IS FOR THE FIRST TIME WHEN selectedDiagram IS NULL
                    if(selectedDiagram != null){
                        selectedDiagram.getRootContainer().getStyleClass().remove(DIAGRAM_VBOX_SELECTED);
                    }
                     
                    // selectedDiagram = NEW CREATED DIAGRAM
                    selectedDiagram = diagram;
                    
                    // NOW TO MAKE THE CLICKED DIAGRAM SELECTED
                    selectedDiagram.getRootContainer().getStyleClass().add(DIAGRAM_VBOX_SELECTED);
                }
            }
        });
        
        // TO MAKE THE OBJECT DRAGGABLE WE WILL SET EVENT HANDLER ON DRAG EVENT
        diagram.getRootContainer().setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me){
                
                // FOR UNDO
                if(clicked){
                    ClassDiagram d = selectedDiagram;
                    DragDiagramAction action = new DragDiagramAction(d.getRootContainer().getLayoutX(), d.getRootContainer().getLayoutY(), selectedDiagram);
                    pushOnStack(action);
                    clicked =false;
                }
                
                // FOR DRAGGING THE DIAGRAM
                if(isSelecting){
                    diagram.getRootContainer().setLayoutX(dragDelX+me.getSceneX());
                    diagram.getRootContainer().setLayoutY(dragDelY+me.getSceneY());
                }
                
                diagram.getRootContainer().setOnMouseReleased(e ->{
                    clicked = true;
                    // STORE THE FINAL POSITION FOR REDO
                    ((DragDiagramAction)undoStack.peek()).setFinalPosition(diagram.getRootContainer().getLayoutX(), diagram.getRootContainer().getLayoutY());
                 });
            }
        });
        
        // TO CHANGE MOUSE OF HOVER OVER LINE
        diagram.getLeftLine().setOnMouseEntered(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent me){
                if(isResizing){
                    space.setCursor(Cursor.H_RESIZE);
                }
            }
        });
        
        diagram.getLeftLine().setOnMouseExited(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent me){
                if(isResizing){
                     space.setCursor(Cursor.DEFAULT);
                }
            }
        });
        
        diagram.getLeftLine().setOnMouseDragged(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent me){
                
                if(isResizing){
                    
                    // FOR UNDO
                    if(clicked){
                      ResizeLeftAction action = new ResizeLeftAction(selectedDiagram);
                      pushOnStack(action);
                      clicked =false;
                    }
                    // FOR RESIZE LEFT
                    diagram.getRootContainer().setPrefWidth(diagram.getRootContainer().getLayoutX()+diagram.getRootContainer().getWidth()-me.getX());
                    diagram.getRootContainer().setLayoutX(me.getX());
                }
                
                diagram.getLeftLine().setOnMouseReleased(e ->{
                    clicked = true;
                    ((ResizeLeftAction)undoStack.peek()).setFinals(diagram.getRootContainer().getLayoutX(),
                            diagram.getRootContainer().getLayoutY(), diagram.getRootContainer().getPrefWidth());
                });
            }
        });
        
        // TO CHANGE MOUSE OF HOVER OVER LINE
        diagram.getRightLine().setOnMouseEntered(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent me){
                if(isResizing){
                    space.setCursor(Cursor.H_RESIZE);
                }
            }
        });
        
        diagram.getRightLine().setOnMouseExited(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent me){
                if(isResizing){
                    space.setCursor(Cursor.DEFAULT);
                }
            }
        });
        
        diagram.getRightLine().setOnMouseDragged(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent me){
                
                if(isResizing){
                    // FOR UNDO
                    if(clicked){
                     ResizeRightAction action = new ResizeRightAction(selectedDiagram);
                     pushOnStack(action);
                     clicked =false;
                    }
                    // FOR RESIZING
                    diagram.getRootContainer().setPrefWidth(me.getX()-diagram.getRootContainer().getLayoutX());
                }
                
                diagram.getRightLine().setOnMouseReleased(e ->{
                    clicked = true;
                    ((ResizeRightAction)undoStack.peek()).setFinals(diagram.getRootContainer().getPrefWidth());
                });
            }
        });
        
    } 
    
    
    public void pushOnStack(Action action){
        undoStack.push(action);
    }
    
    // UNDO
    public void undoAction(){
        
        Action action = undoStack.pop();
        
        if(action instanceof DragDiagramAction){
             ((DragDiagramAction)action).undo();
        }else if (action instanceof ResizeLeftAction){
            ((ResizeLeftAction)action).undo();
        }else if (action instanceof ResizeRightAction){
            ((ResizeRightAction)action).undo();
        }
        
        redoStack.push(action);
        
    }
    
    // REDO
    public void redoAction(){
        
         Action action = redoStack.pop();
         
        if(action instanceof DragDiagramAction){
            ((DragDiagramAction)action).redo( );
        }else if (action instanceof ResizeLeftAction){
            ((ResizeLeftAction)action).redo();
        }else if (action instanceof ResizeRightAction){
            ((ResizeRightAction)action).redo();
        }
    }
    
    //  CHECK IF THE BOX IS LOCAL
    public ForeignClassBox isLocalBox(String name){
        
        boolean local = false;
        ForeignClassBox tempBox = null;

        for(ForeignClassBox ox: foreignBoxesPresent){
            if(ox.getName().equals(name)){
                local = true;
                tempBox = ox;
            }
        }
        return tempBox;
    }
}
