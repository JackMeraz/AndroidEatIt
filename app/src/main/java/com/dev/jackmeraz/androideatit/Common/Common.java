package com.dev.jackmeraz.androideatit.Common;

import com.dev.jackmeraz.androideatit.Model.User;

/**
 * Created by jacobo.meraz on 18/11/2017.
 */

public class Common {

    public static User CurrentUser;

    public static String convertCodigoAStatus(String status) {

        if (status.equals("0"))
            return "En Espera de ser Procesado";
        else if (status.equals("1"))
            return "Preparando Orden para ser Enviada";
        else
            return "Orden en proceso de entrega";
    }
}
