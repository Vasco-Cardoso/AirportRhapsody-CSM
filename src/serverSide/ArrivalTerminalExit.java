package serverSide;

import comInf.Luggages;
import comInf.Message;

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

        Message outMessage = null;                           // mensagem de resposta
        Luggages l = null;

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

            case Message.GNP:
                outMessage = new Message (Message.ACK, getNumPassengers());
                break;

            case Message.SE:
                setEmpty();
                outMessage = new Message (Message.ACK);
                break;
        }

        return outMessage;
    }
}
