/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.apertum.qsystem.zoneboard;

import java.awt.Color;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author egorov
 */
@XmlRootElement(name = "server")
public class ZoneServerProperty {

    public void marshal(OutputStream outputStream) throws JAXBException {
        final JAXBContext jc = JAXBContext.newInstance(new Class[]{ZoneServerProperty.class});
        final Marshaller m = jc.createMarshaller();
        m.setProperty("jaxb.formatted.output", true);
        m.marshal(this, outputStream);
    }

    public static ZoneServerProperty unmarshal(InputStream inputStream) throws JAXBException {
        final JAXBContext jc = JAXBContext.newInstance(new Class[]{ZoneServerProperty.class});
        final Unmarshaller u = jc.createUnmarshaller();
        return (ZoneServerProperty) u.unmarshal(inputStream);
    }
    
    private Integer port;

    public Integer getPort() {
        return port;
    }

    @XmlAttribute(name = "port", required = true)
    public void setPort(Integer port) {
        this.port = port;
    }
    
    private Integer inviteType;

    public Integer getInviteType() {
        return inviteType;
    }

    @XmlAttribute(name = "inviteType", required = true)
    public void setInviteType(Integer inviteType) {
        this.inviteType = inviteType;
    }
    
    private Integer voiceType;

    public Integer getVoiceType() {
        return voiceType;
    }

    @XmlAttribute(name = "voiceType", required = true)
    public void setVoiceType(Integer voiceType) {
        this.voiceType = voiceType;
    }
    private Integer pointType;

    public Integer getPointType() {
        return pointType;
    }

    @XmlAttribute(name = "pointType", required = true)
    public void setPointType(Integer pointType) {
        this.pointType = pointType;
    }

    private String configFile;

    public String getConfigFile() {
        return configFile;
    }

    @XmlAttribute(name = "configFile", required = true)
    public void setConfigFile(String configFile) {
        this.configFile = configFile;
    }

    @XmlElementWrapper(name = "windows")
    @XmlElements({
        @XmlElement(name = "window", type = WindowProperty.class)
    })
    public List<WindowProperty> windows = new ArrayList<>();
    private ArrayList<Zone> zoneList = new ArrayList<>();

    public ArrayList<Zone> getZoneList() {
        return zoneList;
    }

    @XmlElementWrapper(name = "zones")
    @XmlElements({
        @XmlElement(name = "zone", type = Zone.class)
    })
    public void setZoneList(ArrayList<Zone> zoneList) {
        this.zoneList = zoneList;
    }

    @XmlRootElement(name = "zone")
    public static class Zone {

        public Zone(String name, Integer zone) {
            this.name = name;
            this.zone = zone;
        }

        public Zone() {
        }

        private String name;

        public String getName() {
            return name;
        }

        @XmlAttribute(name = "name", required = true)
        public void setName(String name) {
            this.name = name;
        }
        private Integer zone;

        public Integer getZone() {
            return zone;
        }

        @XmlValue()
        public void setZone(Integer zone) {
            this.zone = zone;
        }
    }
    @XmlElementWrapper(name = "colors")
    @XmlJavaTypeAdapter(ColorAdapter.class)
    @XmlElements({
        @XmlElement(name = "color")
    })
    private static ArrayList<Color> colors = new ArrayList<>();

    static class ColorAdapter extends XmlAdapter<String, Color> {

        @Override
        public Color unmarshal(String s) {
            return Color.decode(s);
        }

        @Override
        public String marshal(Color c) {
            return "#" + Integer.toHexString(c.getRGB()).substring(2);
        }
    }

    public static ArrayList<Color> getColors() {
        return colors;
    }

    public static void setColors(ArrayList<Color> colors) {
        ZoneServerProperty.colors = colors;
    }
    private List<AdvWindowProperty> advWindows = new ArrayList<>();

    public List<AdvWindowProperty> getAdvWindows() {
        return advWindows;
    }

    @XmlElementWrapper(name = "advWindows")
    @XmlElements({
        @XmlElement(name = "advWindow", type = AdvWindowProperty.class)
    })
    public void setAdvWindows(List<AdvWindowProperty> advWindows) {
        this.advWindows = advWindows;
    }
}
