package com.victor;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by victormanuel on 03/12/2015.
 */
public class MainForm {
    private JPanel rootPane;
    private JTable uxgrd;
    private JComboBox uxcmbADSL;
    private mySQL mysql;

    public MainForm() {

        mysql = new mySQL();

        uxcmbADSL.setModel(new cmbModel());


    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("MainForm");
        frame.setContentPane(new MainForm().rootPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private class cmbModel implements ComboBoxModel {

        public cmbModel() {

            //buscamos la ip externa del equipo
            String ip = "localhost";
            try {
                URL whatismyip = new URL("http://checkip.amazonaws.com");
                BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
                ip = in.readLine(); //you get the IP as a String
            } catch (IOException e) {
                e.printStackTrace();
            }

            DefaultComboBoxModel combo = new DefaultComboBoxModel();

            try (ResultSet res = mysql.getConnection().createStatement().executeQuery("SELECT id,ip_id FROM ip")) {
                while (res.next()) {
                    combo.addElement(res.getString("name"));
                }
            } catch (Exception e) {}

        }

        @Override
        public void setSelectedItem(Object o) {

        }

        @Override
        public Object getSelectedItem() {
            return null;
        }

        @Override
        public int getSize() {
            return 0;
        }

        @Override
        public Object getElementAt(int i) {
            return null;
        }

        @Override
        public void addListDataListener(ListDataListener listDataListener) {

        }

        @Override
        public void removeListDataListener(ListDataListener listDataListener) {

        }
    }

    public DefaultTableModel cargaDatos(int ip_id) {

        try (PreparedStatement stmt = mysql.getConnection().prepareStatement("SELECT ip_id,download,upload,attdownrate,attuprate,downpower,uppower FROM datos WHERE ip_id=?")) {
            stmt.setInt(1, ip_id);
            stmt.executeQuery();
        } catch (Exception e) {}

        return null;
    }

}
