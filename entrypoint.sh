#!/usr/bin/env bash
echo "Starting project with Params $@"
exec java -Dfile.encoding=UTF-8 $@ -jar app.jar