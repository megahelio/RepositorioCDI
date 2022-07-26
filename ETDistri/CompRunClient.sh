#!/bin/bash
javac rmiInterfaz/*java rmiCliente/*java
java rmiCliente/Client $1 $2 $3