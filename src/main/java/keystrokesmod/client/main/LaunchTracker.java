package keystrokesmod.client.main;

import java.security.*;
import java.nio.charset.*;
import org.apache.http.message.*;
import org.apache.http.client.entity.*;
import org.apache.http.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import io.netty.handler.codec.http.*;
import java.net.*;
import java.util.*;
import java.io.*;

public class LaunchTracker
{
    static void registerLaunch() throws IOException {
        final HttpURLConnection pathsConnection = (HttpURLConnection)new URL("https://launchtracker.raventeam.repl.co/paths").openConnection();
        final String pathsText = getTextFromConnection(pathsConnection);
        pathsConnection.disconnect();
        final StringBuilder fullURL = new StringBuilder();
        fullURL.append("https://launchtracker.raventeam.repl.co");
        for (final String line : pathsText.split("\n")) {
            if (line.startsWith("RavenB+")) {
                final String[] splitLine = line.split(" ~ ");
                fullURL.append(splitLine[splitLine.length - 1]);
            }
        }
        final CloseableHttpClient httpclient = HttpClients.createDefault();
        final HttpPost httppost = new HttpPost(fullURL.toString());
        final List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        String mac = getMac();
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        final byte[] encodedhash = digest.digest(mac.getBytes(StandardCharsets.UTF_8));
        mac = bytesToHex(encodedhash);
        params.add((NameValuePair)new BasicNameValuePair("hashedMacAddr", mac));
        params.add((NameValuePair)new BasicNameValuePair("clientVersion", Raven.versionManager.getClientVersion().toString()));
        params.add((NameValuePair)new BasicNameValuePair("latestVersion", Raven.versionManager.getLatestVersion().toString()));
        params.add((NameValuePair)new BasicNameValuePair("config", Raven.configManager.getConfig().getData().toString()));
        httppost.setEntity((HttpEntity)new UrlEncodedFormEntity((List)params, "UTF-8"));
        final HttpResponse response = (HttpResponse)httpclient.execute((HttpUriRequest)httppost);
        final HttpResponseStatus entity = response.getStatus();
    }
    
    static String getMac() {
        Enumeration<NetworkInterface> networkInterfaces = null;
        try {
            networkInterfaces = NetworkInterface.getNetworkInterfaces();
        }
        catch (SocketException e) {
            throw new RuntimeException(e);
        }
        while (networkInterfaces.hasMoreElements()) {
            final NetworkInterface ni = networkInterfaces.nextElement();
            byte[] hardwareAddress = new byte[0];
            try {
                hardwareAddress = ni.getHardwareAddress();
            }
            catch (SocketException e2) {
                throw new RuntimeException(e2);
            }
            if (hardwareAddress != null) {
                final String[] hexadecimalFormat = new String[hardwareAddress.length];
                for (int i = 0; i < hardwareAddress.length; ++i) {
                    hexadecimalFormat[i] = String.format("%02X", hardwareAddress[i]);
                }
                return String.join(":", (CharSequence[])hexadecimalFormat).toLowerCase();
            }
        }
        return "UNKNOWN";
    }
    
    private static String getTextFromConnection(final HttpURLConnection connection) {
        if (connection != null) {
            try {
                final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String result;
                try {
                    final StringBuilder stringBuilder = new StringBuilder();
                    String input;
                    while ((input = bufferedReader.readLine()) != null) {
                        stringBuilder.append(input).append("\n");
                    }
                    final String res = stringBuilder.toString();
                    connection.disconnect();
                    result = res;
                }
                finally {
                    bufferedReader.close();
                }
                return result;
            }
            catch (Exception ex) {}
        }
        return "";
    }
    
    private static String bytesToHex(final byte[] hash) {
        final StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; ++i) {
            final String hex = Integer.toHexString(0xFF & hash[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
