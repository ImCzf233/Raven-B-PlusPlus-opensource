package keystrokesmod.client.utils;

public class Timer
{
    private final float updates;
    private long last;
    private float cached;
    
    public Timer(final float updates) {
        this.updates = updates;
    }
    
    public float getValueFloat(final float begin, final float end, final int type) {
        if (this.cached == end) {
            return this.cached;
        }
        float t = (System.currentTimeMillis() - this.last) / this.updates;
        switch (type) {
            case 1: {
                t = ((t < 0.5f) ? (4.0f * t * t * t) : ((t - 1.0f) * (2.0f * t - 2.0f) * (2.0f * t - 2.0f) + 1.0f));
                break;
            }
            case 2: {
                t = (float)(1.0 - Math.pow(1.0f - t, 5.0));
                break;
            }
            case 3: {
                t = this.bounce(t);
                break;
            }
        }
        float value = begin + t * (end - begin);
        if (end < value) {
            value = end;
        }
        if (value == end) {
            this.cached = value;
        }
        return value;
    }
    
    public int getValueInt(final int begin, final int end, final int type) {
        return Math.round(this.getValueFloat((float)begin, (float)end, type));
    }
    
    public void start() {
        this.cached = 0.0f;
        this.last = System.currentTimeMillis();
    }
    
    private float bounce(float t) {
        final double i2 = 7.5625;
        final double i3 = 2.75;
        if (t < 1.0 / i3) {
            return (float)(i2 * t * t);
        }
        if (t < 2.0 / i3) {
            return (float)(i2 * (t -= (float)(1.5 / i3)) * t + 0.75);
        }
        if (t < 2.5 / i3) {
            return (float)(i2 * (t -= (float)(2.25 / i3)) * t + 0.9375);
        }
        return (float)(i2 * (t -= (float)(2.625 / i3)) * t + 0.984375);
    }
}
