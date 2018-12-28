import java.math.BigInteger;

public class Client {
    // The private key
    private BigInteger sk;
    // The public key
    public BigInteger pk;
    // This is used both for encryption and decryption
    public BigInteger n;

    public Client() {
            this(300);
    }
    public Client(int str) {
        this(BigInteger.valueOf(10).pow(str), BigInteger.valueOf(10).pow(str+1));
    }

    private Client(BigInteger lower_bound, BigInteger upper_bound) {
        BigInteger q = AdvMath.find_prime(lower_bound, upper_bound);
        BigInteger p = AdvMath.find_prime(lower_bound, upper_bound);
        n = p.multiply(q);
        BigInteger phi = (q.subtract(BigInteger.ONE)).multiply(p.subtract(BigInteger.ONE));
        pk = AdvMath.find_coprime(phi);
        sk = AdvMath.extended_gcd(phi, pk).add(phi);
    }

    public BigInteger encrypt(BigInteger msg) {
        return AdvMath.mod_expo(msg, pk, n);
    }

    public BigInteger decrypt(BigInteger msg) {
        return AdvMath.mod_expo(msg, sk, n);
    }

    @Override
    public String toString() {
        return "|pk is :" + this.pk + " sk is : " + this.sk + " n is : " + this.n + "|";
    }
}
