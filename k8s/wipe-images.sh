#! /bin/bash

K8S_NODES=(k3s-30 k3s-31)
SVCS=(hwk-server hwk-client)

# Clear the services images from the nodes because k3s or containerd
# is ignoring "imagePullPolicy: Always"
for n in ${K8S_NODES[@]};
do
  for s in ${SVCS[@]};
  do
    multipass exec ${n} -- \
    bash -c "sudo k3s ctr images rm registry:5000/${s}:v1"
  done
done
