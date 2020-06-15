#!/usr/bin/env sh

# serverSide
cd bin
echo "starting"
java serverSide.GenRepoMain  &
sleep 1&
java serverSide.ArrLoungeMain &
sleep 1&
java serverSide.ArrTermExitMain &
sleep 1&
java serverSide.ArrTransfTermMain &
sleep 1&
java serverSide.DepTermEntMain &
sleep 1&
java serverSide.DepTransfTermMain &
sleep 1&
java serverSide.LugColPointMain  &
sleep 1&
java serverSide.ReclaimOfficeMain &
sleep 1&
java serverSide.StorageAreaMain  & 

sleep 1 &

# clientSide
echo "Insert the host name "
java clientSide.ClientAirport
