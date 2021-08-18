public class Player
{
    private String name;
    private int shotsFired = 0,shotsMissed = 0,shotsLanded = 0,repeat_shots = 0;
    public static void placeAllShips()
    {
        Board.placeAllShips(Game.user_board);
    }
    public void placeShip(Ship ship,int[] x,Ship.Orientation ori)
    {
        Tile a = new Tile(x[0],x[1]);
        try{
            ship.placeShip(a,ori,Game.user_board,true);
        }catch (OversizeException e){}
        catch (OverlapTilesException e){}
        catch (AdjacentTilesException e){}
    }
    public static void fire(Board board,int[] coordinates)
    {
        if(board.board[coordinates[0]][coordinates[1]].tile_type == Tile.type.SHIP)
        {
            board.board[coordinates[0]][coordinates[1]].tile_type = Tile.type.HIT;
            if (board == Game.pc_board)
            {
                System.out.println("Hit by: " + Game.User.getName());
                Game.User.shotsLanded();
            }
            else
            {
                System.out.println("Hit by: " + Game.Pc.getName());
                Game.Pc.shotsLanded();
            }
        }
        else if(board.board[coordinates[0]][coordinates[1]].tile_type == Tile.type.SEA)
        {
            board.board[coordinates[0]][coordinates[1]].tile_type = Tile.type.MISS;
            if (board == Game.pc_board)
            {
                System.out.println("Miss by: " + Game.User.getName());
                Game.User.shotsMissed();
            }
            else
            {
                System.out.println("Miss by: " + Game.Pc.getName());
                Game.Pc.shotsMissed();
            }
        }
        else if(board.board[coordinates[0]][coordinates[1]].tile_type == Tile.type.HIT)
        {
            if (board == Game.pc_board)
            {
                System.out.println(Game.User.getName() + " Already hit.");
                Game.User.repeatShots();
            }
            else
            {
                System.out.println(Game.Pc.getName() + " Already hit.");
                Game.Pc.repeatShots();
            }
        }
        else
        {
            if (board == Game.pc_board)
            {
                System.out.println(Game.User.getName() + " Already miss.");
                Game.User.repeatShots();
            }
            else
            {
                System.out.println(Game.Pc.getName() + " Already miss.");
                Game.Pc.repeatShots();
            }
        }
        Game.User.shotsFired();
    }
    //getters setters for the stats
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public int shotsFired()
    {
        shotsFired++;
        return shotsFired;
    }
    public int shotsMissed()
    {
        shotsMissed++;
        return shotsMissed;
    }
    public int shotsLanded()
    {
        shotsLanded++;
        return shotsLanded;
    }
    public int repeatShots()
    {
        repeat_shots++;
        return repeat_shots;
    }
    public void getStats()
    {
        
        System.out.println("Shots : " + shotsFired);
        System.out.println("Successful Shots : " + shotsLanded);
        System.out.println("Shots Missed : " + shotsMissed);
        System.out.println("Repeated Shots : " + repeat_shots);
    }
}