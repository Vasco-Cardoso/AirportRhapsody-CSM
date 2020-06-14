#!/usr/bin/env sh

# serverSide
cd bin

java serverSide.repository.GeneralRepository > GeneralRepository 
sleep 1

java serverSide.ArrLoungeMain > ArrLoungeMain &
java serverSide.ArrTermExitMain > ArrTermExitMain &
java serverSide.ArrTransfTermMain > ArrivalTerminalTransferQuay &
java serverSide.DepTermEntMain > DepTermEntMain &
java serverSide.DepTransfTermMain > DepTransfTermMain &
java serverSide.LugColPointMain > LugColPointMain &
java serverSide.RecOfMain > RecOfMain &
java serverSide.StorageAreaMain > StorageAreaMain & 


sleep 1

# clientSide

java clientSide.ClientAirport > ClientAirport 
