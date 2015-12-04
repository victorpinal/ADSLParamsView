package com.victor.datos;

import com.victor.mySQL;

import javax.swing.*;
import java.sql.ResultSet;
import java.util.Vector;

/**
 * Created by victo on 04/12/2015.
 */
public class CmbModel extends DefaultComboBoxModel {

    private Vector<Ip_Class> datos = new Vector<>();

    public CmbModel() {

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
            if (e.equals(new Ip_Class(0,ip,""))) index = datos.indexOf(e);
        }
        return index;
    }

}
