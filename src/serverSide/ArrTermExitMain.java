package serverSide;

import serverSide.communications.ServerCom;
import serverSide.proxy.ClientProxy;
import serverSide.sharedRegions.ArrivalTerminalExit;

import java.net.SocketTimeoutException;

public class ArrTermExitMain {

    private static final int portNumb = 22000;
    public static boolean waitConnection;


    public static void main(String[] args) {
        System.out.println("Starting ArrTermExitMain");

        ArrivalTerminalExit arrTermExit;

        ServerCom scon_arrivalTerminalExit, sconi_arrivalTerminalExit;

        ClientProxy cliProxy;

        scon_arrivalTerminalExit = new ServerCom (portNumb + 6);       // criação do canal de escuta e sua associação
        scon_arrivalTerminalExit.start ();

        arrTermExit = new ArrivalTerminalExit();;

        waitConnection = true;
        while (waitConnection)
        {
            try
            {
                sconi_arrivalTerminalExit = scon_arrivalTerminalExit.accept ();                          // entrada em processo de escuta
                cliProxy = new ClientProxy (sconi_arrivalTerminalExit, arrTermExit);  // lançamento do agente prestador do serviço
                cliProxy.start ();
            }
            catch (SocketTimeoutException e)
            {
            }
        }

        scon_arrivalTerminalExit.end ();
    }
}
