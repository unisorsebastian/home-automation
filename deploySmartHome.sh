#!/bin/bash
#cd /home/sebastian/workspace/home-automation
echo "*****make sure ------ git pull"
echo "*****make sure ------ mvn clean install "
echo 'now stopping tomcat8'
service tomcat8 stop
echo 'now remove tomcat files and copy new WAR'
rm -rf /var/lib/tomcat8/logs/*
rm -rf /var/lib/tomcat8/webapps/SmartHo*

#it seems that vars are not defined
#rm -rf $CATALINA_BASE/logs/*
#rm -rf $CATALINA_BASE/webapps/SmartHo*
cp /home/sebastian/workspace/home-automation/target/SmartHome.war /var/lib/tomcat8/webapps/SmartHome.war
#service tomcat8 start
echo "please -> service tomcat8 start"


