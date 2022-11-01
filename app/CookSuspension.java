
public class CookSuspension {
    private boolean permanentSuspension;
    private double suspensionTime;
    private String suspensionMessage;

    public CookSuspension(){
        this.permanentSuspension = false;
        this.suspensionTime = 0;
        this.suspensionMessage = "";
    }

    //sets permanent suspension to true
    public void setPermanentSuspensionTRUE(){
        this.permanentSuspension = true;
    }

    //sets permanent suspension to false
    public void setPermanentSuspensionFALSE(){
        this.permanentSuspension = false;
    }

    public void setSuspensionTime(double suspensionTime){
        this.suspensionTime = suspensionTime;
    }

    public void setSuspensionMessage(String suspensionMessage){
        this.suspensionMessage = suspensionMessage;
    }

    public String getSuspensionMessage(){
        return this.suspensionMessage;
    }

    public double getSuspensionTime(){
        return this.suspensionTime;
    }
}
