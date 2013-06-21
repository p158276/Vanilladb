package vanillacomm.protocols.events;

import net.sf.appia.core.AppiaEventException;
import net.sf.appia.core.Channel;
import net.sf.appia.core.Event;
import net.sf.appia.core.Session;

import vanillacomm.protocols.consensusUtils.PaxosProposal;

public class PaxosReturn extends Event {
    /**
     * The value proposed for consensus.
     */
    public PaxosProposal decision;

    public PaxosReturn() {
        super();
    }

    public PaxosReturn(PaxosProposal value) {
        super();
        this.decision = value;
    }

    public PaxosReturn(Channel channel, int direction, Session src)
            throws AppiaEventException {
        super(channel, direction, src);
    }
}
