#!/usr/bin/env bash

FRIDAY_CONFIG_DIR="$HOME/.friday"

################################################################################
### INSTALL FRIDAY
################################################################################

./gradlew clean assemble
cp build/libs/*.jar /usr/local/bin/friday.jar
chmod +x friday
cp friday /usr/local/bin

rm -rf "$FRIDAY_CONFIG_DIR"/config
mkdir -p "$FRIDAY_CONFIG_DIR"/config
