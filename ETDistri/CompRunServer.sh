#!/bin/bash
javac rmiInterfaz/*java rmiServidor/*java
rmiregistry &
java rmiServidor/Server $1 $2 $3
killall rmiregistry 