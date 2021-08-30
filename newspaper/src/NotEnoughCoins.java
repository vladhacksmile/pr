public class NotEnoughCoins extends RuntimeException {
    public NotEnoughCoins(String cause) {
        super("Недостаточно монет для совершения операции: " + cause);
    }
}
