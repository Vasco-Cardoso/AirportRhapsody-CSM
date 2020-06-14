package mainProgram;

import clientSide.entities.Passenger;
import clientSide.entities.Porter;
import clientSide.entities.Driver;
import serverSide.repository.GeneralRepository;
import serverSide.sharedRegions.*;


public class Airport {

    public static int nPassengers = 6 ;                               // number of passengers
    public static int nPlaneLandings = 5;                             // number of plane landings
    public static int nSeatingPlaces = 3;                             // bus capacity
    public static int maxBags = 3;                                    // maximum luggage
    public static GeneralRepository logger = new GeneralRepository(nSeatingPlaces, nPassengers, 6,"Report.txt");
    public static boolean airplanesDone = false;


    public static void main(String[] args) {
        Passenger[] passenger = new Passenger[nPassengers]; // Passenger threads array
        Porter porter;                                      // thread porter
        Driver busDriver;                                // thread bus driver


        ArrivalLounge arrivalLounge = new ArrivalLounge();;
        LuggageCollectionPoint collPoint = new LuggageCollectionPoint();
        ReclaimOffice reclaimOffice = new ReclaimOffice();
        ArrivalTransferTerminal arrTransQuay = new ArrivalTransferTerminal();
        DepartureTransferTerminal depTransQuay = new DepartureTransferTerminal();
        ArrivalTerminalExit arrTermExit = new ArrivalTerminalExit();
        DepartureTerminalEntry depTermEntrance = new DepartureTerminalEntry();
        StorageArea tempStorageArea = new StorageArea();

        //entities
        //porter = new Porter(arrivalLounge, collPoint, tempStorageArea);
        //busDriver = new Driver(nSeatingPlaces,arrTransQuay,depTransQuay);


        //Start Simulation
        //porter.start();
        //busDriver.start();

        for(int i=0; i<nPlaneLandings; i++){
            System.out.println("------------- VOO NUMERO"+(i+1)+" ---------------------\n");

            logger.setnFlight(i+1);
            int b = 0;
            for (int j = 0; j < nPassengers; j++){
                //passenger[j] = new Passenger(j, arrivalLounge, collPoint, reclaimOffice, arrTransQuay, depTermEntrance, depTransQuay, arrTermExit);
                b += passenger[j].getNum_bags();
            }
            logger.setnBags(b);
            logger.write(false);

            for (int j = 0; j < nPassengers; j++){
                passenger[j].start();
            }

            for (int j = 0; j < nPassengers; j++){
                try{
                    passenger[j].join();
                }catch (InterruptedException e){}
            }

            depTransQuay.setCanleave();

            logger.clear();
            logger.write(false);
            arrTermExit.setEmpty();
            depTermEntrance.setEmpty();
        }
        airplanesDone = true;
        System.out.println("------------- FLIGHTS OVER ---------------");

        // Signalling the Porter, stuch at TakeARest to wake up and end his life.
        arrivalLounge.signalEnd();

        // Signal porter, it is over.
        //busDriver.setHasDaysWorkEnded();

        // Join the busDriver to end his life.
        /*try
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
            //porter.join();
        }
        catch (InterruptedException e)
        {
            e.fillInStackTrace();
        }*/

        // Logging the last things
        logger.setnLostBags(reclaimOffice.getnLuggagesLost());
        logger.write(true);
    }
}
