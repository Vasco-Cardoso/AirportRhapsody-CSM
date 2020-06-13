package serverSide;

import java.net.SocketTimeoutException;

public class RecOfMain {

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
                System.out.println("SERVER AIRPORT: AFTER SCONIS");

                sconi_reclaimOffice = scon_reclaimOffice.accept ();                          // entrada em processo de escuta
                cliProxy = new ClientProxy (sconi_reclaimOffice, reclaimOffice);  // lançamento do agente prestador do serviço
                cliProxy.start ();
            }
            catch (SocketTimeoutException e)
            {
                System.out.println(e.getStackTrace());
            }
        }

        scon_reclaimOffice.end ();
    }
}
