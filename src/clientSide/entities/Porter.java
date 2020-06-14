package clientSide.entities;

import clientSide.ClientAirport;
import clientSide.stubs.ArrivalLoungeStub;
import clientSide.stubs.LuggageCollectionPointStub;
import clientSide.stubs.StorageAreaStub;
import comInf.Luggages;

import java.util.Objects;


public class Porter extends Thread {
    // Possible different states of the clientSide.entities.Porter
    enum State {
        WAITING_FOR_A_PLANE_TO_LAND,
        AT_THE_PLANES_HOLD,
        AT_THE_LUGGAGE_BELT_CONVEYOR,
        AT_THE_STOREROOM
    }

    // Variables
    private State STATE;
    private Luggages l = null;

    // Regions
    private ArrivalLoungeStub arrivalLounge;
    private LuggageCollectionPointStub luggageCollectionPoint;
    private StorageAreaStub storageArea;

    @Override
    public void run()
    {
        while(!ClientAirport.airplanesDone)
        {
            switch(this.STATE)
            {
                case WAITING_FOR_A_PLANE_TO_LAND:
                    // Logger
                    ClientAirport.logger.setPorterState("WFPL");
                    ClientAirport.logger.write(false);

                    // TakeARest to check what goes next
                    arrivalLounge.takeARest();

                    this.setSTATE(State.AT_THE_PLANES_HOLD);

                    break;
                case AT_THE_PLANES_HOLD:
                    // Logger
                    ClientAirport.logger.setPorterState("ATPH");
                    ClientAirport.logger.write(false);

                    // Tries to collect a bag and either end this state or carryItToAppropriateStore
                    l = arrivalLounge.tryToCollectABag();
                    if(l != null)
                    {
                        carryItToAppropriateStore(l);
                    }
                    else
                    {
                        this.luggageCollectionPoint.noMoreBagsToCollect();
                        //System.out.println("PORTER: no more bags to collect");
                        setSTATE(State.WAITING_FOR_A_PLANE_TO_LAND);
                    }

                    break;
                case AT_THE_LUGGAGE_BELT_CONVEYOR :
                case AT_THE_STOREROOM:
                    this.setSTATE(State.AT_THE_PLANES_HOLD);
                    break;
            }
        }
    }


    /**
     * Carries the luggage to a the appropriate store depending on its transit state
     * @param l luggage to deposit
     */
    private void carryItToAppropriateStore(Luggages l) {
        if(l.getPassenger().isTransit())
        {
            this.storageArea.depositLuggage(l);
            setSTATE(State.AT_THE_STOREROOM);

            // Logger
            ClientAirport.logger.setnBagsStoreroom(this.storageArea.getSize());
            ClientAirport.logger.setPorterState("ATST");
            ClientAirport.logger.write(false);

        }else{

            this.luggageCollectionPoint.depositLuggage(l);
            setSTATE(State.AT_THE_LUGGAGE_BELT_CONVEYOR);

            // Logger
            ClientAirport.logger.setnBagsConveyor(this.luggageCollectionPoint.getSize());
            ClientAirport.logger.setPorterState("ALBC");
            ClientAirport.logger.write(false);
        }
    }

    // ----------------------------------------------------------------------------------------------- //
    // ------------------------------ Constructors, setters and getters ------------------------------ //
    // ----------------------------------------------------------------------------------------------- //

    public Porter(ArrivalLoungeStub arrivalLounge, LuggageCollectionPointStub luggageCollectionPoint, StorageAreaStub storageArea)
    {
        this.arrivalLounge = arrivalLounge;
        this.luggageCollectionPoint = luggageCollectionPoint;
        this.storageArea = storageArea;
        this.STATE = State.WAITING_FOR_A_PLANE_TO_LAND;

        ClientAirport.logger.setPorterState("WFPL");
        ClientAirport.logger.write(false);
    }

    public Porter() {
        this.STATE = State.WAITING_FOR_A_PLANE_TO_LAND;

        ClientAirport.logger.setPorterState("WFPL");
        ClientAirport.logger.write(false);

    }

    public State getSTATE() {
        return this.STATE;
    }

    public void setSTATE(State STATE) {
        this.STATE = STATE;
    }


    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Porter)) {
            return false;
        }
        Porter porter = (Porter) o;
        return Objects.equals(STATE, porter.STATE);
    }


    @Override
    public String toString() {
        return "{" +
            " STATE='" + getSTATE() + "'" +
            "}";
    }

}