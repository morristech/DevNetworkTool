package app.deadmc.devnetworktool.helpers;

/**
 * Created by Feren on 15.06.2016.
 */
public class CheckHelper {
    public static boolean isValidIp (String ip) {
        try {
            if ( ip == null || ip.isEmpty() ) {
                return false;
            }

            String[] parts = ip.split( "\\." );
            if ( parts.length != 4 ) {
                return false;
            }

            for ( String s : parts ) {
                int i = Integer.parseInt( s );
                if ( (i < 0) || (i > 255) ) {
                    return false;
                }
            }
            if ( ip.endsWith(".") ) {
                return false;
            }

            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    public static boolean isValidPort(int port) {
        if ((port <65536) && (port > 0))
            return true;
        else
            return false;
    }

    public static int portFromString(String portString) {
        int port = 80;
        if (!portString.isEmpty()) {
            try {
                port = Integer.parseInt(portString.toString());
            } catch (Exception e) {}
        }
        return port;
    }

}
