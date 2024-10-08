import java.util.ArrayList;
import java.util.Scanner;

// Class to represent train information
class Train {
    String name;
    String time;
    int passengerStrength;
    String trainNumber;
    String sourceStation;
    String destinationStation;
    ArrayList<String> availableSeats;

    public Train(String name, String time, int passengerStrength, String trainNumber, String sourceStation, String destinationStation) {
        this.name = name;
        this.time = time;
        this.passengerStrength = passengerStrength;
        this.trainNumber = trainNumber;
        this.sourceStation = sourceStation;
        this.destinationStation = destinationStation;
        this.availableSeats = initializeSeats(passengerStrength);
    }

    private ArrayList<String> initializeSeats(int strength) {
        ArrayList<String> seats = new ArrayList<>();
        for (int i = 1; i <= strength; i++) {
            seats.add("S" + i); // Assign seat numbers as S1, S2, S3, ...
        }
        return seats;
    }
}

// Class to represent ticket details
class Ticket {
    String passengerName;
    String seatNumber;
    Train train;
    int fare;

    public Ticket(String passengerName, String seatNumber, Train train, int fare) {
        this.passengerName = passengerName;
        this.seatNumber = seatNumber;
        this.train = train;
        this.fare = fare;
    }

    public void displayTicket() {
        System.out.println("Ticket Details:");
        System.out.println("Train: " + train.name);
        System.out.println("Passenger Name: " + passengerName);
        System.out.println("Seat Number: " + seatNumber);
        System.out.println("Train Number: " + train.trainNumber);
        System.out.println("Fare: " + fare);
    }
}

// Class to manage reservations
class Reservation {
    ArrayList<Train> trains;
    ArrayList<Ticket> bookedTickets;
    int fare = 500; // Fixed fare for simplicity

    public Reservation() {
        trains = new ArrayList<>();
        bookedTickets = new ArrayList<>();
        initializeTrains();
    }

    private void initializeTrains() {
        trains.add(new Train("Mumbai - Delhi", "13:05", 50, "1010", "Mumbai", "Delhi"));
        trains.add(new Train("Delhi - Jaipur", "07:00", 50, "2013", "Delhi", "Jaipur"));
        trains.add(new Train("Prayagraj - Delhi", "10:00", 50, "3045", "Prayagraj", "Delhi"));
    }

    public void showTrainInfo(String source, String destination) {
        System.out.println("Available Trains from " + source + " to " + destination + ":");
        boolean trainFound = false; // Flag to check if any train was found
        for (Train train : trains) {
            if (train.sourceStation.equalsIgnoreCase(source) && train.destinationStation.equalsIgnoreCase(destination)) {
                System.out.println(train.name + " | Time: " + train.time + " | Train Number: " + train.trainNumber);
                trainFound = true; // Set flag to true if a matching train is found
            }
        }
        if (!trainFound) {
            System.out.println("No trains available from " + source + " to " + destination + ".");
        }
    }

    public void bookTicket(String passengerName, Train train) {
        if (train.availableSeats.isEmpty()) {
            System.out.println("No available seats on this train.");
            return;
        }

        String seatNumber = train.availableSeats.remove(0); // Automatically assign the next available seat
        Ticket ticket = new Ticket(passengerName, seatNumber, train, fare);
        bookedTickets.add(ticket);
        System.out.println("Ticket booked successfully!");
        ticket.displayTicket();
    }

    public void cancelTicket(String passengerName) {
        boolean ticketFound = false;
        for (Ticket ticket : bookedTickets) {
            if (ticket.passengerName.equalsIgnoreCase(passengerName)) {
                bookedTickets.remove(ticket);
                ticketFound = true;

                // Return the seat to the train
                for (Train train : trains) {
                    if (train.trainNumber.equals(ticket.train.trainNumber)) {
                        train.availableSeats.add(ticket.seatNumber); // Re-add the seat to available seats
                        break;
                    }
                }

                System.out.println("Ticket cancelled successfully!");
                break; // Exit the loop after removing the ticket
            }
        }
        if (!ticketFound) {
            System.out.println("No ticket found for the provided passenger name.");
        }
    }

    public void displayBookedTickets() {
        if (bookedTickets.isEmpty()) {
            System.out.println("No tickets booked.");
            return;
        }
        for (Ticket ticket : bookedTickets) {
            ticket.displayTicket();
        }
    }
}

// Main class to run the Railway Reservation System
public class RailwayReservationSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Reservation reservation = new Reservation();

        while (true) {
            System.out.println("\nRailway Reservation System");
            System.out.println("1. Show Train Information");
            System.out.println("2. Book Ticket");
            System.out.println("3. Cancel Ticket");
            System.out.println("4. Display Booked Tickets");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter Source Station: ");
                    String source = scanner.nextLine();
                    System.out.print("Enter Destination Station: ");
                    String destination = scanner.nextLine();
                    reservation.showTrainInfo(source, destination);
                    break;

                case 2:
                    System.out.print("Enter Passenger Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter Train Number: ");
                    String trainNumber = scanner.nextLine();
                    Train selectedTrain = null;
                    for (Train train : reservation.trains) {
                        if (train.trainNumber.equals(trainNumber)) {
                            selectedTrain = train;
                            break;
                        }
                    }
                    if (selectedTrain != null) {
                        reservation.bookTicket(name, selectedTrain);
                    } else {
                        System.out.println("Invalid Train Number.");
                    }
                    break;

                case 3:
                    System.out.print("Enter Passenger Name to Cancel Ticket: ");
                    String cancelName = scanner.nextLine();
                    reservation.cancelTicket(cancelName);
                    break;

                case 4:
                    reservation.displayBookedTickets();
                    break;

                case 5:
                    System.out.println("Thank you for using Railway Reservation System!");
                    scanner.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
