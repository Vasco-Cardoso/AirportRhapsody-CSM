package clientSide;

import serverSide.GeneralRepository;

public class ClientAirport {

    public static int nPassengers = 6 ;                               // number of passengers
    public static int nPlaneLandings = 5;                             // number of plane landings
    public static int nSeatingPlaces = 3;                             // bus capacity
    public static int maxBags = 3;                                    // maximum luggage
    public static GeneralRepository logger = new GeneralRepository(nSeatingPlaces, nPassengers, 6,"Report.txt");
    public static boolean airplanesDone = false;

    public static void main (String [] args) {
        Passenger[] passenger = new Passenger[nPassengers]; // Passenger threads array
        Porter porter;                                     // thread porter
        Driver busDriver;                                // thread bus driver

        ArrivalLoungeStub arrivalLoungeStub;
        ArrivalTerminalExitStub arrivalTerminalExitStub;
        ArrivalTransferTerminalStub arrivalTransferTerminalStub;
        DepartureTerminalEntryStub departureTerminalEntryStub;
        DepartureTransferTerminalStub departureTransferTerminalStub;
        LuggageCollectionPointStub luggageCollectionPointStub;
        ReclaimOfficeStub reclaimOfficeStub;
        StorageAreaStub storageAreaStub;


        int nIter;                                           // número de iterações do ciclo de vida dos clientes
        String fName;                                        // nome do ficheiro de logging
        String serverHostName = "afonso-N550RN";                   // nome do sistema computacional onde está o servidor
        int serverPortNumb = 22001;                          // número do port de escuta do servidor

        arrivalLoungeStub = new ArrivalLoungeStub(serverHostName, serverPortNumb);
        luggageCollectionPointStub = new LuggageCollectionPointStub(serverHostName, serverPortNumb);
        reclaimOfficeStub = new ReclaimOfficeStub(serverHostName, serverPortNumb);
        arrivalTransferTerminalStub = new ArrivalTransferTerminalStub(serverHostName, serverPortNumb);
        departureTransferTerminalStub = new DepartureTransferTerminalStub(serverHostName, serverPortNumb);
        arrivalTerminalExitStub = new ArrivalTerminalExitStub(serverHostName, serverPortNumb);
        departureTerminalEntryStub = new DepartureTerminalEntryStub(serverHostName, serverPortNumb);
        storageAreaStub = new StorageAreaStub(serverHostName, serverPortNumb);

        porter = new Porter(arrivalLoungeStub, luggageCollectionPointStub, storageAreaStub);
        busDriver = new Driver(nSeatingPlaces, arrivalTransferTerminalStub, departureTransferTerminalStub);


        porter.start();
        busDriver.start();

        for (int i = 0; i < nPlaneLandings; i++) {
            System.out.println("------------- VOO NUMERO" + (i + 1) + " ---------------------\n");

            logger.setnFlight(i + 1);
            int b = 0;
            for (int j = 0; j < nPassengers; j++) {
                passenger[j] = new Passenger(j, arrivalLoungeStub, luggageCollectionPointStub, reclaimOfficeStub, arrivalTransferTerminalStub, departureTerminalEntryStub, departureTransferTerminalStub, arrivalTerminalExitStub);
                b += passenger[j].getNum_bags();
            }
            logger.setnBags(b);
            logger.write(false);

            for (int j = 0; j < nPassengers; j++) {
                passenger[j].start();
            }

            for (int j = 0; j < nPassengers; j++) {
                try {
                    passenger[j].join();
                } catch (InterruptedException e) {
                }
            }

            departureTransferTerminalStub.setCanleave();

            logger.clear();
            logger.write(false);
            arrivalTerminalExitStub.setEmpty();
            departureTerminalEntryStub.setEmpty();
        }
        airplanesDone = true;
        System.out.println("------------- FLIGHTS OVER ---------------");

        // Signalling the Porter, stuch at TakeARest to wake up and end his life.
        arrivalLoungeStub.signalEnd();

        // Signal porter, it is over.
        busDriver.setHasDaysWorkEnded();

        // Join the busDriver to end his life.
        try
        {
            busDriver.join();
        }
        catch (InterruptedException e)
        {
            e.fillInStackTrace();
        }

        // Join the porter to end his life.
        try
        {
            porter.join();
        }
        catch (InterruptedException e)
        {
            e.fillInStackTrace();
        }

        // Logging the last things
        logger.setnLostBags(reclaimOfficeStub.getnLuggagesLost());
        logger.write(true);
    }
}
