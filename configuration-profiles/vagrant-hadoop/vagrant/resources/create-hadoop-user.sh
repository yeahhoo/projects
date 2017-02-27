#!/bin/bash

group=hadoop
username=hadoop
password=hadoop

adduser $username --gecos "First Last,RoomNumber,WorkPhone,HomePhone" --disabled-password
echo "$username:$password" | chpasswd
usermod -a -G $group $username

# removing shady password for user
# sed -i "s/$username:x:/$username::/g" /etc/passwd

# enabling hadoop user with the same powers as root
echo "$username  ALL=(ALL:ALL) ALL" >> /etc/sudoers
