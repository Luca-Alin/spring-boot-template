#!/usr/bin/env bash
rm -rf target
./mvnw -Pnative native:compile
