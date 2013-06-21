package vanillacomm.zabAppl;


import vanillacomm.protocals.totalOrderBroadcast.totalOrderBroadcastLayer;
import vanillacomm.protocols.utils.ProcessSet;
import vanillacomm.protocols.utils.SampleProcess;
import vanillacomm.protocols.zabAppl.ZabApplLayer;
import vanillacomm.protocols.zabAppl.ZabApplSession;
import vanillacomm.protocols.zabAppl.ZabListener;

import vanillacomm.protocols.basicBroadcast.BasicBroadcastLayer;
import vanillacomm.protocols.tcpBasedPFD.TcpBasedPFDLayer;

import vanillacomm.protocols.tcpBasedPFD.PFDStartEvent;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.StringTokenizer;

import net.sf.appia.core.Appia;
import net.sf.appia.core.AppiaCursorException;
import net.sf.appia.core.AppiaDuplicatedSessionsException;
import net.sf.appia.core.AppiaEventException;
import net.sf.appia.core.AppiaInvalidQoSException;
import net.sf.appia.core.Channel;
import net.sf.appia.core.ChannelCursor;
import net.sf.appia.core.Direction;
import net.sf.appia.core.Layer;
import net.sf.appia.core.QoS;
import net.sf.appia.core.events.SendableEvent;
import net.sf.appia.protocols.tcpcomplete.TcpCompleteLayer;

public class ZabAppl extends Thread{
    
    private Channel channel;
    private ZabApplSession session;
    
    public ZabAppl(String view_filename, int self, ZabListener r){
        
        Channel channel = getZabChannel(buildProcessSet(view_filename, self), r);
        this.channel = channel;
    }
    public void run(){
    
        try {
            channel.start();
        } catch (AppiaDuplicatedSessionsException ex) {
            System.err.println("Sessions binding strangely resulted in "
                    + "one single sessions occurring more than "
                    + "once in a channel");
            System.exit(1);
        }

        /* All set. Appia main class will handle the rest */
        System.out.println("Starting Appia...");
        
        Appia.run();
    }
    
    public void ZabSend(Object o){
        try {
            SendableEvent ev = new SendableEvent();
            ev.getMessage().pushObject(o);
            ev.asyncGo(this.channel, Direction.DOWN);
        } catch (AppiaEventException ex) {
            ex.printStackTrace();
        }
    }
    
    public void startPFD() {
        try {
            PFDStartEvent pfdStart = new PFDStartEvent();
            pfdStart.asyncGo(this.channel, Direction.DOWN);
        } catch (AppiaEventException e) {
            e.printStackTrace();
        }
    }
    
    
    private ProcessSet buildProcessSet(String filename, int selfProc) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(filename)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(0);
        } 
        String line;
        StringTokenizer st;
        boolean hasMoreLines = true;
        ProcessSet set = new ProcessSet();
        // reads lines of type: <process number> <IP address> <port>
        while (hasMoreLines) {
            try {
                line = reader.readLine();
                if (line == null)
                    break;
                st = new StringTokenizer(line);
                if (st.countTokens() != 3) {
                    System.err.println("Wrong line in file: "
                            + st.countTokens());
                    continue;
                }
                int procNumber = Integer.parseInt(st.nextToken());
                InetAddress addr = InetAddress.getByName(st.nextToken());
                int portNumber = Integer.parseInt(st.nextToken());
                boolean self = (procNumber == selfProc);
                SampleProcess process = new SampleProcess(
                        new InetSocketAddress(addr, portNumber), procNumber,
                        self);
                set.addProcess(process, procNumber);
            } catch (IOException e) {
                hasMoreLines = false;
            } catch (NumberFormatException e) {
                System.err.println(e.getMessage());
            }
        } // end of while
        
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return set;
    }
    
    private Channel getZabChannel(ProcessSet processes, ZabListener r) {
        /* Create layers and put them on a array */
        Layer[] qos = { new TcpCompleteLayer(),
                new BasicBroadcastLayer() ,
                new TcpBasedPFDLayer(),
                
                /* TODO
                 * Add your layers here
                 */
                new totalOrderBroadcastLayer(),
                new ZabApplLayer()
        };
        System.out.println("In get Zabchannel:"+processes.getSelfRank());
        /* Create a QoS */
        QoS myQoS = null;
        try {
            myQoS = new QoS("ZAB QoS", qos);
        } catch (AppiaInvalidQoSException ex) {
            System.err.println("Invalid QoS");
            System.err.println(ex.getMessage());
            System.exit(1);
        }
        /* Create a channel. Uses default event scheduler. */
        Channel channel = myQoS
                .createUnboundChannel("ZAB Channel");
        /*
         * Application Session requires special arguments: filename and . A
         * session is created and binded to the stack. Remaining ones are
         * created by default
         */
        ZabApplSession sas = (ZabApplSession) qos[qos.length - 1]
                .createSession();
        sas.init(processes, r);
        this.session = sas;
        ChannelCursor cc = channel.getCursor();
        /*
         * Application is the last session of the array. Positioning in it is
         * simple
         */
        try {
            cc.top();
            cc.setSession(sas);
        } catch (AppiaCursorException ex) {
            System.err.println("Unexpected exception in main. Type code:"
                    + ex.type);
            System.exit(1);
        }
        
        return channel;
    }

}
