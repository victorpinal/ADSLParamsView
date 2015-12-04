package com.victor;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

/**
 * Created by victormanuel on 03/12/2015.
 */
public class MainForm {
    private JPanel rootPane;
    private JTable uxgrd;
    private JComboBox uxcmbADSL;

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

        uxcmbADSL.setModel(new cmbModel());
        uxcmbADSL.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                cargaDatos(((Ip_Class) uxcmbADSL.getSelectedItem()).getid());
            }
        });
        if (uxcmbADSL.getItemCount() > 0) {
            uxcmbADSL.setSelectedIndex(((cmbModel) uxcmbADSL.getModel()).getIndexOf(ip));
        }

    }

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

    public void cargaDatos(int ip_id) {

        DefaultTableModel model = new DefaultTableModel(0, 5);
        Vector datos;

        try (PreparedStatement stmt = mySQL.get().getConnection().prepareStatement("SELECT time,download,upload,attdownrate,attuprate FROM datos WHERE ip_id=? ORDER BY time DESC")) {
            stmt.setInt(1, ip_id);
            ResultSet res = stmt.executeQuery();
            while (res.next()) {
                datos = new Vector();
                datos.add(res.getTimestamp("time"));
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

    private class tblModel extends AbstractTableModel {

        public tblModel() {
        }

        @Override
        public int getRowCount() {
            return 0;
        }

        @Override
        public int getColumnCount() {
            return 0;
        }

        @Override
        public Object getValueAt(int i, int i1) {
            return null;
        }
    }

    private class cmbModel extends DefaultComboBoxModel {

        private Vector<Ip_Class> datos = new Vector<>();

        public cmbModel() {

            try (ResultSet res = mySQL.get().getConnection().createStatement().executeQuery("SELECT * FROM ip")) {
                while (res.next()) {
                    datos.addElement(new Ip_Class(res.getInt("id"), res.getString("ip"), res.getString("name")));
                }
            } catch (Exception e) {
            }

        }

        @Override
        public int getSize() {
            return datos.size();
        }

        @Override
        public Object getElementAt(int i) {
            return datos.get(i);
        }

        public int getIndexOf(String ip) {
            int index = 0;
            for (Ip_Class e : datos) {
                if (e.ip.equals(ip)) index = datos.indexOf(e);
            }
            return index;
        }

    }

    private class Ip_Class {
        private int id;
        private String ip;
        private String name;

        public Ip_Class(int id, String ip, String name) {
            this.id = id;
            this.ip = ip;
            this.name = name;
        }

        public int getid() {
            return id;
        }

        @Override
        public String toString() {
            return name + " - " + ip;
        }

    }

}
