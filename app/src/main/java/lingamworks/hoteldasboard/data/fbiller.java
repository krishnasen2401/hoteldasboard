package lingamworks.hoteldasboard.data;

public class fbiller {
    String table;
    String fname;
    int price;
    int qty;
    public fbiller(){}
    public fbiller(String fname,int price,int qty,String table){
        this.fname=fname;
        this.price=price;
        this.qty=qty;
        this.table=table;
    }

    public int getPrice() {
        return price;
    }

    public int getQty() {
        return qty;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }
}
