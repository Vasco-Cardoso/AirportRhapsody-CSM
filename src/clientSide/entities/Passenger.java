package clientSide.entities;

import clientSide.ClientAirport;
import clientSide.stubs.*;
import comInf.Luggages;
import mainProgram.Airport;

import java.io.Serializable;
import java.util.Objects;
import java.util.Random;

public class Passenger extends Thread implements Serializable {

    // Possible different states of the clientSide.entities.Passenger
    enum State 
    {
        AT_THE_DISEMBARKING_ZONE,
        AT_THE_LUGGAGE_COLLECTION_POINT,
        AT_THE_BAGGAGE_RECLAIM_OFFICE,
        EXITING_THE_ARRIVAL_TERMINAL,
        AT_THE_ARRIVAL_TRANSFER_TERMINAL,
        TERMINAL_TRANSFER,
        AT_THE_DEPARTURE_TRANSFER_TERMINAL,
        ENTERING_THE_DEPARTURE_TERMINAL
    }

    // Variáveis
    private boolean transit;
    private State STATE;
    private int num_bags;
    private int num_bags_collected;
    private int id;

    // Regions
    private ArrivalLoungeStub arrivalLounge;
    private LuggageCollectionPointStub luggageCollectionPoint;
    private ReclaimOfficeStub reclaimOffice;
    private ArrivalTransferTerminalStub arrivalTransferTerminal;
    private DepartureTerminalEntryStub departureTerminalEntry;
    private DepartureTransferTerminalStub departureTransferTerminal;
    private ArrivalTerminalExitStub arrivalTerminalExit;

    private boolean die = false;

    @Override
    public void run() {
        while (!die) {
            switch (this.STATE) {
                case AT_THE_DISEMBARKING_ZONE:
                    // Logger
//                    Airport.logger.setPassState(this.id,"ATDZ");
//                    Airport.logger.write(false);

                    // Arrives the serverSide.sharedRegions.ArrivalLounge
                    this.arrivalLounge.disembarkPassenger();

                    // Checks what he has to do
                    if (!whatShouldIDo()) {
                        if (this.num_bags == 0)
                        {
                            goHome();
                        }
                        else
                        {
                            setSTATE(State.AT_THE_LUGGAGE_COLLECTION_POINT);
                        }
                    }
                    else
                    {
                        takeABus();
                    }

                    break;
                case AT_THE_LUGGAGE_COLLECTION_POINT:
                    // Logger
//                    Airport.logger.setPassState(this.id,"ATCP");
//                    Airport.logger.write(false);

                    // Goes collect his bags and return the number of bags he could collect
                    num_bags_collected = this.luggageCollectionPoint.goCollectABag(this);

                    // Checks if the passenger could collect every bag of his own, if not goes to reclaim office
                    if(this.num_bags_collected < this.num_bags)
                    {
                        setSTATE(State.AT_THE_BAGGAGE_RECLAIM_OFFICE);
                    }
                    else
                    {
//                        Airport.logger.setnBagsCollected(this.id,this.num_bags_collected);
//                        Airport.logger.write(false);

                        goHome();
                    }
                    break;
                case AT_THE_BAGGAGE_RECLAIM_OFFICE:
                    // Logger
//                    Airport.logger.setPassState(this.id,"ATRO");
//                    Airport.logger.write(false);

                    // Reports missing bags
                    this.reclaimOffice.reportMissingBags(num_bags - num_bags_collected);

                    // Precedes with his life cycle
                    goHome();

                    break;
                case EXITING_THE_ARRIVAL_TERMINAL:
                    // Logger
//                    Airport.logger.setPassState(this.id, "EAT");
//                    Airport.logger.write(false);

                    // Checks if this passenger is the last one to arrive the terminal, else waits for the last one to signal
                    if(ClientAirport.nPassengers == this.arrivalTerminalExit.getNumPassengers() + this.departureTerminalEntry.getNumPassengers())
                    {
                        this.arrivalTerminalExit.lastPassenger();
                        this.departureTerminalEntry.lastPassenger();
                    }
                    else
                    {
                        this.arrivalTerminalExit.waitLastPassenger();
                    }

                    this.die = true;
                    break;
                case AT_THE_ARRIVAL_TRANSFER_TERMINAL:
                    // Logger
//                    Airport.logger.setPassState(this.getPId(),"AATT");
//                    Airport.logger.write(false);

                    this.arrivalTransferTerminal.enterTheBus();
                    setSTATE(State.TERMINAL_TRANSFER);

                    break;
                case TERMINAL_TRANSFER:
                    // Logger
//                    Airport.logger.setPassState(this.id,"TT");
//                    Airport.logger.write(false);

                    // Leaves the bus
                    departureTransferTerminal.leaveTheBus();
                    setSTATE(State.AT_THE_DEPARTURE_TRANSFER_TERMINAL);

                    break;
                case AT_THE_DEPARTURE_TRANSFER_TERMINAL:
                    // Logger
//                    Airport.logger.setPassState(this.id,"ADTT");
//                    Airport.logger.write(false);

                    this.departureTransferTerminal.leaveDepartureTransferTerminal();
                    prepareNextLeg();
                    break;
                case ENTERING_THE_DEPARTURE_TERMINAL:
                    // Logger
//                    Airport.logger.setPassState(this.getPId(),"EDT");
//                    Airport.logger.write(false);

                    // Checks if this passenger is the last one to arrive the terminal, else waits for the last one to signal
                    if(ClientAirport.nPassengers == this.arrivalTerminalExit.getNumPassengers() + this.departureTerminalEntry.getNumPassengers())
                    {
                        this.arrivalTerminalExit.lastPassenger();
                        this.departureTerminalEntry.lastPassenger();
                    }else{
                        this.departureTerminalEntry.waitLastPassenger();
                    }

                    this.die = true;
                    break;
            }
        }
    }

    // Funções de estado
    private void goHome()
    {
        this.arrivalTerminalExit.arrivedTerminal();
        setSTATE(State.EXITING_THE_ARRIVAL_TERMINAL);
    }


    private boolean whatShouldIDo()
    {
        return this.transit;
    }

    private void takeABus()
    {
        this.arrivalTransferTerminal.arrivedTerminal(this);
        setSTATE(State.AT_THE_ARRIVAL_TRANSFER_TERMINAL);
    }

    private void prepareNextLeg()
    {
        this.departureTerminalEntry.arrivedTerminal();
        setSTATE(State.ENTERING_THE_DEPARTURE_TERMINAL);
    }

    // Funções de estado -- FIM

    // ----------------------------------------------------------------------------------------------- //
    // ------------------------------ Constructors, setters and getters ------------------------------ //
    // ----------------------------------------------------------------------------------------------- //

    public Passenger(int id, ArrivalLoungeStub arrivalLounge, LuggageCollectionPointStub luggageCollectionPoint, ReclaimOfficeStub reclaimOffice, ArrivalTransferTerminalStub arrivalTransferTerminalStub, DepartureTerminalEntryStub departureTerminalEntry, DepartureTransferTerminalStub departureTransferTerminal, ArrivalTerminalExitStub arrivalTerminalExit) {

        this.id = id;
        this.arrivalLounge = arrivalLounge;
        this.luggageCollectionPoint = luggageCollectionPoint;
        this.reclaimOffice = reclaimOffice;
        this.arrivalTransferTerminal = arrivalTransferTerminalStub;
        this.departureTerminalEntry = departureTerminalEntry;
        this.departureTransferTerminal = departureTransferTerminal;
        this.arrivalTerminalExit = arrivalTerminalExit;
        this.STATE = State.AT_THE_DISEMBARKING_ZONE;

        Airport.logger.setPassState(this.id,"ATDZ");
        Airport.logger.write(false);
        setupPassanger();
    }

    /**
     * Method to set the passenger with random transit and number
     * of bags.
     */
    private void setupPassanger()
    {
        Random rd = new Random();
        Random r2 = new Random();
        this.transit = rd.nextBoolean();
        this.num_bags = r2.nextInt(ClientAirport.maxBags);
        Random r = new Random();

        for(int i=0; i<this.num_bags ; i++)
        {
            if(r.nextInt(100) > 0 ) // 10% prob of losing bag
            {
                arrivalLounge.depositLuggage(new Luggages(this));
            }
        }

        Airport.logger.setPassSituation(this.id,this.transit);
        Airport.logger.setnBagsStart(this.id, this.num_bags);
        Airport.logger.setnBagsCollected(this.id, 0);
        Airport.logger.write(false);
    }

    public boolean isTransit() {
        return this.transit;
    }

    public void setSTATE(State STATE) {
        this.STATE = STATE;
    }

    public int getNum_bags() {
        return this.num_bags;
    }

    public int getPId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Passenger)) {
            return false;
        }
        Passenger passenger = (Passenger) o;
        return transit == passenger.transit
                && Objects.equals(STATE, passenger.STATE)
                && num_bags == passenger.num_bags
                && num_bags_collected == passenger.num_bags_collected;
    }

    @Override
    public int hashCode() {
        return Objects.hash(transit, STATE, num_bags, num_bags_collected);
    }

    @Override
    public String toString() {
        return "clientSide.entities.Passenger{" +
                "transit=" + transit +
                ", STATE=" + STATE +
                ", num_bags=" + num_bags +
                ", num_bags_collected=" + num_bags_collected +
                ", id=" + id +
                '}';
    }
}