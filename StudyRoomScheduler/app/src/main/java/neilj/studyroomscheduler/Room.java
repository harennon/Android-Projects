package neilj.studyroomscheduler;

class Room {

    private int roomId;
    private int roomNum;
    private String hall;

    Room(int id, String h, int num){
        roomId = id;
        hall = h;
        roomNum = num;
    }

    public int getRoomId() {
        return roomId;
    }

    public int getRoomNum(){
        return roomNum;
    }

    public String getHall() {
        return hall;
    }
}
