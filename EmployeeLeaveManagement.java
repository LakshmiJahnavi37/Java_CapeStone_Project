import java.util.Scanner;

class EmployeeLeaveSystem {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // Registration details
        String username = "";
        String password = "";
        String role = "";              // Employee, Team Lead, HOD, Manager, Director
        boolean isRegistered = false;
//login 
        boolean isLoggedIn = false;

        // Leave details
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
            sc.nextLine(); // clear buffer

            switch (choice) {

                // ---------------- REGISTER ----------------
                case 1:
                    if (isRegistered) {
                        System.out.println("User already registered.");
                    } else {
                        System.out.print("Create Username: ");
                        username = sc.nextLine();

                        System.out.print("Create Password: ");
                        password = sc.nextLine();

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

                        isRegistered = true;
                        System.out.println("Registration Successful.");
                        System.out.println("Registered Role: " + role);
                    }
                    break;

                // ---------------- LOGIN ----------------
                case 2:
                    if (!isRegistered) {
                        System.out.println("Please register first.");
                    } else {
                        System.out.print("Enter Username: ");
                        String u = sc.nextLine();

                        System.out.print("Enter Password: ");
                        String p = sc.nextLine();

                        if (u.equals(username) && p.equals(password)) {
                            isLoggedIn = true;
                            System.out.println("Login Successful.");
                        } else {
                            System.out.println("Invalid Login Details.");
                        }
                    }
                    break;

                // ---------------- VIEW PROFILE ----------------
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

                // ---------------- APPLY LEAVE ----------------
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

                        // Approval logic
                        if (role.equals("Employee") || role.equals("Team Lead")) {
                            leaveStatus = "Pending";      // Needs Admin approval
                        } else {
                            leaveStatus = "Approved";     // Higher authority
                        }

                        leaveApplied = true;

                        System.out.println("Leave Applied Successfully.");
                        System.out.println("Applied By : " + leaveAppliedBy);
                        System.out.println("Status     : " + leaveStatus);
                    }
                    break;

                // ---------------- VIEW LEAVE STATUS ----------------
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

                // ---------------- ADMIN APPROVAL ----------------
                case 6:
                    if (!leaveApplied) {
                        System.out.println("No leave request available.");
                    } 
                    else if (leaveAppliedBy.equals("HOD") ||
                             leaveAppliedBy.equals("Manager") ||
                             leaveAppliedBy.equals("Director")) {

                        System.out.println("Leave already auto-approved (Higher Authority).");

                    } 
                    else if (!leaveStatus.equals("Pending")) {
                        System.out.println("Leave already processed.");
                    } 
                    else {
                        System.out.println("----- Admin Approval -----");
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
                            System.out.println("Leave Approved by Admin.");
                        } else if (adminChoice == 2) {
                            leaveStatus = "Rejected";
                            System.out.println("Leave Rejected by Admin.");
                        } else {
                            System.out.println("Invalid choice.");
                        }
                    }
                    break;

                // ---------------- LOGOUT ----------------
                case 7:
                    if (isLoggedIn) {
                        isLoggedIn = false;
                        System.out.println("Logged out successfully.");
                    } else {
                        System.out.println("You are not logged in.");
                    }
                    break;

                // ---------------- EXIT ----------------
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
