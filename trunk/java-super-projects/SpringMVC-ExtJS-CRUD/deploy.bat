set tc=C:\project\tools\apache-tomcat-6.0.43
cmd /c mvn install

copy target\*.war %tc%\webapps
