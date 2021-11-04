#!/bin/sh
echo "Running firstboot tasks ..."

# convert arguments into variables
for i in $*; do eval $i; done; unset i

echo "root_prefix = ${root_prefix}"

