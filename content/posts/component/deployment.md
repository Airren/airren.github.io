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
-e PUID=1000   -e PGID=1000   \
-p 8086:80   -p 8463:443   \
-v heimdall:/config      \
linuxserver/heimdall:latest
```





Gogs

```sh
docker run  -d --restart=always --name=gogs \
-p 10022:22 -p 10880:3000 \
-v /home/airren/qiang/gogs:/data gogs/gogs
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



docker volume create jenkins_data
docker run -d --restart=always --name jenkins \
-u 0 --privileged \
-p 8080:8080 -p 50000:50000 \
-v jenkins_data:/var/jenkins_home \
-e https_proxy=http://proxy-prc.intel.com:913 \
-e http_proxy=http://proxy-prc.intel.com:913 \
-e no_proxy=10.0.0.0/8,.sh.intel.com,localhost,127.0.0.1/8 \
-v /run/containerd/containerd.sock:/run/containerd/containerd.sock -v $(which docker):/usr/bin/docker \
jenkins/jenkins:lts-jdk11
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
    -s BDO7-O206-9LEE-ULZZ
  
  
 
```



### Redis

```sh
docker run -d --restart=always --name redis \
-p 6379:6379 redis
```



### RabbitMq

````sh
docker run -d --restart=always --name rabbitMq \
 -e RABBITMQ_DEFAULT_USER=root -e RABBITMQ_DEFAULT_PASS=1q2w3e4r5t \
 -p 5672:5672 -p 15672:15672 \
 --hostname my-rabbit rabbitmq:3-management

````



### Prometheus

```sh
docker volume create prometheus_data
docker run  -d --restart=always --name prometheus\
    -p 9199:9090 \
    -v prometheus_data:/etc/prometheus \
    prom/prometheus
```



### Docker Registry

```sh
docker volume create registry_data
docker run  -d --restart=always --name registry\
    -p 5000:5000 \
    -v registry_data:/var/lib/registry \
    registry
    
# airren@docker:~$ cat /etc/docker/daemon.json
# {
# "insecure-registries": ["docker.lan:5000"]
# }
# http://docker.lan:5000/v2/echo-bio-tools/tags/list
# http://docker.lan:5000/v2/_catalog
```



## configure ubuntu as a router

```shell
sudo iptables -t nat -A POSTROUTING -o eth1 -j MASQUERADE
sudo iptables -A FORWARD -i eth1 -o eth0 -m state --state RELATED,ESTABLISHED -j ACCEPT
sudo iptables -A FORWARD -i eth0 -o eth1 -j ACCEPT

# eth1 wan 
# eth0 lan




sudo iptables -t nat -A POSTROUTING -s 10.8.0.0/24 -o ens160 -j MASQUERADE
sudo iptables -t nat -A POSTROUTING -s 10.0.12.0/24 -o ens160 -j MASQUERADE



vi /etc/sysctl.conf

net.ipv4.ip_forward = 1   # 没有则添加，有修改为1（0禁止，1开启）

sysctl -p

```





## Etcd

```sh
podman run -d --name Etcd-server \
    --network app-tier \
    --publish 2379:2379 \
    --publish 2380:2380 \
    --env ALLOW_NONE_AUTHENTICATION=yes \
    --env ETCD_ADVERTISE_CLIENT_URLS=http://etcd-server:2379 \
    bitnami/etcd:latest
```





## V2ray

```sh
docker run -d --name v2ray -v /home/ubuntu/v2ray/config.json:/etc/v2ray/config.json -p 10086:10086 v2fly/v2fly-core run -c /etc/v2ray/config.json
```



## ChatGPT

```sh
docker run  -e PANDORA_CLOUD=cloud -e PANDORA_SERVER=0.0.0.0:8899 -p 8899:8899 -d pengzhile/pandora
```





## Netdata

```sh
docker run -d --name=netdata   --pid=host   --restart=always   -p 19999:19999   --network=host   -v netdataconfig:/etc/netdata   -v netdatalib:/var/lib/netdata   -v netdatacache:/var/cache/netdata   -v /etc/passwd:/host/etc/passwd:ro   -v /etc/group:/host/etc/group:ro   -v /proc:/host/proc:ro   -v /sys:/host/sys:ro   -v /etc/os-release:/host/etc/os-release:ro   -v /var/run/docker.sock:/var/run/docker.sock:ro   --restart unless-stopped   --cap-add SYS_PTRACE   --cap-add SYS_ADMIN   --security-opt apparmor=unconfined   netdata/netdata
```



VNC Chrome

```sh
```



### Echo-dev

```sh
docker run -d -u 1001:1001 --restart=always --name echo-bio-conda -v /home/echo/echo-cloud:/echo-cloud centos:centos7.9.2009 sleep infinity



docker run --rm  -u 1001:1001 -v /home/echo/echo-cloud:/echo-cloud centos:centos7.9.2009 bash
```

