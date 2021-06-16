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
        } catch (Exception e) {
            return null;
        }
    }
}
