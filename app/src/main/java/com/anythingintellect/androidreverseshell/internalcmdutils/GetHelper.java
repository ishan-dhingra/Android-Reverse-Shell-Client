package com.anythingintellect.androidreverseshell.internalcmdutils;

/**
 * Created by ishan.dhingra on 22/12/16.
 */

public class GetHelper {

    public static String getDirNameFromCmd(String[] cmd) {
        String dirName = cmd[1];
        for (int i = 2; i < cmd.length; i++) {
            dirName += " " + cmd[i];
        }
        return dirName;
    }
}
