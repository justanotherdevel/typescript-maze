package main;



import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
 
import javax.swing.*;
 
public class GridPress {
  private static void createAndShowUI() {
    int width = 7;
    int height = 5;
    GridPressGui view = new GridPressGui(width, height);
    MyGridModel model = new MyGridModel(width, height);
    view.setModel(model);
     
    // for testing purposes
    for (int i = 1; i < 5; i++) {
      model.setCellActive(true, i, 1);
    }
 
    JFrame frame = new JFrame("GridPress");
    frame.getContentPane().add(view.getMainPanel());
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }
 
  public static void main(String[] args) {
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        createAndShowUI();
      }
    });
  }
}
 
class Cell {
  private boolean pressed = false;
  private boolean active = false;
 
  public boolean isPressed() {
    return pressed;
  }
 
  public void setPressed(boolean pressed) {
    this.pressed = pressed;
  }
 
  public boolean isActive() {
    return active;
  }
 
  public void setActive(boolean active) {
    this.active = active;
  }
 
}
 
class MyGridModel {
  public Cell[][] cellGrid;
   
  public MyGridModel(int width, int height) {
    cellGrid = new Cell[width][height];
    for (int col = 0; col < cellGrid.length; col++) {
      for (int row = 0; row < cellGrid[col].length; row++) {
        cellGrid[col][row] = new Cell();
      }
    }
     
  }
   
  public void setCellActive(boolean active, int col, int row) {
    cellGrid[col][row].setActive(active);
  }
 
  public void setCellPressed(boolean pressed, int col, int row) {
    cellGrid[col][row].setPressed(pressed);
  }
 
  public boolean isCellActive(int col, int row) {
    return cellGrid[col][row].isActive();
  }
 
  public boolean isCellPressed(int col, int row) {
    return cellGrid[col][row].isPressed();
  }
}
 
class GridPressGui {
  private static final Dimension GRIDCELL_SIZE = new Dimension(40, 40);
  private static final Color ACTIVE_COLOR = Color.red;
  private static final Color INACTIVE_COLOR = Color.white;
 
  private JPanel mainPanel = new JPanel();
  private MyGridModel model;
  private JLabel[][] labelGrid;
 
  public GridPressGui(int width, int height) {
    mainPanel.setLayout(new GridLayout(height, width));
    labelGrid = new JLabel[height][width];
    MouseListener myMouseListener = new MyMouseListener();
 
    for (int row = 0; row < labelGrid.length; row++) {
      for (int col = 0; col < labelGrid[row].length; col++) {
        JLabel label = new JLabel();
        label.setPreferredSize(GRIDCELL_SIZE);
        label.setOpaque(true);
        label.setBorder(BorderFactory.createLineBorder(Color.black));
        label.addMouseListener(myMouseListener);
        mainPanel.add(label);
        labelGrid[row][col] = label;
      }
    }
  }
   
  public void setModel(MyGridModel model) {
    this.model = model;
  }
 
  public JComponent getMainPanel() {
    return mainPanel;
  }
 
  private class MyMouseListener extends MouseAdapter {
    @Override
    public void mousePressed(MouseEvent e) {
      if (model == null) {
        return;
      }
      JLabel selectedLabel = (JLabel) e.getSource();
      for (int row = 0; row < labelGrid.length; row++) {
        for (int col = 0; col < labelGrid[row].length; col++) {
          if (selectedLabel.equals(labelGrid[row][col])) {
            if (model.isCellPressed(col, row)) {
              return;
            }
            model.setCellPressed(true, col, row);
            if (model.isCellActive(col, row)) {
              selectedLabel.setBackground(ACTIVE_COLOR);
            } else {
              selectedLabel.setBackground(INACTIVE_COLOR);
            }
            return;
          }
        }
      }
    }
  }
 
}