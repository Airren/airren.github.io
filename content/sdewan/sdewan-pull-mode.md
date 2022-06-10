---
title: SDEWAN Setup
---



SDEWAN setup to pull mode





on the overlay node doesn't need set up monitor



1. Setup 4 vms

   ```sh
   ./0_auto_setup.sh
   ```

2. Overlay Preregister

   ```sh
   ewoctl --config ./ewo-config.yaml apply -f pre.yaml
   ewoctl --config ./ewo-config.yaml apply -f cluster-sync-object.yaml
   ```

3. Hub/Edge-1/Edge-2

   - Install flux components

   ```sh
   
   
   
   # need patch the proxy 
   
   
   ```

   - deploy monitor

       ```sh
       
       
       ```

4.  Register edge-1/edge-2

5. Register connection edge-1-hub and Edge2-Hub

   