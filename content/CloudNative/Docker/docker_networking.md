---
title: Docker networking

---





Docker networking is based on an open-source pluggable architecture called the Container Network Model(CNM). libnetwork is Docker's real-work implementation of the CNM, adn it provides all of the Docker's core networking capabilities. Drivers plug into libnework to provide specifice network topologies.







## The theory

At the highest level, Docker networking comprise three major components.



### Single-host bridge network

