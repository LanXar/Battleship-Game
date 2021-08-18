public class Tile
{
    private int x,y;
    public type tile_type;
    public static enum type
    {
        SEA("~"),SHIP("S"),HIT("X"),MISS("O");
        private String desc;
        type(String desc) //enum constructor
        {
            this.desc = desc;
        }
        public String getType() // method that returns the value
        {
            return desc;
        }
    }
    public Tile(int x,int y) // Tile constructor.Has coordinates as input and passes them to the class variable
    {
        this.x = x;
        this.y = y;
    }
    //getters and setters 
    public int getx()
    {
        return this.x;
    }
    public int gety()
    {
        return this.y;
    }
    public void setx(int x)
    {
        this.x = x;
    }
    public void sety(int y)
    {
        this.y = y;
    }
    //draw method that prints the type of the tile depending on a boolean value.
    public void draw(boolean hidden)
    {
        if(hidden == true)
        {
              if(tile_type == Tile.type.SHIP)
              {
                  System.out.print(" " + Tile.type.SEA.getType());
                  Game.x = String.join(" ",Game.x,"~"); //This is used for the swing lib.
              }
              else
              {
                  System.out.print(" " + tile_type.getType());
                  Game.x = String.join(" ",Game.x,tile_type.getType());//This is used for the swing lib.
              }
        }
        else
        {
            System.out.print(" " + tile_type.getType());
            Game.x = String.join(" ",Game.x,tile_type.getType()); //This is used for the swing lib.
        }
    }
}
