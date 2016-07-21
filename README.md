# Android-Reverse-Shell-Client

Android Reverse TCP Shell, opens a connection with server specified by you, which work as tunnel for your commands to client Android device.

**How to use?**

Edit ReverseTcpRunnable.java:

1. Set "host" to server ip/host
2. Set "port" to server port(on which server is listining) 

**Install and Launch**

1. Install using ADB: "adb install reverse.apk"
2. Invoke service: "adb shell am startservice com.anythingintellect.androidreverseshell/.ReverseServic"

**Wait for connection..**

PS: Will share script to automate apk installation when any device connect to your Mac/PC.

