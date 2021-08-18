import java.io.*;
import java.util.*;
abstract class Ship //defines all the ships and their methods
{
    private int Size;
    private Tile start;
    private Orientation ori;
    public String name;
    public static Orientation[] values = Orientation.values();
    public enum Orientation // enum for orientation that can have only 2 values
    {
        Vertical,
        Horizontal;
    }
    public Ship(Tile start,Orientation ori,int Size) //constructor for ship
    {
        this.start = new Tile(start.getx(),start.gety());
        this.ori = ori;
        this.Size = Size;
    }
    // getters setters
    int getSize()
    {
        return this.Size;
    }
    public Tile setStart(Tile x)
    {
        start = x;
        return start;
    }
    public Orientation setOri(Orientation orien)
    {
        ori = orien;
        return ori;
    }
    public boolean shipFits() //method to check if the ship fits on the board
    {
        boolean fits = false;
        if(ori == Orientation.Horizontal)
        {
            if(start.gety()+getSize() <= 7)
            {
                fits = true;
            }
        }
        else
        {
            if(start.getx()+getSize() <= 7)
            {
                fits = true;
            }
        }
        return fits;
    }
    public boolean shipOnShip(Board board) // method to check if the ship overlaps with another ship
    {
        boolean isOnShip = false;
        int count = 0;
        if (ori == Orientation.Horizontal)
        {
            for(int j=0;j<getSize();j++)
            {
                if (board.board[start.getx()][j+start.gety()].tile_type == Tile.type.SEA)
                {
                    count++;
                }
            }
            if(count == getSize())
            {
                isOnShip = true;
            }
        }
        else
        {
            for(int j=0;j<getSize();j++)
            {
                if (board.board[j+start.getx()][start.gety()].tile_type == Tile.type.SEA)
                {
                    count++;
                }
            }
            if(count == getSize())
            {
                isOnShip = true;
            }
        }
        return isOnShip;
    }
    public boolean neighborShip(Board board) //method to check if the ship has a neighbor ship
    {
        int count = 0;
        int count1 = 0;
        boolean hasNoNeighbor = false;
        ArrayList<Tile.type> neighbor = Board.getAdjacentTiles(start,board);
        for (int i=0;i<neighbor.size();i++)
        {
            if(neighbor.get(i) == Tile.type.SEA)
            {
                count++;
            }
        }
        if (count == 4)
        {
            if(ori == Orientation.Horizontal)
            {
                for(int k=0;k<getSize()-1;k++)
                {
                        try{
                            if(board.board[start.getx()-1][k+start.gety()+1].tile_type == Tile.type.SEA)
                            {
                                count1++;
                            }
                        }catch(ArrayIndexOutOfBoundsException e)
                        {
                            count1++;
                        }
                        try{
                            if(board.board[start.getx()+1][k+start.gety()+1].tile_type == Tile.type.SEA)
                            {
                                count1++;
                            }
                        }catch(ArrayIndexOutOfBoundsException e)
                        {
                            count1++;
                        }
                }
                if(count1 == 2*getSize()-2)
                {
                    try{
                        if(board.board[start.getx()][start.gety()+getSize()].tile_type == Tile.type.SEA)
                        {
                            hasNoNeighbor = true;
                        }
                    }catch(ArrayIndexOutOfBoundsException e)
                    {
                        hasNoNeighbor = true;
                    }
                }
            }
            else
            {
                for(int k=0;k<getSize()-1;k++)
                {
                        try{
                            if(board.board[k+start.getx()+1][start.gety()-1].tile_type == Tile.type.SEA)
                            {
                                count1++;
                            }
                        }catch(ArrayIndexOutOfBoundsException e)
                        {
                            count1++;
                        }
                        try{
                            if(board.board[k+start.getx()+1][start.gety()+1].tile_type == Tile.type.SEA)
                            {
                                count1++;
                            }
                        }catch(ArrayIndexOutOfBoundsException e)
                        {
                            count1++;
                        }
                }
                if(count1 == 2*getSize()-2)
                {
                    try{
                        if(board.board[start.getx()+getSize()][start.gety()].tile_type == Tile.type.SEA)
                        {
                            hasNoNeighbor = true;
                        }
                    }catch(ArrayIndexOutOfBoundsException e)
                    {
                        hasNoNeighbor = true;
                    }
                }
            }
        }
        return hasNoNeighbor;
    }
    public boolean placeShip(Board board,Boolean verbose) throws OversizeException, //method that places a ship
                                                                 OverlapTilesException,
                                                                 AdjacentTilesException
                                                                                    
    {
        int count = 0;
        boolean placedShip = false;
        if(shipFits() && shipOnShip(board) && neighborShip(board)) //if the ship fits on the board and doesnt overlap and has no neighbor then place the ship
        {
            if (ori == Orientation.Horizontal)
            {
               for(int j=start.gety();j<getSize()+start.gety();j++)
               {
                    board.board[start.getx()][j].tile_type = Tile.type.SHIP;
               }
               placedShip = true;
            }
            else
            {
               for(int i=start.getx();i<getSize()+start.getx();i++)
               {
                     board.board[i][start.gety()].tile_type = Tile.type.SHIP;
               }
               placedShip = true;
             }
        }
        else // if the ship cannot be placed because one of the following method returned false throw exception to the user equal to the problem that was caused.
        {
            if(shipFits() == false){if(verbose)throw new OversizeException();}
            if(shipOnShip(board) == false){if(verbose)throw new OverlapTilesException();}
            if(neighborShip(board) == false){if(verbose)throw new AdjacentTilesException();}
        }
         if (placedShip) //when the ship is placed print a messaage 
        {
           if(this.name == "Carrier") 
            System.out.println("Carrier placed succesfully.");
           if(this.name == "Battleship") 
            System.out.println("Battleship placed succesfully.");
           if(this.name == "Submarine") 
            System.out.println("Submarine placed succesfully.");
           if(this.name == "Cruiser") 
            System.out.println("Cruiser placed succesfully.");
           if(this.name == "Destroyer") 
            System.out.println("Destroyer placed succesfully.");
        }
         return placedShip;
        }
    public boolean placeShip(Tile start,Orientation ori,Board board,Boolean verbose) throws OversizeException, //same method with more parameters.
                                                                                            OverlapTilesException,
                                                                                            AdjacentTilesException
                                                                                    
    {
        int count = 0;
        boolean placedShip = false;
        if(shipFits() && shipOnShip(board) && neighborShip(board))
        {
            if (ori == Orientation.Horizontal)
            {
               for(int j=start.gety();j<getSize()+start.gety();j++)
               {
                    board.board[start.getx()][j].tile_type = Tile.type.SHIP;
               }
               placedShip = true;
            }
            else
            {
               for(int i=start.getx();i<getSize()+start.getx();i++)
               {
                     board.board[i][start.gety()].tile_type = Tile.type.SHIP;
               }
               placedShip = true;
             }
        }
        else
        {
            if(shipFits() == false){if(verbose)throw new OversizeException();}
            if(shipOnShip(board) == false){if(verbose)throw new OverlapTilesException();}
            if(neighborShip(board) == false){if(verbose)throw new AdjacentTilesException();}
        }
         if (placedShip)
        {
           if(this.name == "Carrier") 
            System.out.println("Carrier placed succesfully.");
           if(this.name == "Battleship") 
            System.out.println("Battleship placed succesfully.");
           if(this.name == "Submarine") 
            System.out.println("Submarine placed succesfully.");
           if(this.name == "Cruiser") 
            System.out.println("Cruiser placed succesfully.");
           if(this.name == "Destroyer") 
            System.out.println("Destroyer placed succesfully.");
        }
         return placedShip;
        }
}