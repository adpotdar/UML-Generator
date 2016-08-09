package Actions;

import jd.data.ClassDiagram;

/**
 * RESIZE IN LEFT DIRECTION EVENT OBJECT
 *
 */
public class ResizeLeftAction extends Action{

    // INITIAL POSITION AND WIDTH
    private double initialX;
    private double initialY;
    private double width;
    
    // FINAL POSITION AND WIDTH
    private double finalX;
    private double finalY;
    private double finalWidth;
    
    // DIAGRAM THAT IS BEING DRAGGED
    private ClassDiagram diagram;
    
    public ClassDiagram getDiagram() {
        return diagram;
    }
    
    public ResizeLeftAction(ClassDiagram diagram){
        initialX = diagram.getRootContainer().getLayoutX();
        initialY = diagram.getRootContainer().getLayoutY();
        width    = diagram.getRootContainer().getWidth();
        this.diagram = diagram;
    }
    
    // SET THE FINAL COORDINATES
    public void setFinals(double finalX,double finalY, double Width){
        this.finalX = finalX;
        this.finalY = finalY;
        this.finalWidth = finalWidth;
    }
    
    // UNDO
    public void undo(){
        diagram.getRootContainer().setLayoutX(initialX);
        diagram.getRootContainer().setLayoutY(initialY);
        diagram.getRootContainer().setPrefWidth(width);
        
    }
    
    // REDO
    public void redo(){
        diagram.getRootContainer().setLayoutX(finalX);
        diagram.getRootContainer().setLayoutY(finalY);
        diagram.getRootContainer().setPrefWidth(finalWidth);
    }
}
