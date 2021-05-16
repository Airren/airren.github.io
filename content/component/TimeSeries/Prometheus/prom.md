---
title: 「Prom」Prometheus 安装及使用简介
menu:
    sidebar:
---



## Prom Intro

[Prometheus](https://github.com/prometheus) 是[SoundCloud](https://soundcloud.com/)开源的系统监控和报警工具集。通过Prometheus可以进行时序数据的采集、监控和报警。

#### 时序数据模型 Time Series Data Model

Metric 是一个对时序指标的统称，例如.：`http_requests_total` - the total number of HTTP requests received，就可以成为一条Metric

在Prometheus 中每一个时序序列都是由于Metric Name 和Key-Value组成的

例如： 

```sh
http_request_total{host=192.168.2.1,status=200}
```

**Metric Name**： `http_request_total` 在Prometheus中Metric Name只能由大小写字母、数字、下划线、冒号组成，且不能以数字开头，对应正则为`[a-zA-Z_:][a-zA-Z0-9_:]*`。冒号保留，会在定义规则的时候使用。

**Label Name**: `host` 和`status`都属于Label. 在Prometheus中Label Name只能由大小写字母、数字、下划线组成，且不能以数字开头，对应的正则为`[a-zA-Z_][a-zA-Z0-9_]*`。一般`_`开头的Labels保留位系统内部使用。

**Notation**

```sh
<Metric Name>{<Label Name>=<Label Value>,...}
api_http_request_total{method="POST", handler="/messages"}
```



> Metric 类型
>
> - Counter：一种累加的Metric, 例如请求总数量，出现的错误数量
> - Gauge：例如 cpu usage，温度，是一个瞬时值
> - Histogram： 柱状图，用于观察结果采样，分组及统计。例如： 请求持续时间。是对一段时间内的数据进行采样，并能够对其指定区间以及总数进行统计。需要**根据区间计算**
> - Summary：类似Histogram, 用于表示一段时间内的数据采样结果，不是临时算出来的，结果早已存储。



## PromQL

PromQL是Prom的数据查询`DSL(Domain Specified Language)`语言。

**结果类型：**

| Type                       | Desc                                   | Demo                        |
| -------------------------- | -------------------------------------- | --------------------------- |
| 瞬时数据（Instant Vector） | 包含一组TimeSeries，每个时序只有一个点 | `http_request_total`        |
| 区间数据（Range Vector）   | 包含一组TimeSeries, 每个时序有多个点   | `http_request_total[5m]`    |
| 纯量数据（Scalar）         | 只有一个数据，没有Time Series          | `count(http_request_total)` |

**查询方式**

```sh
logback_events_total{level="info"}
```



查询支持正则匹配

```sh
http_request_total{code!="200"}  # code != 200
http_request_total{code=~"2.."}  # code = 2xx
http_request_total{code!~"2.."}  # code != 2xx
```



其他查询

```sh
# 取整
floor(avg(http_request_total{code="200"}))
ceil(avg(http_request_total{code="200"}))
# 查看每秒数据
rate(http_request_total[5m])

#模糊查询： level="inxx
logback_events_total{level=~"in.."}
logback_events_total{level=~"in.*"}

```



聚合查询：

```sh
# count
count(logback_event_total)
# sum
sum(logback_event_total)
# avg
avg(logback_event_total)
# topk
topk(2, logback_events_total)

# irate  如查询过去5分钟的平均值 
irate( logback_events_total[5m])
```

## Component

> 所有基础组件的安装都基于Docker。

### Prometheus

```sh
docker pull prom/prometheus
docker run -d -p 0.0.0.0:9090:9090 --name prometheus prom/prometheus
```

安装完成后可以访问`9090`端口，可以看到Prometheus管理页面。如下图为查询`go_routines`的情况。

查询当前数据：

```http
GET http://devbox:9090/api/v1/query?query=go_goroutines&time=1610127237.659itrt
```



![image-20210109012636249](prom/image-20210109012636249.png)

查询历史数据：

```http
GET http://devbox:9090/api/v1/query_range?query=go_goroutines&start=1610126024.257&end=1610126924.257&step=3
```

![image-20210109013121175](prom/image-20210109013121175.png)



配置

```yaml
# my global config
global:
	# 拉取target的默认时间间隔
  scrape_interval:     15s # Set the scrape interval to every 15 seconds. Default is every 1 minute.
  evaluation_interval: 15s # Evaluate rules every 15 seconds. The default is every 1 minute. 执行Rule的时间间隔
  # scrape_timeout is set to the global default (10s). 拉取超时时间

# Alertmanager configuration
alerting:
  alertmanagers:
  - static_configs:
    - targets:
      # - alertmanager:9093

# Load rules once and periodically evaluate them according to the global 'evaluation_interval'.
rule_files:
  # - "first_rules.yml"
  # - "second_rules.yml"

# A scrape configuration containing exactly one endpoint to scrape:
# Here it's Prometheus itself.
scrape_configs:
  # The job name is added as a label `job=<job_name>` to any timeseries scraped from this config.
  - job_name: 'prometheus'
    # metrics_path defaults to '/metrics'
    # scheme defaults to 'http'.
    static_configs:
    - targets: ['localhost:9090']
  - job_name: 'pushgateway'
    # metrics_path defaults to '/metrics'
    # scheme defaults to 'http'.
    static_configs:
    - targets: ['10.227.4.115:9091']
  - job_name: 'alertmanager'
    # metrics_path defaults to '/metrics'
    # scheme defaults to 'http'.
    static_configs:
    - targets: ['10.227.4.115:9093']
```



### PushGateway

安装

```sh
docker pull prom/pushgateway
docker run -d --name pushgateway -p 0.0.0.0:9091:9091 prom/pushgateway
```



### AlertManager





## 参考文档

https://www.jianshu.com/p/93c840025f01

