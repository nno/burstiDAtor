package burstiDAtor;
import java.math.BigDecimal;
import java.math.MathContext;

public class Spike {
    public static int DECIMALS = 6;
    public static long SCALING = (long) Math.pow(10, DECIMALS);
    private long onset;

    private Spike(long onset) {
	this.onset = onset;
    }

    public long getUnscaledOnset() {
	return onset;
    }

    public double doubleValue() {
	return ((double) (onset)) / SCALING;
    }

    public SpikeDelta subtract(Spike other) {
	return new SpikeDelta(other, this);
    }

    public Spike divideFloor(SpikeDelta delta) {
	long d = delta.getUnscaled();
	long count = (int) (onset / d);
	return new Spike(count * d);
    }

    public Spike divideCeil(SpikeDelta delta) {
	long d = delta.getUnscaled();
	long count = (int) Math.ceil(((double) onset) / d);
	return new Spike(count * d);
    }


    public static Spike fromString(String s) {
	BigDecimal bd = new BigDecimal(s);
	long onset = bd.multiply(new BigDecimal(SCALING)).longValueExact();
	return new Spike(onset);
    }

    public static Spike fromDouble(Double v) {
	long onset = (long) (v * SCALING);
	return new Spike(onset);
    }

    public String toString() {
	return "Spike(" + doubleValue() + ")";
    }

    public static void main(String ... args) {
	Spike s = Spike.fromString("123.456127");
	System.out.println(s + "/" + s.onset + "/" + s.SCALING);
    }
}
