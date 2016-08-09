package Actions;

import jd.data.ClassDiagram;

/**
 * RESIZE RIGHT EVENT OBJECT
 */

public class ResizeRightAction extends Action{
    
    // INITIAL WIDTH
    private double width;
    
    // DIAGRAM THAT IS BENG DRAGGED
    private ClassDiagram diagram;
    
    // FINAL WIDTH
    private double finalWidth;
    
    public ClassDiagram getDiagram() {
        return diagram;
    }
    
    public ResizeRightAction(ClassDiagram diagram){
        width    = diagram.getRootContainer().getWidth();
        this.diagram = diagram;
    }
    
    // SET FINAL WIDTH
    public void setFinals(double finalWidth){
        this.finalWidth = finalWidth;
    }
    
    // REDO
    public void undo(){
        diagram.getRootContainer().setPrefWidth(width);
    }
    
    // REDO
    public void redo(){
        diagram.getRootContainer().setPrefWidth(finalWidth);
    }
}
