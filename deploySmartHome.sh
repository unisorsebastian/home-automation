#!/bin/bash
#cd /home/sebastian/workspace/home-automation
echo "make sure you got latest code - git pull"
echo "execute mvn clean install "
echo 'stopping tomcat8'
service tomcat8 stop
echo 'remove tomcat files and copy new WAR'
rm -rf /var/lib/tomcat8/logs/*
rm -rf /var/lib/tomcat8/webapps/SmartHo*
cp /home/sebastian/workspace/home-automation/target/SmartHome.war /var/lib/tomcat8/webapps/SmartHome.war
#service tomcat8 start
echo "please -> service tomcat8 start"













