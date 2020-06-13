package serverSide;

import java.net.SocketTimeoutException;

public class ArrLoungeMain {


    private static final int portNumb = 22000;
    public static boolean waitConnection;


    public static void main(String[] args) {

        ArrivalLounge arrivalLounge;

        ServerCom scon_arrivalLounge, sconi_arrivalLounge;

        ClientProxy cliProxy;

        scon_arrivalLounge = new ServerCom (portNumb + 1);       // criação do canal de escuta e sua associação
        scon_arrivalLounge.start ();

        arrivalLounge = new ArrivalLounge();;

        waitConnection = true;
        while (waitConnection)
        {
            try
            {
                System.out.println("SERVER AIRPORT: AFTER SCONIS");

                sconi_arrivalLounge = scon_arrivalLounge.accept ();                          // entrada em processo de escuta
                cliProxy = new ClientProxy (sconi_arrivalLounge, arrivalLounge);  // lançamento do agente prestador do serviço
                cliProxy.start ();
            }
            catch (SocketTimeoutException e)
            {
                System.out.println(e.getStackTrace());
            }
        }

        scon_arrivalLounge.end ();
    }
}
