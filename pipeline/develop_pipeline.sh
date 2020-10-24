SERVICE_NAME=`cat servicename`
export AWS_DEFAULT_PROFILE=aa-dev
ACCOUNT_ID=`aws sts get-caller-identity --query "Account" --output text`

aws \
cloudformation \
deploy \
--role-arn arn:aws:iam::${ACCOUNT_ID}:role/CloudFormationRole \
--template-file develop_pipeline.yml \
--stack-name dip-${SERVICE_NAME}-develop-pipeline \
--capabilities CAPABILITY_NAMED_IAM \
--parameter-overrides \
ServiceName=${SERVICE_NAME} \
GithubRepo=dip-${SERVICE_NAME} \
GithubToken=`aws ssm --profile aa-dev get-parameter --name GithubToken --with-decryption --query "Parameter.Value" --output text` \
CyclingSecret=`openssl rand -base64 32`


unset AWS_DEFAULT_PROFILE