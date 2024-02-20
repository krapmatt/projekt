package com.example.demo;

import java.util.*;

public class MineSweeper {
    private int[][] visibleField = new int[10][10];
    private int[][] hiddenField = new int[10][10];
    public static void main(String[] args) {
        MineSweeper M = new MineSweeper();
        M.startGame();
    }


    public void startGame() {
        System.out.println("---------Vítej v Hledání min, užij si hru---------");

        setupPole(1);

        boolean flag = true;
        while (flag) {
            displayVisible();
            flag = playMove();
            if(checkWin())
            {
                displayHidden();
                System.out.println("\n---------Vyhrál jsi!---------");
                break;
            }
        }

    }

    public void setupPole(int diff) {
        int var = 0;
        while (var != 10) {
            Random random = new Random();
            int i = random.nextInt(10);
            int j = random.nextInt(10);
            hiddenField[i][j] = 100;
            var++;
        }
        buildHidden();
    }

    public void buildHidden() {
        for (int i=0; i<10; i++) {
            for (int j=0; j<10; j++) {
                int pocet=0;
                if (hiddenField[i][j] != 100) {

                    if (i != 0) {
                        if (hiddenField[i-1][j] == 100) pocet++;
                        if (j!=0) {
                            if(hiddenField[i-1][j-1] == 100) pocet++;
                        }

                    }
                    if (i!=9) {
                        if (hiddenField[i+1][j] == 100) pocet++;
                        if (j!=9) {
                            if (hiddenField[i+1][j+1] == 100) pocet++;
                        }
                    }
                    if (j != 0) {
                        if (hiddenField[i][j-1] == 100) pocet++;
                        if (i != 9) {
                            if (hiddenField[i+1][j-1] == 100) pocet++;
                        }
                    }
                    if (j != 9) {
                        if (hiddenField[i][j+1] == 100) pocet++;
                        if (i != 0) {
                            if (hiddenField[i-1][j+1] == 100) pocet++;
                        }
                    }

                    hiddenField[i][j] = pocet;
                }
            }
        }

    }

    public void displayVisible() {
        System.out.print("\t ");
        for (int i = 0; i<10; i++) {
            System.out.print(" " + i + "  ");
        }

        System.out.print("\n");

        for (int i = 0; i<10; i++) {
            System.out.print(i + "\t| ");
            
            for (int j = 0; j<10; j++) {
                if (visibleField[i][j] == 0) {
                    System.out.print("?");
                } else if (visibleField[i][j] == 50) {
                    System.out.print(" ");
                } else {
                    System.out.print(visibleField[i][j]);
                }
                System.out.print(" | ");
            }
            System.out.print("\n");
        }
    }
    
    public void displayHidden() {
        System.out.print("\t ");
        for (int i=0; i<10; i++) {
            System.out.print(" " + i + "  ");
        }
        System.out.print("\n");
        for (int i=0; i<10; i++) {
            System.out.print(i + "\t| ");
            for (int j=0; j<10; j++) {
                if(hiddenField[i][j] == 0) {
                    System.out.print(" ");
                } else if(hiddenField[i][j] == 100) {
                    System.out.print("X");
                } else {
                    System.out.print(hiddenField[i][j]);
                }
                System.out.print(" | ");
            }
            System.out.print("\n");
        }
    }

    public boolean playMove() {
        Scanner sc= new Scanner(System.in);
        System.out.print("\nČíslo řádku: ");
        int i= sc.nextInt();
        System.out.print("Číslo sloupce: ");
        int j= sc.nextInt();

        if (i<0 || i>9 || j<0 || j>9 || visibleField[i][j]!=0) {
            System.out.print("\nŠpatný vstup!!");
            return true;
        }

        if(hiddenField[i][j] == 100)
        {
            displayHidden();
            System.out.print("A jejda umřel jsi \n-------GAME OVER-------");
            return false;
        }
        else if (hiddenField[i][j]==0) {
            fixVisible(i, j);
        }
        else {
            fixNeighbours(i, j);
        }

        return true;
    }

    public void fixVisible(int i, int j) {
        visibleField[i][j] = 50;
        
        if (i != 0) {
            visibleField[i-1][j] = hiddenField[i-1][j];
            if (visibleField[i-1][j] == 0) visibleField[i-1][j] = 50;
            if (j!=0) {
                visibleField[i-1][j-1] = hiddenField[i-1][j-1];
                if (visibleField[i-1][j-1] == 0) visibleField[i-1][j-1] = 50;
            }
        }
        
        if (i != 9) {
            visibleField[i+1][j] = hiddenField[i+1][j];
            if (visibleField[i+1][j] == 0) visibleField[i+1][j]=50;
            if (j != 9) {
                visibleField[i+1][j+1] = hiddenField[i+1][j+1];
                if (visibleField[i+1][j+1] == 0) visibleField[i+1][j+1] = 50;
            }
        }
        
        if(j != 0) {
            visibleField[i][j-1] = hiddenField[i][j-1];
            if (visibleField[i][j-1] == 0) visibleField[i][j-1] = 50;
            if (i!=9) {
                visibleField[i+1][j-1] = hiddenField[i+1][j-1];
                if (visibleField[i+1][j-1] == 0) visibleField[i+1][j-1] = 50;
            }
        }
        
        if(j != 9) {
            visibleField[i][j+1] = hiddenField[i][j+1];
            if (visibleField[i][j+1] == 0) visibleField[i][j+1] = 50;
            if (i!=0) {
                visibleField[i-1][j+1] = hiddenField[i-1][j+1];
                if (visibleField[i-1][j+1] == 0) visibleField[i-1][j+1] = 50;
            }
        }
    }

    public void fixNeighbours(int i, int j)
    {
        Random random = new Random();
        int x = random.nextInt()%4;

        visibleField[i][j] = hiddenField[i][j];

        if (x == 0) {

            if (i != 0) {
                if (hiddenField[i-1][j] != 100) {
                    visibleField[i-1][j] = hiddenField[i-1][j];
                    if (visibleField[i-1][j] == 0)  visibleField[i-1][j] = 50;
                }
            }

            if (j != 0) {
                if (hiddenField[i][j-1] != 100) {
                    visibleField[i][j-1] = hiddenField[i][j-1];
                    if (visibleField[i][j-1] == 0)  visibleField[i][j-1] = 50;
                }

            }

            if (i != 0 && j != 0) {
                if (hiddenField[i-1][j-1] != 100) {
                    visibleField[i-1][j-1] = hiddenField[i-1][j-1];
                    if (visibleField[i-1][j-1] == 0)  visibleField[i-1][j-1] = 50;
                }

            }
        } else if (x == 1) {
            if (i != 0) {
                if (hiddenField[i-1][j] != 100) {
                    visibleField[i-1][j] = hiddenField[i-1][j];
                    if (visibleField[i-1][j] == 0)  visibleField[i-1][j] = 50;
                }
            }

            if (j != 9) {
                if(hiddenField[i][j+1] != 100) {
                    visibleField[i][j+1] = hiddenField[i][j+1];
                    if (visibleField[i][j+1] == 0)  visibleField[i][j+1] = 50;
                }

            }

            if (i!=0 && j!=9) {
                if(hiddenField[i-1][j+1] != 100)
                {
                    visibleField[i-1][j+1] = hiddenField[i-1][j+1];
                    if (visibleField[i-1][j+1] == 0)  visibleField[i-1][j+1] = 50;
                }
            }

        } else if(x == 2) {
            if (i != 9) {
                if(hiddenField[i+1][j] != 100) {
                    visibleField[i+1][j] = hiddenField[i+1][j];
                    if (visibleField[i+1][j] == 0)  visibleField[i+1][j] = 50;
                }
            }
            if (j != 9) {
                if (hiddenField[i][j+1] != 100) {
                    visibleField[i][j+1] = hiddenField[i][j+1];
                    if (visibleField[i][j+1] == 0)  visibleField[i][j+1] = 50;
                }
            }

            if (i != 9 && j != 9) {
                if (hiddenField[i+1][j+1]!=100) {
                    visibleField[i+1][j+1] = hiddenField[i+1][j+1];
                    if(visibleField[i+1][j+1] == 0)  visibleField[i+1][j+1] = 50;
                }
            }
        } else {
            if (i != 9) {
                if (hiddenField[i+1][j] != 100) {
                    visibleField[i+1][j] = hiddenField[i+1][j];
                    if (visibleField[i+1][j] == 0)  visibleField[i+1][j] = 50;
                }
            }
            if(j != 0) {
                if (hiddenField[i][j-1] != 100) {
                    visibleField[i][j-1] = hiddenField[i][j-1];
                    if (visibleField[i][j-1] == 0)  visibleField[i][j-1] = 50;
                }

            }
            if(i!=9 && j!=0) {
                if(hiddenField[i+1][j-1] != 100) {
                    visibleField[i+1][j-1] = hiddenField[i+1][j-1];
                    if(visibleField[i+1][j-1] == 0)  visibleField[i+1][j-1] = 50;
                }
            }
        }
    }
    
    public boolean checkWin() {
        for (int i=0; i<10; i++) {
            for (int j=0; j<10; j++) {
                if (visibleField[i][j] == 0) {
                    if(hiddenField[i][j] != 100) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    

    
}

