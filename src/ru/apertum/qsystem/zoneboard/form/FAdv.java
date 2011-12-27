/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * FAdv.java
 *
 * Created on 01.07.2010, 16:01:31
 */
package ru.apertum.qsystem.zoneboard.form;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import ru.apertum.qsystem.zoneboard.AdvWindowProperty;
import ru.apertum.qsystem.zoneboard.common.Uses;

/**
 *
 * @author egorov
 */
public class FAdv extends javax.swing.JFrame {

    final private AdvWindowProperty property;

    public AdvWindowProperty getProperty() {
        return property;
    }

    /** Creates new form FGrid */
    public FAdv(AdvWindowProperty property, boolean isDebug) {
        initComponents();
        this.property = property;
        labelImage.setIcon(new ImageIcon(property.getFilePath()));
        if (!Uses.isDebug) {
            addWindowListener(new WindowAdapter() {

                @Override
                public void windowOpened(WindowEvent e) {
                    setExtendedState(JFrame.MAXIMIZED_BOTH);
                }
            });
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelImage = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        labelImage.setBackground(new java.awt.Color(255, 255, 255));
        labelImage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelImage.setName("labelImage"); // NOI18N
        labelImage.setOpaque(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labelImage, javax.swing.GroupLayout.DEFAULT_SIZE, 661, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labelImage, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel labelImage;
    // End of variables declaration//GEN-END:variables
}
