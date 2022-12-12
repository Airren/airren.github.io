---
title: Deploy essential components by Docker
---





K8sStudy:

1. Docker Node: Jenkins, Mysql, casbin_allinone, portioner,hemidall (1 node 4 core, 8G)

2. ODMS with NFS. (3 node, 1 NFS node)
3. K8s node for CNI study
4. K8s node for others, DPDK?
5. Dev node for source code and dev
6. move Openwrt and VM to a same node

## Application

### Casdoor

```sh
docker run -d --restart=always --name casdoor \
-p 8001:8000 \
casbin/casdoor-all-in-one
```



### Heimdall

```sh
docker volume create heimdall
docker run -d --restart unless-stopped --name=heimdall   \
-e PUID=1000   -e PGID=1000   -e TZ=Europe/London   \
-p 8086:80   -p 8463:443   \
-v heimdall:/config      \
linuxserver/heimdall:latest
```



## CI&CD

### Jenkins

```sh
docker volume create jenkins_data
docker run -d --restart=always --name jenkins \
-u 0 --privileged \
-p 8080:8080 -p 50000:50000 \
-v jenkins_data:/var/jenkins_home \
-v /var/run/docker.sock:/var/run/docker.sock -v $(which docker):/usr/bin/docker \
jenkins/jenkins:lts-jdk11

# -v /var/run/docker.sock:/var/run/docker.sock -v $(which docker):/usr/bin/docker \

# must ubuntu20.04 
```



Docker pipeline configuration

```js
           script{
                          docker.withRegistry( '', registryCredential ) {
                             dockerImage.push()
                           }
             }

```



https://octopus.com/blog/jenkins-docker-ecr

https://medium.com/@gustavo.guss/jenkins-building-docker-image-and-sending-to-registry-64b84ea45ee9

## Database

### Mysql

```sh
docker volume create mysql_data

docker run -d --restart=always --name mysql \
-p 3306:3306 \
-e MYSQL_ROOT_PASSWORD=1q2w3e4r%T \
-v mysql_data=/var/lib/mysql \
mysql:8.0.30

```



### Elastic Search 



```sh
docker run -d  --name es01 \
--net elastic -e ES_JAVA_OPTS="-Xms1g -Xmx1g"   \
-p 9200:9200 -p 9300:9300 -it \
docker.elastic.co/elasticsearch/elasticsearch:8.4.1

# /usr/share/elasticsearch/config/certs/http_ca.crt
# /usr/share/elasticsearch/bin/elasticsearch-reset-password -u elastic
```



### Cerebro

```shell
docker volume create cerebro_data
docker run -d --restart=always --name cerebro \
-p 9001:9000 --net elastic  \
-v cerebro_data:/opt/cerebro \
lmenezes/cerebro:0.9.4
```

Add host configration to cerebro

```shell
# /opt/cerebro/conf
hosts = [
  {
    host = "https://10.105.61.90:9200"
    name = "ES Cluster"
    auth = {
      username = "elastic"
      password = "MyPassword"
    }
  }
]

play.ws.ssl {
  trustManager = {
    stores = [
      { type = "PEM", path = "/opt/cerebro/conf/http_ca.crt" }
    ]
  }
  loose = {
       disableHostnameVerification=true }
}

# Disabling certificate validation
# #play.ws.ssl.loose.acceptAnyCertificate=true
```





### MinIO

```sh
docker volume create minio_data

docker run -d --restart=always  --name minio \
   -p 9000:9000 \
   -p 9099:9099 \
   -v minio_data:/data \
   -e "MINIO_ROOT_USER=admin" \
   -e "MINIO_ROOT_PASSWORD=1q2w3e4r%T" \
   minio/minio server /data --console-address ":9099"
```



what is S3

## Docker manager

### Portainer

```sh
docker volume create portainer_data
docker run -d --restart=always --name portainer  \
-p 8000:8000 -p 9443:9443 -p 9090:9000 \
-v /var/run/docker.sock:/var/run/docker.sock   \
-v portainer_data:/data     \
portainer/portainer-ce:latest

```



### Jira

```sh
  docker volume create jira_data
  docker run -d --restart=always --name jira \
  -p 8082:8080 -v jira_data:/var/jira  -e TZ='Asia/Shanghai'\
  haxqer/jira:9.5.0
  
  
  docker exec jira java -jar /var/agent/atlassian-agent.jar \
    -p jira \
    -m haxqer666@gmail.com \
    -n haxqer666@gmail.com \
    -o http://echo-bio.cn:8082 \
    -s  B2GJ-KD5D-6V6J-KCK7
  
  
 
```





## configure ubuntu as a router

```shell
sudo iptables -t nat -A POSTROUTING -o eth1 -j MASQUERADE
sudo iptables -A FORWARD -i eth1 -o eth0 -m state --state RELATED,ESTABLISHED -j ACCEPT
sudo iptables -A FORWARD -i eth0 -o eth1 -j ACCEPT



sudo iptables -t nat -A POSTROUTING -s 10.8.0.0/24 -o ens160 -j MASQUERADE
sudo iptables -t nat -A POSTROUTING -s 10.0.12.0/24 -o ens160 -j MASQUERADE



vi /etc/sysctl.conf

net.ipv4.ip_forward = 1   # 没有则添加，有修改为1（0禁止，1开启）

sysctl -p

```



