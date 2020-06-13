package serverSide;

import java.net.SocketTimeoutException;

public class DepTransfTermMain {

    private static final int portNumb = 22000;
    public static boolean waitConnection;


    public static void main(String[] args) {

        DepartureTransferTerminal depTransQuay;

        ServerCom scon_departureTransferTerminal, sconi_departureTransferTerminal;

        ClientProxy cliProxy;

        scon_departureTransferTerminal = new ServerCom (portNumb + 5);                     // criação do canal de escuta e sua associação
        scon_departureTransferTerminal.start ();

        depTransQuay = new DepartureTransferTerminal();

        waitConnection = true;
        while (waitConnection)
        {
            try
            {
                System.out.println("SERVER AIRPORT: AFTER SCONIS");

                sconi_departureTransferTerminal = scon_departureTransferTerminal.accept ();                          // entrada em processo de escuta
                cliProxy = new ClientProxy (sconi_departureTransferTerminal, depTransQuay);  // lançamento do agente prestador do serviço
                cliProxy.start ();
            }
            catch (SocketTimeoutException e)
            {
                System.out.println(e.getStackTrace());
            }
        }

        scon_departureTransferTerminal.end ();
    }
}
