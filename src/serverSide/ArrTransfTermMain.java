package serverSide;

import java.net.SocketTimeoutException;

public class ArrTransfTermMain {

    private static final int portNumb = 22000;
    public static boolean waitConnection;


    public static void main(String[] args) {

        ArrivalTransferTerminal arrTransQuay;

        ServerCom scon_arrivalTransferTerminal, sconi_arrivalTransferTerminal;

        ClientProxy cliProxy;

        scon_arrivalTransferTerminal = new ServerCom (portNumb + 4);                     // criação do canal de escuta e sua associação
        scon_arrivalTransferTerminal.start ();

        arrTransQuay = new ArrivalTransferTerminal();;

        waitConnection = true;
        while (waitConnection)
        {
            try
            {
                System.out.println("SERVER AIRPORT: AFTER SCONIS");

                sconi_arrivalTransferTerminal = scon_arrivalTransferTerminal.accept ();                          // entrada em processo de escuta
                cliProxy = new ClientProxy (sconi_arrivalTransferTerminal, arrTransQuay);  // lançamento do agente prestador do serviço
                cliProxy.start ();
            }
            catch (SocketTimeoutException e)
            {
                System.out.println(e.getStackTrace());
            }
        }

        scon_arrivalTransferTerminal.end ();
    }
}
