package net.ccbluex.liquidbounce.utils.misc;

import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public final class MiscUtils extends MinecraftInstance {

    public static void showErrorPopup(final String title, final String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }

    public static void showURL(final String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        }catch(final IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static void showSystemNotification(String title, String text, TrayIcon.MessageType type, Long delay) {
        System.out.println(text);
        if (SystemTray.isSupported()) {
            TrayIcon trayIcon = new TrayIcon(Toolkit.getDefaultToolkit().createImage("icon.png"), title);
            trayIcon.setImageAutoSize(true);
            try {
                SystemTray.getSystemTray().add(trayIcon);
            } catch (AWTException e) {
                throw new RuntimeException(e);
            }
            trayIcon.displayMessage(title, text, type);

            if (delay >= 0L) {
                new Thread(() -> {
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    SystemTray.getSystemTray().remove(trayIcon);
                }).start();
            }
        } else {
            ClientUtils.error("SystemTray isn't supported!");
        }
    }

    public static File openFileChooser() {
        if (mc.isFullScreen())
            mc.toggleFullscreen();

        final JFileChooser fileChooser = new JFileChooser();
        final JFrame frame = new JFrame();

        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        frame.setVisible(true);
        frame.toFront();
        frame.setVisible(false);

        final int action = fileChooser.showOpenDialog(frame);
        frame.dispose();

        return action == JFileChooser.APPROVE_OPTION ? fileChooser.getSelectedFile() : null;
    }

    public static File saveFileChooser() {
        if (mc.isFullScreen())
            mc.toggleFullscreen();

        final JFileChooser fileChooser = new JFileChooser();
        final JFrame frame = new JFrame();

        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        frame.setVisible(true);
        frame.toFront();
        frame.setVisible(false);

        final int action = fileChooser.showSaveDialog(frame);
        frame.dispose();

        return action == JFileChooser.APPROVE_OPTION ? fileChooser.getSelectedFile() : null;
    }
}
