#!/bin/bash

cmd="/usr/bin/mongod --replSet my-mongo-set --storageEngine wiredTiger --logpath /data/logs/mylog.log"

if [ "$IS_REPL_INIT" == "yes" ]; then
    echo "param IS_REPL_INIT is ${IS_REPL_INIT} so run as Replica initializer"
    # waiting for mongo2 and mongo3 started
    sleep 5
    set -m
    $cmd &
    /usr/bin/mongo /mongo-confs/init-replica-script.js
    fg
else
    echo "param IS_REPL_INIT is ${IS_REPL_INIT} so run as simple node"
    exec ${cmd}
fi
