/*
 *
 * Hands-On code of the book Introduction to Reliable Distributed Programming
 * by Christian Cachin, Rachid Guerraoui and Luis Rodrigues
 * Copyright (C) 2005-2011 Luis Rodrigues
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
 *
 * Contact
 * 	Address:
 *		Rua Alves Redol 9, Office 605
 *		1000-029 Lisboa
 *		PORTUGAL
 * 	Email:
 * 		ler@ist.utl.pt
 * 	Web:
 *		http://homepages.gsd.inesc-id.pt/~ler/
 * 
 */

package vanillacomm.protocols.zabAppl;

import vanillacomm.protocols.consensusUtils.StringProposal;
import vanillacomm.protocols.events.*;
import vanillacomm.protocols.tcpBasedPFD.PFDStartEvent;
import vanillacomm.protocols.utils.ProcessSet;
import net.sf.appia.core.*;
import net.sf.appia.core.events.SendableEvent;
import net.sf.appia.core.events.channel.ChannelClose;
import net.sf.appia.core.events.channel.ChannelInit;
import net.sf.appia.protocols.common.RegisterSocketEvent;

import java.net.InetSocketAddress;
import java.util.StringTokenizer;



/**
 * Session implementing the sample application.
 * 
 * @author nuno
 */
public class ZabApplSession extends Session {

    Channel channel;
    ZabListener zablistener;

    private ProcessSet processes;

    public ZabApplSession(Layer layer) {
        super(layer);
    }

    public void init(ProcessSet processes, ZabListener zablistener) {
        this.processes = processes;
        this.zablistener = zablistener;
    }

    public void handle(Event event) {
        if(event instanceof SendableEvent)
            handleSendableEvent((SendableEvent) event);
        else if (event instanceof ChannelInit)
            handleChannelInit((ChannelInit) event);
        else if (event instanceof ChannelClose)
            handleChannelClose((ChannelClose) event);
        else if (event instanceof RegisterSocketEvent)
            handleRegisterSocket((RegisterSocketEvent) event);
        else if (event instanceof Crash)
            handleCrashEvent((Crash) event);
    }

    private void handleCrashEvent(Crash event) {
        // TODO Auto-generated method stub
        zablistener.onNodeFail(event.getCrashedProcess());
    }

    /**
     * @param event
     */
    private void handleRegisterSocket(RegisterSocketEvent event) {
        if (event.error) {
            System.out.println("Address already in use!");
            System.exit(2);
        }
    }

    /**
     * @param init
     */
    private void handleChannelInit(ChannelInit init) {
        try {
            init.go();
        } catch (AppiaEventException e) {
            e.printStackTrace();
        }
        channel = init.getChannel();

        try {
            // sends this event to open a socket in the layer that is used has
            // perfect
            // point to point
            // channels or unreliable point to point channels.
            RegisterSocketEvent rse = new RegisterSocketEvent(channel,
                    Direction.DOWN, this);
            rse.port = ((InetSocketAddress) processes.getSelfProcess()
                    .getSocketAddress()).getPort();
            rse.localHost = ((InetSocketAddress) processes.getSelfProcess()
                    .getSocketAddress()).getAddress();
            rse.go();
            ProcessInitEvent processInit = new ProcessInitEvent(channel,
                    Direction.DOWN, this);
            processInit.setProcessSet(processes);
            processInit.go();
        } catch (AppiaEventException e1) {
            e1.printStackTrace();
        }
        System.out.println("Channel is open.");
        
    }

    /**
     * @param close
     */
    private void handleChannelClose(ChannelClose close) {
        channel = null;
        System.out.println("Channel is closed.");
    }
    
    /**
     * @param event
     */
    private void handleSendableEvent(SendableEvent event) {
        if (event.getDir() == Direction.DOWN)
            handleBCast(event);
        else
            handleIncomingEvent(event);
    }

    /**
     * @param event
     */
    private void handleIncomingEvent(SendableEvent event) {
       zablistener.onRecv(event.getMessage().popObject());
    }
    
    
    /**
     * @param event
     */
    private void handleBCast(SendableEvent event) {
        try {
            event.go();
        } catch (AppiaEventException e) {
            e.printStackTrace();
        }
    }

}
