#!/usr/bin/env bash

cd ../
mvn package
cp target/system-block-1.0-SNAPSHOT.jar ~/jvm-sandbox/sandbox/module/