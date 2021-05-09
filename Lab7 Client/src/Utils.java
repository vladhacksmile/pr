import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Вспомогательные утилиты
 */
public class Utils {
    /**
     * Преобразование строки в OrganizationType
     * @param typeName тип организации
     * @return объект перечисления OrganizationType или null
     */
    public static OrganizationType StrToType(String typeName) {
        try {
            return OrganizationType.valueOf(typeName.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Представление строки в виде хэш-суммы
     * @param inputBytes массив байт
     * @param algorithm алгоритм шифрования
     * @return хэш-строка
     */
    public static String getHash(byte[] inputBytes, String algorithm){
        try{
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);;
            messageDigest.update(inputBytes);
            byte[] digestedBytes = messageDigest.digest();
            return DatatypeConverter.printHexBinary(digestedBytes).toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Указанный алгоритм шифрования не найден! " + algorithm);
        }
        return null;
    }
}
