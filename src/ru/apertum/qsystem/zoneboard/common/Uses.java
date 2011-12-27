/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.apertum.qsystem.zoneboard.common;

import java.awt.Component;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import org.apache.log4j.Logger;

/**
 *
 * @author egorov
 */
public class Uses {

    // Ключи выполнения программы
    public static final String KEY_DEBUG = "DEBUG";
    public static final String KEY_INFO = "INFO";
    public static final String KEY_DEMO = "DEMO";
    /**
     * Режим отладки
     */
    public static boolean isDebug = false;
    /**
     * Режим отладки
     */
    public static boolean isDemo = false;

    /**
     *  Собственно, логер лог4Ж
     */
    public final static class Log extends Object {

        public Logger logger = Logger.getLogger("server.file");//**.file.info.trace
    };
    /**
     * Пользуемся этой константой для работы с логом
     */
    public static final Log log = new Log();

    /**
     * Определение политики логирования.
     * @param args параметры командной строки
     * @return вести отладку или нет.
     */
    public static boolean setLogining(String[] args) {
        boolean isDebugin = false;
        Uses.log.logger = Logger.getLogger("zoneboard.file");

        //бежим по параметрам, смотрим, выполняем что надо
        for (int i = 0; i < args.length; i++) {
            // ключ, отвечающий за логирование
            if (Uses.KEY_DEBUG.equalsIgnoreCase(args[i])) {
                Uses.log.logger = Logger.getLogger("zoneboard.file.info.trace");
                isDebugin = true;
            }
            
            if (Uses.KEY_DEMO.equalsIgnoreCase(args[i])) {
                isDemo = true;
            }
        }
        Uses.log.logger.info("СТАРТ ЛОГИРОВАНИЯ. Логгер: " + Uses.log.logger.getName());
        return isDebugin;
    }

    /**
     * Этот класс исключения использовать для програмной генерации исклюсений.
     * Записывает StackTrace и  само исключение в лог.
     * Это исключение не показывает диологовое окно при возникновении ошибки
     * Используется в системе статистики и отчетов.
     * @author egorov
     */
    public static class SilentException extends RuntimeException {

        public SilentException(String textException) {
            super(textException);
            //StringWriter out = new StringWriter();
            //printStackTrace(new PrintWriter(out));
            //log.logger.error("Error!\n"+out.toString(), this);
            log.logger.error("Error!", this);
        }
    }

    /**
     * Для чтения байт из потока.
     * @param stream из него читаем
     * @return byte[] результат
     * @throws java.io.IOException
     * @see readSocketInputStream(InputStream stream)
     */
    public static byte[] readInputStream(InputStream stream) throws IOException {
        final byte[] result;
        final DataInputStream dis = new DataInputStream(stream);
        result = new byte[stream.available()];
        dis.readFully(result);
        return result;
    }

    /**
     * Отцентирируем Окно по центру экрана
     * @param component это окно и будем центрировать
     */
    public static void setLocation(Component component) {
        final Toolkit kit = Toolkit.getDefaultToolkit();
        component.setLocation((Math.round(kit.getScreenSize().width - component.getWidth()) / 2), (Math.round(kit.getScreenSize().height - component.getHeight()) / 2));
    }
    public static final HashMap<Integer, Rectangle> displays = new HashMap<>();

    static {
        final GraphicsDevice[] screenDevices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        int i = 1;
        for (GraphicsDevice graphicsDevice : screenDevices) {
            System.out.println("graphicsDevice = " + graphicsDevice.getIDstring() + " " + graphicsDevice.toString()
                    + "\nРазрешение экрана " + graphicsDevice.getDefaultConfiguration().getBounds().height + "x" + graphicsDevice.getDefaultConfiguration().getBounds().width
                    + "\nГлубина цвета " + graphicsDevice.getDisplayMode().getBitDepth()
                    + "\nЧастота " + graphicsDevice.getDisplayMode().getRefreshRate()
                    + "\nНачало координат " + graphicsDevice.getDefaultConfiguration().getBounds().x
                    + "-" + graphicsDevice.getDefaultConfiguration().getBounds().y);
            displays.put(i++, graphicsDevice.getDefaultConfiguration().getBounds());
        }

    }
    
    

}
