#!/bin/bash
MODULE="api"
VERSION=`grep self conf/dependencies.yml | sed "s/.*$MODULE //"`
TARGET=/Users/fivesmallq/nginx/html/play-$MODULE/$MODULE-$VERSION.zip

rm -fr dist
play dependencies --sync || exit $?
play build-module || exit $?

if [ -e $TARGET ]; then
    echo "Not publishing, $MODULE-$VERSION already exists"
else
	echo "copy to $TARGET"
    cp dist/*.zip $TARGET
fi