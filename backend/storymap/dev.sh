#!/bin/zsh
while read -r line; do
  if [[ "$line" == *=* ]]; then
    export "$line"
  fi
done < .env