package lingamworks.hoteldasboard.data;

public class finalbilldata {
    String date;
    String flist;
    int price;
    String table;
    String mode;
    public finalbilldata(){}
public finalbilldata(String date, String table, String flist, int price, String mode){
    this.date=date;
    this.table=table;
    this.flist=flist;
    this.price=price;
    this.mode=mode;
}
    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public int getPrice() {
        return price;
    }

    public String getDate() {
        return date;
    }

    public String getFlist() {
        return flist;
    }

    public String getMode() {
        return mode;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setFlist(String flist) {
        this.flist = flist;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
