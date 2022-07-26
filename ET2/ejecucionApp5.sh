#!/bin/bash

i=1
while [ $i -lt $(($1 + 1)) ]
do
    java App5.java $i
    i=$(($i+1))
done
