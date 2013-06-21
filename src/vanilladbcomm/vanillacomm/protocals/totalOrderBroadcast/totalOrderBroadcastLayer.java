package vanillacomm.protocals.totalOrderBroadcast;

import net.sf.appia.core.Layer;
import net.sf.appia.core.Session;
import net.sf.appia.core.events.SendableEvent;
import net.sf.appia.core.events.channel.ChannelClose;
import net.sf.appia.core.events.channel.ChannelInit;
import vanillacomm.protocols.events.Crash;
import vanillacomm.protocols.events.ProcessInitEvent;
import vanillacomm.protocols.events.finalEvent;
import vanillacomm.protocols.events.toLeaderEvent;

/**
 * Layer of the total Order Broadcast protocol.
 * 
 * @author nuno
 */
public class totalOrderBroadcastLayer extends Layer {

    public totalOrderBroadcastLayer() {
        /* events that the protocol will create */
        evProvide = new Class[2];
        evProvide[0] = finalEvent.class;
        evProvide[1] = toLeaderEvent.class;

        /*
         * events that the protocol require to work. This is a subset of the
         * accepted events
         */
        evRequire = new Class[3];
        evRequire[0] = SendableEvent.class;
        evRequire[1] = ChannelInit.class;
        evRequire[2] = ProcessInitEvent.class;

        /* events that the protocol will accept */
        evAccept = new Class[7];
        evAccept[0] = SendableEvent.class;
        evAccept[1] = ChannelInit.class;
        evAccept[2] = ChannelClose.class;
        evAccept[3] = ProcessInitEvent.class;
        evAccept[4] = Crash.class;
        evAccept[5] = finalEvent.class;
        evAccept[6] = toLeaderEvent.class;

    }

    /**
     * Creates a new session to this protocol.
     * 
     * @see appia.Layer#createSession()
     */
    public Session createSession() {
        return new totalOrderBroadcastSession(this);
    }

}
