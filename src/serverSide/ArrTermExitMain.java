package serverSide;

import java.net.SocketTimeoutException;

public class ArrTermExitMain {

    private static final int portNumb = 22000;
    public static boolean waitConnection;


    public static void main(String[] args) {

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
                System.out.println("SERVER AIRPORT: AFTER SCONIS");

                sconi_arrivalTerminalExit = scon_arrivalTerminalExit.accept ();                          // entrada em processo de escuta
                cliProxy = new ClientProxy (sconi_arrivalTerminalExit, arrTermExit);  // lançamento do agente prestador do serviço
                cliProxy.start ();
            }
            catch (SocketTimeoutException e)
            {
                System.out.println(e.getStackTrace());
            }
        }

        scon_arrivalTerminalExit.end ();
    }
}
