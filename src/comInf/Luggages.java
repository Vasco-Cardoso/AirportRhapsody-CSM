package comInf;

import clientSide.Passenger;

import java.io.Serializable;
import java.util.Objects;

public class Luggages implements Serializable {

    // Variáveis
    private Passenger passenger;
    private int luggage_id;

    // Construtores, setters e getters

    public Luggages(Passenger passenger_id) {
        this.passenger = passenger_id;
    }

    public Passenger getPassenger() {
        return this.passenger;
    }

    public int getLuggage_id() {
        return this.luggage_id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(passenger, luggage_id);
    }

    @Override
    public String toString() {
        return "{" +
            " passenger_id='" + getPassenger() + "'" +
            ", luggage_id='" + getLuggage_id() + "'" +
            "}";
    }
}

   