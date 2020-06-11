package serverSide;

import comInf.Message;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class DepartureTerminalEntry{

    // Locks and conditions
    ReentrantLock lock = new ReentrantLock();
    Condition lastp = lock.newCondition();

    // Variables
    private int numPassengers;

    //Constructor
    public DepartureTerminalEntry() {
        this.numPassengers = 0;
    }

    public void arrivedTerminal(){
        lock.lock();
        try {
            this.numPassengers++;
        }
        catch (Exception e)
        {

        }
        finally
        {
            lock.unlock();
        }
    }

    /**
     * Method that awaits the last passenger to arrive the
     * serverSide.DepartureTerminalEntry (Uses lock condition).
     */
    public void waitLastPassenger(){
        lock.lock();
        try
        {
            lastp.await();
        }
        catch (Exception e)
        {

        }
        finally
        {
            lock.unlock();
        }
    }

    /**
     * Method called by the last passenger to arrive the terminal
     * and that signals every other passenger to proceed.
     */
    public void lastPassenger() {
        lock.lock();
        try
        {
            lastp.signalAll();
        }
        catch (Exception e)
        {

        }
        finally
        {
            lock.unlock();
        }
    }

    public int getNumPassengers() {
        return numPassengers;
    }

    /**
     * Method that sets the terminal empty. Called in the end of each flight.
     */
    public void setEmpty()
    {
        lock.lock();
        try
        {
            this.numPassengers = 0;
        }
        catch (Exception e)
        {

        }
        finally {
            lock.unlock();
        }
    }

    public Message processAndReply(Message inMessage) {

        Message outMessage = null;                           // mensagem de resposta

        switch (inMessage.getType ()){

            case Message.AT:
                arrivedTerminal();
                outMessage = new Message (Message.ACK);       // gerar resposta
                break;

            case Message.WLP:
                waitLastPassenger();
                outMessage = new Message (Message.ACK);
                break;

            case Message.LP:
                lastPassenger();
                outMessage = new Message (Message.ACK);
                break;
        }

        return outMessage;
    }
}
