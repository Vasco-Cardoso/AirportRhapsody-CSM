package serverSide.sharedRegions;

import clientSide.ClientAirport;
import clientSide.entities.Passenger;
import comInf.Message;
import serverSide.ArrTransfTermMain;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ArrivalTransferTerminal {

    // Locks and conditions
    ReentrantLock lock = new ReentrantLock();
    Condition busBoarding = lock.newCondition();
    Condition waitingqueue = lock.newCondition();
    Condition busFull = lock.newCondition();

    // Variables
    private Queue<Passenger> spots;
    private Queue<Passenger> waitingQ;
    private int numPassengers;
    private int busQsize;

    public ArrivalTransferTerminal() {
        this.numPassengers = 0;
        this.spots = new LinkedList<>();
        this.waitingQ = new LinkedList<>();
    }

    /**
     * A new passenger arrives the terminal and adds himself on the queue
     * while increasing the variable numPassengers.
     * @param p -> clientSide.entities.Passenger arriving
     */
    public void arrivedTerminal(Passenger p){
        lock.lock();
        try
        {
            waitingQ.add(p);
            this.numPassengers++;
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        finally
        {
            lock.unlock();
        }
    }

    /**
     * Method where passengers are stuck until the busDriver announces they can
     * board the bus, whenever this happens they check if the busQueue still has
     * free spots. If it does and if the passenger fills the last spot it signals
     * that the bus is full and so the busDriver can precede with his life cycle.
     */
    public void enterTheBus(){
        lock.lock();
        try
        {
            // Condition where passengers wait for the bus boarding signal
            busBoarding.await();

            // Check if the passenger can still get in the bus
            if(spots.size() < ClientAirport.nSeatingPlaces)
            {
                Passenger p = this.waitingQ.peek();
                this.spots.add(p);

                this.waitingQ.remove();

                // Check if he his the last passenger
                if(this.spots.size() == busQsize)
                {
                    busFull.signal();
                }
            }
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        finally
        {
            lock.unlock();
        }
    }

    /**
     * Method that signals passengers that Bus Boarding is allowed, signaling
     * each one separately.
     */
    public void announcingBusBoarding(){
        lock.lock();
        try {
            busQsize = 0;

            // Waits for at least one passenger to arrive.
            do
            {
                waitingqueue.await(1,TimeUnit.SECONDS);

                // Condition to check if their are three or less passengers ready to board.
                if(this.waitingQ.size() < 3)
                {
                    busQsize = waitingQ.size();
                }
                else
                {
                    busQsize = 3;
                }

            } while(busQsize == 0);

            for(int i=0; i<busQsize; i++)
            {
                busBoarding.signal();
            }

            busFull.await();

        }
        catch (Exception e)
        {
            // TODO
        }
        finally {
            lock.unlock();
        }
    }

    // Setters and getter
    public Queue<Passenger> getSpots() {
        return spots;
    }

    public Queue<Passenger> getWaitingQ() {
        return this.waitingQ;
    }

    public void clearSpots()
    {
        this.spots = new LinkedList<>();
    }

    public int numPassengers(){
        return this.numPassengers;
    }

    public void terminate(){
        ArrTransfTermMain.waitConnection = false;
        System.exit(1);
    }

    public Message processAndReply(Message inMessage) {

        Message outMessage = null;                           // mensagem de resposta

        switch (inMessage.getType ()){

            case Message.AT:
                arrivedTerminal(inMessage.getPass());
                outMessage = new Message (Message.ACK);       // gerar resposta
                break;

            case Message.ETB:
                enterTheBus();
                outMessage = new Message (Message.ACK);
                break;

            case Message.ABB:
                announcingBusBoarding();
                outMessage = new Message (Message.ACK);
                break;

            case Message.GS:

                outMessage = new Message (Message.ACK, getSpots());
                break;

            case Message.CS:
                clearSpots();
                outMessage = new Message (Message.ACK);
                break;

            case Message.TERM:
                terminate();
                break;
        }

        return outMessage;
    }
}
