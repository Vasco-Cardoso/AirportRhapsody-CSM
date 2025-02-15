package serverSide.sharedRegions;

import clientSide.entities.Passenger;
import comInf.Message;
import serverSide.DepTransfTermMain;

import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class DepartureTransferTerminal{

    // Locks and conditions
    ReentrantLock lock = new ReentrantLock();
    Condition letPassOff = lock.newCondition();
    Condition lastp = lock.newCondition();


    // Variables
    private int numPassengers;
    private Queue<Passenger> seats;
    private boolean canleave = false;

    //Constructor
    public DepartureTransferTerminal() {
        this.numPassengers = 0;
    }

    /**
     * Method used to leave this terminal whenever possible.
     */
    public void leaveDepartureTransferTerminal(){
        lock.lock();
        try
        {
            while(!canleave);
            this.numPassengers--;
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
     * Method that waits for the signal given by the bus driver to leave the bus. Whenever the last
     * passenger leaves the bus he must notice everyone else of that so everyone can leave to the arrival
     * terminal.
     */
    public void leaveTheBus()
    {
        lock.lock();
        try {
            letPassOff.await();

            this.numPassengers++;

            // Checks if he is the last passenger to he can signal others.
            if(this.numPassengers == this.seats.size())
            {
                lastp.signal();
                canleave = true;
            }
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
     * Method that signals all the passengers that they are free to leave the bus.
     * @param seats corresponding to the Queue of passengers currently on the bus
     */
    public void parkTheBusAndLetPassOff(Queue<Passenger> seats){
        lock.lock();
        try
        {
            this.seats = seats;
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
     * Method that signals passengers to leave the bus and waits for the last one to leave.
     */
    public void goToArrivalTerminal(){
        lock.lock();
        try{
            letPassOff.signalAll();
            lastp.await();
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

    public Queue<Passenger> getSeats() {
        return seats;
    }

    public void setCanleave(){

        this.canleave = false;
    }

    public void terminate(){
        DepTransfTermMain.waitConnection = false;
        System.exit(1);
    }

    public Message processAndReply(Message inMessage) {

        Message outMessage = null;                           // mensagem de resposta

        switch (inMessage.getType ()){

            case Message.LDTT:
                leaveDepartureTransferTerminal();
                outMessage = new Message (Message.ACK);       // gerar resposta
                break;

            case Message.LTB:
                leaveTheBus();
                outMessage = new Message (Message.ACK);
                break;

            case Message.PTBALPO:
                parkTheBusAndLetPassOff(inMessage.getSeats());
                outMessage = new Message (Message.ACK);
                break;

            case Message.GTAT:
                goToArrivalTerminal();
                outMessage = new Message (Message.ACK);
                break;

            case Message.SCL:
                setCanleave();
                outMessage = new Message (Message.ACK);
                break;

            case Message.TERM:
                terminate();
                break;
        }

        return outMessage;
    }
}
