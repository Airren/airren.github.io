



either follow the other answer and use a fully-qualified image name or you can add docker.io to your set of unqualified search registries:

```
/etc/containers/registries.conf
unqualified-search-registries = ["docker.io"]
```

This will make it possible to use `docker.io` as a fall back with unqualified image names like:

```
podman pull nginx
```