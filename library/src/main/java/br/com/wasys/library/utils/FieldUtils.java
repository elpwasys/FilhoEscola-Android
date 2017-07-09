package br.com.wasys.library.utils;

import android.icu.text.NumberFormat;
import android.text.Editable;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by pascke on 02/08/16.
 */
public class FieldUtils {

    public static String getValue(TextView textView) {
        CharSequence text = textView.getText();
        if (StringUtils.isNotBlank(text)) {
            return String.valueOf(text);
        }
        return null;
    }

    public static String getValue(EditText editText) {
        Editable editable = editText.getText();
        String text = String.valueOf(editable);
        if (StringUtils.isNotBlank(text)) {
            return text;
        }
        return null;
    }

    public static <T> T getValue(Class<T> clazz, EditText editText) {
        T value = null;
        String text = getValue(editText);
        if (StringUtils.isNotBlank(text)) {
            value = TypeUtils.parse(clazz, text);
        }
        return value;
    }


    public static boolean isValueIsNullOrEmpty(EditText editText) {
        Editable editable = editText.getText();
        String text = String.valueOf(editable);
        if (text != null && StringUtils.isNotEmpty(text)) {
            return false;
        }
        return true;
    }

    public static boolean isValueIsNullOrEmpty(String text) {
        if (text != null && StringUtils.isNotEmpty(text)) {
            return false;
        }
        return true;
    }

    public static String removerEspeciaisEspaco(String valor) {
        if (valor == null || valor == "")
            return "";

        return valor.replace(".", "").replace("/", "").replace("\\", "").replace("(", "").replace(")", "").replace(",", "").replace("-", "")
                .replace("#", "").replace("%", "").replace("_", "").replace(" ", "");
    }

    public static String formatarCPF(String cpf) {
        int l = 11 - cpf.length();
        while (l > 0) {
            cpf = "0" + cpf;
            l--;
        }

        return formatarValor(cpf, "999.999.999-99");
    }

    public static String formatarValor(String valor, String mascara) {
        String stringToFormat = removerEspeciaisEspaco(valor.toString());
        int tamString = stringToFormat.length();
        int mc = 0;
        int i = 0;
        String result = "";

        while (i < tamString) {
            if (mc >= mascara.length())
                break;

            if (mascara.charAt(mc) == '9') {
                if (!isNaN(stringToFormat.charAt(i))) {
                    result += stringToFormat.charAt(i);
                    mc++;
                    i++;
                } else
                    i++;
            } else if (mascara.charAt(mc) == '#') {
                result += stringToFormat.charAt(i);
                mc++;
                i++;
            } else {
                result += mascara.charAt(mc);
                mc++;
            }
        }

        return result;
    }

    public static Boolean isNaN(String valor) {
        try {
            Double.parseDouble(valor);
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    public static Boolean isNaN(char valor) {
        return isNaN("" + valor);
    }
}
