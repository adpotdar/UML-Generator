package jd.lines;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import jd.data.ClassDiagram;
import jd.data.Diagram;

// CONNECTOR TO SHOW THE METHOD USE RELATIONSHIP
public class MethodLines extends Line{
    
    Polygon arrow ;
    Pane arrowContainer;
    
    //SPLIT STUFF
    Line childHalf = null;
    InheritanceLines parentHalf = null;
    Pane ellipseContainer;
    boolean alreadySplit = false;
    
    Diagram childDiagram;
    Diagram parentDiagram;
    
    public MethodLines(){
        //this = new Line();
        arrow = new Polygon();
        arrowContainer = new Pane();
    }
    
    public Pane getArrowContainer(){
        return arrowContainer;
    }
    
    double dragDelX;
    double dragDelY;
    
    public MethodLines(Diagram childDiagram,Diagram parentDiagram,VBox child,VBox parent,Pane pane){
        
        this.childDiagram = childDiagram;
        this.parentDiagram = parentDiagram;
        
        //this = new Line();
        arrow = new Polygon();
        arrowContainer = new Pane();
        
        pane.getChildren().add(this);
        pane.getChildren().add(arrowContainer);
        
        //  SET THE POSITION OF LINE
        this.setStartX(child.getLayoutX());
        this.setStartY(child.getLayoutY());
        this.setEndX(parent.getLayoutX());
        this.setEndY(parent.getLayoutY());
        this.setStyle("-fx-stroke-dash-array: 2 12 12 2");
        
        // SET THE POSITION OF ARROW
        arrow.getPoints().addAll(new Double[]{
            0.0,0.0,
            10.0,2.0,
            2.0,10.0
        });
        
        arrow.setStroke(Color.WHITE);
        arrow.setRotate(135);
        
        // TO MAKE AN DIAMOND
        arrow.setRotate(45);
        arrow.setStroke(Color.WHEAT);
        arrowContainer.getChildren().add(arrow);
        
        arrowContainer.setLayoutX(parent.getLayoutX());
        arrowContainer.setLayoutY(parent.getLayoutY());
        
        arrowContainer.layoutXProperty().bind(parent.layoutXProperty());
        arrowContainer.layoutYProperty().bind(parent.layoutYProperty());
        
        this.startXProperty().bind(child.layoutXProperty());
        this.startYProperty().bind(child.layoutYProperty());
        
        this.endXProperty().bind(parent.layoutXProperty());
        this.endYProperty().bind(parent.layoutYProperty());
        lineSplit(this,child,parent,pane);
    }
    
    public void lineSplit(MethodLines line,VBox child, VBox parent,Pane pane){
                 
        this.setOnMouseClicked(new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent me) {
                
                if(me.getClickCount()>=2){
                    
                    if(!alreadySplit){

                        line.setVisible(false);

                        Ellipse el = new Ellipse();
                        el.setRadiusX(5);
                        el.setRadiusY(5);
                        el.setStroke(Color.BLUEVIOLET);
                        pane.getChildren().add(el);

                        ellipseContainer = new Pane();
                        ellipseContainer.setLayoutX((child.getLayoutX()+parent.getLayoutX())/2);
                        ellipseContainer.setLayoutY((child.getLayoutY()+parent.getLayoutY())/2);
                        ellipseContainer.getChildren().add(el);
                        pane.getChildren().add(ellipseContainer);

                        childHalf = new Line();
                        pane.getChildren().add(childHalf);
                        childHalf.startXProperty().bind(child.layoutXProperty());
                        childHalf.startYProperty().bind(child.layoutYProperty());
                        childHalf.endXProperty().bind(ellipseContainer.layoutXProperty());
                        childHalf.endYProperty().bind(ellipseContainer.layoutYProperty());

                        parentHalf = new InheritanceLines();
                        pane.getChildren().add(parentHalf);
                        parentHalf.endXProperty().bind(parent.layoutXProperty());
                        parentHalf.endYProperty().bind(parent.layoutYProperty());
                        parentHalf.startXProperty().bind(ellipseContainer.layoutXProperty());
                        parentHalf.startYProperty().bind(ellipseContainer.layoutYProperty());

                        alreadySplit = true;
                        
                        ellipseContainer.setOnMousePressed(new EventHandler<MouseEvent>(){
                            @Override
                            public void handle(MouseEvent event) {
                                dragDelX = ellipseContainer.getLayoutX()-event.getSceneX();
                                dragDelY = ellipseContainer.getLayoutY()-event.getSceneY();
                            }
                        });

                        ellipseContainer.setOnMouseDragged(new EventHandler<MouseEvent>(){
                            @Override
                            public void handle(MouseEvent event) {
                                ellipseContainer.setLayoutX(dragDelX+event.getSceneX());
                                ellipseContainer.setLayoutY(dragDelY+event.getSceneY());
                            }
                        });
                        
                        ellipseContainer.setOnMouseClicked(e ->{
                            if(e.getClickCount() >=2){
                                if(alreadySplit){
                                    alreadySplit = false;
                                    line.setVisible(true);
                                    pane.getChildren().remove(childHalf);
                                    pane.getChildren().remove(parentHalf);
                                    pane.getChildren().remove(ellipseContainer);
                                }    
                            }
                        });
                    }
                }
            }
            
        });
    }
    public Diagram getChildDiagram() {
        return childDiagram;
    }

    public Diagram getParentDiagram() {
        return parentDiagram;
    }
}
