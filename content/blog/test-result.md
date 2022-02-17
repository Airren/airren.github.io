







## Test Result

### Passed Case:

| Cluster           | k8s     | scc         | crd-controller | cnf   | cert-manager |
| ----------------- | ------- | ----------- | -------------- | ----- | ------------ |
| overlay           | v1.17.0 | old version | latest version | 0.5.1 | v1.1.0       |
| hub/edge-1/edge-2 | v1.23.0 | -           | latest version | 0.5.1 | v1.6.1       |

### Failed Case:

**case1:**

| Cluster           | k8s     | scc         | crd-controller | cnf   | cert-manager |
| ----------------- | ------- | ----------- | -------------- | ----- | ------------ |
| overlay           | v1.23.0 | new version | latest version | 0.5.1 | v1.6.1       |
| hub/edge-1/edge-2 | v1.23.0 | -           | latest version | 0.5.1 | v1.6.1       |

**case2:**

| Cluster           | k8s     | scc         | crd-controller | cnf   | cert-manager |
| ----------------- | ------- | ----------- | -------------- | ----- | ------------ |
| overlay           | v1.23.0 | new version | latest version | 0.5.0 | v1.6.1       |
| hub/edge-1/edge-2 | v1.23.0 | -           | latest version | 0.5.0 | v1.6.1       |

#### **Bugs:**

1. CNF can't auto load cacert "CN=overlay1-cert" from `/etc/ipsec.d/cacerts/localtodevice*_ca.pem`.
   -  If multiple cacerts in one file, Strongswan only can load the first cert through `ipsec rereadall`.
   - Do not find the root cause, on in the Failed Case.
2. If the Default NAT CR  added the Iptables rule to CNF before the Data Plane Tunnel is established, it will add a wrong rule without an interface name. 
   - this bug also exist in Passed Case.

