import java.util.Random;
import java.util.Stack;

public class Client {
    private int sk;
    private int pk;
    private int n;

    private Client(int lower_bound, int upper_bound) {
        int q = find_prime(lower_bound, upper_bound);
        int p = find_prime(lower_bound, upper_bound);
        this.n = q*p;
        this.pk = find_coprime((q-1)*(p-1));
    }

    // Returns a random coprime number to num that is lesser than num
    private static int find_coprime(int num) {
        Random rand = new Random();
        int pivot = rand.nextInt(num - 1);
        while (1 != gcd(num, (pivot % (num-1)) + 1))
            pivot++;
        // We use this weird equation to avoid 1, num
        return (pivot % (num-1)) + 1;

    }

    private static int gcd(int a, int b) {
        int max = Math.max(a, b); int min = Math.min(a, b);
        if (0 == min)
            return max;
        return gcd(max % min, min);
    }

    // Returns a random prime number in [lower_bound, upper_bound)
    private static int find_prime(int lower_bound, int upper_bound) {
        // Try numbers between lower_bound and upper_bound starting from a random index piv
        Random rand = new Random();
        int piv = rand.nextInt(upper_bound - lower_bound);
        for (int i = 0; i < upper_bound - lower_bound; i++) {
            if (is_prime(lower_bound + (piv + i)%(upper_bound - lower_bound)))
                return lower_bound + (piv + i)%(upper_bound - lower_bound);
        }
        throw new RuntimeException("Now prime numbers in [" + lower_bound + ", " + upper_bound + "]");
    }

    private static boolean is_prime(int num) {
        if (num == 1)
            return false;
        if (num == 2)
            return true;

        Random rand = new Random();
        final int SAMPLE_SIZE = 50;
        for (int i = 0; i < SAMPLE_SIZE; i++){
            // Find a random number in [2, num-2]
            int test = rand.nextInt(num-3) + 2;
            if (mod_expo(test, num-1, num) != 1)
                return false;
        }
        return true;
    }

    // Returns (base^power) % mod
    private static int mod_expo(int base, int power, int mod) {
        power %= mod; base %= mod;
        if (0 == power)
            return 1;

        Stack<Integer> divisors = new Stack<>();
        while (1 != power) {
            if (0 == power % 2) {
                divisors.push(2);
                power /= 2;
            } else {
                divisors.push(1);
                power--;
            }
        }
        int res = base;
        while (!divisors.empty()) {
            int temp = divisors.pop();
            if (2 == temp)
                res *= res;
            else
                res = res * base;
            res %= mod;
        }
        return res;
    }
}
