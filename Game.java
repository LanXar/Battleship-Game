import java.util.*;
import javax.swing.*;
public class Game
{
    static Board user_board = new Board(false);
    static Board pc_board = new Board(true);
    static Player User = new Player();
    static Player Pc = new Player();
    static String x;
    public static void main()
    {  
        JFrame f=new JFrame();//creating instance of JFrame   
          
        int[] fire = new int[2];
        User.setName("User");
        Pc.setName("CPU");
        System.out.println("Place ship randomly? Y/N");
        Scanner reader = new Scanner(System.in);
        char c = reader.next(".").charAt(0);
        if(c =='Y')
        {
            Board.drawboards();
            JTextArea area=new JTextArea(x);  
            area.setBounds(25, 25, 450, 450);  
            area.setFont(area.getFont().deriveFont(20f));
            f.add(area);    
            
            f.setSize(525, 540);//400 width and 500 height  
            f.setLayout(null);//using no layout managers  
            f.setVisible(true);//making the frame visible  
            Player.placeAllShips();
            Board.placeAllShips(pc_board);
            Board.drawboards();
            f.remove(area);
            area=new JTextArea(x);  
            area.setBounds(25, 25, 450, 450);  
            area.setFont(area.getFont().deriveFont(20f));
            f.add(area);  
            f.invalidate();
            f.validate();
            f.repaint();
            do
            {
                Player.fire(pc_board,getInput());
                fire = getRandInput();
                Player.fire(user_board,fire);
                Board.drawboards();
                f.remove(area);
                area=new JTextArea(x);  
                area.setBounds(25, 25, 450, 450);  
                area.setFont(area.getFont().deriveFont(20f));
                f.add(area);  
                f.invalidate(); //refresh the board
                f.validate();
                f.repaint();
            }while(Board.allShipSunk(pc_board) == false && Board.allShipSunk(user_board) == false );
            if(Board.allShipSunk(pc_board) == true)
            {
                System.out.println("The winner is: " + User.getName());
            }
            else
            {
                System.out.println("The winner is: " + Pc.getName());
            }
            User.getStats();
            Pc.getStats();
               
        }
        else
        {
            int x[] = getInput();
            Tile start = new Tile(x[0],x[1]); 
            Ship carrier = new Carrier(start,getOrientation());
            
            int y[] = getInput();
            Tile start1 = new Tile(y[0],y[1]); 
            Ship battleship = new Battleship(start1,getOrientation());
            
            int z[] = getInput();
            Tile start2 = new Tile(z[0],z[1]); 
            Ship cruiser = new Cruiser(start2,getOrientation());
            
            int w[] = getInput();
            Tile start3 = new Tile(w[0],w[1]); 
            Ship submarine = new Submarine(start3,getOrientation());
            
            int v[] = getInput();
            Tile start4 = new Tile(v[0],v[1]); 
            Ship destroyer = new Destroyer(start4,getOrientation());
            
            Board.drawboards();
            Board.placeAllShips(pc_board);
            try{
                carrier.placeShip(user_board,true);
            }catch(OversizeException e){}
            catch(OverlapTilesException e){}
            catch(AdjacentTilesException e){}
            try{
                battleship.placeShip(user_board,true);
            }catch(OversizeException e){}
            catch(OverlapTilesException e){}
            catch(AdjacentTilesException e){}
            try{
                cruiser.placeShip(user_board,true);
            }catch(OversizeException e){}
            catch(OverlapTilesException e){}
            catch(AdjacentTilesException e){}
            try{
                submarine.placeShip(user_board,true);
            }catch(OversizeException e){}
            catch(OverlapTilesException e){}
            catch(AdjacentTilesException e){}
            try{
                destroyer.placeShip(user_board,true);
            }catch(OversizeException e){}
            catch(OverlapTilesException e){}
            catch(AdjacentTilesException e){}
            Board.drawboards();
            do
            {
                Player.fire(pc_board,getInput());
                fire = getRandInput();
                Player.fire(user_board,fire);
                Board.drawboards();
            }while(Board.allShipSunk(pc_board) == false && Board.allShipSunk(user_board) == false );
            if(Board.allShipSunk(pc_board) == true)
            {
                System.out.println("The winner is: " + User.getName());
            }
            else
            {
                System.out.println("The winner is: " + Pc.getName());
            }
            User.getStats();
            Pc.getStats();
        }
    }
    public static Ship.Orientation getOrientation()
    {
        System.out.println("Give orientation V/H");
        Scanner reader = new Scanner(System.in);
        char c = reader.next(".").charAt(0);
        Ship.Orientation ori;
        if(c == 'H')
        {
            ori = Ship.Orientation.Horizontal;
        }
        else
        {
            ori = Ship.Orientation.Vertical;
        }
        return ori;
    }
    static int[] getInput()
    {
        System.out.println("Give 2 integers for the location.");
        Scanner scanInt = new Scanner(System.in);
        int[] location = new int[2];
        do
        { 
            for(int i=0;i<2;i++)
            {
                    location[i] = Integer.parseInt(scanInt.next());
            }
       }while((location[0]<0 && location[0]>6)&&(location[1]<0 && location[1]>6));
       return location;
    }
    static int[] getRandInput()
    {
        int[] coordinates = new int[2];
        coordinates[0] = (int)(Math.random() * 6);
        coordinates[1] = (int)(Math.random() * 6);
        return coordinates;
    }
    static boolean randomPlace()
    {
        Scanner reader = new Scanner(System.in);
        char c = reader.next(".").charAt(0);
        boolean chk = false;
        if(c == 'Y')
        {
            chk = true;
        }
        return chk;
    }
}