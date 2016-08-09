package jd.file;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import javax.media.j3d.Sound;
import jd.data.ClassDiagram;
import jd.data.DataManager;
import jf.components.AppDataComponent;
import jf.components.AppFileComponent;
import static jd.controller.pageEditController.diagramsList;
import static jd.controller.pageEditController.isSelecting;
import static jd.controller.pageEditController.selectedDiagram;
import jd.data.ArgumentObject;
import static jd.data.DataManager.foreignBoxesPresent;
import jd.data.ForeignClassBox;
import jd.data.MethodsObject;
import jd.data.VariablesObject;
import jd.gui.Workspace;
import static jd.gui.Workspace.space;
import jd.lines.InheritanceLines;
import jf.AppTemplate;
import static jf.components.AppStyleArbiter.DIAGRAM_VBOX_SELECTED;

/**
 * This class serves as the file management component for this application,
 * providing all I/O services.
 *
 * @author Richard McKenna
 * @author ?
 * @version 1.0
 */
public class FileManager implements AppFileComponent {

    final String JSON_CLASS_DIAGRAM_X = "x";
    final String JSON_CLASS_DIAGRAM_Y = "y";
    final String JSON_CLASS_NAME = "name";
    final String JSON_CLASS_PACKAGE = "package";
    final String JSON_CLASS_PARENT = "parent";
    final String JSON_CLASS_TYPE = "class_type";
    final String JSON_DIAGRAMS = "diagrams"; 
    final String JSON_METHODS_ARRAY = "methods_array";
    final String JSON_VARIABLES_ARRAY = "variables_array";
    
    // NEW ADDITIONS FOR NOW
    final String JSON_LOCAL_INTR_ARRAY = "local_interfaces";
    final String JSON_LOCAL_INTR_NAME = "local_interfaces_name";
    final String JSON_FOREIGN_INTR_ARRAY = "foreign_interfaces";
    final String JSON_FOREIGN_INTR_NAME = "foreign_interfaces_name";
    final String JSON_IMPORTRED_PACKAGE_ARRAY = "imported_packages";
    final String JSON_IMPORTED_PACKAGE_NAME = "imported_packages_name";
    
    
    // FINAL STRINGS FOR METHODS
    final String JSON_METHOD_NAME = "method_name";
    final String JSON_METHOD_RETURN_TYPE = "method_return_type";
    final String JSON_Method_IS_STATIC = "method_is_static";
    final String JSON_METHOD_IS_ABSTRACT = "method_is_Abstract";
    final String JSON_METHOD_ACCESS = "method_access_identifier";
    final String JSON_ARGUMENTS_ARRAY = "method_arguments_array";
    
    // METHOD ARGUMENTS
    final String JSON_ARG_NAME = "arg_name";
    final String JSON_ARG_DATA_TYPE = "arg_data_type";
    
    // CLASS VARIABLES
    final String JSON_VARIABLE_NAME = "variable_name";
    final String JSON_VARIABLE_DATA_TYPE = "variable_data_type";
    final String JSON_VARIABLE_IS_STATIC = "variable_is_static";
    final String JSON_VARIABLE_IS_FINAL = "variable_is_final";
    final String JSON_VARIABLE_ACCESS = "variable_access";
    
    final String JSON_FOREIGN_BOX_ARRAY = "foreign_box_array";
    final String JSON_FOREIGN_BOX_X = "foreign_box_x";
    final String JSON_FOREIGN_BOX_Y = "foreign_box_y";
    final String JSON_FOREIGN_BOX_NAME = "foreign_box_name";
    
    // DATAMANAGER OBJECT TO ADD EVENT HANDLERS
    DataManager dataManager;
    
    
    /**
     * This method is for saving user work, which in the case of this
     * application means the data that constitutes the page DOM.
     * 
     * @param data The data management component for this application.
     * 
     * @param filePath Path (including file name/extension) to where
     * to save the data to.
     * 
     * @throws IOException Thrown should there be an error writing 
     * out data to the file.
     */
    
    @Override
    public void saveData(AppDataComponent data, String filePath) throws IOException {
            StringWriter sw = new StringWriter();
            
            DataManager dataManager = (DataManager)data;
            
            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            fillArrayWithDiagrams(diagramsList,arrayBuilder);
            JsonArray diagramsArray = arrayBuilder.build();
            
            JsonArrayBuilder arrayBuilder1 = Json.createArrayBuilder();
            fillArrayWithForeignBoxes(foreignBoxesPresent, arrayBuilder1);
            JsonArray boxesArray = arrayBuilder1.build();
            
            // THEN PUT IT ALL TOGETHER IN A JSON OBJECT
            JsonObject dataManagerJSO = Json.createObjectBuilder()
                    .add(JSON_DIAGRAMS, diagramsArray)
                    .add(JSON_FOREIGN_BOX_ARRAY, boxesArray)
                    .build();
            
            // AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
            Map<String, Object> properties = new HashMap<>(1);
            properties.put(JsonGenerator.PRETTY_PRINTING, true);
            JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
            JsonWriter jsonWriter = writerFactory.createWriter(sw);
            jsonWriter.writeObject(dataManagerJSO);
            jsonWriter.close();
            
             
            // INIT THE WRITER
            OutputStream os = new FileOutputStream(filePath);
            JsonWriter jsonFileWriter = Json.createWriter(os);
            jsonFileWriter.writeObject(dataManagerJSO);
            String prettyPrinted = sw.toString();
            PrintWriter pw = new PrintWriter(filePath);
            pw.write(prettyPrinted);
            pw.close();
    }
    
    // GOING TO BE USED IN THE ORIGINAL PROGRAM
    public void fillArrayWithDiagrams(ArrayList<ClassDiagram> arr, JsonArrayBuilder arrayBuilder){
        for(ClassDiagram cd : diagramsList){
            JsonObject diagramObject = makeClassDiagramJsonObject(cd);
            arrayBuilder.add(diagramObject);
        }
    }

    // MAKE THE CLASS DIAGRAM OBJECT
    public JsonObject makeClassDiagramJsonObject(ClassDiagram diagram){
        
        ArrayList<MethodsObject> methodsList = diagram.getMethodsList();
        ArrayList<VariablesObject> variablesList = diagram.getVariablesList();
        ArrayList<String> localsList = diagram.getLocalInterfaces();
        ArrayList<String> foreignsList = diagram.getExternalInterfaces();
        ArrayList<String> importsList = diagram.getImportedPackages();
        
        // CREATE THE METHODS ARRAY
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        fillArrayWithMethods(methodsList,arrayBuilder);
        JsonArray methodsArray = arrayBuilder.build();
        
        // CREATE THE VARIABLES ARRAY
        JsonArrayBuilder arrayBuilder1 = Json.createArrayBuilder();
        fillArrayWithVariables(variablesList, arrayBuilder1);
        JsonArray variablesArray = arrayBuilder1.build();
        
        // CREATE THE LCAL INTREFACES ARRAY
        JsonArrayBuilder arrayBuilder2 = Json.createArrayBuilder();
        fillArrayWithLocals(localsList, arrayBuilder2);
        JsonArray localsArray = arrayBuilder2.build();
        
        // CREATE THE FOREIGN INTREFACES ARRAY
        JsonArrayBuilder arrayBuilder3 = Json.createArrayBuilder();
        fillArrayWithForeigns(foreignsList, arrayBuilder3);
        JsonArray foreignsArray = arrayBuilder3.build();
        
        // CREATE THE  IMPORTED PACKAGES ARRAY
        JsonArrayBuilder arrayBuilder4 = Json.createArrayBuilder();
        fillArrayWithImportedPackages(importsList, arrayBuilder4);
        JsonArray importsArray = arrayBuilder4.build();
        
        JsonObject jso = Json.createObjectBuilder()
                .add(JSON_CLASS_DIAGRAM_X, diagram.getRootContainer().getLayoutX())
                .add(JSON_CLASS_DIAGRAM_Y, diagram.getRootContainer().getLayoutY())
                .add(JSON_CLASS_NAME, diagram.getClassName())
                .add(JSON_CLASS_PACKAGE, diagram.getPackage())
                .add(JSON_CLASS_PARENT, diagram.getClassParent())
                .add(JSON_CLASS_TYPE, diagram.getClassType())
                .add(JSON_METHODS_ARRAY, methodsArray)
                .add(JSON_VARIABLES_ARRAY, variablesArray)
                .add(JSON_LOCAL_INTR_ARRAY, localsArray)
                .add(JSON_FOREIGN_INTR_ARRAY,foreignsArray)
                .add(JSON_IMPORTRED_PACKAGE_ARRAY, importsArray)
                .build();
    
        return jso;
    }
    
    // MAKE FOREIGN BOX ARRAY
    public void fillArrayWithForeignBoxes(ArrayList<ForeignClassBox> boxes, JsonArrayBuilder arrayBuilder ){
        for(ForeignClassBox box: boxes){
            JsonObject boxObject= makeBoxJsonObject(box);
            arrayBuilder.add(boxObject);
        }
    }
    public JsonObject makeBoxJsonObject(ForeignClassBox box){
        JsonObject jso = Json.createObjectBuilder()
                .add(JSON_FOREIGN_BOX_X, box.getVBox().getLayoutX())
                .add(JSON_FOREIGN_BOX_Y, box.getVBox().getLayoutY())
                .add(JSON_FOREIGN_BOX_NAME, box.getName())
                .build();
        return jso;
    }
    
    // FILL THE ARRAY WITH VARIABLE'S JSON OBJECTS
    public void fillArrayWithVariables(ArrayList<VariablesObject> variables, JsonArrayBuilder arrayBuilder){
        for(VariablesObject variable : variables){
            JsonObject variableObject = makeVariablesJsonObject(variable);
            arrayBuilder.add(variableObject);
        }
    }
    
    // MAKE THE VARIABLE JSON OBJECT
    public JsonObject makeVariablesJsonObject(VariablesObject variable){
        JsonObject jso = Json.createObjectBuilder()
                .add(JSON_VARIABLE_NAME,variable.getName())
                .add(JSON_VARIABLE_DATA_TYPE,variable.getDataType())
                .add(JSON_VARIABLE_IS_STATIC,variable.isIsStatic())
                .add(JSON_VARIABLE_IS_FINAL,variable.isIsFinal())
                .add(JSON_VARIABLE_ACCESS, variable.getAccess())
                .build();
        
        return jso;
    }
    
    // MAKE JSON LOCAL CLASSES PACKAGE
    public void fillArrayWithLocals(ArrayList<String> localInterfaces, JsonArrayBuilder arrayBuilder){
        for(String local : localInterfaces){
            JsonObject localObject = Json.createObjectBuilder()
                    .add(JSON_LOCAL_INTR_NAME,local )
                    .build();
            arrayBuilder.add(localObject);
        }
    }
    
    // MAKE JSON LOCAL CLASSES PACKAGE
    public void fillArrayWithForeigns(ArrayList<String> importedInterfaces, JsonArrayBuilder arrayBuilder){
        for(String local : importedInterfaces){
            JsonObject localObject = Json.createObjectBuilder()
                    .add(JSON_FOREIGN_INTR_NAME, local)
                    .build();
            arrayBuilder.add(localObject);
        }
    }
    
    // MAKE JSON LOCAL CLASSES PACKAGE
    public void fillArrayWithImportedPackages(ArrayList<String> importedPackages, JsonArrayBuilder arrayBuilder){
        for(String local : importedPackages){
            JsonObject localObject = Json.createObjectBuilder()
                    .add(JSON_IMPORTED_PACKAGE_NAME,local)
                    .build();
            arrayBuilder.add(localObject);
        }
    }
    
    
    // FILL THE ARRAY WITH METHODS
    public void fillArrayWithMethods(ArrayList<MethodsObject> methods, JsonArrayBuilder arrayBuilder){
      
        for(MethodsObject method : methods){
            JsonObject methodObject = makeMethodsJsonObject(method);
            arrayBuilder.add(methodObject);
        }
    }
    
    // MAKE THE METHOD JSON OBJECT
    public JsonObject makeMethodsJsonObject(MethodsObject method){
        
        ArrayList<ArgumentObject> argList = method.getArguments();
        
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        fillArrayWithArguments(argList,arrayBuilder);
        JsonArray argumentsArray = arrayBuilder.build();
        
        JsonObject jso = Json.createObjectBuilder()
                .add(JSON_METHOD_NAME,method.getName())
                .add(JSON_METHOD_RETURN_TYPE,method.getReturnType())
                .add(JSON_Method_IS_STATIC,method.isIsStatic())
                .add(JSON_METHOD_IS_ABSTRACT,method.isIsAbstract())
                .add(JSON_METHOD_ACCESS, method.getAccessIdentifier())
                .add(JSON_ARGUMENTS_ARRAY, argumentsArray)
                .build();
        
        return jso;
    }
    
    public void fillArrayWithArguments(ArrayList<ArgumentObject> arguments, JsonArrayBuilder arrayBuilder){
        
        for(ArgumentObject arg : arguments){
            JsonObject argumentObject = makeArgumentJsonObject(arg);
            arrayBuilder.add(argumentObject);
        }
    }
    
    public JsonObject makeArgumentJsonObject(ArgumentObject arg){
        JsonObject jso = Json.createObjectBuilder()
                .add(JSON_ARG_NAME, arg.getName())
                .add(JSON_ARG_DATA_TYPE, arg.getDataType())
                .build();
        
        return jso;
    }
    
    
    /**
     * This method loads data from a JSON formatted file into the data 
     * management component and then forces the updating of the workspace
     * such that the user may edit the data.
     * 
     * @param data Data management component where we'll load the file into.
     * 
     * @param filePath Path (including file name/extension) to where
     * to load the data from.
     * 
     * @throws IOException Thrown should there be an error reading
     * in data from the file.
     */
    //@Override
    public void loadData(AppDataComponent data, String filePath) throws IOException {
        
        // CLEAR THE SPACE 
        space.getChildren().clear();
              
        // LOAD THE JSON FILE WITH ALL THE DATA
        JsonObject json = loadJSONFile(filePath);
        
        // INIT THE DATAMANGER 
        dataManager = (DataManager)data;
        
        // GET THE ARRAY class class class classclass class class classclass class class classclass class class classclass class class class
        JsonArray diagramsArray = json.getJsonArray(JSON_DIAGRAMS);
        // RESET ALL LISTS
        diagramsList = new ArrayList<ClassDiagram>();
        DataManager.interfacesPresent = new ArrayList<String>();
        
        //LOAD ALL THEM ITEMS 
        for(int i=0;i<diagramsArray.size();i++){
            
             JsonObject diagramJSO = diagramsArray.getJsonObject(i);
 
            // LETS X AND Y LAYOUT COORDINATES
            double layoutX = diagramJSO.getInt( JSON_CLASS_DIAGRAM_X);
            double layoutY = diagramJSO.getInt( JSON_CLASS_DIAGRAM_Y);
            boolean type = diagramJSO.getBoolean(JSON_CLASS_TYPE);
 
            // EROROEOE REOROEOR EORROE EROROE EEROOE RROE EOROROE
            ClassDiagram diagram = new ClassDiagram(space,(double)layoutX,(double)layoutY,type);
            diagram.setClassName(diagramJSO.getString(JSON_CLASS_NAME));
            diagramsList.add(diagram);
            
            diagram.setClassType(type);
            if(!type){
                DataManager.interfacesPresent.add(diagram.getClassName());
            }
           
            diagram.setPackageName(diagramJSO.getString(JSON_CLASS_PACKAGE));
            diagram.setParentName(diagramJSO.getString(JSON_CLASS_PARENT));
 
            JsonArray methodsArray = diagramJSO.getJsonArray(JSON_METHODS_ARRAY);
            diagram.setMethodsList(loadMethods(methodsArray));
 
            JsonArray variablesArray = diagramJSO.getJsonArray(JSON_VARIABLES_ARRAY);
            diagram.setVariablesList(loadVariables(variablesArray));
             
            JsonArray localsArray = diagramJSO.getJsonArray(JSON_LOCAL_INTR_ARRAY);
            diagram.setLocalInterfaces(loadFromStringArrays(localsArray, JSON_LOCAL_INTR_NAME));
            
            JsonArray foreignsArray = diagramJSO.getJsonArray(JSON_FOREIGN_INTR_ARRAY);
            diagram.setExternalInterfaces(loadFromStringArrays(foreignsArray, JSON_FOREIGN_INTR_NAME));
            
            JsonArray importsArray = diagramJSO.getJsonArray(JSON_IMPORTRED_PACKAGE_ARRAY);
            diagram.setImportedPackages(loadFromStringArrays(localsArray, JSON_IMPORTED_PACKAGE_NAME));
                        
            // KEEP ADDING THE CLASSES TO THE GLOBAL LIST OF CLASSES
            diagramsList.add(diagram);
                    
            dataManager.addEventHandler(diagram);

            diagram.updateTwo(diagram.getVariablesList());
            diagram.updateThree(diagram.getMethodsList());
        }
        
        foreignBoxesPresent = new ArrayList<ForeignClassBox>();
        JsonArray diagramsArray1 = json.getJsonArray(JSON_FOREIGN_BOX_ARRAY); 
        
        //LOAD ALL THEM ITEMS 
        for(int i=0;i<diagramsArray1.size();i++){
            JsonObject diagramJSO = diagramsArray1.getJsonObject(i);
 
            double layoutX = (double)diagramJSO.getInt( JSON_FOREIGN_BOX_X);
            double layoutY = (double)diagramJSO.getInt( JSON_FOREIGN_BOX_Y);
            String name = diagramJSO.getString(JSON_FOREIGN_BOX_NAME);
 
            ForeignClassBox box = new ForeignClassBox(layoutX,layoutY,name,space);
            foreignBoxesPresent.add(box);
        }
    }
    
    // THIS WILL BE USED FOR LOCALS, FOREIGNS AND IMPORTS
    public ArrayList<String> loadFromStringArrays(JsonArray arr,String s){
        
        ArrayList<String> names = new ArrayList<String>();
        for(int i=0;i<arr.size();i++){
            JsonObject x = arr.getJsonObject(i);
            String name = x.getString(s);
            names.add(name);
        }
        
        return names;
    }
    
    // HELPER METHOD TO LOAD THE VARIABLES FROM JSON ARRAY
    public ArrayList<VariablesObject> loadVariables(JsonArray variables){
        
        ArrayList<VariablesObject> vars = new ArrayList<VariablesObject>();

        for(int i=0;i<variables.size();i++){
            
            JsonObject var = variables.getJsonObject(i);
            
            String name = var.getString(JSON_VARIABLE_NAME);
            String returnType = var.getString(JSON_VARIABLE_DATA_TYPE);
            boolean isStatic = var.getBoolean(JSON_VARIABLE_IS_STATIC);
            boolean isFinal = var.getBoolean(JSON_VARIABLE_IS_FINAL);
            String access = var.getString(JSON_VARIABLE_ACCESS);
            
            VariablesObject temp = new VariablesObject(name,returnType,isStatic,isFinal,access);
            vars.add(temp);
        }
        return vars;
    }
    
    // HELPER METHOD TO LOAD THE METHODS FROM JSON ARRAY
    public ArrayList<MethodsObject> loadMethods(JsonArray methods){
        
        ArrayList<MethodsObject> meths = new ArrayList<MethodsObject>();

        for(int i=0;i<methods.size();i++){
            
            JsonObject method = methods.getJsonObject(i);
            
            String name = method.getString(JSON_METHOD_NAME);
            String returnType = method.getString(JSON_METHOD_RETURN_TYPE);
            boolean isStatic = method.getBoolean(JSON_Method_IS_STATIC);
            boolean isAbstract = method.getBoolean(JSON_METHOD_IS_ABSTRACT);
            String access = method.getString(JSON_METHOD_ACCESS);
            
            JsonArray argumentsArray = method.getJsonArray(JSON_ARGUMENTS_ARRAY);
            ArrayList<ArgumentObject> args = new ArrayList<ArgumentObject>();
            args = loadArguments(argumentsArray);
            
            MethodsObject temp = new MethodsObject(name,returnType,isStatic,isAbstract,access,args);
            meths.add(temp);
        }
        
        return meths;
    }
    
    // HELPER METHOD FOR ARGUMENTS
    public ArrayList<ArgumentObject> loadArguments(JsonArray arguments){
        
        ArrayList<ArgumentObject> args = new ArrayList<ArgumentObject>();
        
        for(int i=0;i < arguments.size();i++){
         
            JsonObject arg = arguments.getJsonObject(i);
            
            String dataType = arg.getString(JSON_ARG_DATA_TYPE);
            String name = arg.getString(JSON_ARG_NAME);
            
            ArgumentObject temp = new ArgumentObject(dataType,name);
            
            args.add(temp);
        }
        
        return args;
    }
    
    // HELPER METHOD FOR LOADING DATA FROM A JSON FORMAT
    private JsonObject loadJSONFile(String jsonFilePath) throws IOException {
	InputStream is = new FileInputStream(jsonFilePath);
	JsonReader jsonReader = Json.createReader(is);
	JsonObject json = jsonReader.readObject();
	jsonReader.close();
	is.close();
	return json;
    }
    
    /**
     * This method exports the contents of the data manager to a 
     * Web page including the html page, needed directories, and
     * the CSS file.
     * 
     * @param data The data management component.
     * 
     * @param filePath Path (including file name/extension) to where
     * to export the page to.
     * 
     * @throws IOException Thrown should there be an error writing
     * out data to the file.
     */
    @Override
    public void exportData(AppDataComponent data, String filePath) throws IOException {

    }
    
    //@Override
    public void importData(AppDataComponent data, String filePath) throws IOException {
	
    }
    
}
