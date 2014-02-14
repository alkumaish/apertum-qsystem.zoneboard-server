/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.apertum.qsystem.zoneboard;

import java.io.File;
import java.util.LinkedHashSet;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import ru.apertum.qsystem.client.forms.FIndicatorBoard;
import ru.apertum.qsystem.common.CustomerState;
import ru.apertum.qsystem.common.model.QCustomer;
import ru.apertum.qsystem.server.controller.QIndicatorBoardMonitor;
import ru.apertum.qsystem.server.model.QUser;
import static ru.apertum.qsystem.zoneboard.Run.getConfig;
import ru.apertum.qsystem.zoneboard.common.Uses;

/**
 *
 * @author Evgeniy Egorov
 */
public class ZoneBoard extends QIndicatorBoardMonitor {

    final private ZoneServerProperty props;

    public final Element getConfig(String fileName) {
        final String configFile1;
        if (new File(fileName).exists()) {
            configFile1 = fileName;
        } else {
            configFile1 = props.getConfigFile();
        }
        final File boardFile = new File(configFile1);
        if (boardFile.exists()) {
            try {
                return new SAXReader(false).read(boardFile).getRootElement();
            } catch (DocumentException ex) {
                Uses.log.logger.error("Невозможно прочитать файл конфигурации главного табло. " + ex.getMessage());
                return DocumentHelper.createElement("Ответ");
            }
        } else {
            Uses.log.logger.warn("Файл конфигурации главного табло \"" + configFile1 + "\" не найден. ");
            return DocumentHelper.createElement("Ответ");
        }
    }

    /**
     *
     * @param props
     * @param p
     */
    public ZoneBoard(ZoneServerProperty props, WindowProperty p) {
        this.props = props;
        final FIndicatorBoard w = FIndicatorBoard.getIndicatorBoardForZone(getConfig(p.getConfigFile() == null ? "" : p.getConfigFile()), Uses.isDebug);
        setForm(w);
    }

    public final void setForm(FIndicatorBoard form) {
        indicatorBoard = form;
        initIndicatorBoard();
    }
    
    public FIndicatorBoard getForm(){
        return indicatorBoard;
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
    @Override
    public synchronized void inviteCustomer(QUser user, QCustomer customer) {
        //super.inviteCustomer(user, customer); //To change body of generated methods, choose Tools | Templates.
        Record rec = records.get(user.getName());
        if (rec == null) {
            rec = new Record(user.getName(), user.getPoint(), customer.getFullNumber(), "", user.getAdressRS(), getPause());
        } else {
            addItem(rec);
        }
        show(rec);
    }

    @Override
    public synchronized void workCustomer(QUser user) {
        Record rec = records.get(user.getName());
        //запись может быть не найдена после рестарта сервера, список номеров на табло не бакапится
        if (rec == null) {
            rec = new Record(user.getName(), user.getPoint(), user.getCustomer().getFullNumber(), "", user.getAdressRS(), getPause());

        }
        rec.setState(CustomerState.STATE_WORK);
        show(rec);
    }

    @Override
    public synchronized void killCustomer(QUser user) {
        final Record rec = records.get(user.getName());
        //запись может быть не найдена после рестарта сервера, список номеров на табло не бакапится
        if (rec != null) {
            rec.setState(CustomerState.STATE_DEAD);
            removeItem(rec);
            show(rec);
        }
    }
}
