/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.apertum.qsystem.zoneboard;

import java.io.InputStream;
import java.util.ArrayList;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author egorov
 */
@XmlRootElement(name = "window")
public class WindowProperty {

    public static WindowProperty unmarshal(InputStream inputStream) throws JAXBException {
        final JAXBContext jc = JAXBContext.newInstance(new Class[]{WindowProperty.class});
        final Unmarshaller u = jc.createUnmarshaller();
        return (WindowProperty) u.unmarshal(inputStream);
    }
    private String name;

    public String getName() {
        return name;
    }

    @XmlAttribute(name = "name", required = true)
    public void setName(String name) {
        this.name = name;
    }
    private int display;

    public int getDisplay() {
        return display;
    }

    @XmlAttribute(name = "display", required = true)
    public void setDisplay(int display) {
        this.display = display;
    }

    private ArrayList<Integer> zones = new ArrayList<>();

    public ArrayList<Integer> getZones() {
        return zones;
    }

    @XmlElements({
        @XmlElement(name = "zone", type = Integer.class)
    })
    public void setZones(ArrayList<Integer> zones) {
        this.zones = zones;
    }
    
    private String configFile;

    public String getConfigFile() {
        return configFile;
    }

    @XmlAttribute(name = "configFile", required = true)
    public void setConfigFile(String configFile) {
        this.configFile = configFile;
    }
}
