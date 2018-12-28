import java.math.BigInteger;
import java.util.Random;
import java.util.Stack;


public class AdvMath{

    public static BigInteger rand_gen_BigInteger(BigInteger lower_bound,BigInteger upper_bound) {
        Random rnd = new Random();
        BigInteger delta = upper_bound.subtract(lower_bound);
        BigInteger retval = new BigInteger(delta.bitLength(), rnd);
        while (retval.compareTo(delta) == 1) {
            retval = new BigInteger(delta.bitLength(), rnd);
        }
        return retval.add(lower_bound);
    }

    // Returns a random coprime number to num that is lesser than num
    public static BigInteger find_coprime(BigInteger num) {
        // pivot in [0, num-2]
        BigInteger pivot = rand_gen_BigInteger(BigInteger.ZERO, num.subtract(BigInteger.valueOf(2)));
        while (BigInteger.ONE.compareTo(gcd(num,(pivot.mod(num.subtract(BigInteger.ONE))).add(BigInteger.ONE))) != 0) {
            pivot = pivot.add(BigInteger.ONE);
        }
        // We use this weird equation to avoid 1, num
        return (pivot.mod(num.subtract(BigInteger.ONE))).add(BigInteger.ONE);

    }

    public static BigInteger gcd(BigInteger a, BigInteger b) {
        BigInteger max = a.max(b); BigInteger min = a.min(b);
        if (min.compareTo(BigInteger.ZERO) == 0)
            return max;
        return gcd(max.mod(min), min);
    }

    // Returns a random prime number in [lower_bound, upper_bound)
    public static BigInteger find_prime(BigInteger lower_bound, BigInteger upper_bound) {
        // Try numbers between lower_bound and upper_bound starting from a random index piv
        BigInteger piv = rand_gen_BigInteger(BigInteger.ZERO ,upper_bound.subtract(lower_bound).subtract(BigInteger.ONE));
        for (BigInteger i = BigInteger.ZERO; i.compareTo(upper_bound.subtract(lower_bound)) == -1; i = i.add(BigInteger.ONE)) {
            //System.out.println(lower_bound.add(piv.add(i).mod(upper_bound.subtract(lower_bound))));
            if (is_prime(lower_bound.add(piv.add(i).mod(upper_bound.subtract(lower_bound)))))
                return lower_bound.add(piv.add(i).mod(upper_bound.subtract(lower_bound)));
        }
        throw new RuntimeException("Now prime numbers in [" + lower_bound + ", " + upper_bound + "]");
    }

    public static boolean is_prime(BigInteger num) {
        if (num.compareTo(BigInteger.ONE) == 0)
            return false;
        if (num.compareTo(BigInteger.valueOf(2)) == 0)
            return true;

        final int SAMPLE_SIZE = 50;
        for (int i = 0; i < SAMPLE_SIZE; i++){
            // Find a random number in [2, num-2]
            BigInteger test = rand_gen_BigInteger(BigInteger.ZERO, num.subtract(BigInteger.valueOf(4))).add(BigInteger.valueOf(2));
            if (mod_expo(test, num.subtract(BigInteger.ONE), num).compareTo(BigInteger.ONE) != 0)
                return false;
        }
        return true;
    }

    // Returns (base^power) % mod
    public static BigInteger mod_expo(BigInteger base, BigInteger power, BigInteger mod) {
        base = base.mod(mod);
        if (0 == power.compareTo(BigInteger.ZERO))
            return BigInteger.ONE;

        Stack<Integer> divisors = new Stack<>();
        while (0 != power.compareTo(BigInteger.ONE)) {
            if (0 == power.mod(BigInteger.valueOf(2)).compareTo(BigInteger.ZERO)) {
                divisors.push(2);
                power = power.divide(BigInteger.valueOf(2));
            } else {
                divisors.push(1);
                power = power.subtract(BigInteger.ONE);
            }
        }
        BigInteger res = base;
        while (!divisors.empty()) {
            int temp = divisors.pop();
            if (2 == temp)
                res = res.multiply(res);
            else
                res = res.multiply(base);
            res = res.mod(mod);
        }
        return res;
    }

    // returns an s with is the solution of at+bs=gcd(a,b), when a,b are known and a > b
    public static BigInteger extended_gcd(BigInteger a, BigInteger b) {
        BigInteger[] r = new BigInteger[3];
        BigInteger[] t = new BigInteger[3];
        BigInteger[] s = new BigInteger[3];

        r[0] = a; r[1] = b;
        t[0] = BigInteger.ONE; t[1] = BigInteger.ZERO;
        s[0] = BigInteger.ZERO; s[1] = BigInteger.ONE;
        int i;
        for (i = 1; r[i%3].compareTo(BigInteger.ZERO) != 0; i++) {
            /*
            System.out.println("gcd is : " + r[i%3]);
            System.out.println("s is : " + s[i%3]);
            System.out.println("t is : " + t[i%3]);
            */
            if (0 == i)
                continue;
            BigInteger q = (r[(i-1)%3].divide(r[i%3]));
            //System.out.println(q);
            r[(i+1)%3] = r[(i-1)%3].subtract(r[i%3].multiply(q));
            s[(i+1)%3] = s[(i-1)%3].subtract(s[i%3].multiply(q));
            t[(i+1)%3] = t[(i-1)%3].subtract(t[i%3] .multiply(q));
        }
        return s[(i-1)%3];
    }
}
