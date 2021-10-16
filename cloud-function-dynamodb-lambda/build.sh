#!/bin/bash

docker-compose up
sudo chown -R $(whoami):$(whoami) target/