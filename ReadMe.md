# Syslog-ng Monitor for Android - Test Application

## About ##

Syslog-ng is an open source implementation of the Syslog protocol for Unix and Unix-like systems. It extends the original syslogd model with content-based filtering, rich filtering capabilities, flexible configuration options and adds important features to syslog, like using TCP for transport.

### Syslog-ng Monitor for Android ###

[Syslog-ng Monitor for Android] is a monitoring application and with this application, you can execute various commands to know whether the monitored Syslog-ng is alive, get current statistics of Syslog-ng(s), and to view the configuration of Syslog-ng. In this application you can add/store details of Syslog-ng(s) to monitor with ease. You can also edit and delete the details listed after storing. After storing the details you can perform the above mentioned commands on single click.

### Test Application ###

This repository is a test application for [Syslog-ng Monitor for Android]

This comprises of both Unit Testing and Extensive UI Testing. The UI Testing is done with Robotium. 

Robotium is an Android test automation framework that has full support for native and hybrid applications.

## Usage ##

Import both the Projects. Add [Syslog-ng Monitor for Android] to the Build Path.

After Installing [Syslog-ng Monitor for Android] in a device, Run testSyslogngMonitor as Android JUnit Test.

Running individual test classes may not result in successful execution as most of the tests are based on previous test’s results.

### Caution ###

Test Application clear application data before and after test execution
It will result in loss of data stored previously.

[Syslog-ng Monitor for Android]: https://github.com/Krishna41/Syslog-ng-Monitor-Android