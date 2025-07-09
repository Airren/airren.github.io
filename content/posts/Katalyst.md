



In this part, the method based on the Kubelet API polling is referred to as ***Bypass*** Mode, while another method based on NRI is referred to as ***NRI*** Mode.



### Addon

- The ORM support two operational modes: Bypass or NRI. Only one mode can be active at any given time. When creating a new ORM Manger, the current operational mode can be determined by reading the configuration, and it does not support changing the mode during runtime.

  ```go
  const AGENT_MODE_NRI = "nri"
  const AGENT_NODE_BYPASS = "bypass"
  
  type ManagerImpl struct {
  	ctx context.Context
    ....
    // ORM run mode: bypass or nri. 
    // Bypass mode is triggered by polling kubelet api to get the pod event.
    // NRI mode is required containerd version >= 1.7.0 and NRI enabled.
  	mode string
    ....
  }
  
  
  func NewManger(... config *config.Configuration){
  
  	if config.GenericConfiguration.NRIEnable && ValidateNRIStatus(){
  		m.mode=AGENT_MODE_NRI
  	}else{
  		m.mode=AGENT_NODE_BYPASS
  	}
  }
  
  func ValidateNRIStatus() bool{
  // TODO detect NRI status
  }
  ```

  

- The ORM ManagerImpl functions as an NRI stub, implementing processing logic within the corresponding hook event functions.

```go
import "github.com/containerd/nri/pkg/stub"

type ManagerImpl struct {
	ctx context.Context
  ....
  // nriStub is the implementtion of NRI events handlers
	nriStub stub.Stub
  // nriMask stores the specific events that need to be hooked
  nriMask stub.EventMask
  ....
}
```



- In enhancing the ORM implementation, three hook functions are required: `RunPodSandbox()`, `CreateContainer()`, and `RemovePodSandbox()`.

​	Step 1, during `RunPodSanbox()`, the `Admit()` function is triggered. If `Admit()` succeeds, resources are allocated for the container, and the pod creation process continues. If `Admit()` fails, pod creation also fails.

```go
func (m *MangerImpl) RunPodSandbox(podSandbox *api.PodSandbox) error {
	// get podspec from podSandbox -> MetaServer?
  // Admit 
	return m.topologyManager.Admit(pod)
}
```



​	Step 2, after a successful `Admit()`, the process proceeds to the `CreateContainer()` event. At this point, resources have been allocated for the container by `Admit()`. The corresponding resources are updated in the container's spec and returned.

```go
func (m *MangerImpl) CreateContainer(pod *api.PodSandbox, container *api.Container) (*api.ContainerAdjustment, []*api.ContainerUpdate, error) {
  // Update Container Spec from the podResoures
  adjust, err:= m.updateContainer(pod, container)
	return adjust, nil, err
}
```

​	Step 3, During `RemovePodSandbox()`, all resource allocations related to the pod are returned.

```go
func (p *plugin) RemovePodSandbox(pod *api.PodSandbox) error {
	//
	err := m.returnPodResource(pod)
	return err
}
```





### Modification

- If using the NRI Mode, after the allocation of resources is completed in the `Admit()` , the `Allocate()` does not need to execute `syncContainer()`; it should simply return after the resources have been allocated.

```go
func (m *ManagerImpl) Allocate(pod *v1.Pod, container *v1.Container) error {
  ....
  err := m.addContainer(pod, container)
  // return after resource allocate when run in NRIMode
	if err != nil || m.mode == AGENT_MODE_NRI{
		return err
	}
	err = m.syncContainer(pod, container)
	return err
}
```

- In NRI Mode, the executer in `syncContainer()` can be implemented through NRI's `updateContainer()` .

  ```go
  func NewExecutor(cgroupManager cgroupmgr.Manager) Executor {
  
    if m.mode == AGENT_MODE_NRI {
        // executor by NRI's updateContainer()
  			return NewNRIExecutor()
    }else{
        // executor by cgroupManger
  			return &Impl{cgroupManager: cgroupManager}					
  	}
  }
  ```



- The `metaServer` as a member variable of the ORM `ManagerImpl` because it is used in both Bypass and NRI modes.
- TBD: During NRI mode, halt the MetaManager's Reconcile.



### Opens

- Handling failure for Admit(): 
  -  the Pod will enter a retry loop while `RunPodSandbox()` returns with an error(how to fix: integrated with scheduling ?)
  - How to handle `Admit()` failure in Bypass mode. (How to evict the Pod which does not admit?)

- Timeout: 
  - While timeout, in `OnClose()`  invoke `stub.Restart`. 
  - Do `Admit()` with a timeout (configured) context .

- The Containerd version is requited ≥ 1.7.0 and NRI feature enabled.









- `ORMMangerImpl` work as a NRI stub
- Allow users to enable/disable NRI through configuration. Only one mode, either NRI or Kuberlet listener mode, can be active at a time.
- When using NRI mode, use NRI `updateContainer()` as the executor
- When the NRI Plugin crashes, switch back to Kuberlet listener mode
- Admit() is triggered when `RunPodSandbox()`, if False is returned, Pod creation fails.(How to solve the problem of restarting all the time)
- `CreateContainer()` returns the adjusted Container spec based on the Admit information.
- Clear the allocation information of typology when `RemovePodSandbox()`