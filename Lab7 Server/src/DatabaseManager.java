import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.*;

public class DatabaseManager {

    private static final Logger userLogger = LogManager.getLogger(DatabaseManager.class);
    private static final String DB_URL = "jdbc:postgresql://localhost:5674/studs";
    private static final String USER = "";
    private static final String PASS = "";
    private Connection connection = null;
    private Statement statement = null;

    public DatabaseManager() {

    }

    public boolean connect() {
        try {
            Class.forName("org.postgresql.Driver");
            userLogger.info("PostgreSQL JDBC Driver успешно загружен!");

            connection = DriverManager.getConnection(DB_URL, USER, PASS);

            if (connection != null) {
                userLogger.info("Успешное подключение к базе данных!");
                statement = connection.createStatement();
                return true;
            } else {
                userLogger.fatal("Не удалось подключиться к базе данных!");
            }

        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver не найден. Подключите библиотеку PostgreSQL!");
        } catch (SQLException e) {
            userLogger.fatal(e.getMessage());
        }
        return false;
    }

    public boolean execute(String request) {
        try {
            if(statement.execute(request)) return true;
        } catch (SQLException throwables) {
            userLogger.fatal(throwables.getMessage());
        }
        return false;
    }

    public boolean addToCollection(Organization organization) {
        String select = "INSERT INTO collection(name, date, cordx, cordy, annualturnover, type, street, zipcode, author) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(select);
            preparedStatement.setString(1, organization.getName());
            preparedStatement.setString(2, Collection.getFormat().format(organization.getCreationDate()));
            preparedStatement.setFloat(3, organization.getCoordinates().getX());
            preparedStatement.setFloat(4, organization.getCoordinates().getY());
            preparedStatement.setInt(5, organization.getAnnualTurnover());
            if(organization.getType() != null) {
                preparedStatement.setString(6, organization.getType().toString());
            } else {
                preparedStatement.setString(6, null);
            }
            if(organization.getOfficialAddress() != null) {
                preparedStatement.setString(7, organization.getOfficialAddress().getStreet());
                preparedStatement.setString(8, organization.getOfficialAddress().getZipCode());
            } else {
                preparedStatement.setString(7, null);
                preparedStatement.setString(8, null);
            }
            preparedStatement.setString(9, organization.getAuthor());
            if(preparedStatement.executeUpdate() != 0) return true;
        } catch (SQLException throwables) {
            userLogger.fatal("Ошибка при добавлении организации в базу данных! " + throwables.getMessage());
        } catch (ClassCastException e){
            userLogger.fatal("ClassCastException при добавлении организации в базу данных!");
        }
        return false;
    }

    public boolean registerUser(User user) {
        String select = "INSERT INTO users (username, password) VALUES(?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(select);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            if(preparedStatement.executeUpdate() != 0) return true;
        } catch (SQLException throwables) {
            userLogger.fatal("Ошибка при добавлении пользователя в базу данных! " + throwables.getMessage());
        } catch (ClassCastException e){
            userLogger.fatal("ClassCastException при добавлении пользователя в базу данных!");
        }
        return false;
    }

    public boolean clearCollection() {
        String request = "TRUNCATE TABLE collection";
        if(execute(request)) return true;
        return false;
    }

    public boolean clearUsers() {
        String request = "TRUNCATE TABLE users";
        if(execute(request)) return true;
        return false;
    }

    public boolean clearByAuthor(User user) {
        String request = "DELETE FROM collection WHERE author = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(request);
            preparedStatement.setString(1, user.getUsername());
            if(preparedStatement.executeUpdate() != 0) return true;
        } catch (SQLException throwables) {
            userLogger.fatal(throwables.getMessage());
        }
        return false;
    }

    public boolean remove_by_id(int id, User user) {
        if(id > 0) {
            String request = "DELETE FROM collection WHERE (ID = ?) AND (AUTHOR = ?)";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(request);
                preparedStatement.setInt(1, id);
                preparedStatement.setString(2, user.getUsername());
                if(preparedStatement.executeUpdate() != 0) return true;
            } catch (SQLException throwables) {
                userLogger.fatal(throwables.getMessage());
            }
        } else {
            userLogger.fatal("Невозможно удалить элемент из базы данных < 0!");
        }
        return false;
    }

    public boolean remove_greater(int value, User user) {
        String request = "DELETE FROM collection WHERE (annualturnover > ?) AND (author = ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(request);
            preparedStatement.setInt(1, value);
            preparedStatement.setString(2, user.getUsername());
            if(preparedStatement.executeUpdate() != 0) return true;
        } catch (SQLException throwables) {
            userLogger.fatal(throwables.getMessage());
        }
        return false;
    }

    public boolean remove_lower(int value, User user) {
        String request = "DELETE FROM collection WHERE (annualturnover < ?) AND (author = ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(request);
            preparedStatement.setInt(1, value);
            preparedStatement.setString(2, user.getUsername());
            if(preparedStatement.executeUpdate() != 0) return true;
        } catch (SQLException throwables) {
            userLogger.fatal(throwables.getMessage());
        }
        return false;
    }

    public boolean update(int id, String field, Object object) {
        String request = "UPDATE collection set ";
        String end = " WHERE id = " + id;
        PreparedStatement preparedStatement = null;
        try {
            switch (field) {
                case "name":
                    request += "name = ?" + end;
                    preparedStatement = connection.prepareStatement(request);
                    preparedStatement.setString(1, (String) object);
                    break;
                case "coordinates":
                    request += "cordX = ?, cordY = ?" + end;
                    preparedStatement = connection.prepareStatement(request);
                    Coordinates coordinates = (Coordinates) object;
                    preparedStatement.setFloat(1, coordinates.getX());
                    preparedStatement.setFloat(2, coordinates.getY());
                    break;
                case "annualturnover":
                    request += "annualturnover = ?" + end;
                    preparedStatement = connection.prepareStatement(request);
                    preparedStatement.setInt(1, (int) object);
                    break;
                case "type":
                    request += "type = ?" + end;
                    preparedStatement = connection.prepareStatement(request);
                    OrganizationType organizationType = (OrganizationType) object;
                    if(organizationType != null) {
                        preparedStatement.setString(1, organizationType.toString());
                    } else {
                        preparedStatement.setString(1, null);
                    }
                    break;
                case "address":
                    request += "street = ?, zipcode = ?" + end;
                    preparedStatement = connection.prepareStatement(request);
                    Address address = (Address) object;
                    if(address != null) {
                        preparedStatement.setString(1, address.getStreet());
                        preparedStatement.setString(2, address.getZipCode());
                    } else {
                        preparedStatement.setString(1, null);
                        preparedStatement.setString(2, null);
                    }
                    break;
                }
                if(preparedStatement != null) {
                    preparedStatement.executeUpdate();
                    return true;
                }
        } catch (SQLException e){
            userLogger.fatal(e.getMessage());
        }
        return false;
    }

    public ResultSet getUser(User user) {
        ResultSet resultSet = null;
        String select = "SELECT * FROM users WHERE username=? AND password=?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(select);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException | ClassCastException throwables) {
            userLogger.fatal(throwables.getMessage());
        }
        return resultSet;
    }

    public boolean checkUsername(User user) {
        ResultSet resultSet = null;
        String select = "SELECT * FROM users WHERE username=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(select);
            preparedStatement.setString(1, user.getUsername());
            resultSet = preparedStatement.executeQuery();
            int count = 0;
            while(resultSet.next()){
                count++;
            }
            if(count > 0) {
                return true;
            }
        } catch (SQLException | ClassCastException throwables) {
            userLogger.fatal(throwables.getMessage());
        }
        return false;
    }

    public boolean userExists(User user){
        try {
            ResultSet authSet = UDPSocketServer.getDatabaseManager().getUser(user);
            int count = 0;
            while (authSet.next()) {
                count++;
            }
            if(count > 0){
                return true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }



    public Connection getConnection() {
        return connection;
    }

    public Statement getStatement() {
        return statement;
    }

    public static String getDbUrl() {
        return DB_URL;
    }

    public static String getUSER() {
        return USER;
    }
}
