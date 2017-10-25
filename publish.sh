#!/bin/bash

echo "Building project image-processing-service"

name=image-processing-service
version=1.0

if [ "$1" != "" ]; then
	name=$1
fi
if [ "$2" != "" ]; then
	version=$2
fi

powertrain push NAME=$name VERSION=$version
