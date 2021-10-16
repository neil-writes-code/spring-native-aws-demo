#!/bin/bash

FUNCTION_NAME=cloud-function-lambda

echo "Pushing code to AWS"
aws lambda update-function-code \
    --function-name $FUNCTION_NAME \
    --zip-file fileb://target/$FUNCTION_NAME-native.zip
echo

echo "Invoking Lambda"
aws lambda invoke \
    --function-name $FUNCTION_NAME \
    --cli-binary-format raw-in-base64-out \
    --payload '{"message": "hello world!"}' \
    response.json
echo

echo "Lambda Response: "
cat response.json
echo