#!/bin/bash
#cd /home/sebastian/workspace/home-automation
echo 'stop tomcat'
service tomcat8 stop
echo 'remove tomcat files'
rm -rf /var/lib/tomcat8/logs/*
rm -rf /var/lib/tomcat8/webapps/SmartHo*
cp /home/sebastian/workspace/home-automation/target/SmartHome.war /var/lib/tomcat8/webapps/SmartHome.war
#service tomcat8 start














