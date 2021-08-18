#include <iostream>
#include <ctime>
#include <cstdlib>
#include <string>
#include <exception>
#include <vector>
#include <unistd.h>
#include <thread>         // std::this_thread::sleep_for
#include <chrono>         // std::chrono::seconds

using namespace std;

enum Type{SEA, HIT, MISS, SHIP};


ostream& operator<<(ostream& os, Type type)
{
    switch (type)
    {
        case SEA: os << "~"; break;
        case SHIP: os << "S"; break;
        case HIT: os << "X"; break;
        case MISS: os << "O"; break;
    }
}

enum Orientation{Vertical,Horizontal};

class OversizeException : public exception
{

};

class OverlapTilesException : public exception
{

};

class AdjacentTilesException : public exception
{

};

class Tile
{
    int x;
    int y;
    Type type;

public:
    Tile();
    Tile(int z,int w);
    void draw(bool hidden);
    int getx();
    void setx(int i);
    int gety();
    void sety(int j);
    void setType(Type x);
    Type getType();
};

Tile::Tile()
{

}
Tile::Tile(int z,int w)
    {
        x = z;
        y = w;
    }
    void Tile::draw(bool hidden)
    {
        if (hidden == true)
        {
            if(type == SHIP)
            {
                cout << " " << SEA;
            }
            else
            {
                cout << " " << type;
            }
        }
        else
        {
            cout << " " << type;
        }
    }
    int Tile:: getx()
    {
        return x;
    }
    void Tile::setx(int i)
    {
        x = i;
    }
    int Tile::gety()
    {
        return y;
    }
    void Tile::sety(int j)
    {
        y = j;
    }
    Type Tile::getType()
    {
        return type;
    }
    void Tile::setType(Type x)
    {
        type = x;
    }

class Board
{
public:
    Tile* board[5][5];
    Board();
    void drawboard();
    static void placeAllShips(Board board);
    vector<Tile*> getAdjacentTiles(Tile x);
    bool allShipSunk();

};

Board::Board()
{
    for(int i=0;i<5;i++)
    {
        for(int j=0;j<5;j++)
        {
            board[i][j] = new Tile(i,j);
            board[i][j]->setType(SEA);
        }
    }
}

bool Board::allShipSunk()
{
        int count = 0;
        bool yes = false;
        for (int i=0;i<5;i++)
        {
            for (int j=0;j<5;j++)
            {
                if(board[i][j]->getType() != SHIP)
                {
                    count++;
                }
            }
        }
        if(count == 25)
        {
            yes = true;
        }
        return yes;
    }

vector<Tile*> Board::getAdjacentTiles(Tile x)
{
    vector<Tile*> neighbor;
    if(x.getx() != 0)
    {
        neighbor.push_back(board[x.getx()-1][x.gety()]);
    }
    if(x.getx() != 5)
    {
        neighbor.push_back(board[x.getx()+1][x.gety()]);
    }
    if(x.gety() != 5)
    {
        neighbor.push_back(board[x.getx()][x.gety()+1]);
    }
    if(x.gety() != 0)
    {
        neighbor.push_back(board[x.getx()][x.gety()-1]);
    }
    return neighbor;
}

void Board::drawboard()
{
    cout << "  BATTLESHIP" << endl;
        cout << endl;
        cout << "  0 1 2 3 4" << endl;
        for(int i=0;i<5;i++)
        {
            cout << i;
            for(int j=0;j<5;j++)
            {
                board[i][j]->draw(true);
            }
            cout << endl;
        }
}

class Ship
{
    protected:
    Tile start;
    Orientation ori;
    int shipSize;

public:

    Ship(Tile a,Orientation o);
    virtual bool placeShip(Tile strt,Orientation o,Board board) = 0;
    virtual bool shipFits();
    virtual bool shipOnShip(Board board);
    virtual bool neighborShip(Board board);
    virtual int getSize();
    virtual Orientation getOri();
    virtual void setStart(Tile x);
    virtual Tile getStart();
    virtual Orientation setOri(Orientation orien);
};

Ship::Ship(Tile a,Orientation o)
{
    start = a;
    ori = o;
}

int Ship::getSize()
{
    return shipSize;
}

Orientation Ship::getOri()
{
    return ori;
}

Tile Ship::getStart()
{
    return start;
}

void Ship::setStart(Tile x)
{
    start = x;
}

Orientation Ship::setOri(Orientation orien)
{
    ori = orien;
    return ori;
}

bool Ship::shipFits()
{
    bool fits = false;
        if(ori == Horizontal)
        {
            if(start.gety()+getSize() <= 5)
            {
                fits = true;
            }
        }
        else
        {
            if(start.getx()+getSize() <= 5)
            {
                fits = true;
            }
        }
        return fits;
}

bool Ship::shipOnShip(Board board)
{
    bool isOnShip = false;
        int count = 0;
        if (getOri() == Horizontal)
        {
            for(int j=0;j<getSize();j++)
            {
                if (board.board[start.getx()][j+start.gety()]->getType() == SEA)
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
                if (board.board[j+start.getx()][start.gety()]->getType() == SEA)
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

bool Ship::neighborShip(Board temp)
{
    bool ok = true;
    vector<Tile*> nei;
    int tx = 0,ty = 0;

    for(int i = 0;i < getSize();i++)
    {
        nei = temp.getAdjacentTiles(*temp.board[(start.getx()+tx)][(start.gety()+ty)]);
        for(int j = 0;j < nei.size();j++)
        {
            if(nei.at(j)->getType() == SHIP)
            {
                ok = false;
                break;
            }
        }
        if(ori == Horizontal)
        {
            ty++;
        }
        else
        {
            tx++;
        }
        if(!ok)
        {
            break;
        }
    }
    return ok;
}

class Battleship : public Ship
{
public:
    Battleship(Tile start,Orientation o):Ship(start,o)
    {
        shipSize = 4;
    }
    virtual bool placeShip(Tile strt,Orientation o,Board board);
};

bool Battleship::placeShip(Tile strt,Orientation o,Board board)
{
        OversizeException x;
        OverlapTilesException e;
        AdjacentTilesException y;
        bool placedShip = false;
        if(shipFits() && shipOnShip(board) && neighborShip(board))
        {
            if (getOri() == Horizontal)
            {
               for(int j=start.gety();j<getSize()+start.gety();j++)
               {
                    board.board[start.getx()][j]->setType(SHIP);
               }
               placedShip = true;
            }
            else
            {
               for(int i=start.getx();i<getSize()+start.getx();i++)
               {
                     board.board[i][start.gety()]->setType(SHIP);
               }
               placedShip = true;
             }
        }
        else
        {
            if(shipFits() == false){throw x;}
            if(shipOnShip(board) == false){throw e;}
            if(neighborShip(board) == false){throw y;}
        }

         return placedShip;
}


class Cruiser : public Ship
{
 public:
    Cruiser(Tile start,Orientation o) : Ship(start,o)
    {
        shipSize = 3;
    }
    virtual bool placeShip(Tile strt,Orientation o,Board board);
};


bool Cruiser::placeShip(Tile strt,Orientation o,Board board)
{
        OversizeException x;
        OverlapTilesException e;
        AdjacentTilesException y;
        bool placedShip = false;
        if(shipFits() && shipOnShip(board) && neighborShip(board))
        {
            if (getOri() == Horizontal)
            {
               for(int j=start.gety();j<getSize()+start.gety();j++)
               {
                    board.board[start.getx()][j]->setType(SHIP);
               }
               placedShip = true;
            }
            else
            {
               for(int i=start.getx();i<getSize()+start.getx();i++)
               {
                     board.board[i][start.gety()]->setType(SHIP);
               }
               placedShip = true;
             }
        }
        else
        {
            if(shipFits() == false){throw x;}
            if(shipOnShip(board) == false){throw e;}
            if(neighborShip(board) == false){throw y;}
        }

         return placedShip;
}


class Destroyer : public Ship
{
 public:
    Destroyer(Tile start,Orientation o) : Ship(start,o)
    {
        shipSize = 2;
    }
    virtual bool placeShip(Tile strt,Orientation o,Board board);
};


bool Destroyer::placeShip(Tile strt,Orientation o,Board board)
{
        OversizeException x;
        OverlapTilesException e;
        AdjacentTilesException y;
        bool placedShip = false;
        if(shipFits() && shipOnShip(board) && neighborShip(board))
        {
            if (getOri() == Horizontal)
            {
               for(int j=start.gety();j<getSize()+start.gety();j++)
               {
                    board.board[start.getx()][j]->setType(SHIP);
               }
               placedShip = true;
            }
            else
            {
               for(int i=start.getx();i<getSize()+start.getx();i++)
               {
                     board.board[i][start.gety()]->setType(SHIP);
               }
               placedShip = true;
             }
        }
        else
        {
            if(shipFits() == false){throw x;}
            if(shipOnShip(board) == false){throw e;}
            if(neighborShip(board) == false){throw y;}
        }

         return placedShip;
}

void Board::placeAllShips(Board board)
{
            srand(time(NULL));
            Tile start = Tile(rand()%5,rand()%5);
            Orientation ori = Orientation(rand()%2);
            vector<Ship*> ship;
            bool success;
            ship.push_back(new Battleship(start,ori));
            ship.push_back(new Cruiser(start,ori));
            ship.push_back(new Destroyer(start,ori));

            for (int i=0;i<3;i++)
            {
                success = false;
                do{
                    try{
                        success = ship.at(i)->placeShip(start,ori,board);
                    }catch(AdjacentTilesException e){
                                        start.setx(rand()%5);
                                        start.sety(rand()%5);
                                        ori = Orientation(rand()%2);
                                        ship.at(i)->setStart(start);
                                        ship.at(i)->setOri(ori);
                    }
                    catch(OverlapTilesException e){
                                        start.setx(rand()%5);
                                        start.sety(rand()%5);
                                        ori = Orientation(rand()%2);
                                        ship.at(i)->setStart(start);
                                        ship.at(i)->setOri(ori);
                    }
                    catch(OversizeException e){
                                        start.setx(rand()%5);
                                        start.sety(rand()%5);
                                        ori = Orientation(rand()%2);
                                        ship.at(i)->setStart(start);
                                        ship.at(i)->setOri(ori);
                    }
                }while(success == false);
                start.setx(rand()%5);
                start.sety(rand()%5);
                ori = Orientation(rand()%2);
                ship.at(i)->setStart(start);
                ship.at(i)->setOri(ori);
            }
}

class Player
{
private:
    string name;
    int shotsFired;
    int shotsMissed , successfullShots , repeatedShots;
public:
    void fire(Board pc,int* co);
    void getStats();
    static void getInput(int &x, int &y);
    void setStats();
};

void Player::setStats()
{
    shotsFired = 0;
    shotsMissed = 0;
    successfullShots = 0;
    repeatedShots = 0;
}

void Player::fire(Board pc,int* co)
{
    int x = co[0];
    int y = co[1];
    shotsFired++;
    if(pc.board[x][y]->getType() == SHIP)
    {
        pc.board[x][y]->setType(HIT);
        successfullShots++;
        cout << "Successful hit on: " << x << " " << y << endl;
    }
    else if(pc.board[x][y]->getType() == SEA)
    {
        pc.board[x][y]->setType(MISS);
        shotsMissed++;
        cout << "Miss on: " << x << " " << y << endl;
    }
    else if(pc.board[x][y]->getType() == MISS)
    {
        repeatedShots++;
        cout << "This position was already hit." << endl;
    }
    else
    {
        repeatedShots++;
        cout << "This position was already hit." << endl;
    }
}

void Player::getStats()
{
    cout << "Total Shots: " << shotsFired << endl;
    cout << "Successful Shots: " << successfullShots << endl;
    cout << "Missed Shots: " << shotsMissed << endl;
    cout << "Repeated Shots: " << repeatedShots << endl;

}

void Player::getInput(int &x, int &y)
{
    cout << "Enter location." << endl;
    cin >> x;
    cin >> y;
}

int main()
{
    static Board pc = Board();
    Player user = Player();
    Board::placeAllShips(pc);
    pc.drawboard();
    int count = 10;
    int coordinates[2]={0,0};
    int x,y;

    user.setStats();

    do
    {
        user.getInput( x, y);
        coordinates[0] = x;
        coordinates[1] = y;
        user.fire(pc,coordinates);
        pc.drawboard();
        count--;
        cout << "Remaining shots: " << count << endl;
    }while(pc.allShipSunk() == false && count >= 1);
    if(pc.allShipSunk() == true)
    {
        cout << "YOU WIN" << endl;
    }
    else
    {
        cout << "Insert coin to continue..." << endl;
        for(int t=10; t>0;t--){
            this_thread::sleep_for (std::chrono::seconds(1));
            cout << t << endl;
        }
    }

    user.getStats();
    return 0;
}
