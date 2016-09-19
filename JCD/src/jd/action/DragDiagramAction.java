package jd.action;

import jd.action.Action;
import javafx.scene.layout.VBox;
import jd.data.ClassDiagram;

/**
 * DRAG DIAGRAM EVENT OBJECT
 */
public class DragDiagramAction extends Action{
    
    // INITIAL COORDINATES
    private double initialX;
    private double initialY;

    // FINAL COORDINATES
    private double finalX;
    private double finalY;
    
    // DIAGRAM THAT IS BEING DRAGGED
    public ClassDiagram diagram;
    
    public ClassDiagram getDiagram() {
        return diagram;
    }
    
    public DragDiagramAction(double initialX, double initialY, ClassDiagram diagram) {
        this.initialX = initialX;
        this.initialY = initialY;
        this.diagram = diagram;
    }
    
    // SET THE FINAL POSITION OF THE DIAGRAM
    public void setFinalPosition(double finalX,double finalY){
        this.finalX = finalX;
        this.finalY = finalY;
    }
    
    // UNDO
    public void undo(){
        diagram.getRootContainer().setLayoutX(initialX);
        diagram.getRootContainer().setLayoutY(initialY);
    } 
    
    // REDO
    public void redo(){
        diagram.getRootContainer().setLayoutX(finalX);
        diagram.getRootContainer().setLayoutY(finalY);
    }
}
