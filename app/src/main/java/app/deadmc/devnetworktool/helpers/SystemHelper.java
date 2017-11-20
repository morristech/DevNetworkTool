package app.deadmc.devnetworktool.helpers;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by Feren on 08.11.2016.
 */
public class SystemHelper {

    public static String executeCmd(String cmd, boolean sudo){
        try {

            Process p;
            if(!sudo)
                p= Runtime.getRuntime().exec(cmd);
            else{
                p= Runtime.getRuntime().exec(new String[]{"su", "-c", cmd});
            }
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String s;
            String res = "";
            while ((s = stdInput.readLine()) != null) {
                res += s;
            }
            p.destroy();
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }

    public static String getPing(String url) {
        String pingString = SystemHelper.executeCmd("ping -c 1 -w 1 "+url, false);
        return pingString;
    }


}
