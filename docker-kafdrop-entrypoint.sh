#!/bin/bash
cd /
./wait-for-it.sh --timeout=0 broker:9092 && ./kafdrop.sh