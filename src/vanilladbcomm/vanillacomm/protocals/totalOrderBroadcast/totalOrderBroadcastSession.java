package vanillacomm.protocals.totalOrderBroadcast;

import vanillacomm.protocols.events.Crash;
import vanillacomm.protocols.events.ProcessInitEvent;
import vanillacomm.protocols.events.finalEvent;
import vanillacomm.protocols.events.toLeaderEvent;
import vanillacomm.protocols.utils.Debug;
import vanillacomm.protocols.utils.ProcessSet;
import vanillacomm.protocols.utils.SampleProcess;
import net.sf.appia.core.AppiaEventException;
import net.sf.appia.core.Channel;
import net.sf.appia.core.Direction;
import net.sf.appia.core.Event;
import net.sf.appia.core.Layer;
import net.sf.appia.core.Session;
import net.sf.appia.core.events.SendableEvent;
import net.sf.appia.core.events.channel.ChannelInit;

public class totalOrderBroadcastSession extends Session{
    
    /*
     * State of the protocol: the set of processes in the group
     */
    private Channel channel;
    private ProcessSet processes;
    public totalOrderBroadcastSession(Layer layer) {
        super(layer);
        // TODO Auto-generated constructor stub
    }
    public void handle(Event event) {
        // Init events. Channel Init is from Appia and ProcessInitEvent is to
        // know
        // the elements of the group
        if (event instanceof ChannelInit)
            handleChannelInit((ChannelInit) event);
        else if (event instanceof ProcessInitEvent)
            handleProcessInitEvent((ProcessInitEvent) event);
        else if (event instanceof Crash)
            handleCrash((Crash) event);
        else if (event instanceof SendableEvent) {
            if(processes.getSelfRank()==0) //leader node
            {
                if(event instanceof finalEvent){         
                    try {
                        event.go();
                    } catch (AppiaEventException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }else if(event instanceof toLeaderEvent)
                {
                    finalEvent finalEventTemp = null;
                    try {
                        finalEventTemp = new finalEvent(channel,Direction.DOWN,this);
                        finalEventTemp.setSourceSession(this);
                    } catch (AppiaEventException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    Object obj;
                    obj = ((SendableEvent) event).getMessage().popObject();
                    finalEventTemp.getMessage().pushObject(obj);
                    //finalEventTemp.setDir(Direction.DOWN);
                    try {
                        finalEventTemp.init();
                        finalEventTemp.go();
                    } catch (AppiaEventException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }                    
                }else
                {
                    finalEvent finalEventTemp = null;
                    try {
                        finalEventTemp = new finalEvent(channel,Direction.DOWN,this);
                        finalEventTemp.setSourceSession(this);
                    } catch (AppiaEventException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    Object obj;
                    obj = ((SendableEvent) event).getMessage().popObject();
                    finalEventTemp.getMessage().pushObject(obj);
                   // finalEventTemp = finalEventTemp.setMessage((((SendableEvent) event).getMessage().popObject()));
                    //finalEventTemp.setDir(Direction.DOWN);
                    try {
                        finalEventTemp.init();
                        finalEventTemp.go();
                    } catch (AppiaEventException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }else //if the process is not leader node.
            {
                if(event instanceof finalEvent){
                    try {
                        event.go();
                    } catch (AppiaEventException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }else if(event instanceof toLeaderEvent){
                    System.out.println("wrong way!");             
                }else{
                    toLeaderEvent eventTemp = null;
                    try {
                        eventTemp = new toLeaderEvent(channel,Direction.DOWN,this);
                    } catch (AppiaEventException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    Object obj;
                    obj = (((SendableEvent) event).getMessage().popObject());
                    eventTemp.getMessage().pushObject(obj);
                    //eventTemp = eventTemp.setMessage((((SendableEvent) event).getMessage().popObject()));
                 // set source and destination of event message
                    eventTemp.source = processes.getSelfProcess()
                            .getSocketAddress();
                    eventTemp.dest = processes.getProcess(0).getSocketAddress();
                    eventTemp.setSourceSession(this);
                    try {
                        eventTemp.init();                        
                        eventTemp.go();
                    } catch (AppiaEventException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Gets the process set and forwards the event to other layers.
     * 
     * @param event
     */
    private void handleProcessInitEvent(ProcessInitEvent event) {
        processes = event.getProcessSet();
        try {
            event.go();
        } catch (AppiaEventException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the first event that arrives to the protocol session. In this
     * case, just forwards it.
     * 
     * @param init
     */
    private void handleChannelInit(ChannelInit init) {
        channel = init.getChannel();
        try {
            init.go();
        } catch (AppiaEventException e) {
            e.printStackTrace();
        }
    }
    /**
     * Called when some process crashed.
     * 
     * @param crash
     */
    private void handleCrash(Crash crash) {
        int crashedProcess = crash.getCrashedProcess();
        System.out.println("Process " + crashedProcess + " failed.");
        // changes the state of the process to "failed"
        processes.getProcess(crashedProcess).setCorrect(false);
    }
}
