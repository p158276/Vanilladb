package vanillacomm.zabAppl;
import vanillacomm.protocols.zabAppl.ZabListener;

public class StartZab implements ZabListener {
    
    // this is the message sending interval in milliseconds
    private final long interval = 1000;
    
    int count, countChosen;
    boolean flag;
    int myId;
    long timeSent;
    ZabAppl zabappl;
    
    public StartZab(int id){
        zabappl = new ZabAppl("config/view", id, this);
        this.myId = id;
        System.out.println("ID= "+id);
        zabappl.start();

    }
    
    // called when application layer receives a message
    public void onRecv(Object o) {
        String s = (String)o;
        if(flag && (myId + " " + countChosen).equals(s)){
            System.out.println("time: " + (System.currentTimeMillis() - timeSent));
            synchronized(this){
                flag = false;
            }
        }
        System.out.println(s);
    }
    
    public void onNodeFail(int id){
        // do nothing
    }
    
    
    public void go(){
        

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        flag = false;
        count = 0;
        String message;
        zabappl.startPFD();
        
        while(true){
            
            synchronized(this){
                if(!flag){                
                    countChosen = count;
                    timeSent = System.currentTimeMillis();
                    flag = true;
                }
            }
            message = myId + " " + count++;
            
            
            zabappl.ZabSend(message);
            
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
        }
        
    }
    
    public static void main(String args[]) throws InterruptedException{
        
        StartZab zab = new StartZab(Integer.parseInt(args[0]));
        zab.go();

    }
}
