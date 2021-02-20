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
            }
            catch (IncorrectValueException e) {
                System.out.println(e.getMessage());
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
            }
            catch (IncorrectValueException e) {
                System.out.println(e.getMessage());
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
}
