package burstiDAtor;

public class SpikeDelta {
	private long value;

	private SpikeDelta(long value) {
		this.value = value;
	}

	public SpikeDelta(Spike first, Spike last) {
		this(last.getUnscaledOnset() - first.getUnscaledOnset());
	}

	public SpikeDelta subtract(SpikeDelta other) {
		return new SpikeDelta(this.value - other.value);
	}

	public int divideFloor(SpikeDelta other) {
		return (int) Math.floor(this.value / other.value);
	}

	public int divideCeil(SpikeDelta other) {
		return (int) Math.ceil(this.value / other.value);
	}

	public SpikeDelta multiplyBy(long v) {
		return new SpikeDelta(this.value * v);
	}

	public static SpikeDelta fromString(String s) {
		Spike spike = Spike.fromString(s);
		Spike zero = Spike.fromString("0");
		return new SpikeDelta(zero, spike);
	}

	public long getUnscaled() {
		return value;
	}

	public String toString() {
		return "SpikeDelta(" + doubleValue() + ")";
	}

	public double doubleValue() {
		return ((double) (value)) / Spike.SCALING;
	}

	public double divideBy(int v) {
		return ((double) value) / (v);
	}
}
