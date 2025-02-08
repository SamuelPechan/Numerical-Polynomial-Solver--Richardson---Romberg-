import java.io.File;
import java.util.Scanner;
import java.util.function.Function;

public class PolynomialSolver {
    public static void main(String[] args) throws Exception {
        int n = 5;
        double h = .0001;
        double[][] D = new double[n + 1][n + 1];
        String file = args[0];
        boolean loop = true;

        File inputFile = new File(file);
        Scanner input = new Scanner(inputFile);
        int power = input.nextInt();
        double coeff[] = new double[power + 1];

        for (int i = 0; i <= power; i++) {
            if (input.hasNextDouble()) {
                coeff[i] = input.nextDouble();
            }
        }

        Function<Double, Double> f = new Function<Double, Double>() {
            @Override
            public Double apply(Double value) {
                double result = 0;
                for (int i = 0; i <= power; i++) {
                    result += coeff[power - i] * Math.pow(value, i);

                }
                return result;
            }
        };

        while (loop == true) {
            System.out.println(
                    "\nWould you like to perform integration or derivation on the stated polynomial? \nEnter \ni for integration, \nd for derivative, or \nq for quit");
            Scanner input2 = new Scanner(System.in);
            Scanner inputX = new Scanner(System.in);

            switch (input2.next()) {
                case "d":
                    System.out.println("what x value would you like to evaluate?");
                    double x = inputX.nextDouble();
                    Richardson(f, x, n, h, D);
                    System.out.println("your polynomial derivative at x = " + x + " is: \n " + D[n][n] + "\n");
                    break;

                case "i":
                    System.out.println("over what integral from a to b would you like to integrate?");
                    System.out.println("a: ");
                    double a = inputX.nextDouble();
                    System.out.println("b: ");
                    double b = inputX.nextDouble();
                    Romberg(f, a, b, n, D);
                    System.out.println(
                            "your polynomial integrated at a = " + a + " and b = " + b + " is: \n" + D[n][n] + "\n");
                    break;

                case "q":
                    System.out.println("You have chosen to quit");
                    loop = false;
                    break;

                default:
                    System.out.println("Incorrect input please enter i, d or q \n");
                    break;
            }
        }
    }

    public static void Richardson(Function<Double, Double> f, double x, int n, double h, double[][] D) {
        for (int i = 0; i <= n; i++) {
            D[i][0] = (f.apply(x + h) - f.apply(x - h)) / (2 * h);
            for (int j = 1; j <= i; j++) {
                D[i][j] = D[i][j - 1] + (D[i][j - 1] - D[i - 1][j - 1]) / (Math.pow(4, j) - 1);
            }
            h /= 2;
        }
    }

    public static void Romberg(Function<Double, Double> f, double a, double b, int n, double[][] R) {
        double h = b - a;
        R[0][0] = (h / 2) * (f.apply(a) + f.apply(b));
        System.out.println("f(a) = " + f.apply(a));
        System.out.println("f(b) = " + f.apply(b));

        for (int i = 1; i <= n; i++) {
            h /= 2;
            double sum = 0.0;

            for (int k = 1; k <= Math.pow(2, i) - 1; k += 2) {
                sum += f.apply(a + k * h);
            }
            R[i][0] = 0.5 * (R[i - 1][0]) + sum * h;

            for (int j = 1; j <= i; j++) {
                R[i][j] = R[i][j - 1] + ((R[i][j - 1] - R[i - 1][j - 1]) / (Math.pow(4, j) - 1));
            }
        }
    }
}
