import java.util.Scanner;

public class Auth {

    private static User user;
    private static boolean auth = true;
    private static String username;
    private static String password;
    private static String[] authRequest = {"auth"};
    private static String[] regRequest = {"reg"};
    private static Scanner scanner = new Scanner(System.in);

    public static void auth() {
        auth = true;
        System.out.println("Для работы с коллекцией вам необходимо авторизоваться! Если вам нужно зарегистрироваться - введите @reg в поле логина!");
        System.out.print("Введите логин: ");
        username = scanner.nextLine();
        if(username.equals("@reg")) {
            register();
            auth = false;
        } else {
            System.out.print("Введите пароль: ");
            password = scanner.nextLine();
            UDPSocketClient.send(new Command(new User(username, Utils.getHash(password.getBytes(), "SHA-384")), authRequest, null));
        }
    }

    public static void authorized() {
        user = new User(username, Utils.getHash(password.getBytes(), "SHA-384"));
        UDPSocketClient.start();
    }

    public static void register() {
        auth = false;
        System.out.println("Для регистрации укажите логин и пароль! Логин должен быть уникальным! Если вам нужно авторизоваться - введите @auth в поле логина!");
        System.out.print("Введите логин: ");
        String regUsername = scanner.nextLine();
        if(regUsername.equals("@auth")) {
            auth();
            auth = true;
        } else {
            System.out.print("Введите пароль: ");
            String regPassword = scanner.nextLine();
            UDPSocketClient.send(new Command(new User(regUsername, Utils.getHash(regPassword.getBytes(), "SHA-384")), regRequest, null));
        }
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        Auth.user = user;
    }

    public static boolean isAuth() {
        return auth;
    }
}
