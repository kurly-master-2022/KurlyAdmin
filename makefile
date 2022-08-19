main:
	./gradlew clean -x test build
	sam build --profile kurly
	sam deploy --profile kurly

auth:
	aws configure --profile kurly
	cat ~/.aws/config
	cat ~/.aws/credentials