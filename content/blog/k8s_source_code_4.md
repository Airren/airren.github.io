---
title: K8s 源码阅读 4
---











### Short  Names and Categories

Like native resources, custom resources might have long resources names. CRs can have `short names` as well.

Again, kubectl learns about short names via discovery information.

```sh
apiVersion: apiextensions.k8s.io/v1beta1
kind: CustomResourcesDefinition
metadata:
	name: ats.cnat.programming-kubernetes.info
spec:
	...
	shortNames:
	- at
```





Further, CRs--as any other resources--can be part of categories. The most common use is the all category, as in `kubectl get all`. Is lists all user-facing resources in a cluster, like pods and services.

The CRs define in the cluster can join a category or create their own category via the categories field.

```yaml
apiVersion: apiextensions.k8s.io/v1beta1
kind: CustomResourcesDefinition
metadata:
	name: ats.cnat.programming-kubernetes.info
spec:
	...
	categories:
	- all
```



### Printer Cloumns

The kubectl CLI tool users server-side printing to render the output of  kubectl get. This means that it queries the API server for the columns to display and the values in each row.

Custom resources support server-side printer columns as well, via `additionalPrinterColumns`

```yaml
apiVersion: apiextensions.k8s.io/v1beta1
kind: CustomResourcesDefinition
metadata:
	name: ats.cnat.programming-kubernetes.info
spec:
	additionalPrinterColumns: # optional
	- name: kubectl column name
	  type: OpenAPI type for the colum
	  format: OpenAPI format for the column # optional
	  description: human-readdable description of the column #optional
	  priority: integer, always zero supported by kubectl
	  JSONPath: JSON path inside the CR for the despaly value
```



With this, the example CRD from the introduction cloud be extend with addtionalPrinterColumns like this:

```sh
additionalPrinterColumns: # optional
- name: schedule
  type: string
  JSONPath: .spec.schedule
- name: command
  type: string
  JSONPath: .spec.command
- name: phase
  type: string
  JSONPath: .status.phase
```



Then the kubectl would render a cnat resource as follows:

```sh
$ kubectl get ats
Name  SCHEDULE                    COMMAND               PHASE
foo   2019-07-03T02:00:00Z     echo "hello world"       Pending
```









## A Developer's View on Custom Resource





### Dynamic Client





### Typed Client



#### **Anatomy of a type**

Kinds are represented as Golang structs. Usually the `struct is named as the kind` and is `placed in a package corresponding to the group and version` of the GVK at hand. A common convention is to place the GVK `group/version.Kind` into a Go `package pkg/apis/group/version` and Define a Golang struct `Kind` int the file `types.go`.



Every Golang type corresponding to a GVK embeds the `TypeMeta` struct from the package `k8s.io/apimachinery/pkg/apis/meta/v1`. TypeMeta just contains of the `Kind` and `ApiVersion` fields.

```go
type TypeMeta struct{
    APIVersion string `json:"apiVersion,omitempty" yaml:"apiVersion,omitempty"`
    Kind string `json:"kind,omitempty" yaml:"kind,omitempty"`
}
```

In addition, every too-level kind--that is, one that has its own endpoint and therefore one(or more) corresponding GVRs--has to store a name, a namespace for namespaced resources, add a pretty long number of further metelevel fields. All these are stored in a struct called `ObjectMeta` in the package `k8s.io/apimachinery/pkg/meta/v1`:

```go
type ObjectMeta struct{
    Name string `json:"name,omitempty"`
    Namespace string `json:"namespace,omitempty"`
    Labels map[string]string 
    Annotation map[string]string
}
```



Kubernetes top-level types look very similar to each other in the sense that they usually have a spec and  a status. See this example of a deployment from `k8s.io/kubernetes/apps/v1/types.go`

```go
type Deployment struct{
    metav1.TypeMeta `json:",inline"`
    metav1.ObjectMeta `json:"metadata,omitempty"`
    
    Spec DeploymentSpec `json:"spec,ommitempty"`
    Status DeploymentStatus `json:"status,omitempty"`
}
```

While actual content of the types for spec and status differs significantly between different types, this split into spec and status ia a common theme or event a convention in Kubernetes, though it's not technically required. Hence, it is good practice to follow this sturcture of CRDs as well.

#### Golang package structure

As we have seen, the Golang types are traditionally placed in a file called `types.go` in the package `pkg/apis/group/version`. In addition to that file, there are couple more files we want to go through now. Some of them are manually written by the developer, while some are genereated with code generators.



The `doc.go` file describes the API's purpose and includes a number of package-global `code generation tags`:

```go
// package v1alpha1 contains the xxx v1alpha1 API Group

// +k8s:deepcopy-gen=package
// +k8s:protobuf-gen=package
// +k8s:openapi-gen=true
// +k8s:prerelease-lifecycle-gen=true
// +groupName=cnat.programming-kubernetes.info
package v1alpha1
```



Next, `register.go` includes helpers to register the custom resources Golang types into a `scheme`.

```go
package version
import (
	metav1 "k8s.io/apimachinery/pkg/apis/meta/v1"
    "k8s.io/apimachinery/pkg/runtime"
    "k8s.io/apimachinery/pkg/runtime/schema"
    
    group "repo/pkg/apis/group"
)

// SchemeGroupVersion is group version used to register these objects
var SchemeGroupVersion=scheme.GroupVersion{
    Group: group.GroupName 
    Version: "version"
}

// Kind takes an unqualified kind and retruns back a Group qualified GroupKind
func Kind(kind string) scheme.GroupKind{
    return schemeGroupVersion.WithKind(kind).GroupKind
}
// Resource takes an unqualified resource and returns a Group qualified GroupResource
func Resource(resource string) scheme.GroupResource{
    return schemeGroupVersion.WithResource(resource).GroupResource
}

var (
    SchemeBuilder = runtime.NewSchemeBuilder(addKnownTypes)
    AddToScheme = SchemeBuilder.AddToScheme
)

// Adds the list of known types to Scheme
func addKnownTypes(scheme *runtime.Scheme) error{
    scheme.AddKnownTypes(
    	SchemeGroupVersion,
        &SomeKind{},
        &SomeKindList{}
    )
    metav1.AddToGroupVersion(scheme, SchemeGroupVersion)
    return nil
}
```

Then, `zz_generated.deepcopy.go`defines deep-copy methods on the custom resource Golang top-level types. In addition, all substruct become deep-copyable as well.

Because the example use the tag `+k8s:deepcopy-gen=package` in doc.go, the `deepcopy generation` is on an opt-out basis;  that is,  DeepCopy methods are genereaed for every type in the package that does not opt out with `+k8s:deepcopy-gen=false`.

#### Typed Client created via client-gen

With the API package `pkg/apis/group/version` in place, the client generator `client-gen` creates a typed client, in `pkg/generated/clientset/versioned` by default. More precisely, the generated top-level object is a client set. It subsumes a number of API groups, versions, and resources.

The top-level files looks like the following:

```go
```



### Controller-runtime Client of Operator SDK and KubeBuilder

The controller-runtime project provides the basis for the opetator solutions `Operator SDK` adn `Kubebuilder`.

It uses discovery information from the API server to map the kinds to HTTP path.



Here is a quick example of how to use controller-runtime.

```go
import (
	"flag"
    
    corev1 "k8s.io/api/core/v1"
    metav1 "k8s.io/apimachinery/pkg/apis/meta/v1"
    "k8s.io/client-go/kubernetes/scheme"
    "k8s.io/client-go/tools/clientcmd"
    
    runtimeclient "sig.k8s.io/controller-runtime/pkg/client"
)

func main(){
    kubeconfig = flag.String("kubecofig","~/.kube/config","kubeconfig file path")
    flag.Parse()
    config,err := clientcmd.BuildConfigFromFlag("",*kubeconfig)
    cl,_ := runtimeclient.New(config, clientOptions{
        Scheme: sxcheme.Scheme
    })
    podList := &corev1.PodList{}
    err:= cl.List(context.TODO(), client.InNamespace("default"), podList)
}
```



The

