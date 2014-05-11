/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * FZoneBoard.java
 *
 * Created on 18.06.2010, 12:23:26
 */
package ru.apertum.qsystem.zoneboard.form;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import ru.apertum.qsystem.zoneboard.WindowProperty;
import ru.apertum.qsystem.zoneboard.common.Uses;

/**
 *
 * @author egorov
 */
public class FZoneBoard extends javax.swing.JFrame {

    final private WindowProperty property;

    public WindowProperty getProperty() {
        return property;
    }

    public boolean hasZone(Integer port) {
        return getProperty().getZones().contains(port);
    }
    private final Checker checker;
    private final String name;

    /** Creates new form FZoneBoard */
    public FZoneBoard(WindowProperty property) {
        initComponents();
        setLocation((int)Uses.displays.get(property.getDisplay()).getX(), (int)Uses.displays.get(property.getDisplay()).getY());
        //setBounds((int)Uses.displays.get(property.getDisplay()).getX(), (int)Uses.displays.get(property.getDisplay()).getY(), 640, 480);
        this.property = property;
        this.name = property.getName();
        checker = new Checker(property);
        System.out.println("hey!");
        if (!Uses.isDebug) {
            System.out.println("Going to be opend");
            addWindowListener(new WindowAdapter() {

                @Override
                public void windowOpened(WindowEvent e) {
                    System.out.println("Go open");
                    setExtendedState(JFrame.MAXIMIZED_BOTH);
                }
            });
        }
        
    }

    public synchronized void insertMessage(String message, String posName) {
      //  ((EmarkTableModel) table.getModel()).add(message, posName, checker);
      //  table.repaint();
    }

    static public class Checker {

        final WindowProperty property;

        public Checker(WindowProperty property) {
            this.property = property;
        }

    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 895, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 541, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
