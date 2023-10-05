package net.ccbluex.liquidbounce.utils.germmod.uncode;

import org.apache.commons.compress.utils.IOUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Password {
    public static String getCPUID() {
        return runCMD("wmic cpu get ProcessorId", 2);
    }

    public static String getWin32() {
        return runCMD("wmic path win32_physicalmedia get serialnumber", 2);
    }

    public static String runCMD(String command, int right) {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bf = null;
        try {
            Process runtime = Runtime.getRuntime().exec(command.split(" "));
            runtime.getOutputStream().close();
            bf = new BufferedReader(new InputStreamReader(runtime.getInputStream()));
            int sum = 0;
            String baseCode;
            while ((baseCode = bf.readLine()) != null) {
                ++sum;
                if (right <= 0) {
                    stringBuilder.append(baseCode).append("\r\n");
                }
                if (sum == right) {
                    IOUtils.closeQuietly(bf);
                    return baseCode.trim();
                }
            }
            bf.close();
        }
        catch (Exception e) {
            IOUtils.closeQuietly(bf);
            e.printStackTrace();
            return stringBuilder.toString();
        }
        return stringBuilder.toString();
    }
}
