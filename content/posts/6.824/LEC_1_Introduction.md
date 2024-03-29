---
title: 「6.824」 Lecture 1 Introduction
---



## Distributed System Engineering



- parallelism

- Fault tolerance

- Physical

- Security /isolated



Challenges:

- concurrency

- Partial failure

- Performance



Lab 

1. MapReduce
2. Raft for fault tolerrance
3. K/V server
4. Sharding K/V servers



Infrastructure

- storage
- Communication
- Computation 

Abstraction

Implementation

- RPC, Thread，Concurrency

Performance

- scalability , scabale to speed 2 x computer = 2X throughput

Fault Tolerance

- Availability 
- Recoverability
- NV (non-volatile)storage /Replication

Topic - Consistency

- Put (key, value)
- Get(key) -> value



## Map Reduce

##### What is distributed system?

- multiple cooperating computers
- storage for big web sites, MapReduce, peer-to-peer sharing, &c
- lots of critical infrastructure is distributed

##### Why do people build distributed system?

- to increase capacity via parallelism
- to tolerate faults via replication
- to place computing physically close to external entities
- to achieve security via isolation

But:

- many concurrent parts, complex interactions
- must cope with partial failure
- tricky to realize performance potential



Why take this course ?

- interesting -- hard problem, powerful solutions
- used by real system -- driven by rise of big web sites.
- active research area -- important unsolved problems
- hands-on -- you'll build real system in the labs



## Lab

https://pdos.csail.mit.edu/6.824/labs/lab-mr.html



















































Reference:

https://chunlife.top/2020/04/18/The-Google-File-System%E4%B8%AD%E6%96%87%E7%89%88/