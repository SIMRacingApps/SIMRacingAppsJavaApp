To run this sample Java App, you must add the SIMRacingApps Server to the classpath as follows when compiling and running:

There are various Java development tools. Here's how to compile and run with the standard JDK8. 

1. Download the example. Because the example has a package defined, it is important to put the file in a folder with the same name as the package. So, you should have a folder structure that looks like this. ```src\SIMRacingAppsJavaApp\SIMRacingAppsJavaApp.java```
1. Make sure you have the SIMRacingApps Server. You can get the Server from http://downloads.SIMRacingApps.com. It doesn't have to be running. But if it is, then you will have access to all of the Java documentation from the menu.
1. From the "src" folder execute the following to compile, then execute the example.
   1. javac -cp "{pathToServer}/SIMRacingAppsServer_{versionOfServer}.exe" SIMRacingAppsJavaApp\SIMRacingAppsJavaApp.java
   1. java -cp "{pathToServer}/SIMRacingAppsServer_{versionOfServer}.exe;." SIMRacingAppsJavaApp.SIMRacingAppsJavaApp

