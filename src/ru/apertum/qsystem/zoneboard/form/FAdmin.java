/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * FAdmin.java
 *
 * Created on 29.06.2010, 15:48:00
 */
package ru.apertum.qsystem.zoneboard.form;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.xml.bind.JAXBException;
import org.jdesktop.application.Action;
import ru.apertum.qsystem.zoneboard.AdvWindowProperty;
import ru.apertum.qsystem.zoneboard.ZoneServerProperty;
import ru.apertum.qsystem.zoneboard.Run;
import ru.apertum.qsystem.zoneboard.WindowProperty;
import ru.apertum.qsystem.zoneboard.common.Uses;

/**
 *
 * @author egorov
 */
public class FAdmin extends javax.swing.JFrame {

    static private ZoneServerProperty props;
    final static private JComboBox<Integer> cb = new JComboBox<>();
    final static private TableCellEditor editor = new DefaultCellEditor(cb);

    static {
        cb.addItem(1);
        cb.addItem(2);
        cb.addItem(3);
        cb.addItem(4);
        cb.addItem(5);
        cb.addItem(6);
        cb.addItem(7);
        cb.addItem(8);
    }

    /** Creates new form FAdmin */
    public FAdmin(ZoneServerProperty property) {
        initComponents();
        setTitle("Управление зональными табло");
        table.setModel(new AdminTableModel(property));
        setEditorMonNum();
        table.setRowHeight(30);
        Run.refreshWindowsTitle();

        textFieldDeffProp.setText(property.getConfigFile());
        spinnerPort.getModel().setValue(property.getPort());
        comboBoxTypeInvite.setSelectedIndex(property.getInviteType());
        comboBoxVoice.setSelectedIndex(property.getVoiceType());
        comboBoxPointType.setSelectedIndex(property.getPointType());
        listAdvWins.setModel(new DefaultComboBoxModel(property.getAdvWindows().toArray(new AdvWindowProperty[0])));

        comboBoxTypeInvite.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                props.setInviteType(comboBoxTypeInvite.getSelectedIndex());
                Uses.log.logger.debug("Изменили вызов: " + comboBoxTypeInvite.getSelectedIndex());
                saveProp();
            }
        });
        
        comboBoxVoice.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                props.setVoiceType(comboBoxVoice.getSelectedIndex());
                Uses.log.logger.debug("Изменили голос: " + comboBoxVoice.getSelectedIndex());
                saveProp();
            }
        });
        
        comboBoxPointType.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                props.setPointType(comboBoxPointType.getSelectedIndex());
                Uses.log.logger.debug("Изменили тип пункта: " + comboBoxPointType.getSelectedIndex());
                saveProp();
            }
        });

        spinnerPort.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                props.setPort((Integer) spinnerPort.getModel().getValue());
                Uses.log.logger.debug("Изменили port: " + spinnerPort.getModel().getValue());
                saveProp();
            }
        });

        textFieldDeffProp.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                props.setConfigFile(textFieldDeffProp.getText());
                Uses.log.logger.debug("Изменили дефолтный конфиг: " + textFieldDeffProp.getText());
                saveProp();
            }
        });
    }

    private void saveProp() {
        try {
            props.marshal(new FileOutputStream(new File(Run.filePropName)));
        } catch (FileNotFoundException ex) {
            throw new Uses.SilentException("Не найден файл сохранения настроек." + ex.toString());
        } catch (JAXBException ex) {
            throw new Uses.SilentException("Загнулся маршалинг. Изменение расстановой не пошло на пользу. " + ex.toString());
        }
    }

    private void setEditorMonNum() {
        final TableColumnModel cm = table.getColumnModel();
        final TableColumn tc = cm.getColumn(table.getColumnCount() - 1 - 1);
        tc.setCellEditor(editor);
    }

    public FAdmin() {
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        popupMenuAdvWins = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItem2 = new javax.swing.JMenuItem();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        buttonDeleteZone = new javax.swing.JButton();
        buttonAddZone = new javax.swing.JButton();
        buttonAddTablo = new javax.swing.JButton();
        buttonDeleteTablo = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        listAdvWins = new javax.swing.JList();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        textFieldDeffProp = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        spinnerPort = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        comboBoxTypeInvite = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        comboBoxVoice = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        comboBoxPointType = new javax.swing.JComboBox();

        popupMenuAdvWins.setName("popupMenuAdvWins"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(ru.apertum.qsystem.QSystem.class).getContext().getActionMap(FAdmin.class, this);
        jMenuItem1.setAction(actionMap.get("addAdvWin")); // NOI18N
        jMenuItem1.setName("jMenuItem1"); // NOI18N
        popupMenuAdvWins.add(jMenuItem1);

        jSeparator1.setName("jSeparator1"); // NOI18N
        popupMenuAdvWins.add(jSeparator1);

        jMenuItem2.setAction(actionMap.get("removeAdvWin")); // NOI18N
        jMenuItem2.setName("jMenuItem2"); // NOI18N
        popupMenuAdvWins.add(jMenuItem2);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        table.setFont(new java.awt.Font("Tahoma", 1, 12));
        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        table.setName("table"); // NOI18N
        table.setRowSelectionAllowed(false);
        jScrollPane1.setViewportView(table);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setName("jPanel1"); // NOI18N

        buttonDeleteZone.setText("Удалить зону");
        buttonDeleteZone.setName("buttonDeleteZone"); // NOI18N
        buttonDeleteZone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDeleteZoneActionPerformed(evt);
            }
        });

        buttonAddZone.setText("Добавить зону");
        buttonAddZone.setName("buttonAddZone"); // NOI18N
        buttonAddZone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAddZoneActionPerformed(evt);
            }
        });

        buttonAddTablo.setText("Добавить табло");
        buttonAddTablo.setName("buttonAddTablo"); // NOI18N
        buttonAddTablo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAddTabloActionPerformed(evt);
            }
        });

        buttonDeleteTablo.setText("Удалить табло");
        buttonDeleteTablo.setName("buttonDeleteTablo"); // NOI18N
        buttonDeleteTablo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDeleteTabloActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(buttonAddTablo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonDeleteTablo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 288, Short.MAX_VALUE)
                .addComponent(buttonAddZone)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonDeleteZone)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonDeleteZone)
                    .addComponent(buttonAddZone)
                    .addComponent(buttonAddTablo)
                    .addComponent(buttonDeleteTablo))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setName("jPanel2"); // NOI18N

        jScrollPane2.setBorder(javax.swing.BorderFactory.createTitledBorder("Рекламные окна с gif-анимацией"));
        jScrollPane2.setName("jScrollPane2"); // NOI18N

        listAdvWins.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        listAdvWins.setComponentPopupMenu(popupMenuAdvWins);
        listAdvWins.setName("listAdvWins"); // NOI18N
        jScrollPane2.setViewportView(listAdvWins);

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.setName("jPanel3"); // NOI18N

        jLabel1.setText("Файл настроек по умолчанию");
        jLabel1.setName("jLabel1"); // NOI18N

        textFieldDeffProp.setText("jTextField1");
        textFieldDeffProp.setName("textFieldDeffProp"); // NOI18N

        jLabel2.setText("Порт приема команд от плагина");
        jLabel2.setName("jLabel2"); // NOI18N

        spinnerPort.setModel(new javax.swing.SpinnerNumberModel(27007, 255, 64000, 1));
        spinnerPort.setName("spinnerPort"); // NOI18N

        jLabel3.setText("Воспроизводить голосовой вызов");
        jLabel3.setName("jLabel3"); // NOI18N

        comboBoxTypeInvite.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Нет", "Сигнал", "Сигнал, повторно голосом", "Сигнал и голос" }));
        comboBoxTypeInvite.setName("comboBoxTypeInvite"); // NOI18N

        jLabel4.setText("Голос");
        jLabel4.setName("jLabel4"); // NOI18N

        comboBoxVoice.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Даша", "Алёна", "Николай", "Ольга" }));
        comboBoxVoice.setBorder(javax.swing.BorderFactory.createTitledBorder("Установи плагин"));
        comboBoxVoice.setEnabled(false);
        comboBoxVoice.setName("comboBoxVoice"); // NOI18N

        jLabel5.setText("Тип пункта приема");
        jLabel5.setName("jLabel5"); // NOI18N

        comboBoxPointType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Кабинет", "Окно", "Стойка" }));
        comboBoxPointType.setName("comboBoxPointType"); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textFieldDeffProp, javax.swing.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spinnerPort, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(comboBoxTypeInvite, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(comboBoxVoice, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(comboBoxPointType, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(textFieldDeffProp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(spinnerPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(comboBoxTypeInvite, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboBoxVoice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboBoxPointType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addContainerGap(55, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 760, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonAddZoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAddZoneActionPerformed
        final String zoneName = (String) JOptionPane.showInputDialog(this, "Введите название зоны", "Название зоны", 3, null, null, "Зона");
        if (zoneName == null) {
            return;
        }
        if ("".equals(zoneName)) {
            JOptionPane.showConfirmDialog(this, "Название не должно быть пустым.", "Недопустимое значение", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            return;
        }
        for (ZoneServerProperty.Zone z : props.getZoneList()) {
            if (z.getName().equalsIgnoreCase(zoneName)) {
                JOptionPane.showConfirmDialog(this, "Зона с таким названием уже существует.", "Недопустимое значение", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        String res = (String) JOptionPane.showInputDialog(this, "Введите номер зоны", "Номер зоны", 3, null, null, 1);
        if (res == null) {
            return;
        }

        if (!res.matches("^-?\\d+$")) {
            JOptionPane.showConfirmDialog(this, "Требуется ввести натуральное число.", "Недопустимое значение", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            return;
        }
        final Integer zone = Integer.parseInt(res);

        for (ZoneServerProperty.Zone z : props.getZoneList()) {
            if (z.getZone().equals(zone)) {
                JOptionPane.showConfirmDialog(this, "Зона с таким номером уже существует.", "Недопустимое значение", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        props.getZoneList().add(new ZoneServerProperty.Zone(zoneName, zone));

        ((AdminTableModel) table.getModel()).fireTableStructureChanged();
        setEditorMonNum();
    }//GEN-LAST:event_buttonAddZoneActionPerformed

    private void buttonAddTabloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAddTabloActionPerformed
        final String tabloName = (String) JOptionPane.showInputDialog(this, "Введите название табло", "Название табло", 3, null, null, "Табло");
        if (tabloName == null) {
            return;
        }
        if ("".equals(tabloName)) {
            JOptionPane.showConfirmDialog(this, "Название не должно быть пустым.", "Недопустимое значение", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            return;
        }
        for (WindowProperty p : props.windows) {
            if (p.getName().equalsIgnoreCase(tabloName)) {
                JOptionPane.showConfirmDialog(this, "Табло с таким названием уже существует.", "Недопустимое значение", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        final WindowProperty p = new WindowProperty();
        p.setName(tabloName);
        p.setDisplay(1);
        props.windows.add(p);

        ((AdminTableModel) table.getModel()).fireTableDataChanged();
    }//GEN-LAST:event_buttonAddTabloActionPerformed

    private void buttonDeleteTabloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDeleteTabloActionPerformed
        final String[] st = new String[props.windows.size()];
        int i = 0;
        for (WindowProperty p : props.windows) {
            st[i++] = p.getName();
        }
        final String tabloName = (String) JOptionPane.showInputDialog(this, "Удаление табло", "Выберите табло", 3, null, st, st[0]);
        if (tabloName == null) {
            return;
        }
        for (WindowProperty p : props.windows) {
            if (p.getName().equalsIgnoreCase(tabloName)) {
                props.windows.remove(p);
                ((AdminTableModel) table.getModel()).fireTableDataChanged();
                return;
            }
        }

    }//GEN-LAST:event_buttonDeleteTabloActionPerformed

    private void buttonDeleteZoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDeleteZoneActionPerformed
        final String[] st = new String[props.getZoneList().size()];
        int i = 0;
        for (ZoneServerProperty.Zone z : props.getZoneList()) {
            st[i++] = z.getName();
        }
        final String zoneName = (String) JOptionPane.showInputDialog(this, "Удаление зоны", "Выберите зону", 3, null, st, st[0]);
        if (zoneName == null) {
            return;
        }
        for (ZoneServerProperty.Zone z : props.getZoneList()) {
            if (z.getName().equalsIgnoreCase(zoneName)) {
                for (WindowProperty p : props.windows) {
                    for (Integer zone : p.getZones()) {
                        if (zone.equals(z.getZone())) {
                            p.getZones().remove(zone);
                            break;
                        }
                    }
                }
                props.getZoneList().remove(z);
                ((AdminTableModel) table.getModel()).fireTableStructureChanged();
                setEditorMonNum();
                return;
            }
        }
    }//GEN-LAST:event_buttonDeleteZoneActionPerformed

    /**
     * JOptionPane.showMessageDialog(this, "Обновление завершено успешно.", "Обновление", JOptionPane.INFORMATION_MESSAGE);
     * @param args the command line arguments
     */
    public static void main(String args[]) throws FileNotFoundException {
        Uses.isDebug = Uses.setLogining(args);
        final InputStream insProp;
        if (args.length != 0) {
            final File f = new File(args[0]);
            if (f.exists()) {
                insProp = new FileInputStream(f);
                Run.filePropName = args[0];
            } else {
                throw new FileNotFoundException("Not found the configuration file config.xml in first parameter path: " + args[0]);
            }
        } else {
            final File f = new File("config/config.xml");
            if (f.exists()) {
                insProp = new FileInputStream(f);
                Run.filePropName = "config/config.xml";
            } else {
                throw new FileNotFoundException("Not found the configuration file config.xml in current directory.");
            }
        }
        try {
            props = ZoneServerProperty.unmarshal(insProp);
        } catch (JAXBException ex) {
            throw new Uses.SilentException(ex.toString());
        }


        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                final FAdmin form = new FAdmin(props);
                Uses.setLocation(form);
                form.setVisible(true);
            }
        });
    }

    @Action
    public void addAdvWin() {
        final String advWinFile = (String) JOptionPane.showInputDialog(this, "Введите имя файла анимированного GIF", "Фнимированный GIF", 3, null, null, "");
        if (advWinFile == null) {
            return;
        }
        if ("".equals(advWinFile)) {
            JOptionPane.showConfirmDialog(this, "Путь не должен быть пустым.", "Недопустимое значение", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            return;
        }

        final Integer nMon = (Integer) JOptionPane.showInputDialog(this, "Введите номер монитора вывода анимированного GIF", "Номер монитора", 3, null, new Integer[]{1, 2, 3, 4, 5, 6, 7, 8}, 2);
        if (nMon == null) {
            return;
        }

        final AdvWindowProperty p = new AdvWindowProperty();
        p.setDisplay(nMon);
        p.setFilePath(advWinFile);
        props.getAdvWindows().add(p);

        listAdvWins.setModel(new DefaultComboBoxModel(props.getAdvWindows().toArray(new AdvWindowProperty[0])));

        saveProp();
    }

    @Action
    public void removeAdvWin() {
        if (listAdvWins.getSelectedIndex() != -1) {
            for (AdvWindowProperty win : props.getAdvWindows()) {
                if (win.getDisplay() == ((AdvWindowProperty) listAdvWins.getSelectedValue()).getDisplay()
                        && win.getFilePath().equalsIgnoreCase(((AdvWindowProperty) listAdvWins.getSelectedValue()).getFilePath())) {
                    props.getAdvWindows().remove(win);
                    listAdvWins.setModel(new DefaultComboBoxModel(props.getAdvWindows().toArray(new AdvWindowProperty[0])));
                    saveProp();
                    return;
                }
            }
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonAddTablo;
    private javax.swing.JButton buttonAddZone;
    private javax.swing.JButton buttonDeleteTablo;
    private javax.swing.JButton buttonDeleteZone;
    private javax.swing.JComboBox comboBoxPointType;
    private javax.swing.JComboBox comboBoxTypeInvite;
    private javax.swing.JComboBox comboBoxVoice;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JList listAdvWins;
    private javax.swing.JPopupMenu popupMenuAdvWins;
    private javax.swing.JSpinner spinnerPort;
    private javax.swing.JTable table;
    private javax.swing.JTextField textFieldDeffProp;
    // End of variables declaration//GEN-END:variables
}
