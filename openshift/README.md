This directory contains files which can be used to generate a deployment of CBR in an empty OpenShift project. The command used to generate the initial (un-parameterized) version of the main CBR template is the following:

	oc export is,bc,dc,svc,cm,secret,pvc -l promotion-group=cbr -o yaml --as-template="cbr" > sdp-cbr-project-template.yaml

In order to replicate the resource quotas/constraints present across environments, the following commands were used to generate the Quota and LimitRange files:

	oc export quota/sdpcbr-quota -o yaml > sdpcbr-quota.yaml

	oc export LimitRange/sdpcbr-limits -o yaml > sdpcbr-limits.yaml
