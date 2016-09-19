package jd.data;

import java.util.ArrayList;
/**
 * THIS CLASS WILL REPRESENT A METHOD OBJECT,
 *  WHICH MEANS THE THIS CLASS WILL REPRESENT A METHOD
 */
public class MethodsObject {
    
    String name;
    String returnType;
    boolean isStatic;
    boolean isAbstract;
    String accessIdentifier;
    
    ArrayList<ArgumentObject> arguments;

    // CREATES AN METHOD OBJECT
    public MethodsObject(String name, String returnType, boolean isStatic, boolean isAbstract, String accessIdentifier, ArrayList<ArgumentObject> arguments) {
        this.name = name;
        this.returnType = returnType;
        this.isStatic = isStatic;
        this.isAbstract = isAbstract;
        this.accessIdentifier = accessIdentifier;
        this.arguments = arguments;
    }
    
    // TO STRING
    public String toString(){
        String acc ="";
        if(accessIdentifier.equalsIgnoreCase("public")){
            acc = "+";
        }else if(accessIdentifier.equalsIgnoreCase("private")){
            acc = "-";
        }else if(accessIdentifier.equalsIgnoreCase("protected")){
            acc = "#";
        }
        else{
            acc = " ";
        }
        
        String stat="";
        if(isStatic){
            stat ="$";
        }
        
        return stat +acc+ name +"("+ arguments+" ):" + returnType;
    }

    // THIS METHOD WRITES METHOD OBJECT IN COMPILABLE JAVA CODE
     public String toCode(){
        
        String staticc = " ";
        if(isStatic){
            staticc = " static ";
        }
        
        String abstractt = " ";
        if(isAbstract){
            abstractt = " abstract ";
        }
        
        String header = getAccessIdentifier()+ " " + abstractt + staticc+ getReturnType() + " "+ getName()+"( ";
        
        for(int i=0;i<getArguments().size();i++){
            
            ArgumentObject arg = getArguments().get(i);
            
           if(i== getArguments().size()-1){
            header += arg.toCode()+" ";
           }else{
            header += arg.toCode()+", ";
           }
        }
        
        header += "){\n\n";
        
        if(returnType.equals("char")||returnType.equals("byte")||returnType.equals("short") ||  returnType.equals("int")){
            header += "return 0; \n}";
        }else if(returnType.equals("double")){
            header += "return 0.0; \n}";
        }else if(returnType.equals("boolean")){
            header += "return false; \n}";
        }
        else if(returnType.equals("void")){
            header += "\n}";
        }else{
            header += "return null; \n}";
        }
        return header;
    }
     
    // GETTERS
    public String getName() {
        return name;
    }

    public String getReturnType() {
        return returnType;
    }

    public boolean isIsStatic() {
        return isStatic;
    }

    public boolean isIsAbstract() {
        return isAbstract;
    }

    public String getAccessIdentifier() {
        return accessIdentifier;
    }

    public ArrayList<ArgumentObject> getArguments() {
        return arguments;
    }
}
