package lingamworks.hoteldasboard.data;

public class fooditems {
    String name;
    String price;
    String description;
    String imagelocal;
    public fooditems(String name,String price,String description,String imagelocal){
        this.name=name;
        this.price=price;
        this.description=description;
        this.imagelocal=imagelocal;
    }

    public void setImagelocal(String imagelocal) {
        this.imagelocal = imagelocal;
    }

    public String getImagelocal() {
        return imagelocal;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName(){return name;}

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }
    public String getPrice(){return price;}
}
