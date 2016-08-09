package jd.gui;

import javafx.application.Application;
import java.io.IOException;
import static java.lang.reflect.Modifier.isStatic;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.stage.Window;
import static javax.swing.event.DocumentEvent.EventType.REMOVE;
import static jd.PropertyType.ADD_CLASS_ICON;
import static jd.PropertyType.ADD_CLASS_TOOLTIP;
import static jd.PropertyType.ADD_INTERFACE_ICON;
import static jd.PropertyType.ADD_INTERFACE_TOOLTIP;
import static jd.PropertyType.REDO_ICON;
import static jd.PropertyType.REDO_TOOLTIP;
import static jd.PropertyType.REMOVE_ICON;
import static jd.PropertyType.REMOVE_TOOLTIP;
import static jd.PropertyType.RESIZE_ICON;
import static jd.PropertyType.RESIZE_TOOLTIP;
import static jd.PropertyType.SELECT_ICON;
import static jd.PropertyType.SELECT_TOOLTIP;
import static jd.PropertyType.SNAPSHOT_ICON;
import static jd.PropertyType.SNAPSHOT_TOOLTIP;
import static jd.PropertyType.TO_CODE_ICON;
import static jd.PropertyType.TO_CODE_TOOLTIP;
import static jd.PropertyType.UNDO_ICON;
import static jd.PropertyType.UNDO_TOOLTIP;
import static jd.PropertyType.ZOOM_IN_ICON;
import static jd.PropertyType.ZOOM_IN_TOOLTIP;
import static jd.PropertyType.ZOOM_OUT_ICON;
import static jd.PropertyType.ZOOM_OUT_TOOLTIP;
import jd.controller.pageEditController;
import static jd.controller.pageEditController.diagramsList;
import static jd.controller.pageEditController.selectedDiagram;
import jd.data.ClassDiagram;
import jd.data.DataManager;
import static jd.data.DataManager.foreignBoxesPresent;
import jd.data.ForeignClassBox;
import jd.data.MethodsObject;
import jd.data.VariablesObject;
import jd.lines.InheritanceLines;
import jf.ui.AppGUI;
import jf.AppTemplate;
import jf.components.AppWorkspaceComponent;
import static jf.settings.AppStartupConstants.FILE_PROTOCOL;
import static jf.settings.AppStartupConstants.PATH_IMAGES;
import jf.ui.AppGUI;
import properties_manager.PropertiesManager;

/**
 * This class serves as the workspace component for this application, providing
 * the user interface controls for editing work.
 */
public class Workspace extends AppWorkspaceComponent {

    public static double LABEL_WIDTH = 100;
    public static double LABEL_HEIGHT = 30;
    
    public static double LEFT_PANE_WIDTH = 970;
    public static double LEFT_PANE_HEIGHT = 480;
    
//    public static double SPLIT_PANE_WIDTH = 1700;
//    public static double SPLIT_PANE_HEIGHT = 900;
    // HERE'S THE APP
    AppTemplate app;

    // IT KNOWS THE GUI IT IS PLACED INSIDE
    AppGUI gui;

    // TOOLBAR AT THE TOP
    ToolBar toolbar ;
    
    // TO USE PAGE EDIT CONTROLLER METHODS
    pageEditController PageEditController;
    
    // TO USE DATA MANAGER
    DataManager dataManager;
    
    // TOP TOOLBAR BUTTONS
    Button select;
    Button resize;
    Button addClass;
    Button addInterface;
    Button remove;
    Button undo;
    Button redo;
    Button zoomIn;
    Button zoomOut;
    Button toCode;
    Button snapShot;
    
    // FOR ADDING INTERFACES
    Button addLocalInterfaces;
    Button addForeignInterfaces;
    Button addPackages;
    HBox row3_5;
    
    // BIG SPLIT PANE
    SplitPane splitPane;
    
    // SCROLL PANE
    ScrollPane leftPane;
    
    // PANE WHERE WE WILL ADD THE UML SHAPES , IT IS STATIC
    // BECOZ WE USE IT IN LOADING
    public static Pane space;
            
    // RIGHT PANE
    VBox rightPane;
    
    // ROW ONE
    HBox row1;
    Label classLabel;
    TextField classTextField;
    
    //ROW TWO
    HBox row2;
    Label packageLabel;
    TextField packageTextField;
    
    // ROW THREE
    HBox row3;
    Label parentLabel;
    TextField parentTextField;
    
    // ROW 4
    VBox row4;
    HBox row4_1;
    Label varLabel;
    Button varPlus;
    Button varMinus;
    TableView varTable;
    TableColumn<VariablesObject,String> nameCol;
    TableColumn<VariablesObject,String> typeCol;
    TableColumn<VariablesObject,Boolean> staticCol;
    TableColumn<VariablesObject,Boolean> finalCol;
    TableColumn<VariablesObject,String> accessCol;
    ScrollPane varScrollPane;
    
    // ROW 5
    VBox row5;
    HBox row5_1;
    Label methodLabel;
    Button methodPlus;
    Button methodMinus;
    TableView methodTable;
    ScrollPane methodScrollPane;
    
    TableColumn<MethodsObject, String> nameMethodCol;
    TableColumn<MethodsObject, String> returnCol;
    TableColumn<MethodsObject, Boolean> staticMethodCol;
    TableColumn<MethodsObject, Boolean> abstractCol;
    TableColumn<MethodsObject, String> accessMethodCol;
    TableColumn<MethodsObject, String> args;
     
    // CHECK BOX
    VBox checkBoxes;
    
    HBox gridHBox;
    CheckBox gridBox;
    Label gridLabel;
    
    HBox snapHBox;
    CheckBox snapBox;
    Label snapLabel;
    /**
     * Constructor for initializing the workspace, note that this constructor
     * will fully setup the workspace user interface for use.
     *
     * @param initApp The application this workspace is part of.
     *
     * @throws IOException Thrown should there be an error loading application
     * data for setting up the user interface.
     */
    public Workspace(AppTemplate initApp) throws IOException {
	// KEEP THIS FOR LATER
	app = initApp;

	// KEEP THE GUI FOR LATER
	gui = app.getGUI();
        
        // INIT TOOLBAR
        toolbar = new ToolBar();
        
        // INIT DATAMANAGER
        dataManager = (DataManager)app.getDataComponent();
        
        // INIT SPACE
        space = new Pane();
        
        // INIT CHECKBOXES
        checkBoxes = new VBox(2);
        
        gridHBox = new HBox();
        gridBox = new CheckBox();
        gridLabel = new Label("Grid");
        gridHBox.getChildren().addAll(gridBox,gridLabel);
        
        snapHBox = new HBox();
        snapBox = new CheckBox();
        snapLabel = new Label("Snap");
        snapHBox.getChildren().addAll(snapBox,snapLabel);
        
        checkBoxes.getChildren().addAll(gridHBox,snapHBox);
        checkBoxes.setAlignment(Pos.CENTER);
        
        // INIT TOOLBAR BUTTONS
        select = gui.initChildButton(gui.fileToolbarPane, SELECT_ICON.toString(), SELECT_TOOLTIP.toString(), false);
        resize = gui.initChildButton(gui.fileToolbarPane, RESIZE_ICON.toString(), RESIZE_TOOLTIP.toString(), false);
        addClass = gui.initChildButton(gui.fileToolbarPane, ADD_CLASS_ICON.toString(),ADD_CLASS_TOOLTIP.toString(), false);
        addInterface = gui.initChildButton(gui.fileToolbarPane, ADD_INTERFACE_ICON.toString(), ADD_INTERFACE_TOOLTIP.toString(), false);
        remove = gui.initChildButton(gui.fileToolbarPane, REMOVE_ICON.toString(), REMOVE_TOOLTIP.toString(), false);
        undo = gui.initChildButton(gui.fileToolbarPane, UNDO_ICON.toString(), UNDO_TOOLTIP.toString(), false);
        redo = gui.initChildButton(gui.fileToolbarPane, REDO_ICON.toString(), REDO_TOOLTIP.toString(), false);
        zoomIn = gui.initChildButton(gui.fileToolbarPane, ZOOM_IN_ICON.toString(), ZOOM_IN_TOOLTIP.toString(), false);
        zoomOut = gui.initChildButton(gui.fileToolbarPane, ZOOM_OUT_ICON.toString(), ZOOM_OUT_TOOLTIP.toString(), false);
        toCode = gui.initChildButton(gui.fileToolbarPane, TO_CODE_ICON.toString(), TO_CODE_TOOLTIP.toString(), false);
        snapShot = gui.initChildButton(gui.fileToolbarPane, SNAPSHOT_ICON.toString(), SNAPSHOT_TOOLTIP.toString(), false);
        
        // ADD THE CHECKBOXES TO TOOL BAR
        gui.fileToolbarPane.getChildren().add(checkBoxes);
        
        // INIT THE FIRST ROW
        row1 = new HBox();
        classLabel = new Label("Class Name:");
        classLabel.setPrefSize(190,50);
        classTextField = new TextField();
        classTextField.setPrefSize(190,50);
        row1.getChildren().addAll(classLabel,classTextField);
        
        // INIT ROW TWO
        row2 = new HBox();
        packageLabel = new Label("Package Name:");
        packageLabel.setPrefSize(190,50);
        packageTextField = new TextField();
        packageTextField.setPrefSize(190,50);
        row2.getChildren().addAll(packageLabel,packageTextField);
        
        // INIT ROW 3
        row3 = new HBox();
        parentLabel = new Label("Parent");
        parentLabel.setPrefSize(190,50);
        parentTextField = new TextField();
        parentTextField.setPrefSize(190,50);
        row3.getChildren().addAll(parentLabel,parentTextField);
        
        // INIT ROW 3.5
        row3_5 = new HBox(5);
        addLocalInterfaces = new Button("Add Local Interface");
        addForeignInterfaces = new Button("Add Foreign Interface");
        addPackages = new Button("Import Packages");
        row3_5.getChildren().addAll(addLocalInterfaces,addForeignInterfaces,addPackages);
        
        // INIT ROW 4
        row4_1 = new HBox();
        varLabel = new Label("Variables:");
        varLabel.setPrefSize(190,50);
        varPlus = new Button("+");
        varMinus = new Button("-");
        varTable = new TableView<VariablesObject>();
        row4_1.getChildren().addAll(varLabel,varPlus,varMinus);
        
        // INIT ALL COLUMNS OF THE TABLE
        nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory("name"));
        typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory("dataType"));
        staticCol = new TableColumn<>("Static");
        staticCol.setCellValueFactory(new PropertyValueFactory("isStatic"));
        finalCol = new TableColumn<>("Final");
        finalCol.setCellValueFactory(new PropertyValueFactory("isFinal"));
        accessCol = new TableColumn<>("Access");
        accessCol.setCellValueFactory(new PropertyValueFactory("access"));
        
        varTable.getColumns().addAll(nameCol,typeCol,staticCol,finalCol,accessCol);
        
        // INIT ROW 5
        row5_1 = new HBox();
        methodLabel = new Label("Methods");
        methodLabel.setPrefSize(190, 50);
        methodPlus = new Button("+");
        methodMinus = new Button("-");
        methodTable = new TableView();
        row5_1.getChildren().addAll(methodLabel,methodPlus,methodMinus);
        
            // INIT ALL COLUMNS OF ROW 5
        nameMethodCol = new TableColumn<>("Name");
        nameMethodCol.setCellValueFactory(new PropertyValueFactory("name"));
        returnCol = new TableColumn<>("Return");
        returnCol.setCellValueFactory(new PropertyValueFactory("returnType"));
        staticMethodCol = new TableColumn<>("Static");
        staticMethodCol.setCellValueFactory(new PropertyValueFactory("isStatic"));
        abstractCol = new TableColumn<>("Abstract");
        abstractCol.setCellValueFactory(new PropertyValueFactory("isAbstract"));
        accessMethodCol = new TableColumn<>("Access");
        accessMethodCol.setCellValueFactory(new PropertyValueFactory("accessIdentifier"));
        methodTable.getColumns().addAll(nameMethodCol,returnCol,staticMethodCol,abstractCol,accessMethodCol);
        
        //  ADD THE VAR TABLE TO SCROLLPANE
        varScrollPane = new ScrollPane();
        varScrollPane.setPrefSize(350, 200);
        varScrollPane.setContent(varTable);
        
        //  ADD THE METHOD TABLE TO SCROLLPANE
        methodScrollPane = new ScrollPane();
        methodScrollPane.setPrefSize(350,300);
        methodScrollPane.setContent(methodTable);
        
        row4 = new VBox();
        row4.getChildren().addAll(row4_1,varScrollPane);
        
        row5 = new VBox();
        row5.getChildren().addAll(row5_1,methodScrollPane);

        // INIT LEFT PANE
        leftPane = new ScrollPane();
        leftPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        leftPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        leftPane.setPrefSize(800, 700);
        
        // ADD SPACE TO LEFTPANE
        space.setPrefSize(1200, 780);
        space.setOnMouseMoved(e ->{
        });
        leftPane.setContent(space);
        
        // INIT RIGHT PANE
        rightPane = new VBox(5);
        rightPane.setPrefSize(400, 700);
        rightPane.getChildren().addAll(row1,row2,row3,row3_5,row4,row5);
        
        // ADD EVENT HANDELER
        addEventHandlers();
        
        // NOW FINALLY SETUP UP THE WORKSPACE
        workspace = new BorderPane();
        ((BorderPane)workspace).setCenter(leftPane);
        ((BorderPane)workspace).setRight(rightPane);

        initStyle();
    }

    // TO GET Prent tex field
    public TextField getParentTextField() {
        return parentTextField;
    }
    
    public void addEventHandlers(){
        
        PageEditController = new pageEditController(app);

        addClass.setOnAction(e ->{
            PageEditController.isSelecting = false;
            PageEditController.handleAddClassRequest(space);
        });

        addInterface.setOnAction(e ->{
            PageEditController.isSelecting = false;
            PageEditController.handleAddInterfaceRequest(space);
        });
        
        remove.setOnAction(e ->{
            PageEditController.removeClassDiagram(selectedDiagram,space );
            selectedDiagram = null;
        });

        classTextField.setOnAction(e ->{
              PageEditController.handleChangeClassNameRequest(classTextField.getText());
        });

        packageTextField.setOnAction(e ->{
            PageEditController.handleChangePackageNameRequest(packageTextField.getText());
        });

        select.setOnAction(e ->{
            PageEditController.isSelecting = true;
        });

        classTextField.setOnAction(e ->{
            PageEditController.handleChangeClassNameRequest(classTextField.getText());
        });
        
        toCode.setOnAction(e -> {
            dataManager.exportToCode(gui.getWindow());
        });;
        
        resize.setOnAction(e ->{
            PageEditController.handleResizeRequest();
        });
       
        varPlus.setOnAction(e ->{
            AppAddVariableSingleton addVarDialog = AppAddVariableSingleton.getSingleton();
            addVarDialog.init(app.getGUI().getWindow());
            addVarDialog.show("", "");
        }); 
        
        varMinus.setOnAction(e ->{
            AppRemoveVariableSingleton removeVarDialog = AppRemoveVariableSingleton.getSingleton();
            removeVarDialog.init(app.getGUI().getWindow());
            removeVarDialog.show();
        });
        
        methodPlus.setOnAction(e ->{
            AppAddMethodSingleton addMethodDialog = AppAddMethodSingleton.getSingleton();
            addMethodDialog.init(app.getGUI().getWindow());
            addMethodDialog.show();
            
        });
        
        methodMinus.setOnAction(e ->{
            AppRemoveMethodSingleton removeMethodDialog = AppRemoveMethodSingleton.getSingleton();
            removeMethodDialog.init(app.getGUI().getWindow());
            removeMethodDialog.show();
        });
        undo.setOnAction(e ->{
            dataManager.undoAction();
        });
        
        redo.setOnAction(e ->{
            dataManager.redoAction();
        });
        
        addForeignInterfaces.setOnAction(e ->{
            AppAddForeignInterfaceSingleton dialog = AppAddForeignInterfaceSingleton.getSingleton();
            dialog.init(app.getGUI().getWindow());
            dialog.show();
        });
        
        addLocalInterfaces.setOnAction(e ->{
            AppAddLocalInterfaceSingleton dialog = AppAddLocalInterfaceSingleton.getSingleton();
            dialog.init(app.getGUI().getWindow());
            dialog.show();
        });
        
        addPackages.setOnAction(e ->{
            AppAddPackagesSingleton dialog = AppAddPackagesSingleton.getSingleton();
            dialog.init(app.getGUI().getWindow());
            dialog.show();
        });
        
        zoomIn.setOnAction(e ->{
            PageEditController.handleZoomInRequest(space);
        });
        
        zoomOut.setOnAction(e ->{
            PageEditController.handleZoomOutRequest(space);
        });
        
        gridBox.setOnAction(e ->{
            if(gridBox.isSelected()){
                PageEditController.handleAddGridRequest(space);
            }else{
                PageEditController.handleRemoveGridRequest(space);
            }
         });
        
        parentTextField.setOnAction(e ->{
            
            if(parentTextField.getText().trim().equals("")){
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Incorret Input");
                alert.setHeaderText("Parent name can't be left empty!!");
                String s ="Enter a valid parent name.";
                alert.setContentText(s);
                alert.show();
                
            }else{
                // SET THE PARENT NAME OF DIAGRAM
                selectedDiagram.setParentName(parentTextField.getText());
                ClassDiagram diagram = null;

                String parentName = parentTextField.getText();

                VBox parent = null ;
                boolean isLocal = false;
                ClassDiagram tempParent =null;
                
                for(ClassDiagram clas: diagramsList){
                    if(clas.getClassName().equals(parentName)){
                        parent = clas.getRootContainer();
                        tempParent = clas;
                        isLocal = true;
                    }
                }

                if(parent != null && isLocal){
                    InheritanceLines line = new InheritanceLines(selectedDiagram,tempParent,selectedDiagram.getRootContainer(),parent,space);
                    // ADD THERESPECTIVE LINES TO THEIR RESPECTIVE BOXES AND CLASS DIAGRAMS
                    tempParent.addComingInLine(line);
                    selectedDiagram.addGoingOutLine(line);
                    
                    
                    // THIS PART IS THO RESET THE LINE 
                    if(selectedDiagram.getParentLine() == null){
                        
                    }else{
                        space.getChildren().remove(selectedDiagram.getParentLine());
                        space.getChildren().remove(selectedDiagram.getParentLine().getArrowContainer());
                    }
                    
                    selectedDiagram.setParentLine(line);
                    
                }else{
                    ForeignClassBox blackBox = new ForeignClassBox(parentName, space);
                    // TO AVOID CREATING SAME BOXES AGAIN AND AGAIN
                    foreignBoxesPresent.add(blackBox);
                    
                    InheritanceLines line = new InheritanceLines(selectedDiagram,blackBox,selectedDiagram.getRootContainer(),blackBox.getVBox(),space);
                    // ADD THERESPECTIVE LINES TO THEIR RESPECTIVE BOXES AND CLASS DIAGRAMS
                    blackBox.addComingInLine(line);
                    selectedDiagram.addGoingOutLine(line);
                    
                    // THIS PART IS THO RESET THE LINE 
                    if(selectedDiagram.getParentLine() == null){
                    }else{
                        space.getChildren().remove(selectedDiagram.getParentLine());
                        space.getChildren().remove(selectedDiagram.getParentLine().getArrowContainer());
                    }
                    selectedDiagram.setParentLine(line);
                    
                }
            }
        });
        
        snapBox.setOnAction(e ->{
            PageEditController.snapDiagrams();
        });
        
        snapShot.setOnAction(e ->{
            PageEditController.handleSnapshotRequest(space);
        });
    }
    /**
     * This function specifies the CSS style classes for all the UI components
     * known at the time the workspace is initially constructed. 
     */
    
    @Override
    public void initStyle() {
	// NOTE THAT EACH CLASS SHOULD CORRESPOND TO
	// A STYLE CLASS SPECIFIED IN THIS APPLICATION'S
	// CSS FILE
        rightPane.getStyleClass().add(CLASS_RIGHTPANE);
        gui.fileToolbarPane.getStyleClass().add(FILE_TOOLBAR);
    }

    /**
     * This function reloads all the controls for editing tag attributes into
     * the workspace.
     */
    @Override
    public void reloadWorkspace() {

    }
    
    public TextField getClassTextField(){
        return classTextField;
    }
    
    public TextField getPackageTextField(){
        return packageTextField;
    }    
    
    // TO FILL THE VARTABLES IN VAR TABLE
    public void fillVarTable(ObservableList<VariablesObject> vars){
        varTable.setItems(vars);
    }
    
    // TO FILL THE METHODS IN THE METHOD TABLE
    public void fillMethodTable(ObservableList<MethodsObject> methods){
        methodTable.setItems(methods);
    }
}
