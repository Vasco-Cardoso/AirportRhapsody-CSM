package serverSide;

import serverSide.communications.ServerCom;
import serverSide.proxy.ClientProxy;
import serverSide.sharedRegions.DepartureTerminalEntry;

import java.net.SocketTimeoutException;

public class DepTermEntMain {

    private static final int portNumb = 22000;
    public static boolean waitConnection;


    public static void main(String[] args) {

        DepartureTerminalEntry depTermEntrance;

        ServerCom scon_departureTerminalEntry, sconi_departureTerminalEntry;

        ClientProxy cliProxy;

        scon_departureTerminalEntry = new ServerCom (portNumb + 7);                     // criação do canal de escuta e sua associação
        scon_departureTerminalEntry.start ();

        depTermEntrance = new DepartureTerminalEntry();

        waitConnection = true;
        while (waitConnection)
        {
            try
            {
                sconi_departureTerminalEntry = scon_departureTerminalEntry.accept ();                          // entrada em processo de escuta
                cliProxy = new ClientProxy (sconi_departureTerminalEntry, depTermEntrance);  // lançamento do agente prestador do serviço
                cliProxy.start ();
            }
            catch (SocketTimeoutException e)
            {
            }
        }

        scon_departureTerminalEntry.end ();
    }
}
