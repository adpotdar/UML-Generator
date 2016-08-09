package jd.data;

import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import jd.lines.InheritanceLines;
import jd.lines.MethodLines;
import jd.lines.UseLines;

/** 
 * THIS WILL REPRESENTS THE BLACK BOX THAT APPEARS ON 
 *  DIAGRAM WHEN YOU IMPORT OR USE SOME CLASS THAT HAVE NOT 
 *  BEEN CREATED BY THE USER
 */
public class ForeignClassBox extends Diagram{
 
    Label box;
    VBox vbox;
    
    String name;
    
    double dragDelX;
    double dragDelY;
     
    // TO KEEP TRACK OF LINES
    ArrayList<InheritanceLines> inheritanceLinesComingIn = new ArrayList<InheritanceLines>();
    ArrayList<MethodLines> methodLinesComingIn = new ArrayList<MethodLines>();  
    ArrayList<UseLines> useLinesComingIn  = new ArrayList<UseLines>();
    
    public ForeignClassBox(String s,Pane pane){
        box = new Label(s);
        box.setPrefHeight(20);
        box.setStyle("-fx-border-color: black;");
        
        // INIT NAME
        name =s;
        
        vbox = new VBox();
        vbox.getChildren().add(box);
        
        pane.getChildren().add(vbox);
        
        // SET THE POSITION OF THE BOX
        vbox.setLayoutX(Math.random()*400+100);
        vbox.setLayoutY(Math.random()*60+30);
        
        // DELETE SHT
        vbox.setOnMouseClicked(e ->{
            if(e.getClickCount() >=2){
                pane.getChildren().remove(vbox);
                
                for(InheritanceLines line: inheritanceLinesComingIn){
                    pane.getChildren().remove(line.getArrowContainer());
                    pane.getChildren().remove(line);
                }
                for(MethodLines line : methodLinesComingIn){
                    pane.getChildren().remove(line.getArrowContainer());
                    pane.getChildren().remove(line);
                }
                for(UseLines line: useLinesComingIn){
                    pane.getChildren().remove(line.getArrowContainer());
                    pane.getChildren().remove(line);
                    
                }
            }
        });
        
        // MAKE BOX DRAGGABLE
        box.setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent me) {
            dragDelX = vbox.getLayoutX()-me.getSceneX();
            dragDelY = vbox.getLayoutY()-me.getSceneY();
            }
        });
        
        box.setOnMouseDragged(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent me) {
                vbox.setLayoutX(dragDelX+me.getSceneX());
                vbox.setLayoutY(dragDelY+me.getSceneY());
            }
        });    
    }
    
    public ForeignClassBox(double x,double y,String s,Pane pane){
        box = new Label(s);
        box.setStyle("-fx-border-color: black;");
        box.setPrefSize(40, 40);
        
        // INIT NAME
        name =s;
        
        vbox = new VBox();
        vbox.getChildren().add(box);
        
        pane.getChildren().add(vbox);
        
        // SET THE POSITION OF THE BOX
        vbox.setLayoutX(x);
        vbox.setLayoutY(y);
        
        // DELETE SHT
        vbox.setOnMouseClicked(e ->{
            if(e.getClickCount() >=2){
                pane.getChildren().remove(vbox);
                
                for(InheritanceLines line: inheritanceLinesComingIn){
                    pane.getChildren().remove(line.getArrowContainer());
                    pane.getChildren().remove(line);
                }
                for(MethodLines line : methodLinesComingIn){
                    pane.getChildren().remove(line.getArrowContainer());
                    pane.getChildren().remove(line);
                }
                for(UseLines line: useLinesComingIn){
                    pane.getChildren().remove(line.getArrowContainer());
                    pane.getChildren().remove(line);
                    
                }
            }
        });
        // MAKE BOX DRAGGABLE
        box.setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent me) {
            dragDelX = vbox.getLayoutX()-me.getSceneX();
            dragDelY = vbox.getLayoutY()-me.getSceneY();
            
            }
        });
        
        box.setOnMouseDragged(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent me) {
                vbox.setLayoutX(dragDelX+me.getSceneX());
                vbox.setLayoutY(dragDelY+me.getSceneY());
            }
        });  
        
        
    }
    
    public String getName() {
        return name;
    }
    
    public VBox getVBox(){
        return vbox;
    }
    
    public void addComingInLine(Line line){
        if(line instanceof InheritanceLines){
            inheritanceLinesComingIn.add((InheritanceLines)line);
        }else if(line instanceof MethodLines){
            methodLinesComingIn.add((MethodLines)line);
        }else if(line instanceof UseLines){
            useLinesComingIn.add((UseLines)line);
        }
    }
   
    public ArrayList<InheritanceLines> getInheritanceLinesComingIn() {
        return inheritanceLinesComingIn;
    }

    public ArrayList<MethodLines> getMethodLinesComingIn() {
        return methodLinesComingIn;
    }

    public ArrayList<UseLines> getUseLinesComingIn() {
        return useLinesComingIn;
    }
    
    public void removeLineFromParentHalf(Line line){
        if(line instanceof InheritanceLines){
            this.getInheritanceLinesComingIn().remove(line);
        }else if(line instanceof MethodLines){
            this.getMethodLinesComingIn().remove(line);
        }else if(line instanceof UseLines){
            this.getUseLinesComingIn().remove(line);
        }
    }
}