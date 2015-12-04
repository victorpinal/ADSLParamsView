package com.victor;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

/**
 * Created by victormanuel on 03/12/2015.
 */
public class mySQL {

    private static mySQL instance = new mySQL();
    private String connectionstring;

    private mySQL() {

        Preferences preferences = Preferences.userNodeForPackage(mySQL.class);

        //Load connection data
        if (preferences.get("servidor", null) == null) {
            preferences.put("servidor", JOptionPane.showInputDialog(null, "Servidor MySQL", "Configuración", JOptionPane.QUESTION_MESSAGE));
            preferences.put("puerto", JOptionPane.showInputDialog(null, "Puerto", "Configuración", JOptionPane.QUESTION_MESSAGE));
            preferences.put("usuario", JOptionPane.showInputDialog(null, "Usuario", "Configuración", JOptionPane.QUESTION_MESSAGE));
            preferences.put("contraseña", JOptionPane.showInputDialog(null, "Contraseña", "Configuración", JOptionPane.QUESTION_MESSAGE));
        }

        connectionstring = new Formatter().format("jdbc:mysql://%1$s:%2$s/adsl?user=%3$s&password=%4$s",
                preferences.get("servidor", "localhost"),
                preferences.get("puerto", "3306"),
                preferences.get("usuario", null),
                preferences.get("contraseña", null)).toString();

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            Logger.getLogger(mySQL.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    public static mySQL get() {
        return instance;
    }

    public Connection getConnection() throws SQLException {

        return DriverManager.getConnection(connectionstring);

    }

}
