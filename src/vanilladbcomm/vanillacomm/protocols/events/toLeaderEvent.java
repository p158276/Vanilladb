package vanillacomm.protocols.events;

import net.sf.appia.core.AppiaEventException;
import net.sf.appia.core.Channel;
import net.sf.appia.core.Session;
import net.sf.appia.core.events.SendableEvent;

public class toLeaderEvent extends SendableEvent {
    public Object message;
    public toLeaderEvent() {
        super();
    }
    /*public toLeaderEvent setMessage(Object m){
        message =m;
        return this;
    }*/
    public toLeaderEvent(Channel channel, int direction, Session src)
            throws AppiaEventException {
        super(channel, direction, src);
    }
}
