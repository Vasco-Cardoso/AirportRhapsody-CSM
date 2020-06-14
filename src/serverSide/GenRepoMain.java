package serverSide;

import serverSide.communications.ServerCom;
import serverSide.proxy.ClientProxy;
import serverSide.repository.GeneralRepository;

import java.net.SocketTimeoutException;

public class GenRepoMain {

    private static final int portNumb = 22000;
    public static boolean waitConnection;
    public static int nPassengers = 6 ;
    public static int nSeatingPlaces = 3;


    public static void main(String[] args) {

        GeneralRepository genRepo;

        ServerCom scon_genRepo, sconi_genRepo;

        ClientProxy cliProxy;

        scon_genRepo = new ServerCom (portNumb + 9);       // criação do canal de escuta e sua associação
        scon_genRepo.start ();

        genRepo = new GeneralRepository(nSeatingPlaces, nPassengers, 6,"Report.txt");

        waitConnection = true;
        while (waitConnection)
        {
            try
            {
                sconi_genRepo = scon_genRepo.accept ();                          // entrada em processo de escuta
                cliProxy = new ClientProxy (sconi_genRepo, genRepo);  // lançamento do agente prestador do serviço
                cliProxy.start ();
            }
            catch (SocketTimeoutException e)
            {
            }
        }

        scon_genRepo.end ();
    }
}
