---
title: K8s 源码阅读 5 Automating Code Generation
---













## Calling the Generators

Usually, the code generators are called in mostly the same way in every controller project.



Here, `all` means to call all `four standard code generators` for CRs.

**deepcopy-gen**

Generate `func(t *T)DeepCopy() *T` and `func(t *T)DeepCopyInfo(*T)` method.

**client-gen**

 Creates typed client sets

**informer-gen**

Creates informers for CRs that offer an event-base interface to react to changes of CRs on the server.

**lister-gen**

Creates lister for CRs that offer a read-only caching layer for GET and LIST request.

The last two are the basis for building controllers. These four code generator make up a powerful basis for building full-featured, production-ready controllers using the same mechanisms and packages that the Kubernetes upstream controllers are using.



## Controlling the Generators with Tags

While some of the code-generator behavior is controlled via command-line flags as described earlier, a lot more properties are controlled via tags in you Go files. A tag is a specially formatted Go comment in the following form.

```go
// +some-tag
// +some-other-tag=value
```

There are to kind  of tags:

- Global tags above the package line in a file called `doc.go`
- Local tags above a type declaration.





