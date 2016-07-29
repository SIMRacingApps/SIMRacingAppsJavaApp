package SIMRacingAppsJavaApp;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import com.SIMRacingApps.SIMPlugin;
import com.SIMRacingApps.SIMPluginAWTEventDispatcher;
import com.SIMRacingApps.Data;
import com.SIMRacingApps.Server;
import com.SIMRacingApps.JComponents.*;
import com.SIMRacingApps.Widgets.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Rectangle;

public class SIMRacingAppsJavaApp {

    private static String             m_SIM      = "iRacing"; //Default to iRacing
    private static Boolean            m_showFPS  = true;
    private static String             m_play     = ""; //"";
    /**
     * Launch the application.
     */
    public static void main(String[] args) {

        // parse arguments
        for (int i=0; i < args.length; i++) {
            try {
                if ((args[i].equalsIgnoreCase("-showfps") || args[i].equalsIgnoreCase("/showfps")) && (i+1) < args.length) {
                    m_showFPS = new Boolean(args[++i]);
                }
                else
                if ((args[i].equalsIgnoreCase("-sim") || args[i].equalsIgnoreCase("/sim")) && (i+1) < args.length) {
                    m_SIM = args[++i];
                }
                else
                if ((args[i].equalsIgnoreCase("-play") || args[i].equalsIgnoreCase("/play")) && (i+1) < args.length) {
                    m_play = args[++i];
                }
                else {
                    Server.logger().warning(args[i]+" is an unknown option");
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        
        //now place an event in the queue to start the main window.
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    SIMRacingAppsJavaApp window = new SIMRacingAppsJavaApp();
                    window.m_frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private JFrame                    m_frame;
    private JLabel                    m_geardata;
    private FPSWidget                 m_fpswidget;

    /**
     * Create the application.
     */
    public SIMRacingAppsJavaApp() {
        try {
            //This is just a test for detecting screens in case you want to position the frame to one of them
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice[] gs = ge.getScreenDevices();
            for (int j = 0; j < gs.length; j++) {
                GraphicsDevice gd = gs[j];
                GraphicsConfiguration gc = gd.getDefaultConfiguration();
                Rectangle gcBounds = gc.getBounds();
                int xoffs = gcBounds.x;
                int yoffs = gcBounds.y;
                System.err.printf("%s,%d = %d,%d\n", gc.toString(), j, xoffs, yoffs);
            }
            
            //create a connector to the SIM and pass it to the createDispatcher().
            SIMPluginAWTEventDispatcher.createDispatcher(
                    SIMPlugin.createSIMPlugin(m_SIM)
            );
            
            //process connector arguments.
            SIMPluginAWTEventDispatcher.getDispatcher().getSIMPlugin().setPlay(m_play);
            
            //create the window frame
            m_frame = new JFrame();
            m_frame.getContentPane().setBackground(Color.YELLOW);
            m_frame.setBounds(150, 100, 517, 308);
            m_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            m_frame.getContentPane().setLayout(null);

            //create a panel and add it to the frame
            JWidgetPanel widgetPanel = new JWidgetPanel();
            widgetPanel.setPreferredSize(new Dimension(330, 120));
            widgetPanel.setBounds(0, 0, 501, 272);
            widgetPanel.setOpaque(true);
            widgetPanel.setBackground(Color.DARK_GRAY);
            m_frame.getContentPane().add(widgetPanel);
            widgetPanel.setLayout(new JScaledLayout(new GridLayout(0, 2, 5, 0)));

            //create the objects and add them to the panel
            m_fpswidget = new FPSWidget();
            widgetPanel.add(m_fpswidget);
            if (!m_showFPS)
                m_fpswidget.setVisible(false);
            
            JDataLabel dataLabel = new JDataLabel((String) null);
            widgetPanel.add(dataLabel);

            JStaticText lblRpm = new JStaticText();
            lblRpm.setText("RPM:");
            widgetPanel.add(lblRpm);
            lblRpm.setHorizontalAlignment(SwingConstants.RIGHT);

            JDataLabel rpmdata = new JDataLabel("Car/REFERENCE/Gauge/Tachometer/ValueCurrent");
            rpmdata.setText("{RPM}");
            widgetPanel.add(rpmdata);
            rpmdata.setFormat("%.0f %S ");

            JStaticText lblGear = new JStaticText();
            lblGear.setText("GEAR:");
            widgetPanel.add(lblGear);
            lblGear.setHorizontalAlignment(SwingConstants.RIGHT);

            //example of how to register a listener to alter the data before it is displayed.
            m_geardata = new JDataLabel("Car/REFERENCE/Gauge/Gear/ValueCurrent");
            m_geardata.addPropertyChangeListener("data",
                    new PropertyChangeListener() {
                        public void propertyChange(PropertyChangeEvent evt) {
                            Data d = (Data) evt.getNewValue();
                            // normally you would do some fancy formatting that the
                            // setFormat() method cannot handle.
                            m_geardata.setText("[" + d.getString() + "]");
                        }
                    });
            widgetPanel.add(m_geardata);

            JStaticText stctxtPitRoadSpeed = new JStaticText();
            widgetPanel.add(stctxtPitRoadSpeed);
            stctxtPitRoadSpeed.setHorizontalAlignment(SwingConstants.TRAILING);
            stctxtPitRoadSpeed.setText("Pit Road Speed Limit:");

            //example with a default value
            JDataLabel pitspeedlimtdata = new JDataLabel("Track/PitSpeedLimit","{PitSpeedLimt}");
            widgetPanel.add(pitspeedlimtdata);
            pitspeedlimtdata.setFormat("%.0f %s");

            //start the dispatcher thread running to place data into the event queue
            SIMPluginAWTEventDispatcher.getDispatcher().startThread();
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
