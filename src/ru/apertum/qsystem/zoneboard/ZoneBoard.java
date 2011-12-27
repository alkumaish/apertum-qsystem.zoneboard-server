/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.apertum.qsystem.zoneboard;

import java.util.LinkedHashSet;
import ru.apertum.qsystem.client.forms.FIndicatorBoard;
import ru.apertum.qsystem.common.CustomerState;
import ru.apertum.qsystem.server.controller.QIndicatorBoardMonitor;

/**
 *
 * @author Evgeniy Egorov
 */
public class ZoneBoard extends QIndicatorBoardMonitor {

    public void setForm(FIndicatorBoard form){
        indicatorBoard = form;
        initIndicatorBoard();
    }

    @Override
    protected void initIndicatorBoard() {
        setLinesCount(indicatorBoard.getLinesCount());
        setPause(indicatorBoard.getPause());
        if (records.size() != 0) {
            showOnBoard(new LinkedHashSet<>(records.values()));
        }
    }
    
    //**************************************************************************
    //************************** Методы взаимодействия *************************

    public synchronized void inviteCustomer(String userName, String userPoint, String customerPrefix, int customerNumber, int userAddrRS) {
        Record rec = records.get(userName);
        if (rec == null) {
            rec = new Record(userName, userPoint, customerPrefix + customerNumber, userAddrRS, getPause());
        } else {
            addItem(rec);
        }
        show(rec);
    }

    /**
     * На табло оператора долженн перестать мигать номер вызываемого клиента
     * @param user пользователь, который начал работать с клиентом.
     */
    @SuppressWarnings("empty-statement")
    public synchronized void workCustomer(String userName, String userPoint, String customerPrefix, int customerNumber, int userAddrRS) {
        Record rec = records.get(userName);
        //запись может быть не найдена после рестарта сервера, список номеров на табло не бакапится
        if (rec == null) {
            rec = new Record(userName, userPoint, customerPrefix + customerNumber, userAddrRS, getPause());
            ;
        }
        rec.setState(CustomerState.STATE_WORK);
        show(rec);
    }

    /**
     * На табло по определенному адресу должно отчистиццо табло
     * @param user пользователь, который удалил клиента.
     */
    public synchronized void killCustomer(String userName) {
        final Record rec = records.get(userName);
        //запись может быть не найдена после рестарта сервера, список номеров на табло не бакапится
        if (rec != null) {
            rec.setState(CustomerState.STATE_DEAD);
            removeItem(rec);
            show(rec);
        }
    }
}
