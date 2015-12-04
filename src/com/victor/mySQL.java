package com.victor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

/**
 * Created by victormanuel on 03/12/2015.
 */
public class mySQL {

    Preferences preferences = Preferences.userNodeForPackage(mySQL.class);
    String connectionString = "";

    public mySQL() {

        //Load connection data
        if (preferences.get("servidor", null) == null) {
            preferences.put("servidor", JOptionPane.showInputDialog(null, "Servidor MySQL", "Configuración", JOptionPane.QUESTION_MESSAGE));
            preferences.put("puerto", JOptionPane.showInputDialog(null, "Puerto", "Configuración", JOptionPane.QUESTION_MESSAGE));
            preferences.put("usuario", JOptionPane.showInputDialog(null, "Usuario", "Configuración", JOptionPane.QUESTION_MESSAGE));
            preferences.put("contraseña", JOptionPane.showInputDialog(null, "Contraseña", "Configuración", JOptionPane.QUESTION_MESSAGE));
        }

        connectionString = new java.util.Formatter().format("jdbc:mysql://%1$s:%2$s/adsl?user=%3$s&password=%4$s",
                preferences.get("servidor","localhost"),
                preferences.get("puerto","3306"),
                preferences.get("usuario",null),
                preferences.get("contraseña",null)).toString();

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            Logger.getLogger(mySQL.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    public Connection getConnection() throws  SQLException {

        return DriverManager.getConnection(connectionString);

    }


}
