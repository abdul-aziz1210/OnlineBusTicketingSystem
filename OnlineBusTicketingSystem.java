// ✅ Full-featured Single File Java Bus Ticketing System

import java.util.*;

class Booking {
    String operator;
    String name;
    int seatNo;
    String phoneNumber;
    String destinationBegin;
    String destinationEnd;
    double fareAmount;
    String paymentMethod;
    String paymentDetails;

    public Booking(String operator, String name, int seatNo, String phoneNumber, String destinationBegin,
                   String destinationEnd, double fareAmount, String paymentMethod, String paymentDetails) {
        this.operator = operator;
        this.name = name;
        this.seatNo = seatNo;
        this.phoneNumber = phoneNumber;
        this.destinationBegin = destinationBegin;
        this.destinationEnd = destinationEnd;
        this.fareAmount = fareAmount;
        this.paymentMethod = paymentMethod;
        this.paymentDetails = paymentDetails;
    }
}

class User {
    String name;
    String id;
    String password;
    List<Booking> bookings = new ArrayList<>();

    public User(String name, String id, String password) {
        this.name = name;
        this.id = id;
        this.password = password;
    }
}

class Admin {
    String name;
    String id;
    String password;
}

public class OnlineBusTicketingSystem {
    static Scanner scanner = new Scanner(System.in);
    static Set<Integer> bookedSeats = new HashSet<>();
    static Map<String, Double> routeFareMap = new HashMap<>();
    static List<String> busOperators = Arrays.asList("Nabil Paribahan", "Hanif Paribahan", "Shohag Paribahan");
    static List<String> districts = Arrays.asList("Dhaka", "Barishal", "Khulna", "Sylhet", "Mymensingh", "Chattogram", "Rangpur", "Rajshahi");
    static Map<String, User> users = new HashMap<>();
    static Map<String, Admin> admins = new HashMap<>();
    static List<Booking> allBookings = new ArrayList<>();
    static User currentUser = null;

    public static void main(String[] args) {
        initializeFares();
        mainMenu();
    }

    static void initializeFares() {
        routeFareMap.put("Dhaka-Barishal", 600.0);
        routeFareMap.put("Dhaka-Rangpur", 1000.0);
        routeFareMap.put("Barishal-Khulna", 700.0);
        routeFareMap.put("Sylhet-Rajshahi", 900.0);
        routeFareMap.put("Mymensingh-Chattogram", 950.0);
        routeFareMap.put("Khulna-Rajshahi", 850.0);
        routeFareMap.put("Dhaka-Sylhet", 750.0);
        routeFareMap.put("Rangpur-Rajshahi", 800.0);
    }

    static void mainMenu() {
        while (true) {
            System.out.println("\n=== ONLINE BUS TICKETING SYSTEM ===");
            System.out.println("1. User Login/Register");
            System.out.println("2. Admin Panel");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt(); scanner.nextLine();
            switch (choice) {
                case 1 -> userLoginOrRegister();
                case 2 -> adminInterface();
                case 3 -> { System.out.println("Thanks for using the system."); return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    static void userLoginOrRegister() {
        System.out.println("\n1. Register\n2. Login");
        System.out.print("Enter choice: ");
        int choice = scanner.nextInt(); scanner.nextLine();
        if (choice == 1) {
            System.out.print("Enter your name: ");
            String name = scanner.nextLine();
            System.out.print("Choose a user ID: ");
            String id = scanner.nextLine();
            if (users.containsKey(id)) {
                System.out.println("User ID already exists."); return;
            }
            System.out.print("Create a password: ");
            String pass = scanner.nextLine();
            User newUser = new User(name, id, pass);
            users.put(id, newUser);
            System.out.println("User registered successfully.");
        } else if (choice == 2) {
            System.out.print("Enter user ID: ");
            String id = scanner.nextLine();
            System.out.print("Enter password: ");
            String pass = scanner.nextLine();
            if (users.containsKey(id) && users.get(id).password.equals(pass)) {
                currentUser = users.get(id);
                userPanel();
            } else {
                System.out.println("Invalid credentials.");
            }
        }
    }

    static void userPanel() {
        while (true) {
            System.out.println("\n--- User Panel ---");
            System.out.println("1. Book Ticket");
            System.out.println("2. View My Bookings");
            System.out.println("3. Cancel My Booking");
            System.out.println("4. Logout");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt(); scanner.nextLine();
            switch (choice) {
                case 1 -> bookTicket();
                case 2 -> viewMyBookings();
                case 3 -> cancelMyBooking();
                case 4 -> { currentUser = null; return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    static void bookTicket() {
        System.out.println("\nSelect Bus Operator:");
        for (int i = 0; i < busOperators.size(); i++)
            System.out.println((i + 1) + ". " + busOperators.get(i));
        int operatorIndex = scanner.nextInt(); scanner.nextLine();
        String operator = busOperators.get(operatorIndex - 1);

        String phoneNumber;
        while (true) {
            System.out.print("Enter phone number (11 digits): ");
            phoneNumber = scanner.nextLine();
            if (phoneNumber.matches("\\d{11}")) break;
            System.out.println("Invalid phone number.");
        }

        displaySeatLayout();
        int seatNo;
        while (true) {
            System.out.print("Enter seat number (1-40): ");
            seatNo = scanner.nextInt(); scanner.nextLine();
            if (!bookedSeats.contains(seatNo) && seatNo >= 1 && seatNo <= 40) break;
            System.out.println("Seat unavailable or invalid.");
        }
        bookedSeats.add(seatNo);

        List<String> availableRoutes = new ArrayList<>(routeFareMap.keySet());
        for (int i = 0; i < availableRoutes.size(); i++)
            System.out.println((i + 1) + ". " + availableRoutes.get(i) + " - Tk " + routeFareMap.get(availableRoutes.get(i)));
        System.out.print("Select route number: ");
        int routeIndex = scanner.nextInt(); scanner.nextLine();
        String selectedRoute = availableRoutes.get(routeIndex - 1);
        String[] parts = selectedRoute.split("-");
        String from = parts[0], to = parts[1];
        double fare = routeFareMap.get(selectedRoute);

        System.out.print("Select payment method (Cash/Card/Mobile): ");
        String method = scanner.nextLine();
        String details = method.equalsIgnoreCase("Card") ? "Paid by Card" : method.equalsIgnoreCase("Mobile") ? "Mobile Payment Done" : "Cash Received";

        Booking booking = new Booking(operator, currentUser.name, seatNo, phoneNumber, from, to, fare, method, details);
        currentUser.bookings.add(booking);
        allBookings.add(booking);
        System.out.println("Ticket booked successfully.");
    }

    static void viewMyBookings() {
        if (currentUser.bookings.isEmpty()) {
            System.out.println("No bookings found."); return;
        }
        for (Booking b : currentUser.bookings) {
            System.out.println("\nSeat: " + b.seatNo + ", From: " + b.destinationBegin + ", To: " + b.destinationEnd + ", Fare: Tk " + b.fareAmount);
        }
    }

    static void cancelMyBooking() {
        viewMyBookings();
        System.out.print("Enter seat number to cancel: ");
        int seat = scanner.nextInt(); scanner.nextLine();
        Booking toRemove = null;
        for (Booking b : currentUser.bookings) {
            if (b.seatNo == seat) {
                toRemove = b; break;
            }
        }
        if (toRemove != null) {
            currentUser.bookings.remove(toRemove);
            allBookings.remove(toRemove);
            bookedSeats.remove(seat);
            System.out.println("Booking canceled.");
        } else {
            System.out.println("Seat not found.");
        }
    }

    static void displaySeatLayout() {
        System.out.println("Available Seats:");
        for (char row = 'A'; row <= 'J'; row++) {
            for (int col = 1; col <= 4; col++) {
                int seatNumber = (row - 'A') * 4 + col;
                String status = bookedSeats.contains(seatNumber) ? "✘" : "✓";
                System.out.print(row + "" + col + "(" + status + ")  ");
            }
            System.out.println();
        }
    }

    static void adminInterface() {
        System.out.println("\n1. Register Admin\n2. Login Admin");
        int choice = scanner.nextInt(); scanner.nextLine();
        if (choice == 1) {
            System.out.print("Enter admin name: ");
            String name = scanner.nextLine();
            System.out.print("Enter admin ID: ");
            String id = scanner.nextLine();
            System.out.print("Enter password: ");
            String pass = scanner.nextLine();
            Admin admin = new Admin(); admin.name = name; admin.id = id; admin.password = pass;
            admins.put(id, admin);
            System.out.println("Admin registered.");
        } else if (choice == 2) {
            System.out.print("Enter ID: "); String id = scanner.nextLine();
            System.out.print("Password: "); String pass = scanner.nextLine();
            if (admins.containsKey(id) && admins.get(id).password.equals(pass)) {
                adminPanel();
            } else {
                System.out.println("Invalid credentials.");
            }
        }
    }

    static void adminPanel() {
        while (true) {
            System.out.println("\n--- Admin Panel ---");
            System.out.println("1. View All Bookings");
            System.out.println("2. Cancel Booking by Phone");
            System.out.println("3. Logout");
            int ch = scanner.nextInt(); scanner.nextLine();
            if (ch == 1) {
                for (Booking b : allBookings) {
                    System.out.println(b.name + " | " + b.phoneNumber + " | " + b.destinationBegin + "->" + b.destinationEnd);
                }
            } else if (ch == 2) {
                System.out.print("Enter phone number: ");
                String phone = scanner.nextLine();
                Booking found = null;
                for (Booking b : allBookings) {
                    if (b.phoneNumber.equals(phone)) {
                        found = b; break;
                    }
                }
                if (found != null) {
                    allBookings.remove(found);
                    bookedSeats.remove(found.seatNo);
                    users.values().forEach(u -> u.bookings.removeIf(b -> b.phoneNumber.equals(phone)));
                    System.out.println("Booking cancelled.");
                } else {
                    System.out.println("Booking not found.");
                }
            } else {
                return;
            }
        }
    }
}
