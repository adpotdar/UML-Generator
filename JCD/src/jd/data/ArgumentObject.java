package jd.data;

/**
 * THIS CLASS REPRESENTS A ARGUMENT OBJECT
 * 
 */
public class ArgumentObject {

    String dataType;
    String name;

    public ArgumentObject(String dataType,String name){
        this.dataType = dataType;
        this.name = name;
    }
    public String getDataType() {
        return dataType;
    }

    public String getName() {
        return name;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name + ":"+ dataType;
    }
    
    public String toCode(){
        
        String header = getDataType()+" "+getName();
        
        return header;
    }
}
