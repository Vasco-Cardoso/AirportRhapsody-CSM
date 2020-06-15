package serverSide;

import serverSide.communications.ServerCom;
import serverSide.proxy.ClientProxy;
import serverSide.sharedRegions.LuggageCollectionPoint;

import java.net.SocketTimeoutException;

public class LugColPointMain {

    private static final int portNumb = 22000;
    public static boolean waitConnection;


    public static void main(String[] args) {
        System.out.println("Starting LugColPointMain");

        LuggageCollectionPoint collPoint;

        ServerCom scon_luggageCollectionPoint, sconi_luggageCollectionPoint;

        ClientProxy cliProxy;

        scon_luggageCollectionPoint = new ServerCom (portNumb + 2);       // criação do canal de escuta e sua associação
        scon_luggageCollectionPoint.start ();

        collPoint = new LuggageCollectionPoint();

        waitConnection = true;
        while (waitConnection)
        {
            try
            {
                sconi_luggageCollectionPoint = scon_luggageCollectionPoint.accept ();                          // entrada em processo de escuta
                cliProxy = new ClientProxy (sconi_luggageCollectionPoint, collPoint);  // lançamento do agente prestador do serviço
                cliProxy.start ();
            }
            catch (SocketTimeoutException e)
            {
            }
        }

        scon_luggageCollectionPoint.end ();
    }
}
