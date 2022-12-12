



Hi, Kural, 



About the IPV6 SFC, the following is my debug process.

Now, the `func CalculateRoutes()` can genarate the create route to the pod, but can't add to the corresponding pod. 

The calculated result for the sfc-head pod:





But for the agent pod, while adding routes to the container through `func ContainerAddRoute()`  failed. Because in this function, it will get the host network configuration to find the default gateway, to add an extra route to the host network through the default gateway on the host. If the host is without an IPV6 default gateway will cause the failure.



So, I think this path will work normally with the host in an ipv6 environment.

So, shall we still work to fix it?  make it suitable for the host not to run in an IPV6 environment, Or just test this patch in an IPv6 fully support host machine.

Or, Could this work transfer to the NEX team colleagues, I can help the finish the test in IPV6 env or host to fix it and make it suitable for Dual-Stacks.



The attachment is the log of operator and agent.



ipv4 and ipv6 are not isolated in the code, somewhere use hard code to disparate that, and this could be a potential risk.
