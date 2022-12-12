 

 **Leverage TEE to enhance the integrity and security of the SASE network** 

Software-Define wide area network is one of the most essential components of the SASE architecture, which is based on network technologies that create virtualized WAN connections. SD-WAN decouples the network services from the underlying networks and allows the application traffic to be carried independently of the underlying network hardware, enabling the client's connection anywhere to the applications. How to build up a strong shield for the overlay network of SD-WAN has become a hot issue in the industry. Most virtual private network (VPN) technology needs a certificate for authentication and cryptographic operations. The traditional hardware security module (HSM) is the best plugin for most VPN services to provide cryptographic functions., However, in the SASE scenario, most applications need the capability of scaling and dynamic migration capability, but the physical HSM is hard to meet. 

 

Our solution utilizes the Trusted Execution Environment (TEE) technology (e.g., Intel SGX) embedded in generic servers to build and cloud-native HSM service to provide extra defense against the attack by protecting the keys used during a handshake. We can achieve flexibility, performance, security, and cloud-scale network service by integrating inside the SD-WAN solution. In this presentation, we will demonstrate how we leverage the power of hardware TEE to protect the overlay network establishment and transportation. 