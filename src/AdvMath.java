import java.math.BigInteger;
import java.util.Random;

// Some of the code in this class is all ready implemented in the BigInteger class
// But were is the fun in using that one?
public class AdvMath{

    // Returns a random BigInteger in [lowerBound, upperBound]
    public static BigInteger randGenBigInteger(BigInteger lowerBound,BigInteger upperBound) {
        Random rnd = new Random();
        BigInteger delta = upperBound.subtract(lowerBound);
        BigInteger retval = new BigInteger(delta.bitLength(), rnd);
        while (retval.compareTo(delta) == 1) {
            retval = new BigInteger(delta.bitLength(), rnd);
        }
        return retval.add(lowerBound);
    }

    // Returns a random coprime number to num that is lesser than num
    public static BigInteger findCoprime(BigInteger num) {
        // pivot in [0, num-2]
        BigInteger pivot = randGenBigInteger(BigInteger.ZERO, num.subtract(BigInteger.valueOf(2)));
        while (!BigInteger.ONE.equals(gcd(num,(pivot.mod(num.subtract(BigInteger.ONE))).add(BigInteger.ONE)))) {
            pivot = pivot.add(BigInteger.ONE);
        }
        // We use this weird equation to avoid 1, num
        return (pivot.mod(num.subtract(BigInteger.ONE))).add(BigInteger.ONE);

    }

    // Using euclid's algorithm
    public static BigInteger gcd(BigInteger a, BigInteger b) {
        BigInteger max = a.max(b); BigInteger min = a.min(b);
        if (min.compareTo(BigInteger.ZERO) == 0)
            return max;
        return gcd(max.mod(min), min);
    }

    // Returns a random prime number in [lowerBound, upperBound)
    public static BigInteger findPrime(BigInteger lowerBound, BigInteger upperBound) {
        // Try numbers between lowerBound and upperBound starting from a random index piv
        BigInteger piv = randGenBigInteger(BigInteger.ZERO ,upperBound.subtract(lowerBound).subtract(BigInteger.ONE));
        for (BigInteger i = BigInteger.ZERO; i.compareTo(upperBound.subtract(lowerBound)) == -1; i = i.add(BigInteger.ONE)) {
            //System.out.println(lowerBound.add(piv.add(i).mod(upperBound.subtract(lowerBound))));
            if (isPrime(lowerBound.add(piv.add(i).mod(upperBound.subtract(lowerBound)))))
                return lowerBound.add(piv.add(i).mod(upperBound.subtract(lowerBound)));
        }
        throw new RuntimeException("No prime numbers in [" + lowerBound + ", " + upperBound + "]");
    }

    // This test is based on Fermat's little theorem
    // This a test has a chance lower than 1/2^SAMPLE_SIZE of false positive
    public static boolean isPrime(BigInteger num) {
        final int SAMPLE_SIZE = 150;
        if (num.equals(BigInteger.ONE))
            return false;
        if (num.equals(BigInteger.valueOf(2)) || num.equals(BigInteger.valueOf(3)))
            return true;

        for (int i = 0; i < SAMPLE_SIZE; i++){
            // Find a random number in [2, num-2]
            BigInteger test = randGenBigInteger(BigInteger.ZERO, num.subtract(BigInteger.valueOf(4))).add(BigInteger.valueOf(2));
            if (!modExpo(test, num.subtract(BigInteger.ONE), num).equals(BigInteger.ONE)) {
                return false;
            }
        }
        return true;
    }

    // Returns (base^power) % mod
    public static BigInteger modExpo(BigInteger base, BigInteger power, BigInteger mod) {
        if (base.equals(BigInteger.ZERO))
            return BigInteger.ZERO;
        // base case, we need to calc something to the power of 0
        if (power.equals(BigInteger.ZERO))
            return BigInteger.ONE;
        BigInteger retVal = BigInteger.ONE;
        if (power.mod(BigInteger.valueOf(2)).equals(BigInteger.ONE)) {
            retVal = base;
            power = power.subtract(BigInteger.ONE);
        }
        return retVal.multiply(modExpo(base, power.divide(BigInteger.valueOf(2)), mod).pow(2)).mod(mod);

    }

    // Returns an s with is the solution of at+bs=gcd(a,b), when a,b are known and a > b
    // This is an implementation of the extended euclid's algorithm
    public static BigInteger extendedGcd(BigInteger a, BigInteger b) {
        BigInteger[] r = new BigInteger[3];
        BigInteger[] t = new BigInteger[3];
        BigInteger[] s = new BigInteger[3];

        r[0] = a; r[1] = b;
        t[0] = BigInteger.ONE; t[1] = BigInteger.ZERO;
        s[0] = BigInteger.ZERO; s[1] = BigInteger.ONE;
        int i;
        for (i = 1; !r[i%3].equals(BigInteger.ZERO); i++) {
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
