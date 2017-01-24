#!/bin/bash

set -m

#echo "param: ${IS_REPL_INIT}"
cmd="/usr/bin/mongod --replSet my-mongo-set --storageEngine wiredTiger"

#exec ${cmd}

$cmd &

if [ "$IS_REPL_INIT" == "yes" ]; then
    /usr/bin/mongo /mongo-confs/init-replica-script.js
fi

fg