#!/bin/bash
#title		: deploy_cbr.sh
#description	: Deploys a new CBR OpenShift project
#author		: Matt Bajzek (mkbajzek)
#date		: 20180628
#usage		: bash deploy_cbr.sh
#
# Deploy a new OpenShift project for SDP Content Based Routing (based on user input)
#
# Note: this version of this script is essentially a placeholder. A more complete
#       version (which will use the OpenShift CLI commands to sign new certificates
#       with the server's certificate) is currently being tested.
#====================================================================================

SERVICE_ACCOUNT_NAME="amq-service-account"
BROKER_SECRET_NAME="amq-secret-broker"
FOODNET_CLIENT_SECRET_NAME="amq-secret-foodnet-client"
PHINMS_CLIENT_SECRET_NAME="amq-secret-phinms-client"
DEV_TEMPLATE_FILE="sdp-cbr-project-template.yaml"
PROMOTION_TEMPLATE_FILE="sdp-cbr-project-promotion-template.yaml"

DIRECTORY_NAME="ssl"

BROKER_KEYSTORE_FILE="$DIRECTORY_NAME/broker.ks"
FOODNET_CLIENT_KEYSTORE_FILE="$DIRECTORY_NAME/foodnet_client.ks"
PHINMS_CLIENT_KEYSTORE_FILE="$DIRECTORY_NAME/phinms_client.ks"

BROKER_CERT_FILE="$DIRECTORY_NAME/broker_cert"
FOODNET_CLIENT_CERT_FILE="$DIRECTORY_NAME/foodnet_client_cert"
PHINMS_CLIENT_CERT_FILE="$DIRECTORY_NAME/phinms_client_cert"

BROKER_TRUSTSTORE_FILE="$DIRECTORY_NAME/broker.ts"
CLIENT_TRUSTSTORE_FILE="$DIRECTORY_NAME/client.ts"

echo "Logging into the cluster..."
oc login

echo ""
read -p "Project Name: " projectname

oc project "$projectname"

if [ $? -ne 0 ]
then
	exit 1
fi

echo ""
echo "Making directory for certificate and keystore files..."
mkdir -p $DIRECTORY_NAME

echo ""
echo "Creating service account..."
oc create serviceaccount $SERVICE_ACCOUNT_NAME
oc policy add-role-to-user view system:serviceaccount:$projectname:$SERVICE_ACCOUNT_NAME
echo ""

if [ ! -f $BROKER_KEYSTORE_FILE ]; then
	echo "Generating broker keystore (you will create a broker keystore password and need to enter it twice)..."
	keytool -genkey -alias broker -keyalg RSA -keystore $BROKER_KEYSTORE_FILE
else
	echo "Broker keystore found ($BROKER_KEYSTORE_FILE). Skipping..."
	echo ""
fi

if [ ! -f $BROKER_CERT_FILE ]; then
	echo "Generating broker certificate (you will need to enter the broker keystore password)..."
	keytool -export -alias broker -keystore $BROKER_KEYSTORE_FILE -file $BROKER_CERT_FILE
	echo ""
else
	echo "Broker certificate found ($BROKER_CERT_FILE). Skipping..."
	echo ""
fi

if [ ! -f $FOODNET_CLIENT_KEYSTORE_FILE ]; then
	echo "Generating foodnet client keystore (you will create a foodnet client keystore password and need to enter it twice)..."
	keytool -genkey -alias client -keyalg RSA -keystore $FOODNET_CLIENT_KEYSTORE_FILE
else
	echo "Foodnet client keystore found ($FOODNET_CLIENT_KEYSTORE_FILE). Skipping..."
	echo ""
fi

if [ ! -f $FOODNET_CLIENT_CERT_FILE ]; then
	echo "Generating foodnet client certificate (you will need to enter the foodnet client keystore password)..."
	keytool -export -alias client -keystore $FOODNET_CLIENT_KEYSTORE_FILE -file $FOODNET_CLIENT_CERT_FILE
	echo ""
else
	echo "Foodnet client certificate found ($FOODNET_CLIENT_CERT_FILE). Skipping..."
	echo ""
fi

if [ ! -f $PHINMS_CLIENT_KEYSTORE_FILE ]; then
	echo "Generating phinms client keystore (you will create a phinms client keystore password and need to enter it twice)..."
	keytool -genkey -alias client -keyalg RSA -keystore $PHINMS_CLIENT_KEYSTORE_FILE
else
	echo "Phinms client keystore found ($PHINMS_CLIENT_KEYSTORE_FILE). Skipping..."
	echo ""
fi

if [ ! -f $PHINMS_CLIENT_CERT_FILE ]; then
	echo "Generating phinms client certificate (you will need to enter the phinms client keystore password)..."
	keytool -export -alias client -keystore $PHINMS_CLIENT_KEYSTORE_FILE -file $PHINMS_CLIENT_CERT_FILE
else
	echo "Phinms client certificate found ($PHINMS_CLIENT_CERT_FILE). Skipping..."
	echo ""
fi

if [ ! -f $BROKER_TRUSTSTORE_FILE ]; then
	echo "Creating broker truststore from client certificates (you will create a broker truststore password and need to enter it several times)..."
	keytool -import -alias foodnet_client -keystore $BROKER_TRUSTSTORE_FILE -file $FOODNET_CLIENT_CERT_FILE
	echo ""
	keytool -import -alias phinms_client -keystore $BROKER_TRUSTSTORE_FILE -file $PHINMS_CLIENT_CERT_FILE
	echo ""
else
	echo "Broker truststore found ($BROKER_TRUSTSTORE_FILE). Skipping..."
	echo ""
fi

if [ ! -f $CLIENT_TRUSTSTORE_FILE ]; then
	echo "Creating client truststore from broker certificate (you will create a client truststore password and need to enter it several times)..."
	keytool -import -alias broker -keystore $CLIENT_TRUSTSTORE_FILE -file $BROKER_CERT_FILE
	echo ""
else
	echo "Client truststore found ($CLIENT_TRUSTSTORE_FILE). Skipping..."
	echo ""
fi

echo "Generating OpenShift secrets..."
oc secrets new $BROKER_SECRET_NAME $BROKER_KEYSTORE_FILE $BROKER_TRUSTSTORE_FILE
oc secrets add sa/$SERVICE_ACCOUNT_NAME secret/$BROKER_SECRET_NAME
oc secrets new $FOODNET_CLIENT_SECRET_NAME $FOODNET_CLIENT_KEYSTORE_FILE $CLIENT_TRUSTSTORE_FILE
oc secrets add sa/$SERVICE_ACCOUNT_NAME secret/$FOODNET_CLIENT_SECRET_NAME
oc secrets new $PHINMS_CLIENT_SECRET_NAME $PHINMS_CLIENT_KEYSTORE_FILE $CLIENT_TRUSTSTORE_FILE
oc secrets add sa/$SERVICE_ACCOUNT_NAME secret/$PHINMS_CLIENT_SECRET_NAME

template_file="$DEV_TEMPLATE_FILE"
while true;
do
	echo ""
	read -p "Are you deploying this in a development cluster (y/n)? " answer
	case $answer in
		[Yy]* ) template_file="$DEV_TEMPLATE_FILE"
			break
			;;
		[Nn]* ) template_file="$PROMOTION_TEMPLATE_FILE"
			break
			;;
		*)      echo "  Please enter y or n." ;;
	esac
done

echo ""
echo "Uploading the deployment template to the $projectname project's template library..."
oc create -f $template_file -n $projectname

echo ""
echo "Please use your browser to connect to OpenShift and deploy the project from the template."

exit 0
