import java.util.Scanner;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.*;
import org.bson.Document;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EmployeeLeaveManagement {

    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        MongoDatabase db = MongoDBConnection.getDatabase();
        MongoCollection<Document> users = db.getCollection("users");

        String username = "";
        String role = "";
        boolean isRegistered = false;
        boolean isLoggedIn = false;

        int leaveDays = 0;
        String leaveReason = "";
        String leaveAppliedBy = "";
        String leaveStatus = "Not Applied";
        boolean leaveApplied = false;

        int choice;

        do {
            System.out.println("\n==============================");
            System.out.println(" Employee Leave Management ");
            System.out.println("==============================");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. View Profile");
            System.out.println("4. Apply Leave");
            System.out.println("5. View Leave Status");
            System.out.println("6. Admin Approval");
            System.out.println("7. Logout");
            System.out.println("8. Exit");
            System.out.print("Enter your choice: ");

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

               
                case 1:
                    System.out.print("Create Username: ");
                    username = sc.nextLine();

                    System.out.print("Create Password: ");
                    String password = sc.nextLine();

                    System.out.println("Register As:");
                    System.out.println("1. Employee");
                    System.out.println("2. Team Lead");
                    System.out.println("3. HOD");
                    System.out.println("4. Manager");
                    System.out.println("5. Director");
                    System.out.print("Enter choice: ");

                    int roleChoice = sc.nextInt();
                    sc.nextLine();

                    switch (roleChoice) {
                        case 1: role = "Employee"; break;
                        case 2: role = "Team Lead"; break;
                        case 3: role = "HOD"; break;
                        case 4: role = "Manager"; break;
                        case 5: role = "Director"; break;
                        default: role = "Employee";
                    }

                    String hashedPassword = hashPassword(password);

                    Document user = new Document("username", username)
                            .append("password", hashedPassword)
                            .append("role", role);

                    users.insertOne(user);

                    isRegistered = true;
                    System.out.println("Registration Successful & Saved in MongoDB.");
                    break;

               
                case 2:
                    System.out.print("Enter Username: ");
                    String u = sc.nextLine();

                    System.out.print("Enter Password: ");
                    String p = sc.nextLine();

                    String hashedInput = hashPassword(p);

                    Document foundUser = users.find(
                            and(eq("username", u), eq("password", hashedInput))
                    ).first();

                    if (foundUser != null) {
                        isLoggedIn = true;
                        username = foundUser.getString("username");
                        role = foundUser.getString("role");
                        System.out.println("Login Successful.");
                    } else {
                        System.out.println("Invalid Login Details.");
                    }
                    break;

                case 3:
                    if (isLoggedIn) {
                        System.out.println("----- Profile -----");
                        System.out.println("Username : " + username);
                        System.out.println("Role     : " + role);
                        System.out.println("Leave Status : " + leaveStatus);
                    } else {
                        System.out.println("Please login first.");
                    }
                    break;

                case 4:
                    if (!isLoggedIn) {
                        System.out.println("Please login first.");
                    } else if (leaveApplied) {
                        System.out.println("Leave already applied.");
                    } else {
                        System.out.print("Enter Leave Days: ");
                        leaveDays = sc.nextInt();
                        sc.nextLine();

                        System.out.print("Enter Leave Reason: ");
                        leaveReason = sc.nextLine();

                        leaveAppliedBy = role;

                        if (role.equals("Employee") || role.equals("Team Lead")) {
                            leaveStatus = "Pending";
                        } else {
                            leaveStatus = "Approved";
                        }

                        leaveApplied = true;

                        System.out.println("Leave Applied Successfully.");
                        System.out.println("Applied By : " + leaveAppliedBy);
                        System.out.println("Status     : " + leaveStatus);
                    }
                    break;

                case 5:
                    if (!leaveApplied) {
                        System.out.println("No leave applied yet.");
                    } else {
                        System.out.println("----- Leave Details -----");
                        System.out.println("Applied By : " + leaveAppliedBy);
                        System.out.println("Days       : " + leaveDays);
                        System.out.println("Reason     : " + leaveReason);
                        System.out.println("Status     : " + leaveStatus);
                    }
                    break;

                case 6:

    if (!isLoggedIn) {
        System.out.println("Please login first.");
        break;
    }

    if (!leaveApplied) {
        System.out.println("No leave request available.");
        break;
    }

    if (!(role.equals("HOD") || role.equals("Manager") || role.equals("Director"))) {
        System.out.println("Only HOD / Manager / Director can approve or reject leave.");
        break;
    }

    if (!leaveStatus.equals("Pending")) {
        System.out.println("Leave already processed.");
        break;
    }

    System.out.println("----- Approval Section -----");
    System.out.println("Applied By : " + leaveAppliedBy);
    System.out.println("Days       : " + leaveDays);
    System.out.println("Reason     : " + leaveReason);

    System.out.println("1. Approve Leave");
    System.out.println("2. Reject Leave");
    System.out.print("Enter choice: ");

    int adminChoice = sc.nextInt();
    sc.nextLine();

    if (adminChoice == 1) {
        leaveStatus = "Approved";
        System.out.println("Leave Approved by " + role);
    } else if (adminChoice == 2) {
        leaveStatus = "Rejected";
        System.out.println("Leave Rejected by " + role);
    } else {
        System.out.println("Invalid choice.");
    }

    break;

                case 7:
                    if (isLoggedIn) {
                        isLoggedIn = false;
                        System.out.println("Logged out successfully.");
                    } else {
                        System.out.println("You are not logged in.");
                    }
                    break;

              
                case 8:
                    System.out.println("Exiting System...");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 8);

        sc.close();
    }
}
