import java.io.BufferedReader;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Класс, предзначенный для ввода и проверки корректности ввода значений
 */

public class ConsoleEvent {

    /**
     * Контекстное меню для обновление поля, которое задается пользователем
     * @return корректное значение поля, которое пользователь хочет изменить
     */
    public static String update() {
        Scanner input = new Scanner(System.in);
        String name;
        while(true) {
            System.out.print("Укажите имя поля, которое вы хотите изменить: ");
            name = (input.nextLine()).toLowerCase();
            if (name.equals("name") || name.equals("coordinates") || name.equals("annualturnover") || name.equals("type") || name.equals("address") || name.equals("exit")) {
                return name;
            } else if (name.equals("help")) {
                System.out.println("Помощь по полям:\nname - изменить имя\ncoordinates - изменить координаты\nannualTurnover - изменить годовой оборот\ntype - изменить тип\naddress - изменить адрес\nДля выхода введите exit");
            } else {
                System.out.println("Неверное имя поля! Вызовите help для помощи!");
            }
        }
    }

    /**
     * Контекстное меню для обновление поля, которое задается пользователем
     * @return корректное значение поля, которое пользователь хочет изменить
     */
    public static String update(BufferedReader bufferedReader) {
        Scanner input = new Scanner(System.in);
        String name = "";
        Boolean scanned = false;
        while(true) {
            System.out.print("Укажите имя поля, которое вы хотите изменить: ");
            if(scanned) {
                name = (input.nextLine()).toLowerCase();
            } else {
                try {
                    name = bufferedReader.readLine();
                } catch (IOException e) {
                    System.out.println("Ошибка при чтении файла!");
                } catch (NullPointerException e) {
                    System.out.println("Имя поля не должно быть null!");
                }
                System.out.print(name + "\n");
                scanned = true;
            }
            try {
                if (name.equals("name") || name.equals("coordinates") || name.equals("annualturnover") || name.equals("type") || name.equals("address") || name.equals("exit")) {
                    return name;
                } else if (name.equals("help")) {
                    System.out.println("Помощь по полям:\nname - изменить имя\ncoordinates - изменить координаты\nannualTurnover - изменить годовой оборот\ntype - изменить тип\naddress - изменить адрес\nДля выхода введите exit");
                } else {
                    System.out.println("Неверное имя поля! Вызовите help для помощи!");
                }
            } catch (NullPointerException e){
                System.out.println("Имя поля не должно быть null!");
            }
        }
    }

    /**
     * Контекстное меню для получение имени организации, которое задается пользователем
     * @return корректное значение имени организации, заданное пользователем
     */
    public static String getName() {
        Scanner input = new Scanner(System.in);
        String name;
        while(true) {
            try {
                System.out.print("Введите имя компании: ");
                name = input.nextLine();
                if(name.isEmpty()) {
                    throw new IncorrectValueException("Введено некорректное значение, повторите попытку!");
                } else {
                    return name;
                }
            } catch (IncorrectValueException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Контекстное меню для получение имени организации, которое задается пользователем
     * @param bufferedReader сканнер
     * @return корректное значение имени организации, заданное пользователем
     */
    public static String getName(BufferedReader bufferedReader) {
        Scanner input = new Scanner(System.in);
        String name;
        Boolean scanned = false;
        while(true) {
            try {
                System.out.print("Введите имя компании: ");
                if(scanned) {
                    name = input.nextLine();
                } else {
                    name = bufferedReader.readLine();
                    System.out.print(name + "\n");
                    scanned = true;
                }
                if(name.isEmpty()) {
                    throw new IncorrectValueException("Введено некорректное значение, повторите попытку!");
                } else {
                    return name;
                }
            } catch (IncorrectValueException e) {
                System.out.println(e.getMessage());
            } catch (IOException e) {
                System.out.println("Произошла ошибка при чтении файла!");
            } catch (NullPointerException e) {
                System.out.println("Имя не должно быть null!");
            }
        }
    }

    /**
     * Контекстное меню для получение координат организации, которое задается пользователем
     * @return корректное значение объекта класса Coordinates с заданными пользователем координатами организации
     */
    public static Coordinates getCoordinates() {
        Scanner input = new Scanner(System.in);
        float x, y;
        while(true) {
            try {
                System.out.print("Введите координату X: ");
                x = input.nextFloat();
                if(x > 662){
                    throw new IncorrectValueException("Значение X не должно превышать 662!");
                } else {
                    while (true) {
                        try {
                            System.out.print("Введите координату Y: ");
                            y = input.nextFloat();
                            return new Coordinates(x, y);
                        } catch (Exception e) {
                            System.out.println("Введено некорректное значение, повторите попытку!");
                            input.nextLine(); // Flush scanner
                        }
                    }
                }
            } catch (InputMismatchException e) {
                System.out.println("Введено некорректное значение, повторите попытку!");
                input.nextLine(); // Flush scanner
            } catch (IncorrectValueException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Контекстное меню для получение координат организации, которое задается пользователем
     * @param bufferedReader сканнер
     * @return корректное значение объекта класса Coordinates с заданными пользователем координатами организации
     */
    public static Coordinates getCoordinates(BufferedReader bufferedReader) {
        Scanner input = new Scanner(System.in);
        float x, y;
        Boolean scannedX = false, scannedY = false;
        while(true) {
            try {
                System.out.print("Введите координату X: ");
                if(scannedX) {
                    x = input.nextFloat();
                } else {
                    x = Float.parseFloat(bufferedReader.readLine());
                    System.out.print(x + "\n");
                    scannedX = true;
                }
                if(x > 662){
                    throw new IncorrectValueException("Значение X не должно превышать 662!");
                } else {
                    while (true) {
                        try {
                            System.out.print("Введите координату Y: ");
                            if(scannedY) {
                                y = input.nextFloat();
                            } else {
                                y = Float.parseFloat(bufferedReader.readLine());
                                System.out.print(y + "\n");
                                scannedY = true;
                            }
                            return new Coordinates(x, y);
                        } catch (Exception e) {
                            System.out.println("Введено некорректное значение, повторите попытку!");
                            input.nextLine(); // Flush scanner
                        }
                    }
                }
            } catch (InputMismatchException e) {
                System.out.println("Введено некорректное значение, повторите попытку!");
                input.nextFloat(); // Flush scanner
            } catch (IncorrectValueException e) {
                System.out.println(e.getMessage());
            } catch (IOException e) {
                System.out.println("Произошла ошибка при чтении файла!");
            }
        }
    }

    /**
     * Контекстное меню для получение годового оборота организации, которое задается пользователем
     * @return корректное значение годового оборота, заданное пользователем
     */
    public static int getAnnualTurnover() {
        Scanner input = new Scanner(System.in);
        int annualTurnover;
        while(true) {
            try {
                System.out.print("Введите годовой оборот: ");
                annualTurnover = input.nextInt();
                if(annualTurnover > 0){
                    return annualTurnover;
                } else {
                    throw new IncorrectValueException("Годовой оборот должен быть натуральным числом!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Введено некорректное значение, повторите попытку!");
                input.nextLine(); // Flush scanner
            } catch (IncorrectValueException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Контекстное меню для получение годового оборота организации, которое задается пользователем
     * @param bufferedReader сканнер
     * @return корректное значение годового оборота, заданное пользователем
     */
    public static int getAnnualTurnover(BufferedReader bufferedReader) {
        Scanner input = new Scanner(System.in);
        int annualTurnover = 0;
        Boolean scanned = false;
        while(true) {
            try {
                System.out.print("Введите годовой оборот: ");
                if(scanned) {
                    annualTurnover = input.nextInt();
                } else {
                    try {
                        annualTurnover = Integer.parseInt(bufferedReader.readLine());
                    } catch(NumberFormatException e) {
                        System.out.println("Не удалось преобразовать к числу!");
                    }
                    System.out.print(annualTurnover + "\n");
                    scanned = true;
                }
                if(annualTurnover > 0){
                    return annualTurnover;
                } else {
                    throw new IncorrectValueException("Годовой оборот должен быть натуральным числом!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Введено некорректное значение, повторите попытку!");
                input.nextLine(); // Flush scanner
            } catch (IncorrectValueException e) {
                System.out.println(e.getMessage());
            } catch (IOException e) {
                System.out.println("Произошла ошибка при чтении файла!");
            } catch (NullPointerException e) {
                System.out.println("Годовой оборот не может быть null!");
            }
        }
    }

    /**
     * Контекстное меню для типа организации, которое задается пользователем
     * @return корректное значение объекта класса OrganizationType
     */
    public static OrganizationType getType() {
        Scanner input = new Scanner(System.in);
        OrganizationType type;
        String typeName;
        System.out.println("Доступные типы организации: " + Stream.of(OrganizationType.values()).
                map(OrganizationType::name).
                collect(Collectors.joining(", ")));
        while(true) {
            try {
                System.out.print("Введите тип организации: ");
                typeName = input.nextLine();
                type = Utils.StrToType(typeName);
                if (!typeName.isEmpty() && type == null) {
                    throw new IncorrectValueException("Проверьте значение типа организации! Если такое поле null - оставьте строку пустой!");
                } else {
                    return type;
                }
            } catch (IncorrectValueException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Контекстное меню для типа организации, которое задается пользователем
     * @param bufferedReader сканнер
     * @return корректное значение объекта класса OrganizationType
     */
    public static OrganizationType getType(BufferedReader bufferedReader) {
        Scanner input = new Scanner(System.in);
        OrganizationType type;
        String typeName;
        Boolean scanned = false;
        System.out.println("Доступные типы организации: " + Stream.of(OrganizationType.values()).
                map(OrganizationType::name).
                collect(Collectors.joining(", ")));
        while(true) {
            try {
                System.out.print("Введите тип организации: ");
                if(scanned) {
                    typeName = input.nextLine();
                } else {
                    typeName = bufferedReader.readLine();
                    System.out.print(typeName + "\n");
                    scanned = true;
                }
                type = Utils.StrToType(typeName);
                if (!typeName.isEmpty() && type == null) {
                    throw new IncorrectValueException("Проверьте значение типа организации! Если такое поле null - оставьте строку пустой!");
                } else {
                    return type;
                }
            } catch (IncorrectValueException e) {
                System.out.println(e.getMessage());
            } catch (IOException e) {
                System.out.println("Произошла ошибка при чтении файла!");
            } catch (NullPointerException e) {
                System.out.println("Несмотря на то, что тип организации может быть null, поле для него должно присутствовать в файле!");
            }
        }
    }

    /**
     * Контекстное меню для получение адреса организации, который задается пользователем
     * @return корректное значение объекта класса Address с заданными пользователем улицей и индексом
     */
    public static Address getAddress() {
        Scanner input = new Scanner(System.in);
        String street, zipcode;
        System.out.print("Введите улицу: ");
        street = input.nextLine();
        if (!street.isEmpty()) {
            System.out.print("Введите индекс: ");
            zipcode = input.nextLine();
            try {
                return new Address(street, zipcode);
            } catch (IncorrectValueException e) {
                System.err.println(e.getMessage());
            }
        }
        return null;
    }

    /**
     * Контекстное меню для получение адреса организации, который задается пользователем
     * @param bufferedReader сканнер
     * @return корректное значение объекта класса Address с заданными пользователем улицей и индексом
     */
    public static Address getAddress(BufferedReader bufferedReader) {
        Scanner input = new Scanner(System.in);
        String street = "", zipcode = "";
        Boolean scannedStreet = false, scannedZipcode = false;
        System.out.print("Введите улицу: ");
        if(scannedStreet) {
            street = input.nextLine();
        } else {
            try {
                street = bufferedReader.readLine();
            } catch (IOException e) {
                System.out.println("Произошла ошибка при чтении файла!");
            }
            System.out.print(street + "\n");
            scannedStreet = true;
        }
        if (!street.isEmpty()) {
            System.out.print("Введите индекс: ");
            if(scannedZipcode) {
                zipcode = input.nextLine();
            } else {
                try {
                    zipcode = bufferedReader.readLine();
                } catch (IOException e) {
                    System.out.println("Произошла ошибка при чтении файла!");
                }
                System.out.print(zipcode + "\n");
                scannedZipcode = true;
            }
            try {
                return new Address(street, zipcode);
            } catch (IncorrectValueException e) {
                System.err.println(e.getMessage());
            }
        }
        return null;
    }
}