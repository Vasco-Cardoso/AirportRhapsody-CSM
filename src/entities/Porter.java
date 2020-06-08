package entities;

import commonInfrastructures.Luggages;
import mainProgram.Airport;
import sharedRegions.ArrivalLounge;
import sharedRegions.LuggageCollectionPoint;
import sharedRegions.StorageArea;

import java.util.Objects;


public class Porter extends Thread {
    // Possible different states of the entities.Porter
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
    private ArrivalLounge arrivalLounge;
    private LuggageCollectionPoint luggageCollectionPoint;
    private StorageArea storageArea;

    @Override
    public void run()
    {
        while(!Airport.airplanesDone)
        {
            switch(this.STATE)
            {
                case WAITING_FOR_A_PLANE_TO_LAND:
                    // Logger
                    Airport.logger.setPorterState("WFPL");
                    Airport.logger.write(false);

                    // TakeARest to check what goes next
                    arrivalLounge.takeARest();

                    this.setSTATE(State.AT_THE_PLANES_HOLD);

                    break;
                case AT_THE_PLANES_HOLD:
                    // Logger
                    Airport.logger.setPorterState("ATPH");
                    Airport.logger.write(false);

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
            Airport.logger.setnBagsStoreroom(this.storageArea.getSize());
            Airport.logger.setPorterState("ATST");
            Airport.logger.write(false);

        }else{

            this.luggageCollectionPoint.depositLuggage(l);
            setSTATE(State.AT_THE_LUGGAGE_BELT_CONVEYOR);

            // Logger
            Airport.logger.setnBagsConveyor(this.luggageCollectionPoint.getSize());
            Airport.logger.setPorterState("ALBC");
            Airport.logger.write(false);
        }
    }

    // ----------------------------------------------------------------------------------------------- //
    // ------------------------------ Constructors, setters and getters ------------------------------ //
    // ----------------------------------------------------------------------------------------------- //

    public Porter(ArrivalLounge arrivalLounge, LuggageCollectionPoint luggageCollectionPoint, StorageArea storageArea)
    {
        this.arrivalLounge = arrivalLounge;
        this.luggageCollectionPoint = luggageCollectionPoint;
        this.storageArea = storageArea;
        this.STATE = State.WAITING_FOR_A_PLANE_TO_LAND;

        Airport.logger.setPorterState("WFPL");
        Airport.logger.write(false);
    }

    public Porter() {
        this.STATE = State.WAITING_FOR_A_PLANE_TO_LAND;

        Airport.logger.setPorterState("WFPL");
        Airport.logger.write(false);

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