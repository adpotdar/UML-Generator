package jd.data;
/**
 * THIS CLASS WILL REPRESENT A VARIABLE OBJECT, 
 *  i.e IT WILL REPRESENT A VARIABLE.
 */
public class VariablesObject {

    String name;
    String dataType;
    boolean isStatic;
    boolean isFinal;
    String access = "";
  
    // CREATE THE VARIABLE OBJECT
    public VariablesObject(String name, String dataType, boolean isStatic, boolean isFinal, String access) {
        this.name = name;
        this.dataType = dataType;
        this.isStatic = isStatic;
        this.isFinal = isFinal;
        this.access = access;
    }

    @Override
    public String toString() {
        String acc;
        if(access.equalsIgnoreCase("public")){
            acc = "+";
        }else if(access.equalsIgnoreCase("private")){
            acc = "-";
        }else if(access.equalsIgnoreCase("protected")){
            acc = "#";
        }
        else{
            acc = " ";
        }
        
        String stat="";
        if(isStatic){
            stat ="$";
        }
        return stat+acc + name + " : " + dataType;
    }
    
    // CONVERT THE VARIABLE OBJECT INTO THE STRING THAT CAN BE DIRECTLY WRITTEN IN THE UML DIAGRAM
    public String toCode(){
        
        String staticc = " ";
        if(isStatic){
        staticc = " static ";
        }
        
        String finall = "";
        if(isFinal){
            finall = " final ";
        }
        
        String header = getAccess()+" "+ staticc +  getDataType()+" "+ getName()+";";
        
        return header;
    }
    
    // GETTERS
    public boolean isIsFinal() {
        return isFinal;
    }
     
    public String getName() {
        return name;
    }

    public String getDataType() {
        return dataType;
    }

    public boolean isIsStatic() {
        return isStatic;
    }

    public String getAccess() {
        return access;
    }
}
