/**
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ru.apertum.qsystem.zoneboard;

import com.google.gson.Gson;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.MemoryImageSource;
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
import java.util.ServiceLoader;
import javax.swing.JFrame;
import javax.xml.bind.JAXBException;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import ru.apertum.qsystem.common.CustomerState;
import ru.apertum.qsystem.common.SoundPlayer;
import ru.apertum.qsystem.common.cmd.JsonRPC20Error;
import ru.apertum.qsystem.common.cmd.JsonRPC20OK;
import ru.apertum.qsystem.common.cmd.RpcGetInt;
import ru.apertum.qsystem.common.cmd.RpcToZoneServer;
import ru.apertum.qsystem.common.model.QCustomer;
import ru.apertum.qsystem.extra.IChangeCustomerStateEvent;
import ru.apertum.qsystem.server.controller.IIndicatorBoard;
import ru.apertum.qsystem.server.model.QService;
import ru.apertum.qsystem.server.model.QUser;
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
    private static final ArrayList<FZoneBoard> forms = new ArrayList<>();
    public static final HashMap<Integer, ArrayList<IIndicatorBoard>> boards = new HashMap<>();

    public static void main(final String args[]) throws FileNotFoundException, SocketException, IOException {
        Uses.isDebug = Uses.setLogining(args);
        // Загрузка плагинов из папки plugins
        ru.apertum.qsystem.common.Uses.loadPlugins("./plugins/");
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
                    //final FIndicatorBoard w = FIndicatorBoard.getIndicatorBoardForZone(getConfig(p.getConfigFile() == null ? "" : p.getConfigFile()), Uses.isDebug);
                    final ZoneBoard board = new ZoneBoard(props, p);
                    //board.setForm(w);
                    for (Integer n : p.getZones()) {
                        synchronized (boards) {
                            ArrayList<IIndicatorBoard> list = boards.get(n);
                            if (list == null) {
                                list = new ArrayList<>();
                                boards.put(n, list);
                            }
                            list.add(board);
                        }
                    }

                    if (board.getForm() != null) {
                        int x, y;
                        if (Uses.isDebug && !Uses.isDemo) {
                            int k = getK();
                            x = 100 * k;
                            y = 100 * k;
                        } else {
                            x = (int) Uses.displays.get(p.getDisplay()).getX() + 10;
                            y = (int) Uses.displays.get(p.getDisplay()).getY() + 10;
                        }
                        toPosition(board.getForm(), Uses.isDebug, x, y);
                        board.getForm().setVisible(true);
                    }
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

    public static void toPosition(final JFrame form, boolean isDebug, int x, int y) {
        // Определим форму нв монитор
        form.setLocation(x, y);
        form.setAlwaysOnTop(!isDebug);
        form.setResizable(isDebug);
        // Отрехтуем форму в зависимости от режима.
        if (!isDebug) {

            form.setAlwaysOnTop(true);
            form.setResizable(false);
            // спрячем курсор мыши
            int[] pixels = new int[16 * 16];
            Image image = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(16, 16, pixels, 0, 16));
            Cursor transparentCursor = Toolkit.getDefaultToolkit().createCustomCursor(image, new Point(0, 0), "invisibleCursor");
            form.setCursor(transparentCursor);
            form.addWindowListener(new WindowAdapter() {

                @Override
                public void windowOpened(WindowEvent e) {
                    form.setExtendedState(JFrame.MAXIMIZED_BOTH);
                }
            });
        } else {
            form.setSize(1280, 720);
        }
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
            final ArrayList<IIndicatorBoard> list = rpc.getResult() == null ? null : boards.get(rpc.getResult().getUserAddrRS());
            if (list != null) {
                final QUser user = new QUser();
                user.setName(rpc.getResult().getUserName());
                user.setPoint(rpc.getResult().getUserPoint());
                user.setAdressRS(rpc.getResult().getUserAddrRS());
                final QCustomer customer = new QCustomer();
                customer.setPrefix(rpc.getResult().getCustomerPrefix());
                customer.setNumber(rpc.getResult().getCustomerNumber());
                final QService service = new QService();
                service.setId(Long.MIN_VALUE);
                customer.setService(service);
                user.setCustomer(customer);

                switch (rpc.getMethod()) {
                    case "ping":
                        final int res;
                        System.out.println(rpc.getParams().textData);
                        switch (rpc.getParams().textData) {
                            case "2":
                                res = 1;
                                break;
                            default:
                                res = -300;
                        }
                        ansver = new RpcGetInt(res);
                        break;
                    case "show":
                        System.out.println(rpc.getResult().getUserAddrRS());
                        for (IIndicatorBoard board : list) {
                            System.out.println("do show");
                            board.inviteCustomer(user, customer);
                        }
                        // просигналим звуком
                        SoundPlayer.inviteClient(rpc.getResult().getCustomerPrefix() + rpc.getResult().getCustomerNumber(), rpc.getResult().getUserPoint(), true, props.getInviteType(), props.getVoiceType(), props.getPointType());
                        ansver = new JsonRPC20OK();
                        break;
                    case "repeat":
                        System.out.println(rpc.getResult().getUserAddrRS());
                        // просигналим звуком
                        SoundPlayer.inviteClient(rpc.getResult().getCustomerPrefix() + rpc.getResult().getCustomerNumber(), rpc.getResult().getUserPoint(), true, props.getInviteType(), props.getVoiceType(), props.getPointType());
                        ansver = new JsonRPC20OK();
                        break;
                    case "work":
                        System.out.println(rpc.getResult().getUserAddrRS());
                        for (IIndicatorBoard board : list) {
                            System.out.println("do work");
                            board.workCustomer(user);
                        }
                        ansver = new JsonRPC20OK();
                        break;
                    case "kill":
                        System.out.println(rpc.getResult().getUserAddrRS());
                        for (IIndicatorBoard board : list) {
                            System.out.println("do kill");
                            board.killCustomer(user);
                        }
                        ansver = new JsonRPC20OK();
                        break;
                    default:
                        System.out.println("Warning: default nethod");
                        ansver = new JsonRPC20Error(JsonRPC20Error.ErrorRPC.UNKNOWN_ERROR, "Warning: default nethod");
                }
            } else {
                System.out.println("do nothing. no zone");
                ansver = new JsonRPC20OK();
            }

            CustomerState cs = CustomerState.STATE_FINISH;
            switch (rpc.getMethod()) {
                case "ping":
                    break;
                case "show":
                    cs = CustomerState.STATE_INVITED;
                    break;
                case "repeat":
                    break;
                case "work":
                    cs = CustomerState.STATE_WORK;
                    break;
                case "kill":
                    break;
                default:
                    System.out.println("Warning: default nethod");
            }
            // поддержка расширяемости плагинами
            for (final IChangeCustomerStateEvent event : ServiceLoader.load(IChangeCustomerStateEvent.class)) {
                System.out.println("Вызов SPI расширения. Описание: " + event.getDescription());
                try {
                    event.change(rpc.getResult().getUserPoint(), rpc.getResult().getCustomerPrefix(), rpc.getResult().getCustomerNumber(), cs);
                } catch (Throwable tr) {
                    System.err.println("Вызов SPI расширения завершился ошибкой. Описание: " + tr);
                }
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
