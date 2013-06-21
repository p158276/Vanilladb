package vanillacomm.protocols.events;

import net.sf.appia.core.AppiaEventException;
import net.sf.appia.core.Channel;
import net.sf.appia.core.Session;
import net.sf.appia.core.events.SendableEvent;

public class finalEvent extends SendableEvent{
    public Object message;
    public finalEvent() {
        super();
    }
   /*public finalEvent setMessage(Object m){
        message =m;
        return this;
    }*/
    public finalEvent(Channel channel, int direction, Session src)
            throws AppiaEventException {
        super(channel, direction, src);
    }
}
