package tests.burstiDAtor;

import static org.junit.Assert.assertEquals;

import java.util.Vector;

import org.junit.Test;

import burstiDAtor.Burst;
import burstiDAtor.Bursts;
import burstiDAtor.Props;
import burstiDAtor.Spikes;

public class BurstsTest {

    @Test
    public void test() {
        final double[] onsets = { 1d, 1.1d, 1.13d, 1.25d, 1.42d, 1.44d, 4d };
        Spikes s = new Spikes("foo.txt", onsets);
        Bursts b = new Bursts(s);

        Vector<Burst> bursts = b.getBursts();
        System.out.println(bursts);
        assertEquals(bursts.size(), 2);
        Burst b0 = bursts.get(0);
        Props s0=b0.getStats();

        Props p0 = new Props();
        p0.put("BuDur", 0.15);
        p0.put("SpFreq", 20.);
        p0.put("center", 1.175);
        p0.put("firstSp", 1.1);
        p0.put("interSp", 0.075);
        p0.put("lastSp", 1.25);
        p0.put("nSp", 3);

        for (String k : p0.keyList()) {
            assertAlmostEqual(p0.get(k), s0.get(k));
        }

        Burst b1=bursts.get(1);
        Props s1=b1.getStats();

        Props p1=new Props();
        p1.put("BuDur",0.02);
        p1.put("SpFreq", 100.);
        p1.put("center",1.43);
        p1.put("firstSp",1.42);
        p1.put("interSp",0.02);
        p1.put("lastSp",1.44);
        p1.put("nSp",2);

        for (String k : p1.keyList()) {
            assertAlmostEqual(p1.get(k), s1.get(k));
        }

    }

    public static void assertAlmostEqual(Object v, Object w) {
        double eps=.0000001;

        if (v instanceof Double && w instanceof Double) {
            assert(Math.abs(((Double) v)-((Double) w))<eps);
        } else {
            assertEquals(v,w);
        }

    }

}
