 java -jar -Xmx128m cloud-servant-view-0.0.1-SNAPSHOT.jar
 
 ssh pi@192.168.0.13 -p 51313
 
 deploy to: /usr/cloud-servant-view
 
 mkdir /usr/cloud-servant-view
 sudo chown -R pi:1000 /usr/cloud-servant-view
 
 scp -P 51313 cloud-servant-view.jar pi@92.115.183.17:/usr/cloud-servant-view
 nohup java -jar -Xmx128m -Dname=cloud-servant-view cloud-servant-view.jar >/dev/null &
 
 ########################################################################################
 Copy resource images
 
 scp -P 51313 /local/path/to/resource/image pi@92.115.183.17:/media/pi/seagate/repo-min/img-resources
 
 ########################################################################################################
 ###	Create Linux job(using systemd) Automatic start on system startup
 ########################################################################################################

 (1) Move to /lib/systemd/system

 (2) Copy cloud-servant-view.service

 	scp -P 51313 cloud-servant-view.service pi@92.115.183.17:/usr/cloud-servant-view
 	
 	ssh pi@92.115.183.17 -p 51313
 	sudo cp cloud-servant-view.service /lib/systemd/system

 (3) Adjust ExecStart:
	 - set correct full java path
	 - set correct full cloud-servant path
	
 (4) Start as service:
	
	 sudo systemctl enable cloud-servant-view.service
	 sudo systemctl start cloud-servant-view.service
	
	 To check servcie status:
	
	 sudo systemctl status cloud-servant-view.service
	