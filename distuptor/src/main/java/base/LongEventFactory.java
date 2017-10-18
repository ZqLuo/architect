package base;

import com.lmax.disruptor.EventFactory;

/**
 * Created by zqLuo
 */
public class LongEventFactory implements EventFactory {
    public Object newInstance() {
        return new LongEvent();
    }
}
