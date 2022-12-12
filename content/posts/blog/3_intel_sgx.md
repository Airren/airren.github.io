---
title: Intel SGX

---



## 概述

https://www.intel.com/content/www/us/en/developer/tools/software-guard-extensions/overview.html

https://blog.quarkslab.com/overview-of-intel-sgx-part-1-sgx-internals.html

Intel Software Guard Extensions(Intel SGX) 保护选定的代码和数据不被泄露的和修改。开发者可以把应用程序划分到CPU强化的enclave中或者内存中可执行的保护区域，即使在受攻击的平台中也可以提高安全性。使用这种新的应用层可信执行环境，开发者能够启用身份和记录隐私，安全浏览和数据保护(DRM)或者任何需要安全存储机密或者保护数据的高保障安全应用场景中。

- 机密性和完整性， 即使在OS、BIOS、VMM或者SMM层存在特权恶意软件的情况下也可以保证安全。
- 低学习曲线，和父应用程序类似的OS编程模型，并且在主CPU上执行
- 远程认证 远程部分能够认证一个应用程序的enclave的身份，并且安全的将密钥、凭据和敏感数据提供为enclave
- 最小的可能攻击面， CPU边界成为攻击面外围，所有的数据、内存、外围之外的IO都是加密的。



最小攻击面的硬件辅助可信执行环境。



### intel SGX保护的应用程序

Intel SGX应用程序由两个部分组成： 不可信代码和可信Enclave. 开发者可以创建一对多的可信enclave用来支持分布式体系结构。

常用应用有密钥，专有算法，生物识别数据和CSR生成等。

程序运行时， Intel SGX指令在一个特定的保护内存区域中创建和执行enclave，该区域有由开发者定义的受限入口和出口函数。能够防止数据泄露。在CPU范围中的enclave和数据运行在一个clean的环境中， enclave数据写入到磁盘会被加密，并且校验其完整性。



上图中的流程

1. Application由可信和不可信部分构成
2. App运行和创建evclave， enclave放入到可信内存中
3. 可信函数被调用，执行会转换到enclave中
4. enclave可以访问所有进程数据，外部要访问enclave数据被禁止
5. 可信函数返回enclave数据



对enclave有未授权的访问和内存侦听是有可能的

### 认证Enclave和加密数据

当前，ODM(原始设备制造上)和ISV(独立软件提供商) 通常在制造时或通过无法以机密方式证明XXX。

Intel SGX使用enclave之间本地认证或者第三方远程认证的方式来保证应用程序没有受到破坏。



应用程序受保护的部分会加载到一个Enclave，它的代码和数据都会收到监测。会发送一个请求到远端服务器，用来验证这个Enclave是否是可靠的Intel 处理器生成的。 如果认证了Enclave的身份，远端就会信任Enclave并安全的提供密钥，凭证和数据.

Intel SGX 包括一个生成CPU和Enclave特定“密封密钥”的指令。密钥能够用来安全的存储和取回可鞥你需要保存在磁盘中的敏感信息。



### Intel SGX 实现新的安全模型

Intel SGX 是在很多公司、大学的安全研究人员以及政府安全机构的支持下创建的，上百家ISV与Intel合作，使用Intel SGX来保护关键任务应用程序。



## Set up SGX develop environment

- Install SGX driver

- Install SGX SDK

- Install SGX PSW

直接按照[官方文档](https://github.com/intel/linux-sgx#build-the-intelr-sgx-sdk-and-intelr-sgx-sdk-installer)依次安装上述3个组件



### Sample Enclave Demo

```makefile
############# SGX SDK  Setting 
# 编译平台和模式配置

############## APP Setting 
# 主要是指定
# App_Cpp_Files 需要编译的cpp文件
# App_Include_Paths 包含目录
# App_Cpp_Objects 输出

############### Enclave setting
# Enclave_Cpp_Files 
# Enclave_Include_Paths
# Enclave_Cpp_Objects

# eld 编译配置
################ App Object 
################ Enclave Object


```



### Enclave 相关开发流程

1. 使用edl文件定义不可信app和enclave之间的接口
2. 实现app和enclave函数
3. 编译 app 和enclave。编译中 Edger8r生成可信和不可信的代理/桥函数，Enclave签名工具生成enclave的metadata和签名
4. 在模拟和硬件模式下运行和调试app，详细看debug enclave的内容
5. 准备发布app 和enclave



### 编写Enclave函数									

在app角度，使用不可信代理函数调用enclave函数(ECALL)跟调用其他函数没有区别。Enclave函数只是有些显示的c/c++函数。

开发者只能使用c和c++(Native)开发enclave函数，其他不支持。

Enclave函数依赖特定版本的c/c++运行时库,STL,同步和几个其他可信库。这些依赖库时Intel SGX SDK的一部分。

开发者也可以使用其他可信的库，但必须保证这些库遵循内置Enclave函数的规则：

1. Enclave函数不能使用所有可用的32位或64位指令。查看Intel Software Guard Extensions Programming Reference 文档中Enclave合法指令列表。
2. Enclave函数只运行在用户态。使用需要cpu特权的指令会造成enclave出错。
3. 如果被调用函数静态链入了Enclave，那么是可以在Enclave中调用这个函数(函数需要在enclave image中)。linux的共享对象(shared object)不支持。

> 在编译时，如果Enclave Image还有找不到的依赖，那么Enclave签名会失败。

在Enclave中调用外面的函数叫做OCALLs.



Intel SGX 规则和限制

| Feature                            | Supported | Description                                                  |
| ---------------------------------- | --------- | ------------------------------------------------------------ |
| 语言                               | 部分      | Native C/C++。 Enclave 接口函数只能使用c                     |
| C/C++调用其他共享对象              | 不支持    | 可以用OCALL来调用外部函数                                    |
| C/C++调用系统提供的C/C++/STL标准库 | 不支持    | Intel SGX SDK中提供了替代的可信版本                          |
| 系统API调用                        | 不支持    | 使用OCALL                                                    |
| C++框架                            | 不支持    | 包括MFC，QT，Boost*                                          |
| 调用C++类函数                      | 支持      | 包括C++类，静态和inline函数                                  |
| 内置函数                           | 部分      | 如果这些内置函数使用的是支持的指令。Intel SGX SDK提供的函数运行。 |
| 内联汇编                           | 部分      | 和内置函数一样                                               |
| 模板函数                           | 部分      | 只支持Enclave的内置函数                                      |
| Ellipse()                          | 部分      | 只支持Enclave的内置函数                                      |
| Varargs(va_list)                   | 部分      | 只支持Enclave的内置函数                                      |
| 同步                               | 部分      | Intel SGX SDK提供了同步的函数/对象集合，spin-lock，mutex和condition variable |
| 线程支持                           | 部分      | 不支持在Enclave内创建线程。Enclave内运行的线程是不可信的app创建的。spin-lock、mutex和条件变量API可以在Enclave内用于线程同步。 |
| TLS                                | 部分      | Only implicitly via__thread                                  |
| 动态内存分配                       | 支持      | Enclave内存是有限的资源，在Enclave创建时，制定了最大Heap的大小 |
| C++异常                            | 支持      | 虽然有些影响性能                                             |
| SEH异常                            | 不支持    | Intel SGX SDK 提供了API用于注册函数或者异常句柄来处理部分硬件异常(详细可见 Customer Exception Handling) |
| 信号                               | 不支持    | Enclave内不支持signal                                        |



### 调用Enclave内函数

Enclave加载成功之后，可以获得一个Enclave的ID,在使用ECALL时作为参数使用。有时候，可以在ECALL内部使用OCALL。比如需要在Enclave中计算某些秘密信息，edl文件如下

```edl
//demo.edl
enclave{
	// Add your definition of "secret_t" here
	trusted{
		public void get_secret([out] secret_t* secret);
	};
	untrusted{
		// This OCALL if for illustration purposed only,
		// It should not be used in a real enclave,
		// unless it is during the development phare for debuging purposes.
		void dump_secret((in) const secret_t* secret);
	};
}
```

sgx_edger8r 使用上面的edl会生成不可信的代理函数，用于ECALL和OCALL的可信代理函数

```c++
// untursted agent function (for app)
sgx_status_t get_secret(sgx_enclave_id_t eid, secret_t* secret);
// trusted agent function ()
sgx_status_t dump_secret(const secret_t* secret);
```

使用不可信的代理函数调用时，会自动调入Enclave，将参数传给真正可信的get_secret()。 在APP中使用一个ECALL

```c++
// An enclave call(ECALL) will happen here
secret_t secret;
sgx_status_t status = get_secret(eid, &secret);
```

Enclave 中的可信函数可以使用OCALL调用可信代理函数dump_secret 来dump秘密信息。它会自动jiang-参数传给Enclave外真正的dump_secret 函数。Enclave外的这个函数需要开发者实现链入APP中。

可信和不可信函数都会返回一个 sgx_status_t 类型的返回值。如果代理函数成功了，会返回SGX_SUCCESS，否则会返回指定的错误值。



### 调用Enclave外函数

有些时候，Enclave内代码需要调用外部不可信内存中的函数来使用操作系统的功能，比如系统调用，I/O 操作等等。这种调用就叫做OCALL。

这些函数需要在EDL文件的untrusted 中声明。

Enclave的加载非常类似与系统加载dll。app的函数地址空间共享给了Enclave，所有Enclave可以直接调用创建该Enclave的app的函数. ??

不使用OCALL，直接调用app的函数会在运行时发生异常。

> 包装函数(代理函数)会从保护内存(Enclave)中拷贝参数到未保护内存中，因为外部函数不能直接访问保护内存区域。实际上就是 OCALL删除被copy到不可信的堆栈中，根据OCALL的参数个数，有可能会引起栈溢出。



OCALL函数必须遵循一下限制

1. OCALL函数必须是C函数，或者C链接的C++函数。
2. 引用Enclave内数据的指针必须在EDL文件中注释为pointer direction属性，包装函数会对这些指针做浅拷贝(一层)。
3. Enclave内不会捕捉异常，需要开发者在不可信包装函数中处理这些异常。
4. OCALL函数原型中不能有ellipse(...)或va_list

举个栗子：

```c++
// Step 1 - Add a declaration for foo in the EDL file
// foo.edl
enclave{
    untrusted{
        [cdecl] void foo(int param);
    }
}
// Step 2 (optional but highly recommended ) - write a trusted, user-friendly wrapper. This function is part of the enclave's trusted code.
// The wrapper function ocall_foo function will look like:
// Enclave's trusted code
#include "foo_t.h"
void ocall_foo(int parm){
    // it is necessary to check the return value of foo()
    if (foo(param) != SGX_SUCCESS){
        abort();
    }
}

// Setp 3 - write an untrusted foo function
// untrusted code
void foo(int param){
    // the implementation of foo
}
```



sgx_edger8r 将会生成调用不可信函数foo的不可信桥函数，这两个函数都是app的部分，不是在Enclave中。



### 在Enclave 中链接库

#### 动态链接库

Enclave不可以依赖一个动态链接库。Enclave加载器特意设计成禁止Enclave中使用动态链接库。Enclave的保护依赖于加载时，计算机的Enclave中所有代码和数据的准确测量值，因此，动态链接库链入会增加复杂度。

> Enclave 文件如果有任何未识别的依赖，Enclave的签名过程都会失败。所以Enclave必须有一个空的IAT。

#### 静态库

只有静态库，没有任何依赖你就可以链接它。

Intel SGX SDK提供下面这些可信的库

| Name                          | Description                                                  | State                  |
| ----------------------------- | ------------------------------------------------------------ | ---------------------- |
| sgx_trts.lib                  | Intel SGX 内部组件                                           | 在HW模式下运行必须链接 |
| sgx_trts_sim.lib              | Intel SGX 内部组件(仿真模式)                                 | 仿真模式下必须链接     |
| sgx_tstdc.lib                 | 标准c库(math string 等)                                      | 必须链接               |
| sgx_tcxx.lib, sgx_tstdcxx.lib | 标准C++库,STL                                                | 可选                   |
| sgx_tsrevice.lib              | 数据封装/解封(加密), 可信架构Enclave支持，Elliptic  Curve Diffie-Hellman(EC HD)库等 | HW模式下必须链接       |
| sgx_tsrevice_sim.lib          | sgx_tsrevice.lib 对应的仿真模式库                            | 仿真模式下必须链接     |
| sgx_tcrypto.lib               | 加密算法库                                                   | 必须链接               |
| sgx_tkey_exchange.lib         | 可信密钥交换库                                               | 可选                   |
| sgx_tprotected_fd.lib         | Intel 保护文件系统库                                         | 可选                   |

#### 仿真库

Intel SGX SDK提供仿真库让软件运行在仿真模式下(不许要Intel SGX硬件支持)。有一个可信仿真库和一个不可信仿真库。不可信仿真库提供不可信运行时库来管理连接了可信仿真库的Enclave功能。包括在Enclave外仿真执行Intel SGX指令：ECREATE，EADD，EEXTENED, EINT, EREMOVE 和EENTER。可信仿真库住哟啊负责Enclave内使用的Intel SGX指令的仿真：EEXIT, EGETKEY, EREPORT.

> 仿真模式不需要CPU支持Intel SGX。然而处理器至少支持SSE 4.1 指令。



#### 加载不可信SGXDLL

SGX DDL安装在系统目录中，附带PSW(sgx_urts.dll 和sgx_ae_service.dll)。你必须锁定SGX app的安装目录，否则就要显式的加载这两个dll(全路径)。



假设攻击者获得app安装目录的控制权，插入了一个恶意的sgx dll到这个目录，如果app隐式的加载sgx dll，这个恶意的dll 会优先于系统目录原始sgx dll被加载。(DLL 劫持)

为了确保SGX app从系统目录中加载SGX dll， app必须显式架子加载这两个dll，顺序是

- sgx_uae_servcie.dll

- sgx_urts.sll



### Encalve 配置文件

Enclave配置文件是一个XML文件，包含了用户定义的Enclave参数。XML文件是Enclave工程的一部分，sgx_sign会用这个配置文件作为输入来创建Enclave的签名和元数据。

下表为配置文件各个参数的定义，所有的参数都是可选的，如果没有定义就会使用默认值。

| Tag          | Description                                                  | Default       |
| ------------ | ------------------------------------------------------------ | ------------- |
| ProdID       | ISV assigned Product ID                                      | 0             |
| ISVSVN       | ISV assigned SVN                                             | 0             |
| TCSNum       | The number of TCS. Muster greater than 0                     | 1             |
| TCSPolicy    | TCS management policy. 0 - TCS is bound to the untrusted thread. 1 - TCS is not bound to the untrusted thread. | 1             |
| StackMaxSize | The maximum stack size per thread. Must be 4k aligned.       | 0x40000  256k |
| HeapMaxSize  | The maximun heap size for the  process. Must be 4k aligned.  | 0x100000 1MB  |
| DisableDebug | Enclave can not be debug . 0 - Enclabe can be debug          | 0             |
| MiscSelect   | The desired Misc feature                                     | 0             |
| MiscMask     | The mask bit for the Misc Feature                            | 0xFFFFFFFF    |



MiscSelect 和MiscMask 是为了未来功能扩展使用的。目前MiscSelect必须是0，否则Enclave不会加载成功。

为了不浪费重要的保护内存资源，你可以使用sgx_emmt 测量工具适当的设置StackMaxSize 和HeapMaxSize。 可以看看Enclave Memory Measurement工具的详细说明。

如果enclave中没有足够的Stack空间，ECALL会返回SGX_ERROR_STACK_OVERRUN的错误代码。提示需要分配更多的StackMaxSize。

### Enclave Project Configuration

**仿真**

仿真模式下工作方式和调试模式一样，仿真模式不需要有硬件支持。使用Intel SGX 指令来进行软件模拟。单步签名是对仿真Enclave签名的默认方式

**调试**

如果设置了Debug选项，Enclave编译为调式模式，编译出来的Enclave包含有调试信息和符号信息。 通过SGX_DEBUG_FLAG传递给sgx_create_enclave。 允许Enclave加载到 Enclave 调试模式。单步签名是这个工程的默认签名方式。用于这种模式的签名密钥不需要使用白名单中的值。

**预发行版**

编译器会使用最优化编译Enclave为发行模式。在这种模式下Enclave加载到Enclave调试模式。这种模式下Project会定义预处理器标记EDEBUG在预处理器配置中。当EDEBUG预处理器标记定义了，它会Enable SGX_DEBUG_FLAG, 会加载Enclave到Enclave调试模式。这种模式下也是单步签名作为签名方式。签名密钥不需要再白名单中。

**发行版**

加载Enclave到Enclave发行模式下。会禁用SGX_DEBUG_FLAG标记。只有再NDEBUG未定义或者EDEBUG定义了之后SGX_DEBUG_FLAG才可用。调试模式下NDEBUG没有定义，所以SGX_DEBUG_FLAG可用。发行模式下配置中NDEBUG已定义，SGX_DEBUG_FLAG不可用，所以加载Enclave到Enclave发行模式。Two-Step签名是这种模式下的默认签名方式，Enclave需要使用白名单中的签名密钥。

### 加载/卸载Enclave

Enclave源码会编译为动态连接库。使用Enclave，需要调用 sgx_create_enclave()来加载enclave.dll 到保护内存中。enclave.dll 必须使用sgx_sign.exe 来签名。当第一次加载Enclave时，加载会获取以个launch token， 然后保存用于后续in/out参数。开发者可以使用launch token到文件中，然后第二次加载enclave时，app可以从token保存的文件中获取token。准备一个有效的launch token来提高加载性能。开发者必须使用sgx_destory_enclave()并传递sgx_enclave_id_t作为参数来卸载Enclave。



下面时加载和卸载Enclave的示例代码

```c++
#include <stdio.h>
#include <tchar.h>
#include "sgx_urts.h"
#define ENCLAVE_FILE_T("Enclave.signed.dll")
int main(int argc,char* argv[]){
    sgx_enclave_id_t eid;
    sgx_status_t ret = SGX_SUCCESS;
    sgx_launch_token_t token = {0};
    int updated = 0;
    
    // Create the Enclave with above launch token.
    ret = sgx_create_enclave(ENCLAVE_FILE, SGX_DEBUG_FLAG, &token,&updated，&eid, NULL);
    if (ret != SGX_SUCCESS){
        printf("App: error %#x, failed to create enclave.\n",ret);
        return -1;
    }
    // A bunch of Enclave calls(ECALL) will happen here.
    // Destory the encalve when all Encalve calls finished
    if (SGX_SUCCESS != sgx_destory_enclave(eid))
        return -1;
    return 0;
}
```



处理电源事件

保护内存加密密钥保存在支持SGX的cpu中，每次电源事件(suspend 和休眠)会销毁。因此当电源发生改变时，Enclave内存会被移除，所有enclave数据将不可访问。所以，当系统恢复了之后，任何后续的ECALL都会返回错误SGX_ERROR_ENCLAVE_LOST. 这个错误表示SGX enclave在断电时丢失了。







## SGX secure Enclaves

#### Software Guard Extensions: Non-hierarchical Trust Model	

Secure Enclave:

- Application can instantiate CPU-based trusted execution environment.
- Shielded from all other running software: confidentiality and integrity.
- Direct access to enclave memory is disallowed even form OS/Hypervisor since they are untrusted.
- Reduced Trusted Computing Base

Enclave Identity

- Fully measured (hash) at creation time
- Measurement establishes enclave idgentity
- Used for attestation: allows a remote entity to establish trust on an enclave.



Enclave Page Cache (EPC)

- Stores running enclaves
- Reserved by firmer at boot time
- Treated specially by CPU
  - Memory traffic goes through encryption engine
  - Not accessible when  CPU not in enclave mode
  - An enclave can only assess its own EPC pages
- Managed by OS/VMM
  - Allocation, mapping, eviction





## K8s SGX Device Plugin

This video demonstrates the Intel(R) Software Guard Extensions ECDSA Quote Generation in Kuberntes

The key building blocks are:

- Intel(R) Software Guard Extensions (SGX) Flexible Launch Control capable system(registered)
- Intel(R) SGX driver(RFC v41) for the host kernel
- Intel(R) SGX PCKID Certificate Caching Service configured

Let's get started!

1. Check the Kubernetes Cluster is in good shape

   ```sh
   kubectl get nodes
   # Check all pods status
   # certmanager x 3
   # coredns x 2
   # weave
   kubectl get pods -A
   # create the demo namespace
   kubectl create ns sgx-ecdsa-quote
   
   # Pull: devel images and tag them as 0.23.0
   sudo ctr -n k8s.io i pull docker.io/intel/intel-sgx-plugin:devel
   sudo ctl -n k8s.io i pull docker.io/intel/intel-sgx-initcontainer:devel
   ```

2. Deploy node-feature-discovery for Kubernetes

   It's used to label SGX capable nodes and register SGX EPC as an extened resource

   ```sh
   kubectl apply -k intel-device-plugins-for-kubernetes/deployments/sgx_nfd
   
   # Check its pod is running
   kubectl wait --for=condition=Ready pod/nfd-worker-vcm4z -n node-feature-discovery
   ```

3. Deploy Intel Device Plugin Operator as a DaemonSet

   
   
   ```sh
   kubectl apply -k intel-device-plugins-for-kubernetes/deployments/sgx_plugin/overlays/epc-nfd/
   ```
   
4. Verify node resource

   ```sh
   kubectl describe node <node name> | grep sgx.intel.com
   ```

   

   Both node labels and resources for SGX are in place

5. Run Intel(R) SGX DCAP ECDSA Quote Generation(out-of-proc)

   ```sh
   #  Make the pre-built images available (from docker save)
   sudo ctr -n k8s.io i import sgx-aesmd.tar
   sudo ctr -n k8s.io i import sgx-demo.tar
   # Deploy Intel(R) AESMD
   kubectl apply -k intel-device-plugins-for-kubernetes/deployments/sgx_aesmd/
   # Deploy Intel(R) SGX DCAP ECDSA Quote Genetation
    k apply -k intel-device-plugins-for-kubernetes/deployments/sgx_enclave_apps/overlays/sgx_ecdsa_aesmd_quote/
   
   kubectl logs ecdsa-quote-intelsgx-demo-job-npwvf -n sgx-ecdsa quote
   
   
   # Intel(R) SGX DCAP QuoteGenerationSample successfully requested a quote from Intel(R) AESMD
   
   # Delete the deployment
    k delete -k intel-device-plugins-for-kubernetes/deployments/sgx_enclave_apps/overlays/sgx_ecdsa_aesmd_quote/
   
   ```

6. Run Intel(R) SGX DCAP ECDSA Quote Generation(in-proc)

   ```sh
   # Deploy Intel(R) SGX DACP ECDSA Quote Generation
   kubectl apply -k intel-device-plugins-for-kubernetes/deployments/sgx_enclave_apps/overlays/sgx_ecdsa_aesmd_quote
   
   kubectl logs inproc-ecdsa-quote-intelsgx-demo-job
   
   # Intel(R) SGX DCAP QuoteGenerationsSmaple successfully generated a quote using DCAP Quote Provider Library.
   ```

   

7. This  video demonstrated the Intel(R) Sofrware Guard Extensions in Kubernetes

   The following topics were covered:

   - SGX Kubernetes Device Plugin deployment with an Operator
   - Intel(R) SGX node resource and featured label registration to Kubernetes*
   - Intel(R) SGX DCAP ECDSA  Quote Generation





 I encountered a problem with the hardware when trying to enable the SGX device plugin demo. This feature needs the CPU support FLC, but mine doesn’t support that. If anyone has a supported NUC, can you exchange it with mine **NUC11TVHv7 32G**

Check Method : https://www.intel.com/content/www/us/en/support/articles/000057420/software/intel-security-products.html

> On a Linux* system, execute cpuid in a terminal
>
> Open a terminal and run: $ cpuid | grep -i sgx
> Look for output: SGX_LC: **SGX launch config supported = true**
>
> 













Device Plugin Pod

```sh
apiVersion: v1
kind: Pod
metadata:
  name: ubuntu-sgx-demo
spec:
  containers:
    -
      name: ubuntu-sgx
      image: airren/ubuntu-sgx:v0.0.1
      imagePullPolicy: IfNotPresent
      workingDir: "/"
      command: ["sleep","3600"]
      resources:
        limits:
          sgx.intel.com/epc: "512Ki"
```





### Deploy SGX Plugin Quickly



```sh
 k apply -k deployments/nfd/overlays/sgx
 k apply -k deployments/nfd/overlays/node-feature-rules
 k apply -k deployments/sgx_plugin/overlays/epc-nfd
```













server mesh controller

clusternet. use the latested version image



service discovery , A   Bhow to find the the server in a

































参考资料

https://zhuanlan.zhihu.com/p/39976702

https://zhuanlan.zhihu.com/p/39912478



protobuf



800-810-1972 Toll-free

400-810-1972 Local Toll (For Mobile)

Monday to Friday 08:00 - 18:00 GMT +8



