**Vagrant Mongo Replica config**

This instruction creates Vagrant with installed MongoDb ReplicaSet. Follow the below steps to have it:

```sh
cd /vagrant-mongo/vagrant
vagrant up
```

Check that everything installed well and replica is configured, to do that SSH to a host and connect to mongo service:

```sh
vagrant ssh mongod2
mongo --host 192.168.12.102 --port 40000
```

To clean resources run the following commands:

```sh
vagrant halt
vagrant destroy
```

vagrant-mongo/vagrant/shared -> /vagrant is a shared folder.

Please don't remove vagrant-mongo/vagrant/shared/data folder.