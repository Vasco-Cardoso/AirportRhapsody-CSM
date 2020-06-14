package clientSide;

import clientSide.entities.Driver;
import clientSide.entities.Passenger;
import clientSide.entities.Porter;
import clientSide.stubs.*;

import java.util.Scanner;

public class ClientAirport {

    public static int nPassengers = 6 ;                               // number of passengers
    public static int nPlaneLandings = 5;                             // number of plane landings
    public static int nSeatingPlaces = 3;                             // bus capacity
    public static int maxBags = 3;                                    // maximum luggage
    public static boolean airplanesDone = false;
    public static GeneralRepositoryStub logger;

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

        Scanner scanner = new Scanner(System. in);

        int nIter;                                           // número de iterações do ciclo de vida dos clientes
        String fName;                                        // nome do ficheiro de logging
        System.out.print("Nome do sistema computacional onde está o servidor? ");
        String serverHostName = scanner. nextLine();
        //String serverHostName = "afonso-N550RN";           // nome do sistema computacional onde está o servidor
        int serverPortNumb = 22000;                          // número do port de escuta do servidor

        arrivalLoungeStub = new ArrivalLoungeStub(serverHostName, serverPortNumb + 1);
        luggageCollectionPointStub = new LuggageCollectionPointStub(serverHostName, serverPortNumb + 2);
        reclaimOfficeStub = new ReclaimOfficeStub(serverHostName, serverPortNumb + 3);
        arrivalTransferTerminalStub = new ArrivalTransferTerminalStub(serverHostName, serverPortNumb + 4);
        departureTransferTerminalStub = new DepartureTransferTerminalStub(serverHostName, serverPortNumb + 5);
        arrivalTerminalExitStub = new ArrivalTerminalExitStub(serverHostName, serverPortNumb + 6);
        departureTerminalEntryStub = new DepartureTerminalEntryStub(serverHostName, serverPortNumb + 7);
        storageAreaStub = new StorageAreaStub(serverHostName, serverPortNumb + 8);
        logger = new GeneralRepositoryStub(serverHostName, serverPortNumb + 9);

        porter = new Porter(arrivalLoungeStub, luggageCollectionPointStub, storageAreaStub);
        busDriver = new Driver(nSeatingPlaces, arrivalTransferTerminalStub, departureTransferTerminalStub);


        porter.start();
        busDriver.start();

        for (int i = 0; i < nPlaneLandings; i++) {
            System.out.println("------------- VOO NUMERO " + (i + 1) + " ---------------------");

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
        ClientAirport.airplanesDone = true;
        System.out.println("------------- FLIGHTS OVER ---------------");

        // Signalling the Porter, stuck at TakeARest to wake up and end his life.
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


        // Logging the last things
        logger.setnLostBags(reclaimOfficeStub.getnLuggagesLost());
        logger.write(true);

        // Join the porter to end his life.
        try
        {
            porter.join();
        }
        catch (InterruptedException e)
        {
            e.fillInStackTrace();
        }

        arrivalLoungeStub.terminate();
        luggageCollectionPointStub.terminate();
        reclaimOfficeStub.terminate();
        arrivalTransferTerminalStub.terminate();
        departureTransferTerminalStub.terminate();
        arrivalTerminalExitStub.terminate();
        departureTerminalEntryStub.terminate();
        storageAreaStub.terminate();
        logger.terminate();

    }
}
