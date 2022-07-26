#!/bin/bash

i=1
while [ $i -lt $(($1 + 1)) ]
do
    java Escenario4.java $i 200
    i=$(($i+1))
done

