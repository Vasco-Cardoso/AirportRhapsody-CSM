package serverSide;

import serverSide.communications.ServerCom;
import serverSide.proxy.ClientProxy;
import serverSide.sharedRegions.ReclaimOffice;

import java.net.SocketTimeoutException;

public class ReclaimOfficeMain {

    private static final int portNumb = 22000;
    public static boolean waitConnection;


    public static void main(String[] args) {

        ReclaimOffice reclaimOffice;

        ServerCom scon_reclaimOffice, sconi_reclaimOffice;

        ClientProxy cliProxy;

        scon_reclaimOffice = new ServerCom (portNumb + 3);                     // criação do canal de escuta e sua associação
        scon_reclaimOffice.start ();

        reclaimOffice = new ReclaimOffice();

        waitConnection = true;
        while (waitConnection)
        {
            try
            {
                sconi_reclaimOffice = scon_reclaimOffice.accept ();                          // entrada em processo de escuta
                cliProxy = new ClientProxy (sconi_reclaimOffice, reclaimOffice);  // lançamento do agente prestador do serviço
                cliProxy.start ();
            }
            catch (SocketTimeoutException e)
            {
            }
        }

        scon_reclaimOffice.end ();
    }
}
