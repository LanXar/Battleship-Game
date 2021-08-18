import java.util.*;

public class Board
{
    Tile[][] board = new Tile[7][7];
    boolean hidden;
    public Board(Boolean hidden) //Board constructor.Stores new Tiles in an array each one having type SEA
    //and coordinates equal to the position of the board.Boolean hidden as parameter for printing the board.
    {
        for(int i=0;i<7;i++)
        {
            for(int j=0;j<7;j++)
            {
                this.board[i][j] = new Tile(i,j);
                this.board[i][j].tile_type = Tile.type.SEA;
            }
        }
        this.hidden = hidden;
    }
    public static void drawboards() //method to print the boards.
    {
        Game.x = "  - - Y O U - -\t\t- O P P O N E N T -\n   0 1 2 3 4 5 6\t   0 1 2 3 4 5 6\n";
        System.out.println(Game.x);
        for(int i=0;i<7;i++)
        {
            System.out.print(i);
            Game.x = String.join(" ",Game.x,Integer.toString(i));
            for(int j=0;j<7;j++)
            {
                Game.user_board.board[i][j].draw(Game.user_board.hidden);
            }
            System.out.print("   "+i);
            Game.x = String.join(" ",Game.x,"\t",Integer.toString(i));
            for(int k=0;k<7;k++)
            {
                Game.pc_board.board[i][k].draw(Game.pc_board.hidden);
            }
            System.out.println();
            Game.x = String.join(" ",Game.x,"\n");
        }
    }
    public static ArrayList<Tile.type> getAdjacentTiles(Tile tile,Board board) //returns the adjacent tiles
    //of a tile which is given as a parameter in an arraylist
    {
        ArrayList<Tile.type> adjacent = new ArrayList<>();  

        try{
                adjacent.add(board.board[tile.getx()-1][tile.gety()].tile_type);}
                catch (ArrayIndexOutOfBoundsException e){
                    adjacent.add(Tile.type.SEA);
                }
        try{
                adjacent.add(board.board[tile.getx()][tile.gety()-1].tile_type);}
                catch (ArrayIndexOutOfBoundsException e){
                    adjacent.add(Tile.type.SEA);
                }
        try{
                adjacent.add(board.board[tile.getx()+1][tile.gety()].tile_type);}
                catch (ArrayIndexOutOfBoundsException e){
                    adjacent.add(Tile.type.SEA);
                }
        try{
                adjacent.add(board.board[tile.getx()][tile.gety()+1].tile_type);}
                catch (ArrayIndexOutOfBoundsException e){
                    adjacent.add(Tile.type.SEA);
                }

        return adjacent;
    }
    public static void placeAllShips(Board board) //method to create and place all the ships randomly
    {
            Tile start = new Tile((int)(Math.random() * 6),(int)(Math.random() * 6)); //start tile with random coordinates x = [0,6] y = [0,6]
            Ship.Orientation ori =  Ship.values[(int)(Math.random() * 2)]; // random assingment of orientation
            ArrayList<Ship> ship = new ArrayList<>(); //array list to store ships
            boolean success; // boolean to check if ship was placed
            //adding ships to the array list
            ship.add(new Carrier(start,ori));
            ship.add(new Battleship(start,ori));
            ship.add(new Submarine(start,ori));
            ship.add(new Cruiser(start,ori));
            ship.add(new Destroyer(start,ori));

            for (int i=0;i<5;i++) // for loop for all the ships
            {
                success = false; // ship was not placed
                do{ // place ship on board until success = true
                    try{
                        success = ship.get(i).placeShip(board,true);
                    }catch(AdjacentTilesException e){ //if you catch exception set new random values to the ship until there are not exceptions and the ship is placed
                                        start.setx((int)(Math.random() * 6));
                                        start.sety((int)(Math.random() * 6));
                                        ori =  Ship.values[(int)(Math.random() * 2)];
                                        ship.get(i).setStart(start);
                                        ship.get(i).setOri(ori);
                    }
                    catch(OverlapTilesException e){
                                        start.setx((int)(Math.random() * 6));
                                        start.sety((int)(Math.random() * 6));
                                        ori =  Ship.values[(int)(Math.random() * 2)];
                                        ship.get(i).setStart(start);
                                        ship.get(i).setOri(ori);
                    }
                    catch(OversizeException e){
                                        start.setx((int)(Math.random() * 6));
                                        start.sety((int)(Math.random() * 6));
                                        ori =  Ship.values[(int)(Math.random() * 2)];
                                        ship.get(i).setStart(start);
                                        ship.get(i).setOri(ori);
                    }
                }while(success == false);
                //set new random values for the next ship to be placed
                start.setx((int)(Math.random() * 6));
                start.sety((int)(Math.random() * 6));
                ori =  Ship.values[(int)(Math.random() * 2)];
                ship.get(i).setStart(start);
                ship.get(i).setOri(ori);
            }
    }
    public static boolean allShipSunk(Board board) //method to check if the ships where sunk
    {
        int count = 0;
        boolean yes = false;
        for (int i=0;i<7;i++)
        {
            for (int j=0;j<7;j++)
            {
                if(board.board[i][j].tile_type != Tile.type.SHIP)
                {
                    count++; // counts the number of types that are different than ship
                }
            }
        }
        if(count == 49) //if count = 49 then all ships were sunk
        {
            yes = true;
        }
        return yes;
    }
}