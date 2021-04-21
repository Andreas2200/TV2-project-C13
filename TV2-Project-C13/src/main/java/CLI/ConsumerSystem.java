package CLI;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ConsumerSystem
{
    private static String userDB = "userDB.txt";
    private static String programDB = "programDB.txt";
    private static String creditDB = "creditDB.txt";
    private static String linkedCreditDB = "linkedCreditDB.txt";

    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Program> programs = new ArrayList<>();
    private ArrayList<Credit> credits = new ArrayList<>();

    private HashMap<Credit,Integer> linkedCredits = new HashMap<>();

    private boolean running = true;
    private boolean loggedIn = false;

    private User activeUser = null;

    private Scanner scanner = new Scanner(System.in);

    public void runApp()
    {
        loadUsers();
        loadPrograms();
        loadCredits();
        homeText();
        while (running)
        {
            if(loggedIn)
            {
                if(activeUser.getRole().equals("User"))
                {
                    textLoggedInUser();
                    switch (scanner.nextInt())
                    {
                        case 1:
                            System.out.println(showPrograms()); //Show programs
                            break;
                        case 2:
                            changeUserInfo(); //Change user info
                            break;
                        case 3:
                            logOut();
                            break;
                        default:
                            System.out.println("Command not recognized");
                            break;
                    }
                }
                if(activeUser != null && activeUser.getRole().equals("Admin"))
                {
                    textLoggedInAdmin();
                    switch (scanner.nextInt())
                    {
                        case 1:
                            System.out.println(showPrograms()); //Show programs
                            break;
                        case 2:
                            changeUserInfo(); //Change user info
                            break;
                        case 3:
                            addProgram(); //Add program
                            break;
                        case 4:
                            addCredit(); //Add credit to program
                            break;
                        case 5:
                            logOut();
                            break;
                        default:
                            System.out.println("Command not recognized");
                            break;
                    }
                }
            }
            else
            {
                textFrontPage();
                switch (scanner.next())
                {
                    case "1":
                        loggedIn = logIn();
                        break;
                    case "2":
                        createUser();
                        break;
                    case "3":
                        forgottenPassword();
                        break;
                    case "4":
                        exitProgram();
                        break;
                    default:
                        System.out.println("Command not recognized");
                        break;
                }
            }
        }
    }

    private void loadUsers()
    {
        try(Scanner reader = new Scanner(new File(userDB),StandardCharsets.UTF_8))
        {
            while(reader.hasNextLine())
            {
                String[] tempStringArray = reader.nextLine().split(";");
                users.add(new User(tempStringArray[0],tempStringArray[1],tempStringArray[2],Integer.parseInt(tempStringArray[3]),tempStringArray[4]));
            }
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    private void saveUser(User user)
    {
        try(FileWriter fileWriter = new FileWriter(userDB, StandardCharsets.UTF_8, true))
        {
            fileWriter.append(user.toString() + "\n");
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    private void updateUsers()
    {
        try(FileWriter fileWriter = new FileWriter(userDB, StandardCharsets.UTF_8))
        {
            for (User user: users)
            {
                fileWriter.write(user.toString() + "\n");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void loadPrograms()
    {
        try(Scanner reader = new Scanner(new File(programDB),StandardCharsets.UTF_8))
        {
            while(reader.hasNextLine())
            {
                String[] tempStringArray = reader.nextLine().split(";");
                //programs.add(new Program(tempStringArray[0],tempStringArray[1],tempStringArray[2]));
            }
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    private void saveProgram(Program program)
    {
        try(FileWriter fileWriter = new FileWriter(programDB, StandardCharsets.UTF_8, true))
        {
            fileWriter.append(program.toSaveFile() + "\n");
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    private void loadCredits()
    {
        try(Scanner reader = new Scanner(new File(creditDB),StandardCharsets.UTF_8))
        {
            while(reader.hasNextLine())
            {
                String[] tempStringArray = reader.nextLine().split(";");
                credits.add(new Credit(tempStringArray[0],tempStringArray[1]));
            }
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    private void saveCredit(Credit credit)
    {
        try(FileWriter fileWriter = new FileWriter(creditDB, StandardCharsets.UTF_8, true))
        {
            fileWriter.append(credit.toString() + "\n");
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    private void saveLinkedCredit(Credit credit, int programId)
    {
        try(FileWriter fileWriter = new FileWriter(linkedCreditDB, StandardCharsets.UTF_8, true))
        {
            String appendString = "" + credit.toString() + ";" + programId + ";";
            fileWriter.append(appendString);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    private void homeText()
    {
        System.out.println("Welcome to the TV2 credit system PROTOTYPE \nYou have the following options:");
    }

    private void textLoggedInUser()
    {
        System.out.print("\nWelcome "+ activeUser.getName() +": \n" +
                "1: Programs \n" +
                "2: Change info \n" +
                "3: logout \n" +
                ">");
    }

    private void textLoggedInAdmin()
    {
        System.out.print("\nMain page: \n" +
                "1: Programs \n" +
                "2: Change info \n" +
                "3: Add program \n" +
                "4: Add credit to program \n" +
                "5: logout \n" +
                ">");
    }

    private void textFrontPage()
    {
        System.out.print("\nLogin:\n" +
                "1: Login \n" +
                "2: Create user \n" +
                "3: Reset password \n" +
                "4: Exit program \n" +
                ">");
    }

    private void exitProgram()
    {
        System.out.print("Exit program? \n" +
                "1: Yes. \n" +
                "2: No. \n" +
                ">");
        switch (scanner.nextInt())
        {
            case 1:
                running = false;
                break;
            default:
                break;
        }
    }

    private void createUser()
    {
        System.out.print("Name: ");
        String tempName = scanner.next();
        System.out.print("Username: ");
        String tempUsername = scanner.next();
        System.out.print("Password: ");
        String tempPassword = scanner.next();
        System.out.print("Age: ");
        int tempAge = scanner.nextInt();

        if(checkUserExits(new User(tempName,tempUsername,tempPassword,tempAge)))
        {
            System.out.println("Username taken. Choose another");
        }
        else
        {
            User tempUser = new User(tempName,tempUsername,tempPassword,tempAge);
            users.add(tempUser);
            saveUser(tempUser);
        }
    }

    private void logOut()
    {
        activeUser = null;
        loggedIn = false;
        System.out.println("Successfully logged out");
    }

    private void forgottenPassword()
    {
        boolean isInfoChanged = false;
        System.out.print("Enter username: ");
        String tempUsername = scanner.next();
        System.out.print("Enter name: ");
        String tempName = scanner.next();
        System.out.print("Enter age: ");
        int tempAge = scanner.nextInt();

        for (User user: users)
        {
            if(user.checkInfo(tempName,tempUsername.toLowerCase(),tempAge))
            {
                System.out.print("Type in new password: ");
                user.setPassword(scanner.next());
                System.out.println("Changed password");
                isInfoChanged = true;
                updateUsers();
                break;
            }
        }
        if(!isInfoChanged)
        {
            System.out.println("No users found");
        }
    }

    private boolean checkUserExits(User newUser)
    {
        for (User user: users)
        {
            if(user.getUsername().equals(newUser.getUsername()))
            {
                return true;
            }
        }
        return false;
    }

    private boolean logIn()
    {
        System.out.print("Username: ");
        String tempUsername = scanner.next();
        System.out.print("Password: ");
        String tempPass = scanner.next();

        for (User user:users)
        {
            if(user.getUsername().equals(tempUsername.toLowerCase()))
            {
                if(user.getPassword().equals(tempPass))
                {
                    activeUser = user;
                    System.out.print("Logged in successfully");
                    return true;
                }
            }
        }
        if(activeUser == null)
        {
            System.out.println("Invalid username or password");
        }
        return false;
    }

    private String showPrograms() {
        String returnString = "";

        if (programs.size() > 0) {
            for (Program program : programs) {
                returnString += program.toString();
            }
        } else {
            returnString += "No programs added";
        }

        return returnString;
    }

    private void changeUserInfo()
    {
        if(activeUser.getRole().equals("Admin"))
        {
            textChangeUserInfoAdmin();
            switch (scanner.nextInt())
            {
                case 1:
                    changeName();
                    break;
                case 2:
                    changePassword();
                    break;
                case 3:
                    changeUserRole();
                    break;
                default:
                    System.out.println("Command not recognized");
                    break;

            }
        }
        else
        {
            textChangeUserInfoUser();
            switch (scanner.nextInt())
            {
                case 1:
                    changeName();
                    break;
                case 2:
                    changePassword();
                    break;
                default:
                    System.out.println("Command not recognized");
                    break;
            }
        }
    }

    private void textChangeUserInfoAdmin()
    {
        System.out.print("1: Change name \n" +
                "2: change password \n" +
                "3: change role \n" +
                ">");
    }

    private void textChangeUserInfoUser()
    {
        System.out.print("1: Change name \n" +
                "2: change password \n" +
                ">");
    }

    private void changeName()
    {
        System.out.print("New name: ");
        String tempName = scanner.next();
        System.out.print("Are you sure you want to change your name to " + tempName + "? y/n");
        switch (scanner.next())
        {
            case "y":
                activeUser.changeName(tempName);
                updateUsers();
                break;
            case "n":
                break;
            default:
                System.out.print("Are you sure you want to change your name to " + tempName + "? y/n");
                break;
        }
    }

    private void changePassword()
    {
        System.out.print("New password: ");
        String tempPass = scanner.next();
        System.out.print("Are you sure you want to change your name to " + tempPass + "? y/n");
        switch (scanner.next())
        {
            case "y":
                activeUser.setPassword(tempPass);
                updateUsers();
                break;
            case "n":
                break;
            default:
                System.out.print("Are you sure you want to change your name to " + tempPass + "? y/n");
                break;
        }
    }

    private void changeUserRole()
    {
        System.out.println("Roles: Admin, User");
        System.out.println("Users:");

        for (int i = 0; i < users.size(); i++)
        {
            System.out.println(i+1+ ": \n" +
                    "Username: " + users.get(i).getUsername()+"\n" +
                    "Role: " + users.get(i).getRole() + "\n");
        }
        System.out.print("Who do you want to change?");
        User tempUser = users.get(scanner.nextInt()-1);
        System.out.print("New role: ");
        tempUser.setRole(scanner.next());
        System.out.println(tempUser.getUsername() + "'s role has been changed to " + tempUser.getRole());
        updateUsers();
    }

    private void addProgram()
    {
        System.out.print("Name of show: ");
        String tempName = scanner.next();
        System.out.print("Release date(DD/MM-YYYY): ");
        String tempDate = scanner.next();
        System.out.print("Aired on: ");
        String tempShowedOn = scanner.next();

        //Program tempProgram = new Program(tempName,tempDate,tempShowedOn);
        //programs.add(tempProgram);
        //saveProgram(tempProgram);
    }

    private void addCredit()
    {
        System.out.print("Name: ");
        String tempName = scanner.next();
        System.out.print("Role: ");
        String tempRole = scanner.next();

        Credit tempCredit = new Credit(tempName,tempRole);
        credits.add(tempCredit);
        saveCredit(tempCredit);
        linkCreditProgram(tempCredit);
    }

    private void linkCreditProgram(Credit credit)
    {
        for (int i = 0; i < programs.size(); i++)
        {
            System.out.println(i+1 +":\n" +
                    "Name: " + programs.get(i).getName() +"\n" +
                    "Aired on: " + programs.get(i).getShowedOn() + "\n");
        }
        System.out.print("What program has he/she worked on?");
        Program tempProgram = programs.get(scanner.nextInt());
        //tempProgram.addCredit(credit);
        linkedCredits.put(credit,tempProgram.getId());
        saveLinkedCredit(credit, tempProgram.getId());
    }
}
