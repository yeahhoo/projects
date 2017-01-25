#!/bin/bash

cmd="/usr/bin/mongod --replSet my-mongo-set --storageEngine wiredTiger"

if [ "$IS_REPL_INIT" == "yes" ]; then
    echo "param IS_REPL_INIT is ${IS_REPL_INIT} so run as Replica initializer"
    set -m
    $cmd &
    /usr/bin/mongo /mongo-confs/init-replica-script.js
    fg
else
    echo "param IS_REPL_INIT is ${IS_REPL_INIT} so run as simple node"
    exec ${cmd}
fi
