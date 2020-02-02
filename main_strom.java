import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.util.Scanner;
import java.awt.event.KeyListener;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.lang.StringBuilder;
import java.io.FileReader;
import java.lang.Math;
import java.util.Vector;

public class main_strom extends JPanel implements KeyListener {
  static final double pi = Math.PI;
  static final double gx = 0.0; //Gravitation x Komponente
  static final double gy = 0.0; //Gravitation y Komponente
  static final int iterations_between_paint = 100;
  static final int iterations_between_smash = 10; 
  static final int xg = 1200; //x Rahmen
  static final int yg = 600; //y Rahmen 
  static final int thickness = 20;    
  static final int std_radius = 2;
  static final int std_mass   = std_radius*std_radius*std_radius;
  static final double v_def = 100.0; //Default Geschwindigkeit
  static final double v_ran = 20.0; 
  static final int n = 2000; //Anzahl von Teilchen
  static final int virtual_seconds = 50;
  static final double t = 0.0001; //1/100000.0; //dt
  static final double Body_Length = 200.0;
  static final int number_of_paintings = (int)(virtual_seconds/(t*iterations_between_paint));
  
  
  //Counter
  static int paint_counter = 0;
  static int paint_index = 0;
  static int it = 0;
  
  //Particles and Sections
  static double[] rx = new double[n];
  static double[] ry = new double[n];
  static int[] la = new int[n];
  static int[] in = new int[n];
  static double[] vx = new double[n];
  static double[] vy = new double[n];
  static int[][] xhist = new int[n][number_of_paintings];
  static int[][] yhist = new int[n][number_of_paintings];
  static int[][] X = new int[2][3];
  static int[][] Y = new int[X.length][X[0].length]; 
  static double[][] Oalpha = new double[X.length][X[0].length-1]; 
  
  static final int s = (int)(X[0].length/2);
  static int run = 1;
  static long time_cur = 0;
  static long time_start = 0;
  static int between = 10;
  
  
  public main_strom() {}
  
  public void keyTyped(KeyEvent e) {}
  
  public void keyPressed(KeyEvent e) {
  if(e.getKeyCode() == 37) { //left
    System.out.println(37); //factor += Math.pow(10, factor2);
      //changed = true;
      }
    if(e.getKeyCode() == 39) {//right
    System.out.println(39); //factor -= Math.pow(10, factor2);
      //changed = true;
      }
    if(e.getKeyCode() == 38) { //up
    System.out.println(38); //factor2++;
      if(between > 0) between -= 1;
      //KD += 0.001;
      //changed = true;
      }
    if(e.getKeyCode() == 40) { //down
    System.out.println(40); //factor2--;
      between += 1;
      //KD -= 0.001;
      //changed = true;
      }
    if(e.getKeyCode() == 8) { //delete
    System.out.println(19); //factor2--
      run *= -1;
      //KD -= 0.001;
      //changed = true;
      }
    }
  
  public void keyReleased(KeyEvent e) {};
  
  public static double f(double x) {
  //return (int)(Math.sqrt(100*100-(x-100)*(x-100))); //circle
    return (10*Math.sqrt(x));
    //return (int)(30*Math.log(x));         
    }
  
  public static double squ(double x) {
  return x*x;  
    }
  
  public static double sin(double x) {
  return Math.sin(x); //x-(x*x*x)/6.0+(x*x*x*x*x)/120.0-(x*x*x*x*x*x*x)/5040.0;  
    }
  
  public static double cos(double x) {
  return Math.cos(x); //1.0-(x*x)/2.0+(x*x*x*x)/24.0-(x*x*x*x*x*x)/720.0;  
    }
  
  public static void recreate(int i) {
  rx[i] = 0;
    ry[i] = (int)(Math.random()*(yg));
    vx[i] = v_def+Math.random()*(2*v_ran)-v_ran;
    vy[i] = Math.random()*(2*v_ran)-v_ran;
    la[i] = i;
    }
  
  public static void doSectionStuff(int i, int k, int j) {
  double betac = angle(vx[i], vy[i]);
    double cosbetac = cos(betac);
    double sinbetac = sin(betac);
    double v = Math.sqrt(vx[i]*vx[i]+vy[i]*vy[i]); 
    //impx += std_mass*v*(cosbetac-cos0); impy += std_mass*v*(sinbetac-sin0);
    if(betac < 0) betac += 2*pi;
    vx[i] = v*Math.cos(2*Oalpha[k][j]-betac); //Math.cos(betac); //v*tan(betac)[0]; 
    vy[i] = v*Math.sin(2*Oalpha[k][j]-betac); //Math.sin(betac); //v*tan(betac)[1];
    rx[i] += vx[i]*t; 
    ry[i] += vy[i]*t;  
    }  
  
  public static void smash(int i, int j) {
  double x1 = rx[i];
    double y1 = ry[i];
    double x2 = rx[j];
    double y2 = ry[j];
    double v1x = vx[i];
    double v1y = vy[i];
    double v2x = vx[j];
    double v2y = vy[j];
    double phi = 0;
    double theta1 = 0;
    double theta2 = 0;
    if((x1-x2) > 0) {
    phi += pi;
      }
    if(v1x < 0) {
    theta1 += pi;  
      }
    if(v2x < 0) {
    theta2 += pi;  
      }     
    phi += Math.atan((y1-y2)/(x1-x2+0.00001));  
    theta1 += Math.atan(v1y/v1x);
    theta2 += Math.atan(v2y/v2x);
    
    double m1 = std_mass;
    double m2 = std_mass;
    double v1 = Math.sqrt(v1x*v1x+v1y*v1y);
    double v2 = Math.sqrt(v2x*v2x+v2y*v2y);
    double cos1 = cos(theta1-phi);
    double cos2 = cos(theta2-phi);
    double cos0 = cos(phi);
    double sin1 = sin(theta1-phi);
    double sin2 = sin(theta2-phi);
    double sin0 = sin(phi);
    
    double u1x = (v1*cos1*(m1-m2)+2*m2*v2*cos2)/(m1+m2)*cos0-v1*sin1*sin0;
    double u1y = (v1*cos1*(m1-m2)+2*m2*v2*cos2)/(m1+m2)*sin0+v1*sin1*cos0;
    
    double u2x = (v2*cos2*(m2-m1)+2*m1*v1*cos1)/(m1+m2)*cos0-v2*sin2*sin0;
    double u2y = (v2*cos2*(m2-m1)+2*m1*v1*cos1)/(m1+m2)*sin0+v2*sin2*cos0;
    
    vx[i] = u1x;
    vy[i] = u1y;
    vx[j] = u2x;
    vy[j] = u2y;
    
    if(Math.sqrt(squ(y1-y2)+squ(x1-x2)) < (2*std_radius) && (i<n || j<n)) {    
    phi += pi;
      rx[i] = rx[j]+(int)((2*std_radius+1)*cos(phi));
      ry[i] = ry[j]+(int)((2*std_radius+1)*sin(phi)); 
      }
    la[i] = j;
    la[j] = i;
    rx[i] += 2*vx[i]*t; 
    ry[i] += 2*vy[i]*t;  
    rx[j] += 2*vx[j]*t; 
    ry[j] += 2*vy[j]*t;      
    }
  
  public static double angle(double dx, double dy) {
  if(dx == 0 && dy > 0) {
    return pi/2;
      } else if(dx == 0 && dy < 0) {
    return 1.5*pi;
      } // end of if-else
    double beta = Math.atan(dy/dx);
    //System.out.println(beta/pi*180);
    if(dx < 0 && dy >= 0) {
    beta = pi+beta;
      } else if(dx < 0 && dy < 0) {
    beta = 1.5*pi-beta;
      } else if(dx >= 0 && dy < 0) {
    beta = 2*pi+beta;
      }
    return beta;
    }
  
  public static boolean inSection(int i, int k, int j) {
  double x = rx[i];
    double y = ry[i];
    double sx1 = X[k][j];                        
    double sy1 = Y[k][j];
    double sx2 = X[k][j+1];
    double sy2 = Y[k][j+1];
    double ux = sx2-sx1;
    double uy = sy2-sy1;
    double m = uy/(ux+0.00001);
    double b = sy1-m*sx1;
    
    double d = m*x+b-y;
    if(d < 0) d *= -1.0;
    
    if((((sx1 <= x && x <= sx2) || (sx2 <= x && x <= sx1)) || ((sy1 <= y && y <= sy2) || (sy2 <= y && y <= sy1))) && d <= 1.5*std_radius) {
    return true;
      }
    return false;
    }
  
  public static int minint(int[] a) {
  int min = a[0];
    for (int i = 1; i < a.length; i++) {
    if(a[i] < min) {
      min = a[i];  
        }  
      }
    return min;  
    }
  
  public static double min(double[] a) {
  double min = a[0];
    for (int i = 1; i < a.length; i++) {
    if(a[i] < min) {
      min = a[i];  
        }  
      }
    return min;  
    }
  
  public double max(double[] a) {
  double max = a[0];
    for (int i = 1; i < a.length; i++) {
    if(a[i] > max) {
      max = a[i];  
        }  
      }
    return max;  
    } 
  
  public static double val(double x) {
  if(x < 0) {
    x *= -1.0;  
      }
    return x;    
    }
  
  public void paint(Graphics g) {
  super.paint(g); 
    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    //g2d.setColor(Color.BLACK);
    //g2d.fillRect(0,0,xg,yg);
    if(it*t < virtual_seconds) {
    if(it%1 == 0) {
      g2d.setColor(Color.BLACK);
        int[] xc = {95, 505, 505, 95};
        int[] yc = {95, 95, 305, 305};
        g2d.drawPolygon(xc, yc, 4);
        g2d.drawString("Progess: "+(int)(100*it*t/virtual_seconds)+"%", 50, 50);
        g2d.drawString("running since: "+(time_cur-time_start)/(60*1000000000.0), 200, 50);
        g2d.drawString("Estimate : "+((time_cur-time_start)/(60*1000000000.0*it*t/virtual_seconds)), 400, 50);
        int[] xc2 = {100, 100+4*(int)(100*it*t/virtual_seconds), 100+4*(int)(100*it*t/virtual_seconds), 100};
        int[] yc2 = {100, 100, 300, 300};
        g2d.drawPolygon(xc2, yc2, 4);
        }
      } else {
    g2d.setColor(Color.BLUE);
      g2d.drawString("iter "+paint_index, 500, 50);
      g2d.drawString("time "+between, 600, 50); 
      
      if(paint_index < number_of_paintings) {
      g2d.setColor(Color.RED);    
        for (int i = 0; i < X.length; i++) {
        for (int j = 0; j < X[0].length-1; j++) {
          g2d.drawLine((int)X[i][j], (int)Y[i][j], (int)X[i][j+1], (int)Y[i][j+1]);
            }
          }
        
        for(int i=0; i<n; ++i) {
        if(vx[i] > 0) {
          g2d.setColor(new Color(0, 0, (int)vx[i]));
            } else {
          g2d.setColor(new Color(0, (int)(-vx[i]), 0)); 
            }
          g2d.fillOval(xhist[i][paint_index]-std_radius, yhist[i][paint_index]-std_radius, 2*std_radius, 2*std_radius);
          }  
        }
      }
    }    
  public void init() {    
  }
  
  public static void main(String[] args) throws InterruptedException {
  
    
    /*int num_po_row = s/2;
    for (int i = 0; i < num_po_row; i++) {
    X[0][i] = xg/5+(int)(Body_Length/num_po_row)*(num_po_row-i-1);       
    Y[0][i] = (int)(yg/2-f(num_po_row-i-1));
    
    X[1][i] = xg/2+(int)(Body_Length/num_po_row)*(num_po_row-i-1);       
    Y[1][i] = (int)(yg/2-f(num_po_row-i-1));
    }
    for (int i = num_po_row; i < 2*num_po_row; i++) {
    X[0][i] = xg/5+(int)(Body_Length/num_po_row)*(i-num_po_row+1);
    Y[0][i] = (int)(yg/2+f(i-num_po_row+1));
    
    X[1][i] = xg/2+(int)(Body_Length/num_po_row)*(i-num_po_row+1);
    Y[1][i] = (int)(yg/2+f(i-num_po_row+1));
    }
    
    for (int i = 2*num_po_row; i < 4*num_po_row; i++) {
    X[0][i] = X[0][4*num_po_row-1-i]+thickness;
    Y[0][i] = Y[0][4*num_po_row-1-i];
    
    X[1][i] = X[1][4*num_po_row-1-i]+thickness;
    Y[1][i] = Y[1][4*num_po_row-1-i];
    }*/
    
    X[0][0] = 100;
    Y[0][0] = 0;  
    X[0][1] = xg/2;
    Y[0][1] = yg/2-50;    
    X[0][2] = xg;
    Y[0][2] = yg/2-50;
    
    X[1][0] = 100;
    Y[1][0] = yg;    
    X[1][1] = xg/2;
    Y[1][1] = yg/2+50;  
    X[1][2] = xg;
    Y[1][2] = yg/2+50; 
    
    
    
    for (int i = 0; i < X.length; ++i) {
    for (int j = 0; j < X[0].length-1; ++j) {
      Oalpha[i][j] = angle((X[i][j+1]-X[i][j]), (Y[i][j+1]-Y[i][j]));
        }
      }
    
    TestWriter TW = new TestWriter();
    
    
    
    for (int i=0; i<n; ++i) {
    recreate(i);
      }
    int zeilen_vor_inhalt = 4;
    
    System.out.println("number of paintings         = "+number_of_paintings);
    System.out.println("number of calculated frames = "+number_of_paintings*iterations_between_paint);
    String[] content = new String[zeilen_vor_inhalt+X.length*X[0].length+n*number_of_paintings];
    
    content[0] = "0 "+n+" n";
    content[1] = "0 "+number_of_paintings;
    content[2] = "0 "+xg+" "+yg;
    content[3] = "0 "+X.length+" "+X[0].length;
    
    for (int j = 0; j < X.length; j++) {
    for(int i = 0; i < X[j].length; ++i) {
      content[zeilen_vor_inhalt+j*X[0].length+i] = (j+1)+" "+X[j][i]+" "+Y[j][i];
        }
      }        
    
    main_strom points = new main_strom();
    JFrame frame = new JFrame("CFD");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.addKeyListener(points);
    frame.add(points);
    frame.setSize(xg, yg); // x,y
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
    
    long timenow;
    long[] t_sec = new long[5];
    
    time_start = System.nanoTime();
    
    while(it*t <= virtual_seconds) {
    timenow = System.nanoTime();
      for (int i = 0; i < n; ++i) {
      vx[i] += gx*t;              
        vy[i] += gy*t;
        rx[i] += vx[i]*t;
        ry[i] += vy[i]*t;
        }
      t_sec[0] += System.nanoTime()-timenow;
      
      for (int i=0; i<n; ++i) {
      timenow = System.nanoTime();
        if(rx[i] < 0) {
        recreate(i);
          } else if(rx[i] > xg) {
        recreate(i);
          } else if(ry[i] < 0) {
        recreate(i);
          } else if(ry[i] > yg) {
        recreate(i);
          }
        t_sec[1] += System.nanoTime()-timenow;
        if(it%iterations_between_smash == 0) {
        timenow = System.nanoTime();
          for (int j=0; j < i; ++j) {
          if((squ(ry[i]-ry[j])+squ(rx[i]-rx[j])) <= squ(2*std_radius) && la[i] != j && la[j] != i && in[i] <= 0) {
          smash(i, j);
          }
          }
          t_sec[2] += System.nanoTime()-timenow;       
          }
          timenow = System.nanoTime();
        for (int k = 0; k < Oalpha.length; k++) {
        for (int j = 0; j < Oalpha[k].length; ++j) {
        if(inSection(i, k, j)) {
          doSectionStuff(i, k, j);
            in[i] = 100;
              break;
              } 
              }
            }
          in[i]--;
        t_sec[3] += System.nanoTime()-timenow;      
        }
        timenow = System.nanoTime();
      if(it%iterations_between_paint == 0) {
      if(paint_counter < number_of_paintings) {
      int start = X.length*X[0].length+zeilen_vor_inhalt+n*paint_counter;
        for (int i = start; i < start+n; i++) {
          content[i] = "0 "+(int)rx[i-start]+" "+(int)ry[i-start];
          
            xhist[i-start][paint_counter] = (int)rx[i-start];
            yhist[i-start][paint_counter] = (int)ry[i-start];
            }  
            paint_counter++;
          points.repaint();
          }
          }
        t_sec[4] += System.nanoTime()-timenow;
      time_cur = System.nanoTime();
      it++;
      }
      
    long t2 = System.nanoTime()-time_start;
    double umrechnung = 60.0*1000000000;
    System.out.println("1.5 - n = "+n+" / v_s = "+virtual_seconds+" / iter_bw_paint = "+iterations_between_paint+" / dt = "+t+" / t[min] = "+((t2)/umrechnung));
    /*long su = 0;
    for (int i = 0; i < t_sec.length; i++) {
    System.out.print(" T_"+i+" = "+((float)t_sec[i]/t2));
    su += t_sec[i];
    }
    System.out.println("");
    System.out.println((float)su/t2);*/
    TW.write(content);
    Sound so = new Sound();
    so.bing();
    
    while(true) {
    for (paint_index = 0; paint_index < number_of_paintings; paint_index++) {
    if(run == 1) points.repaint();
      try {
        Thread.sleep(between);
        } catch(Exception e) {} 
          }
        }
      }
    }    
  

