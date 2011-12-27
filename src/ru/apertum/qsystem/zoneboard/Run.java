/**
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.apertum.qsystem.zoneboard;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.bind.JAXBException;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import ru.apertum.qsystem.client.forms.FIndicatorBoard;
import ru.apertum.qsystem.common.cmd.JsonRPC20;
import ru.apertum.qsystem.common.cmd.JsonRPC20Error;
import ru.apertum.qsystem.common.cmd.RpcGetInt;
import ru.apertum.qsystem.common.cmd.RpcToZoneServer;
import ru.apertum.qsystem.zoneboard.ZoneServerProperty.Zone;
import ru.apertum.qsystem.zoneboard.common.Uses;
import ru.apertum.qsystem.zoneboard.form.FAdv;
import ru.apertum.qsystem.zoneboard.form.FZoneBoard;

/**
 *
 * @author egorov
 */
public class Run {

    static private ZoneServerProperty props;
    static public String filePropName;
    static private ArrayList<FZoneBoard> forms = new ArrayList<>();
    public static final HashMap<Integer, ArrayList<ZoneBoard>> boards = new HashMap<>();

    public static void main(final String args[]) throws FileNotFoundException, SocketException, IOException {
        Uses.isDebug = Uses.setLogining(args);
        final InputStream insProp;
        if (args.length != 0) {
            final File f = new File(args[0]);
            if (f.exists()) {
                insProp = new FileInputStream(f);
                filePropName = args[0];
            } else {
                throw new FileNotFoundException("Not found the configuration file config.xml in first parameter path: " + args[0]);
            }
        } else {
            final File f = new File("config/config.xml");
            if (f.exists()) {
                insProp = new FileInputStream(f);
                filePropName = "config/config.xml";
            } else {
                throw new FileNotFoundException("Not found the configuration file config/config.xml in current directory.");
            }
        }
        try {
            props = ZoneServerProperty.unmarshal(insProp);
        } catch (JAXBException ex) {
            throw new Uses.SilentException(ex.toString());
        }

        // Окна 

        for (final WindowProperty p : props.windows) {
            if (!Uses.displays.containsKey(p.getDisplay())) {
                continue;
            }
            java.awt.EventQueue.invokeLater(new Runnable() {

                @Override
                public void run() {
                    //final FZoneBoard w = new FZoneBoard(p);
                    final FIndicatorBoard w = FIndicatorBoard.getIndicatorBoardForZone(getConfig(p.getConfigFile() == null ? "" : p.getConfigFile()), Uses.isDebug);
                    final ZoneBoard board = new ZoneBoard();
                    board.setForm(w);
                    for (Integer n : p.getZones()) {
                        synchronized (boards) {
                            ArrayList<ZoneBoard> list = boards.get(n);
                            if (list == null) {
                                list = new ArrayList<>();
                                boards.put(n, list);
                            }
                            list.add(board);
                        }
                    }


                    int x, y;
                    if (Uses.isDebug && !Uses.isDemo) {
                        int k = getK();
                        x = 100 * k;
                        y = 100 * k;
                    } else {
                        x = (int) Uses.displays.get(p.getDisplay()).getX() + 10;
                        y = (int) Uses.displays.get(p.getDisplay()).getY() + 10;
                    }
                    w.toPosition(Uses.isDebug, x, y);
                    w.setVisible(true);
                }
            });
        }
        // Окна с рекламой
        for (final AdvWindowProperty p : props.getAdvWindows()) {
            if (!Uses.displays.containsKey(p.getDisplay())) {
                continue;
            }
            java.awt.EventQueue.invokeLater(new Runnable() {

                @Override
                public void run() {
                    final FAdv adv = new FAdv(p, Uses.isDebug);
                    int x, y;
                    if (Uses.isDebug) {
                        int k = getK();
                        x = 100 * k;
                        y = 100 * k;
                    } else {
                        x = (int) Uses.displays.get(p.getDisplay()).getX();
                        y = (int) Uses.displays.get(p.getDisplay()).getY();
                    }
                    adv.setLocation(x, y);
                    adv.setVisible(true);
                }
            });
        }
        // привинтить сокет на локалхост
        //  запуска захвата портов
        new Thread(new SocketRunnable(props.getPort())).start();

    }
    static int k = 1;

    public static int getK() {
        return k++;
    }

    public static Element getConfig(String fileName) {
        final String configFile;
        if (new File(fileName).exists()) {
            configFile = fileName;
        } else {
            configFile = props.getConfigFile();
        }
        final File boardFile = new File(configFile);
        if (boardFile.exists()) {
            try {
                return new SAXReader(false).read(boardFile).getRootElement();
            } catch (DocumentException ex) {
                Uses.log.logger.error("Невозможно прочитать файл конфигурации главного табло. " + ex.getMessage());
                return DocumentHelper.createElement("Ответ");
            }
        } else {
            Uses.log.logger.warn("Файл конфигурации главного табло \"" + configFile + "\" не найден. ");
            return DocumentHelper.createElement("Ответ");
        }
    }

    static public void refreshWindowsTitle() {
        for (FZoneBoard form : forms) {
            StringBuilder caption = new StringBuilder("    ");
            for (Integer port : form.getProperty().getZones()) {
                for (Zone pos : props.getZoneList()) {
                    if (pos.getZone().equals(port)) {
                        caption.append(pos.getName()).append("(").append(pos.getZone()).append("); ");
                    }
                }
            }
            form.setTitle(form.getProperty().getName() + caption.toString());
        }

    }

    static class SocketRunnable implements Runnable {

        private final Integer port;

        public SocketRunnable(Integer port) {
            this.port = port;
        }

        @Override
        public void run() {

            final ServerSocket server;
            try {
                Uses.log.logger.info("Сервер системы захватывает порт \"" + port + "\".");
                server = new ServerSocket(port);
            } catch (IOException e) {
                throw new Uses.SilentException("Ошибка при создании серверного сокета: " + e);
            } catch (Exception e) {
                throw new Uses.SilentException("Ошибка сети: " + e);
            }
            Uses.log.logger.debug("Старт цикла приема сообщений.");
            // слушаем порт
            while (!Thread.interrupted()) {
                // из сокета клиента берём поток входящих данных
                final Socket socket;
                try {
                    socket = server.accept();
                } catch (IOException e) {
                    throw new Uses.SilentException("Ошибка при получении входного потока: " + e.getStackTrace());
                }
                StringBuilder sb = new StringBuilder();
                final InputStream inR;
                try {
                    inR = socket.getInputStream();
                } catch (IOException ex) {
                    throw new Uses.SilentException("Ошибка сети 1: " + ex);
                }


                final String data;
                try {
                    // подождать пока хоть что-то приползет из сети, но не более 10 сек.
                    int i = 0;
                    while (inR.available() == 0 && i < 100) {
                        Thread.sleep(100);//бля
                        i++;
                    }

                    StringBuilder sb1 = new StringBuilder(new String(Uses.readInputStream(inR)));
                    while (inR.available() != 0) {
                        sb1 = sb1.append(new String(Uses.readInputStream(inR)));
                        Thread.sleep(150);//бля
                    }
                    data = URLDecoder.decode(sb1.toString(), "utf-8");
                } catch (IOException ex) {
                    throw new Uses.SilentException("Ошибка при чтении из входного потока: " + ex);
                } catch (InterruptedException ex) {
                    throw new Uses.SilentException("Проблема со сном: " + ex);
                } catch (IllegalArgumentException ex) {
                    throw new Uses.SilentException("Ошибка декодирования сетевого сообщения: " + ex);
                }


                Uses.log.logger.trace("Сообщение:\n" + data);

                final RpcToZoneServer rpc;
                final Gson gson = new Gson();

                rpc = gson.fromJson(data, RpcToZoneServer.class);
                try {
                    new Thread(new WorkRunnable(rpc, socket.getOutputStream())).start();
                } catch (IOException ex) {
                    throw new Uses.SilentException("Ошибка обработки и отправки сетевого сообщения: " + ex);
                }

            }

        }
    }

    static class WorkRunnable implements Runnable {

        public final RpcToZoneServer rpc;
        public final OutputStream os;

        public WorkRunnable(RpcToZoneServer rpc, OutputStream os) {
            this.rpc = rpc;
            this.os = os;
        }

        @Override
        public void run() {
            Uses.log.logger.trace("Выполняем метод: \"" + rpc.getMethod());
            System.out.print("Run method: " + rpc.getMethod() + "  parameter: ");
            final Object ansver;
            switch (rpc.getMethod()) {
                case "ping":
                    final int res;
                    System.out.println(rpc.getParams().textData);
                    switch (rpc.getParams().textData) {
                        case "1":
                            res = 1;
                            break;
                        default:
                            res = -300;
                    }
                    ansver = new RpcGetInt(res);
                    break;
                case "show":
                    System.out.println(rpc.getResult().getUserAddrRS());
                    ArrayList<ZoneBoard> list = boards.get(rpc.getResult().getUserAddrRS());
                    for (ZoneBoard board : list) {
                        System.out.println("do show");
                        board.inviteCustomer(rpc.getResult().getUserName(), rpc.getResult().getUserPoint(), rpc.getResult().getCustomerPrefix(), rpc.getResult().getCustomerNumber(), rpc.getResult().getUserAddrRS());
                    }
                    ansver = new JsonRPC20();
                    break;
                case "work":
                    System.out.println(rpc.getResult().getUserAddrRS());
                    list = boards.get(rpc.getResult().getUserAddrRS());
                    for (ZoneBoard board : list) {
                        System.out.println("do work");
                        board.workCustomer(rpc.getResult().getUserName(), rpc.getResult().getUserPoint(), rpc.getResult().getCustomerPrefix(), rpc.getResult().getCustomerNumber(), rpc.getResult().getUserAddrRS());
                    }
                    ansver = new JsonRPC20();
                    break;
                case "kill":
                    System.out.println(rpc.getResult().getUserAddrRS());
                    list = boards.get(rpc.getResult().getUserAddrRS());
                    for (ZoneBoard board : list) {
                        System.out.println("do kill");
                        board.killCustomer(rpc.getResult().getUserName());
                    }
                    ansver = new JsonRPC20();
                    break;
                default:
                    System.out.println("Warning: default nethod");
                    ansver = new JsonRPC20Error();
            }


            try (PrintWriter writer = new PrintWriter(os)) {
                try {
                    Gson gson = new Gson();
                    final String message = gson.toJson(ansver);
                    Uses.log.logger.trace("Высылаем результат: \"" + message + "\"");
                    writer.print(URLEncoder.encode(message, "utf-8"));
                } catch (UnsupportedEncodingException ex) {
                    throw new Uses.SilentException("Ошибка отправки сетевого сообщения: " + ex);
                }
                Uses.log.logger.trace("Высылали результат.");
                writer.flush();
            }
        }
    }
}
