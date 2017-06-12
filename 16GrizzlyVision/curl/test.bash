#!/bin/bash

API_KEY="AIzaSyCsXUM3ohNVC6rQmJ4dHrYu9m0QvNrhw9U"
curl -v -k -s -H "Content-Type: application/json" \
    https://vision.googleapis.com/v1/images:annotate?key=${API_KEY} \
    --data-binary @request.json


