package serverSide;

import comInf.Message;

import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ArrivalTerminalExit  {

    // Locks and conditions
    ReentrantLock lock = new ReentrantLock();
    Condition lastp = lock.newCondition();

    // Variables
    private int numPassengers;

    // Methods

    public ArrivalTerminalExit()
    {
        this.numPassengers = 0;
    }

    /**
     * A new passenger arrives the terminal and the variable numPassengers gets increased.
     */
    public void arrivedTerminal()
    {
        lock.lock();
        try
        {
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
     * Method that blocks the passenger while he waits for the last passenger to arrive to
     * this terminal, so everyone can leave together.
     */
    public void waitLastPassenger()
    {
        lock.lock();
        try
        {
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

    /**
     * Method that gets called by the last passenger to arrive this terminal and sets every other one
     * free to precede with their life (in this case end his life cycle).
     */
    public void lastPassenger()
    {
       lock.lock();
       try
       {
           lastp.signalAll();
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
            System.out.println(e);
        }
        finally {
            lock.unlock();
        }
    }

    public Message processAndReply(Message inMessage) {
    }
}
