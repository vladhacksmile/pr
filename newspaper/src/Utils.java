import java.util.Random;
public class Utils {

  public static int getRandom(int a, int b) {
      return a + (int) (Math.random() * b);
  }

  public static int getRandom(int n) {
      Random random = new Random();
      return random.nextInt(n);
  }

  public static int getProcent(int num, int procent){
      return (num * procent) / 100;
  }
}
