#!/usr/bin/env bash

mkdir microservices
cd microservices

spring init \
--boot-version=3.1.0 \
--build=gradle \
--type=gradle-project \
--java-version=1.8 \
--packaging=jar \
--name=product-service \
--package-name=se.magnus.microservices.core.product \
--groupId=se.magnus.microservices.core.product \
--dependencies=actuator,webflux \
--version=1.0.0-SNAPSHOT \
product-service