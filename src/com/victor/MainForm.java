package com.victor;

import com.victor.datos.CmbModel;
import com.victor.datos.Ip_Class;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Formatter;
import java.util.Locale;
import java.util.Vector;

/**
 * Created by victormanuel on 03/12/2015.
 */
public class MainForm {
    private JPanel rootPane;
    private JTable uxgrd;
    private JComboBox<Ip_Class> uxcmb;
    private JTextArea txtResumen;

    public MainForm() {

        String ip = "127.0.0.1";

        //buscamos la Ip_Class externa del equipo
        try {
            URL whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
            ip = in.readLine(); //you get the IP as a String
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Inicializar el combo de IPs
        uxcmb.setModel(new CmbModel());

        uxcmb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                cargaDatos(((Ip_Class) uxcmb.getSelectedItem()).getid());
                cargaResumen(((Ip_Class) uxcmb.getSelectedItem()).getid());
            }
        });

        if (uxcmb.getItemCount() > 0) {
            uxcmb.setSelectedIndex(((CmbModel) uxcmb.getModel()).getIndexOf(ip));
        }

        //Pintar las lineas del grid
        uxgrd.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1L;
			@Override
            public Component getTableCellRendererComponent(JTable jTable, Object value, boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(jTable, value, isSelected, hasFocus, row, column);
                if (column == 1 && row >= 0) {
                    //c.setBackground(new Color(0,255,0,(int)value));
                }
                return c;
            }
        });

    }

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("MainForm");
        frame.setContentPane(new MainForm().rootPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void cargaDatos(int ip_id) {

        String cols[] = {"Hora","Down","Up","Att.Down","Att.Up"};
        DefaultTableModel model = new DefaultTableModel(null,cols);
        Vector<Object> datos;

        try (PreparedStatement stmt = mySQL.get().getConnection().prepareStatement("SELECT time,download,upload,attdownrate,attuprate FROM datos WHERE ip_id=? ORDER BY time DESC")) {

            stmt.setInt(1, ip_id);
            ResultSet res = stmt.executeQuery();

            while (res.next()) {
                datos = new Vector<Object>();
                datos.add(new SimpleDateFormat("dd/MM/yy HH:mm").format(res.getTimestamp("time")));
                datos.add(res.getInt("download"));
                datos.add(res.getInt("upload"));
                datos.add(res.getInt("attdownrate"));
                datos.add(res.getInt("attuprate"));
                model.addRow(datos);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        uxgrd.setModel(model);

    }

    public void cargaResumen(int ip_id) {

        try (PreparedStatement stmt = mySQL.get().getConnection().prepareStatement("SELECT * FROM resumen WHERE ip_id=?")) {

            stmt.setInt(1, ip_id);
            ResultSet res = stmt.executeQuery();

            StringBuilder sb = new StringBuilder();
            Formatter formatter = new Formatter(sb, Locale.getDefault());

            while (res.next()) {

                formatter.format("%6s registros %n", res.getInt("NumRecords"));
                formatter.format("%6s días desde %s %n", res.getInt("NumDays"),
                        new SimpleDateFormat("dd/MM/yy HH:mm").format(res.getTimestamp("Min_Date")));
                formatter.format("%nUltimo %s %n",
                        new SimpleDateFormat("dd/MM/yy HH:mm").format(res.getTimestamp("Max_Date")));

                formatter.format("%nSNR   DOWNLOAD          UPLOAD %n");
                formatter.format(" MAX  %8s(%3s) %10s(%3s) %n",
                        res.getInt("Max_DOWN_SNR"),
                        res.getInt("LAST_DOWN_SNR") - res.getInt("Max_DOWN_SNR"),
                        res.getInt("Max_UP_SNR"),
                        res.getInt("LAST_UP_SNR") - res.getInt("Max_UP_SNR"));
                formatter.format(" MIN  %8s(%3s) %10s(%3s) %n",
                        res.getInt("Min_DOWN_SNR"),
                        res.getInt("LAST_DOWN_SNR") - res.getInt("Min_DOWN_SNR"),
                        res.getInt("Min_UP_SNR"),
                        res.getInt("LAST_UP_SNR") - res.getInt("Min_UP_SNR"));
                formatter.format(" AVG  %8s(%3s) %10s(%3s) %n",
                        res.getInt("Avg_DOWN_SNR"),
                        res.getInt("LAST_DOWN_SNR") - res.getInt("Avg_DOWN_SNR"),
                        res.getInt("Avg_UP_SNR"),
                        res.getInt("LAST_UP_SNR") - res.getInt("Avg_UP_SNR"));
                formatter.format(" LAST %8s %15s %n",
                        res.getInt("LAST_DOWN_SNR"),
                        res.getInt("LAST_UP_SNR"));

                formatter.format("%nATT   DOWNLOAD          UPLOAD %n");
                formatter.format(" MAX  %8s(%5s) %8s(%5s) %n",
                        res.getInt("Max_DOWN"),
                        res.getInt("LAST_DOWN") - res.getInt("Max_DOWN"),
                        res.getInt("Max_UP"),
                        res.getInt("LAST_UP") - res.getInt("Max_UP"));
                formatter.format(" MIN  %8s(%5s) %8s(%5s) %n",
                        res.getInt("Min_DOWN"),
                        res.getInt("LAST_DOWN") - res.getInt("Min_DOWN"),
                        res.getInt("Min_UP"),
                        res.getInt("LAST_UP") - res.getInt("Min_UP"));
                formatter.format(" AVG  %8s(%5s) %8s(%5s) %n",
                        res.getInt("Avg_DOWN"),
                        res.getInt("LAST_DOWN") - res.getInt("Avg_DOWN"),
                        res.getInt("Avg_UP"),
                        res.getInt("LAST_UP") - res.getInt("Avg_UP"));
                formatter.format(" LAST %8s %15s %n",
                        res.getInt("LAST_DOWN"),
                        res.getInt("LAST_UP"));

                formatter.format("%nPWR   DOWNLOAD          UPLOAD %n");
                formatter.format(" MAX  %8s %15s %n",
                        res.getInt("Max_DOWN_Power"),
                        res.getInt("Max_UP_Power"));
                formatter.format(" MIN  %8s %15s %n",
                        res.getInt("Min_DOWN_Power"),
                        res.getInt("Min_UP_Power"));
            }
            formatter.close();

            txtResumen.setText(sb.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
