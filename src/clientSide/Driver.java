package clientSide;

import mainProgram.Airport;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

public class Driver extends Thread{

    // Possible different states of the clientSide.Driver
    enum State {
        PARKING_AT_THE_ARRIVAL_TERMINAL,
        DRIVING_FORWARD,
        PARKING_AT_THE_DEPARTURE_TERMINAL,
        DRIVING_BACKWARDS
    }

    // Locks
    ReentrantLock lock = new ReentrantLock();

    // Variables
    private State STATE;
    private Queue<Passenger> seats;
    private int nseats;
    private boolean hasDaysWorkEnded = true;

    // Regions
    private ArrivalTransferTerminalStub arrivalTransferTerminal;
    private DepartureTransferTerminalStub departureTransferTerminal;

    @Override
    public void run()
    {
        while(true)
        {
            switch (this.STATE)
            {
                case PARKING_AT_THE_ARRIVAL_TERMINAL:
                    Airport.logger.setDriverState("PAAT");
                    Airport.logger.write(false);

                    arrivalTransferTerminal.clearSpots();

                    if(!this.hasDaysWorkEnded)
                    {
                        return;
                    }

                    // Announces boarding and after that returns the seats.
                    arrivalTransferTerminal.announcingBusBoarding();
                    seats = arrivalTransferTerminal.getSpots();

                    // Changes state
                    this.goToDepartureTerminal();

                    break;
                case DRIVING_FORWARD:
                    Airport.logger.setDriverState("DF");
                    Airport.logger.write(false);

                    try
                    {
                        // To simulate a trip we added a 1 second drive between stages after the first one.
                        sleep(1000);

                        // Changes state
                        this.setSTATE(State.PARKING_AT_THE_DEPARTURE_TERMINAL);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }

                    break;
                case PARKING_AT_THE_DEPARTURE_TERMINAL:
                    Airport.logger.setDriverState("PADT");
                    Airport.logger.write(false);

                    try {
                        // To simulate a trip we added a 1 second drive between stages after the first one.
                        sleep(1000);

                        departureTransferTerminal.parkTheBusAndLetPassOff(seats);
                        departureTransferTerminal.goToArrivalTerminal();

                        // Changes state
                        this.setSTATE(State.DRIVING_BACKWARDS);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }

                    break;
                case DRIVING_BACKWARDS:
                    Airport.logger.setDriverState("DB");
                    Airport.logger.write(false);

                    try
                    {
                        // To simulate a trip we added a 1 second drive between stages after the first one.
                        sleep(1000);

                        // Changes state
                        this.parkTheBus();

                        break;
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
            }
        }
    }

    public void parkTheBus()
    {
        this.setSTATE(State.PARKING_AT_THE_ARRIVAL_TERMINAL);
    }


    private void goToDepartureTerminal()
    {
        this.setSTATE(State.DRIVING_FORWARD);
    }

    // ----------------------------------------------------------------------------------------------- //
    // ------------------------------ Constructors, setters and getters ------------------------------ //
    // ----------------------------------------------------------------------------------------------- //

    // Constructor
    public Driver(int n_seats, ArrivalTransferTerminalStub arrivalTransferTerminal, DepartureTransferTerminalStub departureTransferTerminal) {
        System.out.println("Setting up driver");
        this.seats = new LinkedList<>();
        this.nseats = n_seats;
        this.arrivalTransferTerminal = arrivalTransferTerminal;
        this.departureTransferTerminal = departureTransferTerminal;
        this.STATE = State.PARKING_AT_THE_ARRIVAL_TERMINAL;

        Airport.logger.setDriverState("PAAT");
        Airport.logger.write(false);
    }


    public State getSTATE() {
        return this.STATE;
    }

    public void setSTATE(State STATE) {
        this.STATE = STATE;
    }

    public Queue<Passenger> getSeats() {
        return this.seats;
    }

    public void setHasDaysWorkEnded()
    {
        this.hasDaysWorkEnded = false;
    }
    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Driver)) {
            return false;
        }
        Driver driver = (Driver) o;
        return Objects.equals(STATE, driver.STATE) && Objects.equals(seats, driver.seats);
    }

    @Override
    public int hashCode() {
        return Objects.hash(STATE, seats);
    }

    @Override
    public String toString() {
        return "{" +
                " STATE='" + getSTATE() + "'" +
                ", seats='" + getSeats() + "'" +
                "}";
    }

}