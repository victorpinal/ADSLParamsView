package com.victor;

import com.victor.datos.CmbModel;
import com.victor.datos.Ip_Class;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Formatter;
import java.util.Vector;

/**
 * Created by victormanuel on 03/12/2015.
 */
public class MainForm {
    private JPanel rootPane;
    private JTable uxgrd;
    private JComboBox uxcmb;

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("MainForm");
        frame.setContentPane(new MainForm().rootPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

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

        uxcmb.setModel(new CmbModel());
        uxcmb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                cargaDatos(((Ip_Class) uxcmb.getSelectedItem()).getid());
            }
        });
        if (uxcmb.getItemCount() > 0) {
            uxcmb.setSelectedIndex(((CmbModel) uxcmb.getModel()).getIndexOf(ip));
        }

        uxgrd.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable jTable, Object value, boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(jTable, value, isSelected, hasFocus, row, column);
                if (column == 1 && row >= 0) {
                    c.setBackground(new Color(0,255,0,(int)value));
                } else {
                    c.setBackground(Color.white);
                }
                return c;
            }
        });

    }

    public void cargaDatos(int ip_id) {

        String cols[] = {"Hora","Down","Up","Att.Down","Att.Up"};
        DefaultTableModel model = new DefaultTableModel(null,cols);
        Vector datos;

        try (PreparedStatement stmt = mySQL.get().getConnection().prepareStatement("SELECT time,download,upload,attdownrate,attuprate FROM datos WHERE ip_id=? ORDER BY time DESC")) {

            stmt.setInt(1, ip_id);
            ResultSet res = stmt.executeQuery();

            while (res.next()) {
                datos = new Vector();
                datos.add(new Formatter().format("%1$tD %1$tR",res.getTimestamp("time")));
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


}
