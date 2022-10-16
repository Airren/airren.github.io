Elastic Search 

### cerebro

install  cerebro by docker

```shell
docker run -d -p 9001:9000 --net elastic  lmenezes/cerebro:0.8.3
```

Add host configration to cerebro

```shell
# /opt/cerebro/conf
hosts = [
  {
    host = "https://172.18.0.2:9200"
    name = "Secured Cluster"
    auth = {
      username = "elastic"
      password = "uj6neSA=STQgd*SDIsyN"
    }
  }
]

play.ws.ssl {
  trustManager = {
    stores = [
      { type = "PEM", path = "/opt/cerebro/http_ca.crt" }
    ]
  }
}

# Disabling certificate validation
# #play.ws.ssl.loose.acceptAnyCertificate=true
```





### configure ubuntu as a router

```shell
sudo iptables -t nat -A POSTROUTING -o eth1 -j MASQUERADE
sudo iptables -A FORWARD -i eth1 -o eth0 -m state --state RELATED,ESTABLISHED -j ACCEPT
sudo iptables -A FORWARD -i eth0 -o eth1 -j ACCEPT
```



```shell

```
