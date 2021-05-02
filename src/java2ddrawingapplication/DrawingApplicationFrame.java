/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package java2ddrawingapplication;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;

/**
 *
 * @author acv
 */
public class DrawingApplicationFrame extends JFrame
{

    // Create the panels for the top of the application. One panel for each
    // line and one to contain both of those panels.
    private final JPanel panel1;
    private final JPanel panel2;
    private final JPanel enclosingPanel;

    // create the widgets for the firstLine Panel.
    private final JButton undo;
    private final JButton clear;
    private final JLabel shapeLabel;
    private final JComboBox<String> shapeList;
    private static final String[] shapes = {"Line", "Oval", "Rectangle"};
    private final JCheckBox filledBox;
    
  
    //create the widgets for the secondLine Panel.
    private final JCheckBox gradient;
    private final JButton color1;
    private final JButton color2;
    private final JTextField lineWidth;
    private final JLabel widthLabel;
    private final JTextField dashLength;
    private final JLabel lengthLabel;
    private final JCheckBox dashedBox;
    // Variables for drawPanel.
    private final JPanel drawPanel;
    private String currShape;
    private Color regularColor;
    private Color gradientColor;
    private ArrayList<MyShapes> drawings;
    private MyShapes newShape;
    // add status label
    private final JLabel statusLabel;
    // Constructor for DrawingApplicationFrame
    public DrawingApplicationFrame()
    {
        super("Java 2D Drawings");
        setLayout(new BorderLayout());
        
        // add widgets to panels
        
        panel1 = new JPanel();
        panel2 = new JPanel();
        
        // firstLine widgets
        
        undo = new JButton("Undo");
        clear = new JButton("Clear");
        shapeLabel = new JLabel("Shape:");
        shapeList = new JComboBox(shapes);
        currShape = shapes[0];
        filledBox = new JCheckBox("Filled");
        
        ButtonHandler buttonHandler = new ButtonHandler();
        undo.addActionListener(buttonHandler);
        clear.addActionListener(buttonHandler);
        shapeList.addItemListener(new ComboBoxHandler());
        
        panel1.add(undo);
        panel1.add(clear);
        panel1.add(shapeLabel);
        panel1.add(shapeList);
        panel1.add(filledBox);
        
        // secondLine widgets
        
        gradient = new JCheckBox("Use Gradient");
        color1 = new JButton("Color 1");
        color2 = new JButton("Color 2");
        widthLabel = new JLabel("Line Width:");
        lineWidth = new JTextField("10", 2);
        lengthLabel = new JLabel("Dash Length:");
        dashLength = new JTextField("15", 2);
        dashedBox = new JCheckBox("Dashed");
        
        color1.addActionListener(buttonHandler);
        color2.addActionListener(buttonHandler);
  
        
        panel2.add(gradient);
        panel2.add(color1);
        panel2.add(color2);
        panel2.add(widthLabel);
        panel2.add(lineWidth);
        panel2.add(lengthLabel);
        panel2.add(dashLength);
        panel2.add(dashedBox);
        
        // add top panel of two panels
        
        enclosingPanel = new JPanel();
        enclosingPanel.setLayout(new GridLayout(2,1));
        enclosingPanel.add(panel1);
        enclosingPanel.add(panel2);
        
        // add topPanel to North, drawPanel to Center, and statusLabel to South
        
        add(enclosingPanel, BorderLayout.NORTH);
        drawPanel = new DrawPanel();
        drawPanel.setBackground(Color.WHITE);
        //drawPanel.addMouseMotionListener(new MouseHandler());
        add(drawPanel, BorderLayout.CENTER);
        statusLabel = new JLabel();
        add(statusLabel, BorderLayout.SOUTH);
        
        drawings = new ArrayList<MyShapes>();
        regularColor = Color.BLACK;
        gradientColor = Color.WHITE;
        //add listeners and event handlers
    }

    // Create event handlers, if needed

    // Create a private inner class for the DrawPanel.
    private class DrawPanel extends JPanel
    {

        public DrawPanel()
        {
            addMouseMotionListener(new MouseHandler());
            addMouseListener(new MouseHandler());
        }

        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            //loop through and draw each shape in the shapes arraylist
            for(MyShapes shape : drawings){
                shape.draw(g2d);
            }
            
        }


        private class MouseHandler extends MouseAdapter implements MouseMotionListener
        {

            public void mousePressed(MouseEvent event)
            {
                Stroke stroke;
                if (dashedBox.isSelected()){
                    float dash[] = {Integer.parseInt(dashLength.getText())};
                    stroke = new BasicStroke(Integer.parseInt(lineWidth.getText()), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10, dash, 0);
                }else{
                    stroke = new BasicStroke(Integer.parseInt(lineWidth.getText()), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
                }
                
                Paint paint;
                if(gradient.isSelected()){
                    paint = new GradientPaint(0, 0, regularColor, 50, 50, gradientColor, true);
                }else{
                    paint = regularColor;
                        
                }
                
                if(currShape.equals("Oval")){
                    if(filledBox.isSelected()){
                        newShape = new MyOval(new Point(event.getX(), event.getY()), new Point(event.getX(), event.getY()), paint, stroke, true);
                    }else{
                        newShape = new MyOval(new Point(event.getX(), event.getY()), new Point(event.getX(), event.getY()), paint, stroke, false);
                    }
                }else if(currShape.equals("Rectangle")){
                    if(filledBox.isSelected()){
                        newShape = new MyRectangle(new Point(event.getX(), event.getY()), new Point(event.getX(), event.getY()), paint, stroke, true);
                    }else{
                        newShape = new MyRectangle(new Point(event.getX(), event.getY()), new Point(event.getX(), event.getY()), paint, stroke, false);
                    }
                }else{
                    newShape = new MyLine(new Point(event.getX(), event.getY()), new Point(event.getX(), event.getY()), paint, stroke);
                }
                
                drawings.add(newShape);
            }

            public void mouseReleased(MouseEvent event)
            {
               repaint();
            }

            @Override
            public void mouseDragged(MouseEvent event)
            {
                drawings.get(drawings.size()- 1).setEndPoint(new Point(event.getX(), event.getY()));
                repaint();
                statusLabel.setText(String.format("(%d, %d)", event.getX(), event.getY()));
            }
            
            @Override
            public void mouseMoved(MouseEvent event)
            {
                statusLabel.setText(String.format("(%d, %d)", event.getX(), event.getY()));
            }
        }

    }
    
    private class ButtonHandler implements ActionListener{
        
       @Override
       public void actionPerformed(ActionEvent event){
           
           if(event.getSource() == undo){
               if(!drawings.isEmpty()){
                   drawings.remove(drawings.size() - 1);
                   repaint();
               }
           }
           
           if(event.getSource() == clear){
               while(!drawings.isEmpty()){
                   drawings.remove(drawings.size() - 1);
               }
               repaint();   
               
           }
           
           if(event.getSource() == color1){
               Color newColor = JColorChooser.showDialog(null, "Choose a color", regularColor);
               if(newColor != null){
                   regularColor = newColor;
               }
           }
           
           if(event.getSource() == color2){
               Color newColor = JColorChooser.showDialog(null, "Choose a color", gradientColor);
               if(newColor != null){
                   gradientColor = newColor;
               }
           }
           
       }
    }
    
    private class ComboBoxHandler implements ItemListener{
      
        @Override
        public void itemStateChanged(ItemEvent event) {
             
            if(event.getStateChange() == ItemEvent.SELECTED){
                currShape = shapes[shapeList.getSelectedIndex()];
            }
            
        }
    }
}
