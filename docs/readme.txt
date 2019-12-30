 java -jar -Xmx128m cloud-servant-view-0.0.1-SNAPSHOT.jar
 
 deploy to: /usr/cloud-servant-view
 
 mkdir /usr/cloud-servant-view
 sudo chown -R pi:1000 /usr/cloud-servant-view
 
 scp -P 51313 cloud-servant-view.jar pi@92.115.183.17:/usr/cloud-servant-view
 nohup java -jar -Xmx128m -Dname=cloud-servant-view cloud-servant-view.jar >/dev/null &