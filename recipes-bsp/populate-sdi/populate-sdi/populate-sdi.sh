#!/bin/sh
echo "Populate /sdi filesystem ..."

# convert arguments into variables
for i in $*; do eval $i; done; unset i

tar -C /sdi --skip-old-files -xzf /opt/populate-sdi-1.0.tar.gz
rm ${root_prefix}/opt/populate-sdi-1.0.tar.gz

