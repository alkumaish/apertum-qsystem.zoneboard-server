/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.apertum.qsystem.zoneboard.form;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import javax.swing.table.AbstractTableModel;
import javax.xml.bind.JAXBException;
import ru.apertum.qsystem.zoneboard.ZoneServerProperty;
import ru.apertum.qsystem.zoneboard.Run;
import ru.apertum.qsystem.zoneboard.common.Uses;

/**
 *
 * @author egorov
 */
public class AdminTableModel extends AbstractTableModel {

    final private ZoneServerProperty property;

    public AdminTableModel(ZoneServerProperty property) {
        this.property = property;
    }

    @Override
    public int getRowCount() {
        return property.windows.size();
    }

    @Override
    public int getColumnCount() {
        return property.getZoneList().size() + 1 + 1;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        if (columnIndex == getColumnCount() - 1) {
            return property.windows.get(rowIndex).getDisplay();
        }

        return columnIndex == 0
                ? property.windows.get(rowIndex).getName()
                : (property.windows.get(rowIndex).getZones().contains(property.getZoneList().get(columnIndex - 1).getZone()));
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex < getColumnCount() - 1) {
            if ((Boolean) aValue) {
                property.windows.get(rowIndex).getZones().add(property.getZoneList().get(columnIndex - 1).getZone());
            } else {
                property.windows.get(rowIndex).getZones().remove(property.getZoneList().get(columnIndex - 1).getZone());
            }
            Uses.log.logger.debug("Изменили настройку вывода для окна: " + property.windows.get(rowIndex).getName() + ((Boolean) aValue ? " добавили " : " убрали ") + "окно " + property.getZoneList().get(columnIndex - 1).getName());

        } else {
            property.windows.get(rowIndex).setDisplay((Integer) aValue);
            Uses.log.logger.debug("Изменили настройку вывода для окна: " + property.windows.get(rowIndex).getName() + " на монитор " + aValue);
        }


        try {
            property.marshal(new FileOutputStream(new File(Run.filePropName)));
        } catch (FileNotFoundException ex) {
            throw new Uses.SilentException("Не найден файл сохранения настроек." + ex.toString());
        } catch (JAXBException ex) {
            throw new Uses.SilentException("Загнулся маршалинг. Изменение расстановой не пошло на пользу. " + ex.toString());
        }

        Run.refreshWindowsTitle();
    }

    @Override
    public void fireTableDataChanged() {
        super.fireTableDataChanged();
        try {
            property.marshal(new FileOutputStream(new File(Run.filePropName)));
        } catch (FileNotFoundException ex) {
            throw new Uses.SilentException("Не найден файл сохранения настроек." + ex.toString());
        } catch (JAXBException ex) {
            throw new Uses.SilentException("Загнулся маршалинг. Изменение расстановой не пошло на пользу. " + ex.toString());
        }
    }

    @Override
    public void fireTableStructureChanged() {
        super.fireTableStructureChanged();
        try {
            property.marshal(new FileOutputStream(new File(Run.filePropName)));
        } catch (FileNotFoundException ex) {
            throw new Uses.SilentException("Не найден файл сохранения настроек." + ex.toString());
        } catch (JAXBException ex) {
            throw new Uses.SilentException("Загнулся маршалинг. Изменение расстановой не пошло на пользу. " + ex.toString());
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == getColumnCount() - 1) {
            return Integer.class;
        }
        return columnIndex == 0 ? String.class : Boolean.class;
    }

    @Override
    public String getColumnName(int column) {
        /*
        if (column == getColumnCount() - 2) {
        return "Блины";
        }
         * 
         */
        if (column == getColumnCount() - 1) {
            return "Монитор";
        }

        return column == 0 ? "" : property.getZoneList().get(column - 1).getName() + " / " + property.getZoneList().get(column - 1).getZone();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex != 0;
    }
}
