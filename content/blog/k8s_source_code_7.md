---
title: K8s 源码阅读7
---



In this chapter we'll discuss the operational aspects of  controllers and operators, showing you how to package them, walking you through best practices for running controllers in production, and making sure that your extension points don't break your Kubernetes cluster, security, or performance-wise.

## Lifecycle Management and Packaging





Let's start with the low-hanging fruit: packaging and delivering your controllers so that a user can install it  in a straightforward manner.



### Packaging: The Challenge

While Kubernetes defines resources with manifest, typically written in YAML, a low-level interface to declare the state of resources, these manifest files have shortcoming. Most importantly in the context of packaging containerized apps, the YAML manifests are static; that is, all values in a YAML manifest are fixed. This means that if you want to change the container images in a deployment manifest, for example, you have to create a new manifest.

Let's look a concrete example, Assume you have the following Kubernetes deployment encoded in a YAML manifest called `mycontroller.yaml`, representing the custom controller you'd like user to install:

```yaml
apiVersion: apps/v1beta1
kind: Deployment
metadata:
	name: mycustomcontroller
spec:
	replicas: 1
	template:
		metadata:
			labels:
				app: customcontroller
    spec:
    	containers:
    	- name: thecontroller
    	  images: example/controller:0.1.0
    	  ports:
    	  - containerPort: 9999
    	  env:
    	  - name: REGION
    	    value: eu-west-1
```



Imagine the environment variable REGION defines certain runtime properties of you controller, such as the availability of other service like a managed service mesh. In other words, while the default values `eu-west-1` might be a sensible one, users can and likely will overwrite it, based on their own preferences or policies.

Now, given that the YAML manifest `mycontroller.yaml` itself is a static file with all values defined at the time of writing -- and clients such as `kubectl` don't inherently support variable parts in the manifest -- how do you enable users to supply variable values or overwrite existing values at runtime? That is, how in the preceding example can a user set REGION to, say, us-east-2 when they're installing it, using (for example) kubeclt apply?

To overcome these limitations of build-time, static YAML manifest  in Kubernetes, there are a few options to templatize the manifest (Helm, for example) or otherwise enable variable input (Kustomize), depending on user-provided values or runtime properties.



### Helm

Helm, which touts itself as the package manager for Kubernetes, was originally developed by Deis and is now  a Cloud Native Computing Foundation(CNCF) project with major contributors form Microsoft, Google, and Bitnami(now pat of VMware).

Helm helps you to install and upgrade Kubernetes applications by defining and applying so-called charts, effectively parameterized YAML manifests. Here is an excerpt of an example chart template.

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: {{ include "flagger.fullname" .}}
...
spec:
	replicas: 1
	strategy:
		type: Recreate
	selector:
		matchLabels:
			app.kubernetes.io/name: {{ template "flagger.name" .}}
			app.kubernetes.io/instance: {{ .Release.Name }}
		template:
			metadata:
				labels:
					app.kubernetes.io/name: {{ template "flagger.name" .}}
					app.kubernetes.io/instance: {{ .Release.Name }}
			sepc:
				serviceAccountName: {{ template "flagger.serviceAccountName" .}}
				containers:
				- name: flagger
				  securityContext:
				  	readOnlyRootFileSystem: true
				  	runAsUser: 10001
				  image: "{{ .Values.image.repository }}:{{ .Values.image.tag }}"
				
```



As you can see, variables are encoded in `{{ ._Some.value.here_ }}` format, which happens to be Go templates.

To install a chart, you can run the `helm install` command. While Helm has several ways to find and install charts, the easiest is to use on of the official stable charts:

```sh
# get the latest list of charts
helm repo update

# install MySQL
helm install stable/mysql

# list running apps
helm ls

# remove it
helm delete <helm name>
```

 



In order to package you controller, you will need to create a Helm Chart for it and publish it somewhere, by default to a public repository indexed and accessible through the Helm hub. 



Helm is popular, partly because of it ease of  use for end users. However, some argue the current Helm architecture introduces security risks. The good news is that the community is actively working on addressing those.











