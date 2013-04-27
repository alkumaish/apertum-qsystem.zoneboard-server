/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.apertum.qsystem.zoneboard;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author egorov
 */
@XmlRootElement(name = "advWindow")
public class AdvWindowProperty {

    private String filePath;

    public String getFilePath() {
        return filePath;
    }

    @XmlAttribute(name = "filePath", required = true)
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    private int display;

    public int getDisplay() {
        return display;
    }

    @XmlAttribute(name = "display", required = true)
    public void setDisplay(int display) {
        this.display = display;
    }

    @Override
    public String toString() {
        return "Монитор " + display + " Настройки " + filePath;
    }
    
    
}
