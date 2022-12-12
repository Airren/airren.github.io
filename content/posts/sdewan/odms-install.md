---
title: ODMS Install
---



Home Page: https://wiki.ith.intel.com/display/ITSODMS/Observability+Data+Management+Suite+Home

[TOC]

## Operator

### Prometheus-operator

https://prometheus-operator.dev/

https://github.com/prometheus-operator/prometheus-operator

The prometheus operator manages the Promentheus Clust atop Kubernetes.



### Opentelemetry-operator

https://github.com/open-telemetry/opentelemetry-operator



### Kubernetes reflector

https://github.com/emberstack/kubernetes-reflector



Reflector is a Kubernetes addon designed to monitor changes to resources (secrets and configmaps) and reflect changes to mirror resources in the same or other namespaces.





## OpenTelemetry

### Agent & Gateway	

Configure by CR `opentelemetrycollectors.opentelemetry.io`

**Agent** collect Metric, Logs, and can add labels or attributes, and finally push the data to the `opentelemetry gateway`.

**Gateway** receive the data from all of the **Agent**, and store the data to prometheus, jaeger and elasticsearch.

### Metrics







## Jaeger





# Metrics	

## Exporter

Telegraf

cAdvisor

eBPF-exporter

IPMI-exporter

EMON-exporter

OpenTelemetry-Collector		

## Prometheus

### Service Discovery

prometheus discovery metrics api by CR `servicemonitors.monitoring.coreos.com`.

For kubernetes components metrics, add `servicemonitor` by [kube-prometheus-stack](https://github.com/prometheus-community/helm-charts/tree/main/charts/kube-prometheus-stack).

```sh
NAMESPACE        NAME                                           AGE
default          odms-grafana                                   2d19h
default          odms-kube-prometheus-alertmanager              2d19h # kube-prometheus-stack
default          odms-kube-prometheus-apiserver                 2d19h # kube-prometheus-stack
default          odms-kube-prometheus-coredns                   2d19h # kube-prometheus-stack
default          odms-kube-prometheus-kube-controller-manager   2d19h # kube-prometheus-stack
default          odms-kube-prometheus-kube-etcd                 2d19h # kube-prometheus-stack
default          odms-kube-prometheus-kube-proxy                2d19h # kube-prometheus-stack
default          odms-kube-prometheus-kube-scheduler            2d19h # kube-prometheus-stack
default          odms-kube-prometheus-kubelet                   2d19h # kube-prometheus-stack
default          odms-kube-prometheus-prometheus                2d19h # kube-prometheus-stack
default          odms-kube-state-metrics                        2d19h
default          odms-otel-gateway-monitor                      2d19h # Metrics for otel
default          odms-prometheus-node-exporter                  2d19h
odms-operators   odms-kube-prometheus-operator                  17d   # kube-prometheus-stack
```



## Grafana

### Configure DataResource

```yaml
kind: ConfigMap
metadata:
  labels:
    app: odms-otel-grafana
    grafana_datasource: "1"    # label for grafane, who is the operator?
  name: odms-otel-grafana-datasource
  namespace: default
apiVersion: v1
data:
  datasource.yaml: |-
    apiVersion: 1
    datasources:
      - name: "Prometheus"
```



### Configure DashBoard

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  labels:
    grafana_dashboard: "1"   # label for grafane, who is the operator?
  name: odms-dashboards
  namespace: default
data:
	CPU.json: |-
		# dashboard json configuration
```











## NFS





1. Where is the grafana dashboras configuration?
2. Where is the data resource of Metrics, Log & Trace?
3. 

















## Reference

[How to install and configure an NFS server on Ubuntu](https://www.tecmint.com/install-nfs-server-on-ubuntu/)

[NFS external provisioner](https://github.com/kubernetes-sigs/nfs-subdir-external-provisioner)

