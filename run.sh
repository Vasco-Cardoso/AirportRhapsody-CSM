cd out/production/AirportRhapsody-CSM/
ls
echo "Running Shared Memories"
java serverSide/ArrLoungeMain &
java serverSide/ArrTermExitMain &
java serverSide/ArrTransfTermMain &
java serverSide/DepTermEntMain &
java serverSide/DepTransfTermMain &
java serverSide/LugColPointMain &
java serverSide/RecOfMain &
java serverSide/StorageAreaMain &

echo "Running clients"
sleep 1
java clientSide/ClientAirport 

echo "Now we wait"